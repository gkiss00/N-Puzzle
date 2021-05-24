all :				
					javac Puzzle.java
					javac Generator.java

clean :				
					rm -rf *.class
					rm -rf */*.class


.PHONY:				all clean