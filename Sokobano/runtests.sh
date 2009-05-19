#!/bin/bash

TIMEOUT=5
LEVELSET=1
SETSIZE=40
for i in $(seq -w 1 $SETSIZE); do
	java -cp bin gdi1sokoban.planning.GoSolveYourself -g -t $TIMEOUT res/levelSet/$LEVELSET/Level_$i.txt > results/level${i}-${TIMEOUT}-secs
done
