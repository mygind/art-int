** Installing and running the program **
Make sure you are located in the Sokobano/ folder.
install program by running
	make
		-ignore the warnings
		
run the program using
	java -cp bin/ gdi1sokoban.planning.GoSolveYourself MAP
		where map is the desired level the solver should solve. The level must look as described on http://www.sokobano.de/wiki

And number of levels can be found in Sokobano/res/levelSet 
fx Sokobano/res/levelSet/0/Level_01.txt

** Unstable beta version, worth trying **
For a 3D solving experience run 
	startLinux.sh
When reaching the GameStartFrame press ``Planning'' instead of ``Start'' and do not touch the keyboard afterwards as the planner will not be able to solve the puzzle if interrupted.
