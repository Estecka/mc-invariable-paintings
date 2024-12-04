#!/bin/bash
set -eu -o pipefail

echo >&2 -e "\n\t# items";
./.asset_generator.sh ./.templates/items.json \
	textures/item/ .png \
	items/         .json \
	;

echo >&2 -e "\n\t# models";
./.asset_generator.sh ./.templates/models.json \
	textures/item/ .png \
	models/item/   .json \
	;
