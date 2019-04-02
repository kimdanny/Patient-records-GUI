import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.*;
import java.time.*;
/**
 * <Reference>
 * 1. private method 'mostCommonOne' is inspired by arshajii's answer.
 *   - Arshajii (2013) 'Java-get most common element in a list', Available at: https://stackoverflow.com/questions/19031213/java-get-most-common-element-in-a-list
 *
 */


public class Model {
    private ReadCSV readCSV = new ReadCSV();
    private ReadJSON readJSON = new ReadJSON();
    private List<Patient> patientList;

    private JSONFormatter jsonFormatter;
    private StringBuilder allPatientsJSON;


    // Read an input file and generate Patient objects
    public List<Patient> readFile(String filepath){
        return readCSV.CSVtoList(filepath);
    }
    public List<Patient> readJSON(String filepath) throws IOException {
        return readJSON.JSONtoList(filepath);
    }

    // Constructor that links file info to patientList and jsonFormatter
    public Model(String filepath) throws Exception {
        patientList = (filepath.endsWith(".csv")) ? this.readFile(filepath) : this.readJSON(filepath);
        jsonFormatter = new JSONFormatter(filepath);
        allPatientsJSON = jsonFormatter.allPatientsJSON();
    }

    /******************
     * Getter methods *
     ******************/
    // all patient in json format
    protected StringBuilder getAllPatientJSON(){
        return allPatientsJSON;
    }

    // single patient in json format searched by ID
    protected StringBuilder getSinglePatientJSONbyID(String MustBeID){
        return jsonFormatter.singlePatientJSONbyID(MustBeID);
    }

    // single patient in json format searched by first and last name
    protected StringBuilder getSinglePatientJSONbyName(String first, String last){
        return jsonFormatter.singlePatientJSONbyName(first, last);
    }

    protected List<String> getFirstNamesOfAllPatient(){
        List<String> nameList = new ArrayList<>();
        for (Patient patient : patientList){
            nameList.add(patient.getFIRST());
        }
        return nameList;
    }

    protected List<String> getIDOfAllPatient(){
        List<String> idList = new ArrayList<>();
        for (Patient patient : patientList){
            idList.add(patient.getID());
        }
        return idList;
    }

    protected List<Patient> getDeadPatients(){
        List<Patient> deadList = new ArrayList<>();
        for (Patient patient : patientList){
            if (patient.getDEATHDATE() != ""){
                deadList.add(patient);
            }
        }
        return deadList;
    }

    protected String getAverageAge(){
        List<Integer> ageList = new ArrayList<>();
        int sum = 0;
        int averageAge;

        //Getting current yy mm dd in int
        String[] current = getCurrentDate().split("-");
        int currentYear = Integer.parseInt(current[0]);
        int currentMonth = Integer.parseInt(current[1]);
        int currentDate = Integer.parseInt(current[2]);

        LocalDate currentTobeSubtractFrom = LocalDate.of(currentYear, currentMonth, currentDate);

        // Getting patient's yy mm dd in int
        for (Patient patient : patientList){
            String getBirthDate = patient.getBIRTHDATE();
            String[] birthDate = getBirthDate.split("-");
            int year = Integer.parseInt(birthDate[0]);
            int month = Integer.parseInt(birthDate[1]);
            int day = Integer.parseInt(birthDate[2]);

            LocalDate birthdate = LocalDate.of(year, month, day);

            int age = getAge(birthdate, currentTobeSubtractFrom);

            ageList.add(age);
        }

        // calculating average age
        for (int age : ageList){
            sum = sum + age;
        }
        averageAge = sum / ageList.size();

        return ""+averageAge;
    }

    // helper method for getAverageAge
    // return time difference in year as an int type
    private int getAge(LocalDate birthDate, LocalDate currentDate) {
        return Period.between(birthDate, currentDate).getYears();
    }

    // helper method for getAverageAge
    // return current yyyy-mm--dd in String
    private String getCurrentDate(){
        return LocalDate.now().toString();
    }



    protected String getMostCommonYearOfBirth(){
        List<String> yearsList = new ArrayList<>();

        for (Patient patient : patientList){
            String birthDate = patient.getBIRTHDATE();
            String[] result = birthDate.split("-");
            yearsList.add(result[0]);
        }
        return mostCommonOne(yearsList);
    }

    // helper class for getMostCommonYearOfBirth()
    // Finds most common element in the list and returns the one in String
    private String mostCommonOne(List<String> inputList) {

        Map<String, Integer> map = new HashMap<>();
        String resultKey = null;
        int tempNumber = 0;

        for(int x = 0; x < inputList.size(); x++) {

            Integer howMany = map.get(inputList.get(x));
            if(howMany == null) {
                map.put(inputList.get(x), 1);
            }
            else {
                map.put(inputList.get(x), howMany+1);
            }
        }

        for(Map.Entry<String, Integer> entry: map.entrySet()) {
            if(entry.getValue() > tempNumber) {
                resultKey = entry.getKey();
                tempNumber = entry.getValue();
            }
        }
        return resultKey;
    }


    /**
     * Search methods
     * ID is a distinct value, so if matched -> break
     * @param ID
     * @return Patient type
     */
    protected Patient searchByID(String ID){
        // traversing
        Patient matchedPatient = null;
        for (Patient patient : patientList){
            if (patient.getID().equals(ID)){
                matchedPatient = patient;
                break;
            }
        }
        return matchedPatient;
    }

    /**
     * Search methods
     * First name is not a distinct value, so no breaking
     * @param First
     * @return Patient type
     */
    protected List<Patient> searchByFirst(String First){
        List<Patient> result = new ArrayList<>();
        // traversing
        for (Patient patient : patientList){
            if (patient.getFIRST().equals(First)){
                result.add(patient);
            }
        }
        return result;
    }

    /**
     * Search methods
     * Last name is not a distinct value, so no breaking
     * @param Last
     * @return Patient type
     */
    protected List<Patient> searchByLast(String Last){
        List<Patient> result = new ArrayList<>();
        // traversing
        for (Patient patient : patientList){
            if (patient.getLAST().equals(Last)){
                result.add(patient);
            }
        }
        return result;
    }


    public List<Patient> getOldest(){
        List<Patient> oldestPatientList = new ArrayList<>();

        int oldestAge = 0;

        //Getting current yy mm dd in int
        String[] current = getCurrentDate().split("-");
        int currentYear = Integer.parseInt(current[0]);
        int currentMonth = Integer.parseInt(current[1]);
        int currentDate = Integer.parseInt(current[2]);

        LocalDate currentYearMonthDate = LocalDate.of(currentYear, currentMonth, currentDate);

        for (Patient patient : patientList){
            // Getting patient's age
            String getBirthDate = patient.getBIRTHDATE();
            String[] birthDate = getBirthDate.split("-");
            int year = Integer.parseInt(birthDate[0]);
            int month = Integer.parseInt(birthDate[1]);
            int day = Integer.parseInt(birthDate[2]);

            LocalDate birthdate = LocalDate.of(year, month, day);

            int patientAge = getAge(birthdate, currentYearMonthDate);

            if (patientAge >= oldestAge){
                oldestAge = patientAge;
                oldestPatientList.add(patient);
            }

        }
        return oldestPatientList;
    }

    public List<Patient> AgeRange0to19() {

        List<Patient> result = new ArrayList<>();

        //Getting current yy mm dd in int
        String[] current = getCurrentDate().split("-");
        final int currentYear = Integer.parseInt(current[0]);
        final int currentMonth = Integer.parseInt(current[1]);
        final int currentDate = Integer.parseInt(current[2]);

        LocalDate currentYearMonthDate = LocalDate.of(currentYear, currentMonth, currentDate);

        for (Patient patient : patientList) {
            // Getting patient's age
            String getBirthDate = patient.getBIRTHDATE();
            String[] birthDate = getBirthDate.split("-");
            int year = Integer.parseInt(birthDate[0]);
            int month = Integer.parseInt(birthDate[1]);
            int day = Integer.parseInt(birthDate[2]);

            LocalDate birthdate = LocalDate.of(year, month, day);

            int patientAge = getAge(birthdate, currentYearMonthDate);

            // check age range
            if (0 <= patientAge && patientAge <= 19) {
                result.add(patient);
            }
        }
        return result;

    }

}
