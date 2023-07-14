# Wordle
Java Clone of the web-based game _Wordle_


## Table of Content:
- [Screenshots](#screenshots)
- [Technologies](#technologies)
- [Setup](#setup)
- [Approach](#approach)
- [Status](#status)

## Screenshots

![image](https://github.com/arjuntrivedi/Wordle/assets/72959325/54f2b584-3acf-4c23-a59b-70bf66c60e3e)
![image](https://github.com/arjuntrivedi/Wordle/assets/72959325/cc856ae1-5063-48e2-9876-2c8cb9524477)
![image](https://github.com/arjuntrivedi/Wordle/assets/72959325/7a5b66ce-535e-4ac0-926a-93e1a828689c)


## Technologies
I used `Java`, `JavaFXGUI`, and `Gradle`

## Setup
- create a Gradle project and install all JavaFX dependencies
- download or clone the repository
- run `WordleApplication.java


## Approach
  1. **Requirements:** I identified the required components for the project, such as the game board, word dictionary, player data, and more.
  2. **Design:** I organized the program functionalities into various classes. Instances of classes were used in other classes to utilize Java's OOP features. For instance, individual letters and words were saved as objects, making it easier to perform operations on them (checking if the word is correct and determining letter color result).
  3. **Implementation:** The rules of the Wordle game were first implemented in the classes and tested in the _console_. Then, the Java GUI was created which used the same game rule implementation created earlier. Therefore, the user can play the game in both the Command Line and GUI formats.

## Status
The game is complete and ready for playing!
