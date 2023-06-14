#!/bin/bash

HEADER=\
'{
	"pools": [{
		"rolls":1,
		"entries":['

ENTRY=\
'
			{
				"type":"item", "name":"minecraft:painting",
				"functions": [{
					"function":"set_nbt", 
					"tag": "{ EntityTag: { variant:\"${VARIANT}\" } }"
				}]
			}'

TAIL=\
'		]
	}]
}'

NAMES="
minecraft:alban
minecraft:aztec
minecraft:aztec2
minecraft:bomb
minecraft:burning_skull
minecraft:bust
minecraft:courbet
minecraft:creebet
minecraft:donkey_kong
minecraft:earth
minecraft:fighters
minecraft:fire
minecraft:graham
minecraft:kebab
minecraft:match
minecraft:pigscene
minecraft:plant
minecraft:pointer
minecraft:pool
minecraft:sea
minecraft:skeleton
minecraft:skull_and_roses
minecraft:stage
minecraft:sunset
minecraft:void
minecraft:wanderer
minecraft:wasteland
minecraft:water
minecraft:wind
minecraft:wither
"

TABLE="./vanilla_paintings.json"
FIRST_LOOP=true


echo -n "$HEADER" >$TABLE
for v in $NAMES;
do 
	if [[ $FIRST_LOOP == true ]]
	then
		FIRST_LOOP=false
	else
		echo -en "			," >>$TABLE
	fi
	export variant="$v";
	echo >&2 $v
	envsubst >>$TABLE <<<"$ENTRY"
done;
echo "$TAIL" >>$TABLE