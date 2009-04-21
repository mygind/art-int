
if [ -d "/System/Library/Frameworks/JavaVM.framework/Versions/1.6.0/Home/bin/" ]; then
/System/Library/Frameworks/JavaVM.framework/Versions/1.6.0/Home/bin/java -Xmx128m -cp ".:./bin:./src_test:./test:./lib/lwjgl-2.0.1/jar/jinput.jar:./lib/lwjgl-2.0.1/jar/lwjgl.jar:./lib/lwjgl-2.0.1/jar/lwjgl-util.jar:./lib/jmf.jar:./lib/lwjgl-2.0.1/jar/lwjgl_util.jar:./lib/lwjgl-2.0.1/jar/lwjgl_util_applet.jar:./lib/lwjgl-2.0.1/jar/lwjgl_test.jar.jar:./lib/jmf.jar" -Djava.library.path="lib/lwjgl-2.0.1/native/linux/:lib/lwjgl-2.0.1/native/macosx/" gdi1sokoban.Application
else java -Xmx128m -cp ".:./bin:./src_test:./test:./lib/lwjgl-2.0.1/jar/jinput.jar:./lib/lwjgl-2.0.1/jar/lwjgl.jar:./lib/lwjgl-2.0.1/jar/lwjgl-util.jar:./lib/jmf.jar:./lib/lwjgl-2.0.1/jar/lwjgl_util.jar:./lib/lwjgl-2.0.1/jar/lwjgl_util_applet.jar:./lib/lwjgl-2.0.1/jar/lwjgl_test.jar.jar:./lib/jmf.jar" -Djava.library.path="lib/lwjgl-2.0.1/native/linux/:lib/lwjgl-2.0.1/native/macosx/" gdi1sokoban.Application
fi
