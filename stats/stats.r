bf <- read.table("test0-01_bf.stat", header=T)
astar <- read.table("test0-01_astar.stat", header=T)

plot(bf$depth, bf$discovered_state, "p")
X11()
plot(astar$depth, astar$discovered_state, "p")

