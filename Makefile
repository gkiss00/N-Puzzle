all :				
					javac Puzzle.java
					javac Generator.java

run :				
					javac Generator.java
					java Generator 3 2000 1
					javac Puzzle.java
					java Puzzle puzzle.txt 1

clean :				
					rm -rf *.class
					rm -rf */*.class


.PHONY:				all clean