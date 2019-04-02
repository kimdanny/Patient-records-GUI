import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

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

    Model model;

    // Panels
        // Back Panel
    private JPanel topPanel;
    private JPanel searchBackPanel;
    private JPanel listBackPanel;
    private JPanel textBackPanel;
        // search sub-panels
    private JPanel searchByIDAreaPanel;
    private JPanel searchByNameAreaPanel;
    private JPanel searchButtonPanel;

        // list sub-panels
    private JPanel listButtonPanel;
    private JPanel listStatButtonPanel;
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
    private JButton removeButton;
    private JButton showSinglePatientButton;
        // In textButtonPanel
    private JButton cutButton;
    private JButton copyButton;
    private JButton pasteButton;
    private JButton loadButton;
    private JButton saveButton;

    // Regarding Search
    // JTextField is one row text area -> good for search area
    private JTextField searchByIDArea;
    private JTextField searchByNameArea;

    // Regarding List
    private JList<String> list;
    private DefaultListModel<String> listModel;
    private JScrollPane listScroller;

    // Regarding text
    private JTextArea textArea;
    private JScrollPane textScroller;

    // constructor - main part of GUI
    public GUI() throws Exception {
        // initial Settings
        super("Patients GUI");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        pack();
        // Set size to ScreenSize
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int screenHeight = (int) screenSize.getHeight();
        int screenWidth = (int) screenSize.getWidth();
        setBounds(0, 0, screenWidth, screenHeight);

        createGUI();
        setList();

        setVisible(true);

        model = new Model(loadFile());
    }



    public void createGUI(){
        createListScrollerPanel();
        createListButtonPanel();
        createStatisticsListButtonPanel();
        createListBackPanel();

        createTextAreaPanel();
        createTextButtonPanel();
        createTextBackPanel();

        createSearchByIDAreaPanel();
        createSearchByNameAreaPanel();
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

    private void createSearchByIDAreaPanel(){
        searchByIDArea = new JTextField();
        searchByIDArea.setColumns(35);
        searchByIDAreaPanel = new JPanel(new BorderLayout());
        searchByIDAreaPanel.setBorder(BorderFactory.createTitledBorder("Search by ID"));
        searchByIDAreaPanel.add(searchByIDArea, BorderLayout.CENTER);
    }

    private void createSearchByNameAreaPanel(){
        searchByNameArea = new JTextField();
        searchByNameArea.setColumns(15);
        searchByNameAreaPanel = new JPanel(new BorderLayout());
        searchByNameAreaPanel.setBorder(BorderFactory.createTitledBorder("Search by First Name"));
        searchByNameAreaPanel.add(searchByNameArea, BorderLayout.CENTER);
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
        searchBackPanel.add(searchByIDAreaPanel, BorderLayout.WEST);
        searchBackPanel.add(searchByNameAreaPanel, BorderLayout.CENTER);
        searchBackPanel.add(searchButtonPanel, BorderLayout.EAST);
    }

    /*********************
     * List area (West)*
     *********************/

    private void setList() {
        listModel = new DefaultListModel<>();
        list.setModel(listModel);
    }

    private void createListScrollerPanel(){
        list = new JList<>();
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
        createRemoveButton();
        createShowSinglePatientButton();
        listButtonPanel.add(removeButton, 0);
        listButtonPanel.add(showSinglePatientButton, 1);
    }

    private void createStatisticsListButtonPanel(){
        listStatButtonPanel = new JPanel();
        createAverageAgeButton();
        createMostCommonYearButton();
        listStatButtonPanel.add(averageAgeButton, 0);
        listStatButtonPanel.add(mostCommonYearButton, 1);
    }

    private void createListBackPanel(){
        listBackPanel = new JPanel(new BorderLayout());
        listBackPanel.setBorder(BorderFactory.createTitledBorder("Searched result"));
        listBackPanel.add(listScroller, BorderLayout.NORTH);
        listBackPanel.add(listButtonPanel, BorderLayout.CENTER);
        listBackPanel.add(listStatButtonPanel, BorderLayout.SOUTH);
    }


    /*******************
     * Text area (East)*
     *******************/
    private void createTextAreaPanel(){
        textArea = new JTextArea();
        textArea.setColumns(40);
        //textArea.setText("Load files or type here");
        textScroller = new JScrollPane(textArea);
        textAreaPanel = new JPanel(new BorderLayout());
        textAreaPanel.add(textScroller, BorderLayout.CENTER);

    }

    private void createTextButtonPanel(){
        textButtonPanel = new JPanel();
        createLoadButton();
        createSaveButton();
        createClearButton();
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
        loadButton = new JButton("Load CSV/JSON");
        loadButton.addActionListener((ActionEvent e) -> loadFile());
    }

    private void createSaveButton() {
        saveButton = new JButton("Save as JSON");
        saveButton.addActionListener((ActionEvent e) -> saveFile());
    }

    private void createClearButton() {
        cutButton = new JButton("Clear text");
        cutButton.addActionListener((ActionEvent e) -> clear());
    }

    private void createCopyButton() {
        copyButton = new JButton("Copy text");
        copyButton.addActionListener((ActionEvent e) -> copy());
    }

    private void createPasteButton() {
        pasteButton = new JButton("Paste text");
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

    private void createRemoveButton(){
        removeButton = new JButton("Remove");
        removeButton.addActionListener((ActionEvent e) -> removeButtonClicked());
    }

    private void createShowSinglePatientButton(){
        showSinglePatientButton = new JButton("Show the Patient Information");
        showSinglePatientButton.addActionListener((ActionEvent e) -> showSinglePatientButtonClicked());
    }



    /**
     * Controller
     * Below methods are responses for the action
     * loadFile and saveFile methods are inspired by Dr.Graham Roberts' example code
     */
    private String loadFile()
    {
        JFileChooser fc = new JFileChooser();
        int returnVal = fc.showOpenDialog(this);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            File file = fc.getSelectedFile();
            try
            {
                String filepath = file.getAbsolutePath();
                return filepath;
            }
            catch (Exception exp)
            {
                JOptionPane.showMessageDialog(this, "Unable to load the file", "File Error",
                        JOptionPane.ERROR_MESSAGE);
            }
        }
        return null;
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
                StringBuilder allPatientJSON = model.getAllPatientJSON();
                BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(file +".json"));
                bufferedWriter.write(String.valueOf(allPatientJSON));
                bufferedWriter.close();
                JOptionPane.showMessageDialog(this, "All patients information has been saved in json format");

            }
            catch (Exception exp)
            {
                JOptionPane.showMessageDialog(this, "Unable to save the file", "File Error",
                        JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void clear() {
        textArea.setText("");
    }

    private void copy() {
        textArea.copy();
        textArea.requestFocus();
    }

    private void paste() {
        textArea.paste();
        textArea.requestFocus();
    }

    private void searchButtonClicked(){

        String inputID = searchByIDArea.getText();
        String inputFirst = searchByNameArea.getText();

        try {
            // ID is distinct value so just add one element to the listModel
            if (inputID.length() > 0) {
                listModel.clear();
                listModel.addElement(model.searchByID(inputID).getID());
                searchByIDArea.setText("");

            }
            // Name is not a distinct value so add inside the for loop (patientList can hold multiple names)
            if (inputFirst.length() > 0) {
                List<Patient> patientList = model.searchByFirst(inputFirst);
                listModel.clear();
                for (Patient patient : patientList) {
                    listModel.addElement(patient.getFIRST() + " " + patient.getLAST());
                }
                searchByNameArea.setText("");
            }

        }
        catch (NullPointerException e){
            // when nothing found, popup
            JOptionPane.showMessageDialog(this, "No Result Found", "Error Message",
                    JOptionPane.ERROR_MESSAGE);
            searchByIDArea.setText("");
            searchByNameArea.setText("");
        }

    }

    private void loadAllPatientsButtonClicked(){

        List<String> allPatientID = model.getIDOfAllPatient();
        for (String patient : allPatientID){
            listModel.addElement(patient);
        }
    }

    private void averageAgeButtonClicked(){

        String averageAge = model.getAverageAge();
        JOptionPane.showMessageDialog(this, "Average Age of all patients is " + averageAge,
                "Average Age", JOptionPane.INFORMATION_MESSAGE);
    }

    private void mostCommonYearButtonClicked(){

        String commonYear = model.getMostCommonYearOfBirth();
        JOptionPane.showMessageDialog(this, "The Most common year of birth of all patients is " + commonYear,
                "Most common year of birth", JOptionPane.INFORMATION_MESSAGE);
    }

    private void removeButtonClicked(){
        int i = list.getSelectedIndex();
        if (!(i < 0) || (i > listModel.size())) {
            listModel.remove(i);
        }
        removeButton.setEnabled(false);
    }

    private void showSinglePatientButtonClicked(){
        String selectedValue = list.getSelectedValue();

        // This is the case where ID is clicked
        if (selectedValue.contains("-")){
            StringBuilder singlePatientJSONID =  model.getSinglePatientJSONbyID(selectedValue);
            // write singlePatientJSONID in textArea
            textArea.setText("");
            // append allows users to compare two or more patients in the same textArea
            textArea.append(String.valueOf(singlePatientJSONID));
        }

        // This is the case where name is clicked
        else {
            // separate first and last name
            String name[] = selectedValue.split(" ");
            final String firstName = name[0];
            final String lastName = name[1];
            StringBuilder singlePatientJSONName = model.getSinglePatientJSONbyName(firstName, lastName);
            // write singlePatientJSONName in textArea
            textArea.setText("");
            // append allows users to compare two or more patients in the same textArea
            textArea.append(String.valueOf(singlePatientJSONName));
        }

    }


    private void listClicked() {
        int n = list.getSelectedIndex();
        if (!(n < 0) || (n > listModel.size())) {
            removeButton.setEnabled(true);
            showSinglePatientButton.setEnabled(true);
        }
    }


    public static void main(String args[]){
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                try {
                    new GUI();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

}
