package com.example.sirma_task.service;

import com.example.sirma_task.model.Employee;
import com.opencsv.CSVReader;
import org.springframework.stereotype.Service;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.*;

@Service
public class DataService {

    public static void start() {
        List<String[]> allLines = readInputData();                                  //method, reading from CSV file
        HashMap<Integer, TreeSet <Employee>> sortByProjectId = sortData(allLines);  //method, sorting all data by project ID
        printMaxDurationForPair(sortByProjectId);                                   //method, printing Sample output
    }

    private static List<String[]> readInputData() {
        List<String[]> fileLines = new ArrayList<>();
        try {
            FileReader fileReader = new FileReader("employeesData.CSV");
            CSVReader csvReader = new CSVReader(fileReader);
            csvReader.readNext();   //readNext() method of the CSVReader class reads the next line of the .csv file and returns it in the form of a String array

            String[] line = csvReader.readNext(); //we have skipped first informational line of .csv file
            while (line!= null) {
                fileLines.add(line);
                line= csvReader.readNext();     //go to next line
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return fileLines;
    }

    private static final int FIRST = 0;   // use constants corresponding to CSV file format
    private static final int SECOND = 1;
    private static final int THIRD = 2;
    private static final int FOURTH = 3;
    private static HashMap<Integer, TreeSet<Employee>> sortData(List<String[]> allLines) {
        HashMap<Integer, TreeSet<Employee>> sortedData = new HashMap<>();

        for (String[] element: allLines) {
            Employee employee = new Employee();
            employee.setEmpId(Integer.parseInt(element[FIRST].trim()));          //trim is used to delete any whitespaces
            employee.setProjectId(Integer.parseInt(element[SECOND].trim()));     //set employee`s information

            String startDate = element[THIRD];
            if (startDate.trim().equalsIgnoreCase("null")){            //null in lower or uppercase is equivalent to today
                employee.setDateFrom(LocalDate.now());
            } else {
                employee.setDateFrom(LocalDate.parse(startDate.trim()));
            }

            String endDate = element[FOURTH];
            if (endDate.trim().equalsIgnoreCase("null")){
                employee.setDateTo(LocalDate.now());
            } else {
                employee.setDateTo(LocalDate.parse(endDate.trim()));
            }

            if (!sortedData.containsKey(employee.getProjectId())) {      //if such key does not exist, create it (projectID is key)
                sortedData.put(employee.getProjectId(), new TreeSet<>());
            }
            sortedData.get(employee.getProjectId()).add(employee);      //then add employee
        }
        return sortedData;
    }

    private static void printMaxDurationForPair(HashMap<Integer, TreeSet <Employee>> table) {
        long maxDuration = 0;
        int emp1 = 0;
        int emp2 = 0;
        for (Map.Entry<Integer, TreeSet<Employee>> entry : table.entrySet()) {
            Employee first = entry.getValue().first();
            Employee second = entry.getValue().last();

            if (entry.getValue().size() == 2) {                         //consider only projects where two employees had worked on
                if (!second.getDateFrom().isAfter(first.getDateTo())){  //consider if two persons have worked together on project
                    if (second.getDateTo().isBefore(first.getDateTo()) ||
                        second.getDateTo().isEqual(first.getDateTo())) {
                            if (isPairExisting(first, second, emp1, emp2)) {
                                //accumulating days if this pair of employees has already worked together on other project
                                maxDuration += (ChronoUnit.DAYS.between(second.getDateFrom(), second.getDateTo()) + 1);
                            } else if ((ChronoUnit.DAYS.between(second.getDateFrom(), second.getDateTo()) + 1) > maxDuration){
                                //if these employees have not worked together, create new pair if common time spent on project is higher than current LONGEST PERIOD
                                maxDuration = (ChronoUnit.DAYS.between(second.getDateFrom(), second.getDateTo()) + 1) ;
                                emp1 = first.getEmpId();
                                emp2 = second.getEmpId();
                            }
                    } else {
                        //same logic, considering start and end date for two employees
                        if (isPairExisting(first, second, emp1, emp2)) {
                            maxDuration += (ChronoUnit.DAYS.between(second.getDateFrom(), first.getDateTo()) + 1);
                        } else if((ChronoUnit.DAYS.between(second.getDateFrom(), first.getDateTo())+1) > maxDuration) {
                            maxDuration = (ChronoUnit.DAYS.between(second.getDateFrom(), first.getDateTo())+1) ;
                            emp1 = first.getEmpId();
                            emp2 = second.getEmpId();
                        }
                    }
                }
            }
        }
        System.out.println("---Sample output---:\n" + emp1 + "," + emp2 + "," + maxDuration);
    }

    private static boolean isPairExisting(Employee first, Employee second, int emp1, int emp2) {
        return (first.getEmpId() == emp1 && second.getEmpId() == emp2) || (first.getEmpId() == emp2 && second.getEmpId() == emp1);
    }
}
