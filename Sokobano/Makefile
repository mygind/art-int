export CLASSPATH+=".:./bin:./src_test:./test:./lib/lwjgl-2.0.1/jar/jinput.jar:./lib/lwjgl-2.0.1/jar/lwjgl.jar:./lib/lwjgl-2.0.1/jar/lwjgl-util.jar:./lib/jmf.jar:./lib/lwjgl-2.0.1/jar/lwjgl_util.jar:./lib/lwjgl-2.0.1/jar/lwjgl_util_applet.jar:./lib/lwjgl-2.0.1/jar/lwjgl_test.jar.jar:./lib/jmf.jar" 
export SOURCEPATH+="./src:./planning_src"
export DISPLAY=:0
FILES=$(shell find src -name *.java)


.PHONY: compile

compile: $(FILES)
	if [ ! -d bin ]; then mkdir bin; fi
	javac -sourcepath $(SOURCEPATH) -d bin src/gdi1sokoban/Application.java 

run: 
	java -Xmx128m -Djava.library.path="lib/lwjgl-2.0.1/native/linux/:planning_src" gdi1sokoban.Application

clean:
	if [ -d bin ]; then rm -rf bin; fi
