# Spellchecker Application
The Project JSC spellchecker application is designed to check and correct misspelled words in text files using a dictionary and a suggestion algorithm.

## Files

1.  **SpellcheckerApp.java**
    -   Main class responsible for coordinating the spellchecking process.
2.  **UserInterface.java**
    -   Manages the user interface using Java Swing to allow users to input files, perform spellchecks, and view suggestions.
3.  **Dictionary.java**
    -   Represents the dictionary containing a list of valid words.
4.  **CheckWord.java**
    -   Represents a class to manage misspelled words and their suggested corrections.
5.  **words_alpha.txt**
    -   A text file containing a list of valid English words, used as the dictionary.
6.  **user_dict.txt**
    -   A text file containing a list of custom user words, used as the dictionary.

## Build and Run Instructions
### Build the application:
1. Open a terminal and navigate to the project directory.
2. Compile the Java files using the following command:
```
javac *.java
```
### Run the application:
1. Run the application using the following command:
```
java UserInterface
```
2. The GUI will open, allowing you to browse and select a text file for spellchecking.
3. Perform spellchecking, view suggestions, and choose to save or discard changes.

### User Dictionary
-   You can add words to your user-specific dictionary by selecting the "Add to Dictionary" option in the user interface.

### Notes
-   Ensure that both `words_alpha.txt` and `user_dict.txt` are present in the project directory.
