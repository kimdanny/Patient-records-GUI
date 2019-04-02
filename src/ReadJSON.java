import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.*;

public class ReadJSON {

    List<Patient> patientList = new ArrayList<>();
    List<String> headersList = new ArrayList<>();

    public String[] JSONToString(String filepath) {
        try{
            // getting streams
            InputStream inputStream = new FileInputStream(filepath);
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            String line = bufferedReader.readLine();
            StringBuilder stringBuilder = new StringBuilder();

            // append line by line to StringBuilder
            while(line != null){
                stringBuilder.append(line);
                line = bufferedReader.readLine();
            }
            String converted = stringBuilder.toString();

            // replacing all unnecessary things
            converted = converted.replaceAll("\\{", "");
            converted = converted.replaceAll("}", "");
            converted = converted.replaceAll("\"patients\":", "");
            converted = converted.replaceAll("\\[", "");
            converted = converted.replaceAll("]", "");
            converted = converted.replaceAll(":", ",");
            converted = converted.replaceAll("\"", "");
            converted = converted.replaceFirst("\\s*", "");

            String[] jsonString = converted.split("\\s*,\\s*", -1);

            return jsonString;

        }
        catch (IOException e){
            System.out.println("Error: " + e.getMessage());
        }
        return null;
    }

    // Getting only headers for jsonFormatter
    public List<String> getHeadersToList(String filepath) {

        String[] convertedJSON = JSONToString(filepath);
        for (int i = 0; i<20; i++){
            String header = convertedJSON[2*i];
            headersList.add(header);
        }

        return headersList;
    }


    public List<Patient> JSONtoList(String filepath) {

        String[] convertedJSON = JSONToString(filepath);
        Patient patient;

        for (int i=1; i<convertedJSON.length; i=i+40){
            Patient patientMap = new Patient();

            patientMap.setID            (convertedJSON[i]);
            patientMap.setBIRTHDATE     (convertedJSON[i+2]);
            patientMap.setDEATHDATE     (convertedJSON[i+4]);
            patientMap.setSSN           (convertedJSON[i+6]);
            patientMap.setDRIVERS       (convertedJSON[i+8]);
            patientMap.setPASSPORT      (convertedJSON[i+10]);
            patientMap.setPREFIX        (convertedJSON[i+12]);
            patientMap.setFIRST         (convertedJSON[i+14]);
            patientMap.setLAST          (convertedJSON[i+16]);
            patientMap.setSUFFIX        (convertedJSON[i+18]);
            patientMap.setMAIDEN        (convertedJSON[i+20]);
            patientMap.setMARITAL       (convertedJSON[i+22]);
            patientMap.setRACE          (convertedJSON[i+24]);
            patientMap.setETHNICITY     (convertedJSON[i+26]);
            patientMap.setGENDER        (convertedJSON[i+28]);
            patientMap.setBIRTHPLACE    (convertedJSON[i+30]);
            patientMap.setADDRESS       (convertedJSON[i+32]);
            patientMap.setCITY          (convertedJSON[i+34]);
            patientMap.setSTATE         (convertedJSON[i+36]);
            patientMap.setZIP           (convertedJSON[i+38]);

            patient = patientMap;
            patientList.add(patient);

        }

        return patientList;
    }




}

