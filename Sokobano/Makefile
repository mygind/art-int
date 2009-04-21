export CLASSPATH+=":./src_test:./test:./lib/lwjgl-2.0.1/jar/jinput.jar:./lib/lwjgl-2.0.1/jar/lwjgl.jar:./lib/lwjgl-2.0.1/jar/lwjgl-util.jar:./lib/jmf.jar:./lib/lwjgl-2.0.1/jar/lwjgl_util.jar:./lib/lwjgl-2.0.1/jar/lwjgl_util_applet.jar:./lib/lwjgl-2.0.1/jar/lwjgl_test.jar.jar:./lib/jmf.jar:./src" 
SOURCEPATH+="./src"

compile:
	if [ ! -d bin ]; then mkdir bin; fi
	javac -sourcepath $(SOURCEPATH) -d bin src/gdi1sokoban/Application.java 


clean:
	if [ -d bin ]; then rm -rf bin; fi