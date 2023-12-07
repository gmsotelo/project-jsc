import javax.swing.*;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultHighlighter;
import javax.swing.text.Highlighter;
import java.awt.*;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author gsotelo
 * Generates a JDialog prompt to provide user with spellchecking options
 */
public class SpellcheckerApp extends JDialog {
    private JTextField misspelledSentenceField;
    private JButton ignoreButton;
    private JButton addToDictionaryButton;
    private JButton confirmButton;
    private JComboBox<String> replacementOptions;

    private String selectedReplacement;
    private String misspelledWord;
    private Dictionary dictionary;

    /**
     * Constructor initializes a dialog box with prompts for the user to correct the misspelled word
     * @param parent Provides a frame for the dialog box
     * @param mispelledWord A single instance of a misspelled word which the user has the option of spellchecking
     * @param misspelledSentence The sentence containing the misspelled word
     * @param replacementSuggestions Suggested replacement words generated by the Dictionary class
     * @param title The name of the dialog box
     */
    public SpellcheckerApp(Frame parent, String mispelledWord,
                            String misspelledSentence, List<String> replacementSuggestions, String title) {
        super(parent, title, true);
        this.misspelledWord = mispelledWord;
        initComponents(misspelledSentence, replacementSuggestions);
        dictionary = new Dictionary();
    }

    /**
     * Generates a dialog box using the given parameters
     * @param misspelledSentence The sentence containing the misspelled word
     * @param replacementSuggestions Suggested replacement words generated by the Dictionary class
     */
    private void initComponents(String misspelledSentence, List<String> replacementSuggestions) {
        misspelledSentenceField = new JTextField(misspelledSentence);
        misspelledSentenceField.setEditable(false);
        highlightMisspelledWord(misspelledSentence);

        if (replacementSuggestions.isEmpty()) {
            replacementSuggestions = Collections.singletonList("None Available");
        }
        
        replacementOptions = new JComboBox<>(replacementSuggestions.toArray(new String[0]));

        ignoreButton = new JButton("Ignore");
        ignoreButton.addActionListener(e -> ignoreAction());

        addToDictionaryButton = new JButton("Add to Dictionary");
        addToDictionaryButton.addActionListener(e -> addToDictionaryAction());

        confirmButton = new JButton("Confirm");
        confirmButton.addActionListener(e -> confirmAction());

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayout(3, 1));
        buttonPanel.add(ignoreButton);
        buttonPanel.add(addToDictionaryButton);
        buttonPanel.add(confirmButton);

        JPanel optionsPanel = new JPanel(new BorderLayout());
        optionsPanel.add(new JLabel("Suggestion:"), BorderLayout.WEST);
        optionsPanel.add(replacementOptions, BorderLayout.CENTER);

        JPanel contentPanel = new JPanel(new BorderLayout());
        contentPanel.add(misspelledSentenceField, BorderLayout.NORTH);
        contentPanel.add(optionsPanel, BorderLayout.CENTER);
        contentPanel.add(buttonPanel, BorderLayout.EAST);

        setContentPane(contentPanel);
        pack();
        setLocationRelativeTo(null);
    }

    /**
     * Uses the Highlighter class to highlight the misspelled word in yellow
     * @param misspelledSentence The sentence containing the misspelled word
     */
    private void highlightMisspelledWord(String misspelledSentence) {
        misspelledSentenceField.setText(misspelledSentence);
        Highlighter highlighter = misspelledSentenceField.getHighlighter();
        Highlighter.HighlightPainter painter = new DefaultHighlighter.DefaultHighlightPainter(Color.YELLOW);

        // This pattern accounts for contractions or possessive forms.
        String regex = "\\b" + Pattern.quote(misspelledWord) + "\\b|\\b" + Pattern.quote(misspelledWord) + "'\\w+\\b";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(misspelledSentence);

        while (matcher.find()) {
            try {
                highlighter.addHighlight(matcher.start(), matcher.end(), painter);
            } catch (BadLocationException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Ignores the misspelled word and does nothing.
     */
    private void ignoreAction() {
        selectedReplacement = null; // No replacement selected
        dispose(); // Close the dialog
    }

    /**
     * Add the misspelled word to the user dictionary
     */
    private void addToDictionaryAction() {
        selectedReplacement = null;
        dictionary.addUserDictionary(misspelledWord);
        dispose(); // Close the dialog
    }

    /**
     * Allows the user to confirm the selected replacement word is correct
     */
    private void confirmAction() {
        if (Objects.requireNonNull(replacementOptions.getSelectedItem()).toString().equals("None Available"))
            ignoreAction();
        else
            selectedReplacement = (String) replacementOptions.getSelectedItem();
        dispose(); // Close the dialog
    }

    /**
     * Accessor method to retrieve the user's selected replacement word from the JComboBox dropdown menu
     * @return The selected replacement word
     */
    public String getSelectedReplacement() {
        return selectedReplacement;
    }
}