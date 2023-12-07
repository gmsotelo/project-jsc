import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;

/**
 * @author gsotelo
 * Generates an intuitive interface for the user to interact with
 */
public class UserInterface {
    private Path userFile;
    private JTextField browseText;
    private JTextField output;
    private Dictionary dictionary;
    private ArrayList<String> corrections = new ArrayList<>();

    /**
     * Constructor creates the spellchecker app interface
     */
    public UserInterface() {
        dictionary = new Dictionary();
        dictionary.wordList("words_alpha.txt"); // Load words from a file

        JFrame frame = new JFrame("Project JSC (Java SpellChecker)");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1012, 616);

        JPanel mainPanel = new JPanel(new GridLayout(6, 3));

        // Heading 1 Label
        JLabel heading1 = new JLabel("Java Spell Checker.");
        heading1.setFont(new Font("SANS_SERIF", Font.BOLD, 32));
        mainPanel.add(createPanel(heading1));

        // Heading 2 Label
        JLabel heading2 = new JLabel("Choose a ASCII Based File to SpellCheck");
        heading2.setFont(new Font("SANS_SERIF", Font.PLAIN, 18));
        heading2.setForeground(Color.GRAY);
        mainPanel.add(createPanel(heading2));

        JPanel browse = new JPanel();
        browseText = new JTextField("Spellcheck...");
        browseText.setFont(new Font("SANS_SERIF", Font.PLAIN, 18));
        browseText.setForeground(Color.LIGHT_GRAY);
        browseText.setPreferredSize(new Dimension(300, 30));

        // Browse File Button
        JButton browseFiles = new JButton("Browse");
        browseFiles.setFont(new Font("SANS_SERIF", Font.PLAIN, 18));
        browseFiles.setForeground(Color.WHITE);
        browseFiles.setBackground(Color.DARK_GRAY);
        browseFiles.addActionListener(e -> {
            try {
                inputFile();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        });

        browse.add(browseText);
        browse.add(browseFiles);
        mainPanel.add(createPanel(browse));

        // Output Text Box
        output = new JTextField("Output...");
        output.setFont(new Font("SANS_SERIF", Font.PLAIN, 14));
        output.setForeground(Color.LIGHT_GRAY);
        output.setPreferredSize(new Dimension(400, 30));
        mainPanel.add(createPanel(output));

        JPanel bottomButtons = new JPanel();
        // Download Button
        JButton download = new JButton("Download");
        download.setFont(new Font("SANS_SERIF", Font.PLAIN, 18));
        download.setForeground(Color.WHITE);
        download.setBackground(Color.DARK_GRAY);
        download.addActionListener(e -> downloadAction());

        // Reset Button
        JButton reset = new JButton("Reset");
        reset.setFont(new Font("SANS_SERIF", Font.PLAIN, 18));
        reset.setForeground(Color.WHITE);
        reset.setBackground(Color.DARK_GRAY);
        reset.addActionListener(e -> resetAction());

        bottomButtons.add(download);
        bottomButtons.add(reset);
        mainPanel.add(createPanel(bottomButtons));

        frame.add(mainPanel);
        frame.setVisible(true);
    }

    /**
     * A container to hold swing components
     * @param component A swing component that will be attached to the JPanel
     * @return A JPanel container with the provided swing components
     */
    private JPanel createPanel(Component component) {
        JPanel panel = new JPanel();
        panel.add(component);
        return panel;
    }

    /**
     * Allows the user to select a save location of their spellcheck output; provides prompts before overwriting any files
     */
    private void downloadAction() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Save File");
        String outputString = String.join(". ", corrections);

        int userSelection = fileChooser.showSaveDialog(null);

        if (userSelection == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();

            try {
                if (selectedFile.exists()) {
                    int result = JOptionPane.showConfirmDialog(null,
                            "File already exists. Do you want to overwrite it?",
                            "File Exists", JOptionPane.YES_NO_OPTION);

                    if (result != JOptionPane.YES_OPTION) {
                        return; // Cancel the download if the user chooses not to overwrite
                    }
                }

                Files.writeString(selectedFile.toPath(), outputString);

                JOptionPane.showMessageDialog(null,
                        "File downloaded successfully.",
                        "Download Successful", JOptionPane.INFORMATION_MESSAGE);
            } catch (IOException e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(null,
                        "Error downloading file.",
                        "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    /**
     * Resets the labels containing the file path and outputted text
     */
    private void resetAction() {
        browseText.setText("");
        output.setText("");
        corrections.clear();
    }

    /**
     * Provides the logical algorithm needed to detect misspelled words, double word repetitions, and mis-capitalization
     * @param input A string that needs to be spellchecked
     */
    public void checkWord(String input) {
        dictionary.userWordList("user_dict.txt");
        String[] sentences = input.split("\\. ");

        for (String sentence : sentences) {
            String[] words = sentence.split(" ");

            // Check for double word repetitions
            for (int j = 0; j < words.length - 1; j++) {
                if (words[j].equalsIgnoreCase(words[j + 1])) {
                    SpellcheckerApp options = new SpellcheckerApp(null, words[j], sentence,
                            Collections.singletonList("Delete redundant word"),
                            "Double Word Repetition");
                    options.setVisible(true);
                    if (options.getSelectedReplacement() != null) {
                        sentence = sentence.replaceFirst(words[j], "");
                    }
                }
            }

            for (String word : words) {
                // Check for mis-capitalized words at the beginning of each sentence
                if (!Character.isUpperCase(words[0].charAt(0))) {
                    SpellcheckerApp options = new SpellcheckerApp(null, word, sentence,
                            Collections.singletonList(words[0].substring(0, 1).toUpperCase() + words[0].substring(1)),
                            "First word in sentence should be capitalized");
                    options.setVisible(true);
                    if (options.getSelectedReplacement() != null) {
                        sentence = sentence.replace(words[0], options.getSelectedReplacement());
                        words[0] = words[0].replace(words[0], options.getSelectedReplacement());
                    }
                }

                // Check dictionary to see if word is valid
                if (!dictionary.isValid(word) && !word.matches(".*\\d.*")) {
                    CheckWord suggestion = new CheckWord(word, dictionary.getSuggestions(word));
                    SpellcheckerApp options = new SpellcheckerApp(null, word, sentence,
                            suggestion.getSuggestedWords(),
                            "Misspelled Word");
                    options.setVisible(true);
                    if (options.getSelectedReplacement() != null) {
                        sentence = sentence.replace(word, options.getSelectedReplacement());
                    }
                }

                // Check for mixed capitalization within each word
                if (!word.substring(1).equals(word.substring(1).toLowerCase())) {
                    CheckWord suggestion = new CheckWord(word, Collections.singletonList(word.toLowerCase()));
                    SpellcheckerApp options = new SpellcheckerApp(null, word, sentence,
                            suggestion.getSuggestedWords(),
                            "Mixed Capitalization");
                    options.setVisible(true);
                    if (options.getSelectedReplacement() != null) {
                        sentence = sentence.replace(word, options.getSelectedReplacement());
                    }
                }
            }
            corrections.add(sentence);
        }
    }

    /**
     * Calls the checkWord function and then writes the results to the output label
     * @param input A string that needs to be spellchecked
     */
    public void displayOutput(String input) {
        checkWord(input);
        String outputString = String.join(". ", corrections);
        output.setText(outputString);
    }

    /**
     * Prompts the user to select a text file on their device which will be used by the application
     * @throws IOException If the program encounters any issues with file handling
     */
    public void inputFile () throws IOException {
        if (userFile != null) {
            JOptionPane.showMessageDialog(null, "Selecting a new file will overwrite your previous output",
                    "Warning", JOptionPane.INFORMATION_MESSAGE);
            resetAction();
        }

        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Select File to Open");
        String text;

        FileDialog dialog = new FileDialog((Frame) null, "Select File to Open");
        dialog.setMode(FileDialog.LOAD);
        dialog.setVisible(true);
        String file = dialog.getFile();
        if (file == null) {
            JOptionPane.showMessageDialog(null, "Please choose a file.",
                    "Error", JOptionPane.INFORMATION_MESSAGE);
        } else {
            userFile = Path.of(file);
            browseText.setText(String.valueOf(userFile));
            text = Files.readString(Paths.get(String.valueOf(userFile)));
            output.setText(text);
            displayOutput(text);
        }
        dialog.dispose();
    }

    /**
     * Runs the UserInterface class until the user decides to exit
     * @param args Arguments for main. None are required by this app
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(UserInterface::new);
    }
}