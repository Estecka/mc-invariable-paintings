#!/bin/bash
set -eu -o pipefail;
IFS='';

DST="./build/libs/Invariable-Paintings-CIT.zip";
SRC="./src/client/resources/resourcepacks/vanilla-cit";

rm -f "$DST";
mkdir -p $(dirname "$DST");

DST=$(realpath "$DST");
cd "$SRC";
zip "$DST" -r -9 .;
