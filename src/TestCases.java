import java.util.Arrays;
import java.util.List;

public class TestCases {

    public static void main(String[] args) {
        // CheckWord Tests
        String originalWord = "exampel";
        List<String> suggestedWords = Arrays.asList("example", "exemplar");
        CheckWord checkWord = new CheckWord(originalWord, suggestedWords);

        // Testing getOriginalWord method
        if (originalWord.equals(checkWord.getOriginalWord())) {
            System.out.println("getOriginalWord Test Passed");
        } else {
            System.out.println("getOriginalWord Test Failed");
        }

        // Testing getSuggestedWords method
        if (suggestedWords.equals(checkWord.getSuggestedWords())) {
            System.out.println("getSuggestedWords Test Passed");
        } else {
            System.out.println("getSuggestedWords Test Failed");
        }

        // Dictionary Tests
        testWordList();
        testUserWordList();
        testAddUserDictionary();
        testIsValid();
        testGetSuggestions();
    }

    private static void testWordList() {
        Dictionary dictionary = new Dictionary();
        dictionary.wordList("words_alpha.txt");
        System.out.println("Word List Test Passed");
    }

    private static void testUserWordList() {
        Dictionary dictionary = new Dictionary();
        dictionary.userWordList("user_dict.txt");
        System.out.println("User Word List Test Passed");
    }

    private static void testAddUserDictionary() {
        Dictionary dictionary = new Dictionary();
        dictionary.addUserDictionary("testword");
        System.out.println("Add User Dictionary Test Passed");
    }

    private static void testIsValid() {
        Dictionary dictionary = new Dictionary();
        dictionary.wordList("words_alpha.txt");
        dictionary.userWordList("user_dict.txt");

        boolean valid = dictionary.isValid("testword");
        if (valid) {
            System.out.println("IsValid Test Passed");
        } else {
            System.out.println("IsValid Test Failed");
        }
    }

    private static void testGetSuggestions() {
        Dictionary dictionary = new Dictionary();
        dictionary.wordList("words_alpha.txt");
        dictionary.userWordList("user_dict.txt");

        List<String> suggestions = dictionary.getSuggestions("testwor");

        // Check if suggestions is not null and has elements
        if (suggestions != null && !suggestions.isEmpty()) {
            System.out.println("Get Suggestions Test Passed");
        } else {
            System.out.println("Get Suggestions Test Failed");
        }
    }
}
