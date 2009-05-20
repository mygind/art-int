#!/bin/bash

for i in $*; do
	HEIGHT=$(cat $i | wc -l);
	WIDTH=$(cat $i | wc -L);
	echo $i $(echo ${HEIGHT}*${WIDTH} | bc)
done
