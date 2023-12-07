import java.util.List;

/**
 * @author gsotelo
 * Holds the original word and the replacement suggestions
 */
public class CheckWord {
    private String originalWord;
    private List<String> suggestedWords;

    /**
     * Constructor to initialize a String for the original misspelled word and the list of suggested replacement words
     * @param originalWord The original misspelled word
     * @param suggestedWords A list of suggested replacement words
     */
    public CheckWord(String originalWord, List<String> suggestedWords) {
        this.originalWord = originalWord;
        this.suggestedWords = suggestedWords;
    }

    /**
     * Accessor to retrieve the original misspelled word
     * @return the original misspelled word
     */
    public String getOriginalWord() {
        return originalWord;
    }

    /**
     * Accessor to retrieve the list of suggested replacement words
     * @return the list of suggested replacement words
     */
    public List<String> getSuggestedWords() {
        return suggestedWords;
    }
}