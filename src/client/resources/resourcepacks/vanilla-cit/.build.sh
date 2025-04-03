#!/bin/bash
set -eu -o pipefail

echo >&2 -e "\n\t# items";
./.asset_generator.sh ./.templates/items-variants.json \
	textures/item/ .png \
	items/         .json \
	;

echo >&2 -e "\n\t# models";
./.asset_generator.sh ./.templates/models.json \
	textures/item/ .png \
	models/item/   .json \
	;

echo >&2 -e "\n\t# Monolithic item state";

rm -f .cases
separator="";
template=".templates/items-case.json"
shopt -s globstar

for d in ./assets/*
do if [[ -d $d ]]
then
	export NAMESPACE=$(basename $d)
	fullPrefix="./assets/$NAMESPACE/textures/item/painting/";
	srcSuffix=".png";
	for f in $fullPrefix**/**/*$srcSuffix
	do if [[ -f $f ]]
	then
		export NAME=${f#$fullPrefix}
		export NAME=${NAME%$srcSuffix}

		dst=".cases"
		echo >&2 "$NAMESPACE:$NAME";
		echo -n >>$dst "$separator";
		envsubst <"$template" >>$dst
		separator=$',\n'
	fi
	done;
fi
done;

export CASES=`cat .cases | tr -d $'\r'`;
envsubst <".templates/items-monolith.json" >"./assets/minecraft/items/painting.json"
rm .cases

