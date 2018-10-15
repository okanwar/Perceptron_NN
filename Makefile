jc javac c compile: 
	# test -d bin || mkdir bin
	# javac -d bin *.java
	javac *.java

cr: compile 

clean:
	-rm *.class
