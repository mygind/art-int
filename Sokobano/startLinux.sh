export DISPLAY=:0
java -Xmx128m -cp ".:./bin:./src_test:./test:./lib/lwjgl-2.0.1/jar/jinput.jar:./lib/lwjgl-2.0.1/jar/lwjgl.jar:./lib/lwjgl-2.0.1/jar/lwjgl-util.jar:./lib/jmf.jar:./lib/lwjgl-2.0.1/jar/lwjgl_util.jar:./lib/lwjgl-2.0.1/jar/lwjgl_util_applet.jar:./lib/lwjgl-2.0.1/jar/lwjgl_test.jar.jar:./lib/jmf.jar" -Djava.library.path="lib/lwjgl-2.0.1/native/linux/" gdi1sokoban.Application
