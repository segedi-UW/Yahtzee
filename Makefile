run: compile
	java Yahtzee -i
compile: Game.class Player.class Yahtzee.class
Game.class: Game.java
	javac Game.java
Player.class: Player.java
	javac Player.java
Yahtzee.class: Yahtzee.java
	javac Yahtzee.java
test: testCompile
testCompile: compile 
clean:
	rm *.class
