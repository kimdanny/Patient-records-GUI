import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

/**
 * <Create a simple dashboard application>
 *
 * 1. Load csv file selected via a file chooser
 * 2. Save a JSON format file and read it back in again as an alternative to csv format
 * 3. Display the list of patient in a list view, so that when one is selected the full patient info is displayed
 * 4. Support searching by at least the patient name and ideally by other fields as well. Searches can display multiple results
 * 5. Add additional functionality such as simple statistics (average age or age distribution, most common year of birth)
 *
 * Rules
 * - Make use of public methods in class Model. No direct use of the other classes
 *
 * Advice
 * - Model class will need to be split into several classes as you add code
 * - You may want to introduce one or more Controller classes to coordinate the interaction between the UI and the Model class
 *
 */

public class GUI extends JFrame {

    Model model = new Model();

    // Panels
        // Back Panel
    private JPanel topPanel;
    private JPanel searchBackPanel;
    private JPanel listBackPanel;
    private JPanel textBackPanel;
        // search sub-panels
    private JPanel searchAreaPanel;
    private JPanel searchButtonPanel;
        // list sub-panels
    //private JPanel listScrollerPanel;
    private JPanel listButtonPanel;
        // text sub-panels
    private JPanel textAreaPanel;
    private JPanel textButtonPanel;

    // Buttons
        // In searchButtonPanel
    private JButton searchButton;
    private JButton loadAllPatientsButton;
        // In listButtonPanel
    private JButton averageAgeButton;
    private JButton mostCommonYearButton;
        // In textButtonPanel
    private JButton cutButton;
    private JButton copyButton;
    private JButton pasteButton;
    private JButton loadButton;
    private JButton saveButton;

    // Regarding Search
    private JTextArea searchArea;

    // Regarding List
    private JList<String> list;
    private DefaultListModel<String> listModel;
    private JScrollPane listScroller;

    // Regarding text
    private JTextArea textArea;
    private JScrollPane textScroller;

    // constructor - main part of GUI
    public GUI() {
        super("Patient GUI");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        pack();
        setBounds(200, 100, 1000, 700);
        // initial settings for constructor
        // here put functions below
        createGUI();
        setList();


        setVisible(true);
    }

    public void createGUI(){
        //setTitle("Patient GUI with set title");
        createListScrollerPanel();
        createListButtonPanel();
        createListBackPanel();
        createTextAreaPanel();
        createTextButtonPanel();
        createTextBackPanel();
        createSearchAreaPanel();
        createSearchButtonPanel();
        createSearchBackPanel();

        createTopPanel(); // last function
    }

    private void createTopPanel() {

        topPanel = new JPanel(new BorderLayout());
        topPanel.add(searchBackPanel, BorderLayout.NORTH);
        topPanel.add(listBackPanel, BorderLayout.WEST);
        topPanel.add(textBackPanel, BorderLayout.CENTER);

        add(topPanel, BorderLayout.CENTER);
    }

    /*********************
     * Search area (North)*
     *********************/

    private void createSearchAreaPanel(){
        searchArea = new JTextArea();
        searchArea.setColumns(30);
        searchArea.setRows(1);
        searchArea.setText("Search here");
        searchAreaPanel = new JPanel(new BorderLayout());
        searchAreaPanel.setBorder(BorderFactory.createEtchedBorder());
        searchAreaPanel.add(searchArea, BorderLayout.CENTER);
    }

    private void createSearchButtonPanel(){
        searchButtonPanel = new JPanel();
        createSearchButton();
        createLoadAllPatientsButton();
        searchButtonPanel.add(searchButton);
        searchButtonPanel.add(loadAllPatientsButton);
    }

    private void createSearchBackPanel(){
        searchBackPanel = new JPanel(new BorderLayout());
        searchBackPanel.setBorder(BorderFactory.createLineBorder(Color.black, 2));
        searchBackPanel.add(searchAreaPanel, BorderLayout.WEST);
        searchBackPanel.add(searchButtonPanel, BorderLayout.CENTER);
    }

    /*********************
     * List area (West)*
     *********************/

    private void setList() {
        listModel = new DefaultListModel<String>();
        list.setModel(listModel);
    }

    private void createListScrollerPanel(){
        list = new JList<String>();
        list.addMouseListener(
                new MouseAdapter()
                {
                    @Override
                    public void mouseClicked(MouseEvent e)
                    {
                        listClicked();
                    }
                });
        listScroller = new JScrollPane(list);
    }

    private void createListButtonPanel(){
        listButtonPanel = new JPanel();
        createAverageAgeButton();
        createMostCommonYearButton();
        listButtonPanel.add(averageAgeButton);
        listButtonPanel.add(mostCommonYearButton);
    }

    private void createListBackPanel(){
        listBackPanel = new JPanel(new BorderLayout());
        listBackPanel.setBorder(BorderFactory.createTitledBorder("Searched result"));
        listBackPanel.add(listScroller, BorderLayout.NORTH);
        listBackPanel.add(listButtonPanel, BorderLayout.CENTER);
    }


    /*******************
     * text area (East)*
     *******************/
    private void createTextAreaPanel(){
        textArea = new JTextArea();
        textArea.setColumns(40);
        textArea.setText("Load files or type here");
        textScroller = new JScrollPane(textArea);
        textAreaPanel = new JPanel(new BorderLayout());
        //textAreaPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        textAreaPanel.add(textScroller, BorderLayout.CENTER);

    }

    private void createTextButtonPanel(){
        textButtonPanel = new JPanel(/*new GridLayout(1, 1, 1, 1)*/);
        createLoadButton();
        createSaveButton();
        createCutButton();
        createCopyButton();
        createPasteButton();
        textButtonPanel.add(loadButton);
        textButtonPanel.add(saveButton);
        textButtonPanel.add(cutButton);
        textButtonPanel.add(copyButton);
        textButtonPanel.add(pasteButton);
    }

    private void createTextBackPanel(){
        textBackPanel = new JPanel(new BorderLayout());
        textBackPanel.setBorder(BorderFactory.createTitledBorder("Patient(s) Information"));
        textBackPanel.add(textAreaPanel, BorderLayout.CENTER);
        textBackPanel.add(textButtonPanel, BorderLayout.SOUTH);
    }



    /**
     * Buttons Creation
     * These methods should be included in create'X'ButtonPanel()
     */
    private void createLoadButton() {
        loadButton = new JButton("Load");
        loadButton.addActionListener((ActionEvent e) -> loadFile());
    }

    private void createSaveButton() {
        saveButton = new JButton("Save");
        saveButton.addActionListener((ActionEvent e) -> saveFile());
    }

    private void createCutButton() {
        cutButton = new JButton("Cut");
        cutButton.addActionListener((ActionEvent e) -> cut());
    }

    private void createCopyButton() {
        copyButton = new JButton("Copy");
        copyButton.addActionListener((ActionEvent e) -> copy());
    }

    private void createPasteButton() {
        pasteButton = new JButton("Paste");
        pasteButton.addActionListener((ActionEvent e) -> paste());
    }

    private void createAverageAgeButton(){
        averageAgeButton = new JButton("Average Age");
        averageAgeButton.addActionListener((ActionEvent e) -> averageAgeButtonClicked());
    }

    private void createMostCommonYearButton(){
        mostCommonYearButton = new JButton("Most Common Year of Birth");
        mostCommonYearButton.addActionListener((ActionEvent e) -> mostCommonYearButtonClicked());
    }

    private void createSearchButton(){
        searchButton = new JButton("Search");
        searchButton.addActionListener((ActionEvent e) -> searchButtonClicked());
    }

    private void createLoadAllPatientsButton(){
        loadAllPatientsButton = new JButton("Load All Patients");
        loadAllPatientsButton.addActionListener((ActionEvent e) -> loadAllPatientsButtonClicked());
    }



    /**
     * Controller
     * Below methods are responses for the action
     */
    private void loadFile()
    {
        JFileChooser fc = new JFileChooser();
        int returnVal = fc.showOpenDialog(this);
        if (returnVal == JFileChooser.APPROVE_OPTION)
        {
            File file = fc.getSelectedFile();
            try
            {
                textArea.read(new FileReader(file), null);
            }
            catch (IOException exp)
            {
                JOptionPane.showMessageDialog(this, "Unable to load the file", "File Error",
                        JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void saveFile()
    {
        JFileChooser fc = new JFileChooser();
        int returnVal = fc.showSaveDialog(this);
        if (returnVal == JFileChooser.APPROVE_OPTION)
        {
            File file = fc.getSelectedFile();
            try
            {
                textArea.write(new FileWriter(file));
            }
            catch (IOException exp)
            {
                JOptionPane.showMessageDialog(this, "Unable to save the file", "File Error",
                        JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void cut()
    {
        textArea.cut();
        textArea.requestFocus();
    }

    private void copy()
    {
        textArea.copy();
        textArea.requestFocus();
    }

    private void paste()
    {
        textArea.paste();
        textArea.requestFocus();
    }

    private void searchButtonClicked(){
        /*
        * 1. search inputfield 에서 getText()를 해서 어떠한 변수(s)에 담아놓는다
        * 2. if s.length() > 0 :
        *   2-1. enhanced for loop을 이용하여 서치결과와 맞는 것들을 하나씩 리스트에 추가한다
        * 3. search inputfield.setText("");
        * */

    }

    private void loadAllPatientsButtonClicked(){
        /*
         * 1. enhanced for loop을 이용하여 all patients를 하나씩 리스트에 추가한다
         * */

    }

    private void averageAgeButtonClicked(){
        // do sth with this
        // print this out to pop up???
        model.getAverageAge();
    }

    private void mostCommonYearButtonClicked(){
        // do sth with this
        // print this out to pop up???
        String commonYear = model.getMostCommonYearOfBirth();
    }

    private void listClicked() {
        int n = list.getSelectedIndex();
        if (!(n < 0) || (n > listModel.size())) {
            // show clicked single patient in json format in text area
            //removeButton.setEnabled(true);
        }
    }



    public static void main(String args[]){
        SwingUtilities.invokeLater(GUI::new);
    }

}