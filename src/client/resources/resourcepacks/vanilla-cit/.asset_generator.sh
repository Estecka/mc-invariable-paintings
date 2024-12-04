#!/bin/bash
set -eu -o pipefail
shopt -s globstar
IFS=""

if [[ $# -lt 5 ]]
then
	echo """
Usage: $0 <template> <srcPrefix> <srcSuffix> <dstPrefix> <dstSuffix>

The script seeks assets in all namespaces, whose pathes match the pattern:
	./assets/*/<srcPrefix>**<srcSuffix>
The wildcards are exported in the variables \${NAMESPACE} and \${NAME} respectively. The name will not not contain any part of the source prefix or suffix. The wildcard for name will search subdirectories recursively.

From every thusly found asset, a new asset at ./assets/\${NAMESPACE}/<dstPrefix>\${NAME}<dstSuffix> will be created using the provided template. This template is fed through envsubst, so that the tokens \${NAMESPACE} and \${NAME} in this file will be substitued with the previously found values. Additional variables may still be provided via the environment.

The suffixes for both source and destination typically need to contain the file extensions.
""";

	[[ $# -eq 0 ]];
	exit;
fi;

template=$1;
srcPrefix=$2;
srcSuffix=$3;
dstPrefix=$4;
dstSuffix=$5;
shift 5;

for d in ./assets/*
do if [[ -d $d ]]
then
	export NAMESPACE=$(basename $d)
	fullPrefix="./assets/$NAMESPACE/$srcPrefix";
	for f in $fullPrefix**/**/*$srcSuffix
	do if [[ -f $f ]]
	then
		export NAME=${f#$fullPrefix}
		export NAME=${NAME%$srcSuffix}

		dst="./assets/$NAMESPACE/$dstPrefix$NAME$dstSuffix"
		echo >&2 "$NAMESPACE:$NAME -> $dst";

		mkdir -p $(dirname $dst);
		envsubst <"$template" >$dst
	fi
	done;
fi
done;

