#!/bin/bash
set -eu -o pipefail;
IFS='';

PACK="Invariable-Paintings-CIT.zip";
rm -f $PACK;
zip $PACK -r \
	assets/ \
	pack.mcmeta \
	pack.png \
	;
