set terminal postscript dashed color
set title "Level 01"
set output "level01.ps"
plot "level01-5.dat" using 1:2 title "BFSolver" with lines, \
     "level01-5.dat" using 1:3 title "A(i)" with lines, \
     "level01-5.dat" using 1:4 title "A(+{c,i})" with lines, \
     "level01-5.dat" using 1:5 title "A(+{c,i,4})" with lines, \
     "level01-5.dat" using 1:6 title "A(+{*{i,i},c,4})" with lines, \
     "level01-5.dat" using 1:7 title "A(+{*{s,s},c,4})" with lines, \
     "level01-5.dat" using 1:8 title "A(+{*{B,B,s,s},c,4})" with lines, \
     "level01-5.dat" using 1:9 title "A(+{*{B,B,a,a},c,4})" with lines, \
     "level01-5.dat" using 1:10 title "A(rand)" with lines;                            
reset


