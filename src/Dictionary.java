import java.io.*;
import java.util.*;

/**
 * @author gsotelo
 * Class containing the app's default dictionary and the user dictionary
 */
public class Dictionary {
    private Set<String> wordSet;
    private Set<String> userDictionary;

    /**
     * Constructor initializes the default dictionary and the user dictionary
     */
    public Dictionary() {
        wordSet = new HashSet<>();
        userDictionary = new HashSet<>();
    }

    /**
     * Reads the content of the dictionary file and writes it to memory
     * @param filename The dictionary file to be read
     */
    public void wordList(String filename) {
        try (Scanner scanner = new Scanner(new File(filename))) {
            while (scanner.hasNext()) {
                wordSet.add(scanner.next().toLowerCase());
            }
        } catch (FileNotFoundException e) {
            System.err.println("Dictionary file not found: " + filename);
        }
    }

    /**
     * Reads the content of the user dictionary file and writes it to memory
     * @param filename The user dictionary file to be read
     */
    public void userWordList(String filename) {
        try (Scanner scanner = new Scanner(new File(filename))) {
            while (scanner.hasNext()) {
                userDictionary.add(scanner.next().toLowerCase());
            }
        } catch (FileNotFoundException e) {
            System.err.println("Dictionary file not found: " + filename);
        }
    }

    /**
     * Adds a misspelled word to the user's dictionary
     * @param word A misspelled word
     */
    public void addUserDictionary(String word) {
        try {
            BufferedWriter bw = new BufferedWriter(new FileWriter("user_dict.txt", true));
            bw.append(word);
            bw.newLine();
            bw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Validates whether the word exists in either the default or user's dictionaries
     * @param word A misspelled word
     * @return True if the word is in either dictionary, false otherwise
     */
    public boolean isValid(String word) {
        word = word.replaceAll("\\W+|\\d+", "");
        return wordSet.contains(word.toLowerCase()) || userDictionary.contains(word.toLowerCase());
    }

    /**
     * Generates suggestions for a word using a different number of algorithms
     * @param word The word being spellchecked
     * @return A list of valid words suggested by the algorithm
     */
    public List<String> getSuggestions(String word) {

        List<String> suggestions = new ArrayList<>();

        // generate suggestions by removing letters
        for (int i = 0; i < word.length(); i++) {
            String suggestion = word.substring(0, i) + word.substring(i + 1);
            if (isValidSuggestion(suggestion)) {
                suggestions.add(suggestion);
            }
        }

        // generate suggestions by inserting letters
        for (char c = 'a'; c <= 'z'; c++) {
            for (int i = 0; i <= word.length(); i++) {
                String suggestion = word.substring(0, i) + c + word.substring(i);
                if (isValidSuggestion(suggestion)) {
                    suggestions.add(suggestion);
                }
            }
        }

        // generate suggestions by swapping consecutive letters
        for (int i = 0; i < word.length() - 1; i++) {
            String suggestion = word.substring(0, i) + word.charAt(i + 1) + word.charAt(i) + word.substring(i + 2);
            if (isValidSuggestion(suggestion)) {
                suggestions.add(suggestion);
            }
        }

        // generate suggestions by inserting space or hyphen
        for (int i = 1; i < word.length(); i++) {
            String suggestionSpace = word.substring(0, i) + " " + word.substring(i);
            String suggestionHyphen = word.substring(0, i) + "-" + word.substring(i);

            if (isValidSuggestion(suggestionSpace)) {
                suggestions.add(suggestionSpace);
            }

            if (isValidSuggestion(suggestionHyphen)) {
                suggestions.add(suggestionHyphen);
            }
        }
        return suggestions;
    }

    /**
     * Helper method to check if a suggested word is also a valid word
     * @param suggestion The suggested word
     * @return True if valid, false otherwise
     */
    private boolean isValidSuggestion (String suggestion){
        if (userDictionary != null) {
            return wordSet.contains(suggestion) || userDictionary.contains(suggestion);
        } else
            return wordSet.contains(suggestion);
    }
}