#!/bin/bash
for i in $*; do 
    gpf=$(echo $i | sed -e 's/\.dat$/.gp/');
    output=$(echo $i | sed -e 's/\.dat/.ps/');
    name=$( echo $i | sed -e 's/' -e '/-.*\.dat//' 
    
    echo set terminal epslatex color >> $gpd
    echo set title "Level 01" >> $gpd
    echo set output "${output}" >> $gpd
    echo plot "level01-5.dat" using 1:2 title "BFSolver" with lines, \ >> $gpd
    echo "level01-5.dat" using 1:3 title "A(i)" with lines, \ >> $gpd
    echo "level01-5.dat" using 1:4 title "A(+{c,i})" with lines, \ >> $gpd
    echo "level01-5.dat" using 1:5 title "A(+{c,i,4})" with lines, \ >> $gpd
    echo "level01-5.dat" using 1:6 title "A(+{*{i,i},c,4})" with lines, \ >> $gpd
    echo "level01-5.dat" using 1:7 title "A(+{*{s,s},c,4})" with lines, \ >> $gpd
    echo "level01-5.dat" using 1:8 title "A(+{*{B,B,s,s},c,4})" with lines, \ >> $gpd
    echo "level01-5.dat" using 1:9 title "A(+{*{B,B,a,a},c,4})" with lines, \ >> $gpd
    echo "level01-5.dat" using 1:10 title "A(rand)" with lines;  >> $gpd
    echo reset >> $gpd


done

