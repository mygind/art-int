#!/bin/bash
for i in $*; do 
    gpf=$(echo $i | sed -e 's/\.dat$/.gp/');
    output=$(echo $i | sed -e 's/\.dat/.ps/');
    name="Level $( echo $i | sed -e 's/^level//'| sed -e 's/-.*\.dat$//' )"

    echo $name

    echo "" > $gpf
    echo set terminal postscript color >> $gpf
    echo set xlabel \"Depth\" >> $gpf
    echo set ylabel \"States\" >> $gpf
    echo set title \"$name\" >> $gpf
    echo set output \"${output}\" >> $gpf
    echo plot \"$i\" using 1:2 title \"BFSolver\" with lines, \\>> $gpf
    echo \"$i\" using 1:3 title "\"A(i)\"" with lines, \\>> $gpf
    echo \"$i\" using 1:4 title "\"A(+{c,i})\"" with lines, \\>> $gpf
    echo \"$i\" using 1:5 title "\"A(+{c,i,4})\"" with lines, \\>> $gpf
    echo \"$i\" using 1:6 title "\"A(+{*{i,i},c,4})\"" with lines, \\>> $gpf
    echo \"$i\" using 1:7 title "\"A(+{*{s,s},c,4})\"" with lines, \\>> $gpf
    echo \"$i\" using 1:8 title "\"A(+{*{B,B,s,s},c,4})\"" with lines, \\>> $gpf
    echo \"$i\" using 1:9 title "\"A(+{*{B,B,a,a},c,4})\"" with lines, \\>> $gpf
    echo \"$i\" using 1:10 title "\"A(rand)\"" with lines  >> $gpf
    echo reset >> $gpf

    gnuplot $gpf

done

