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


    private static HashMap<Integer, TreeSet<Employee>> sortData(List<String[]> allLines) {
        HashMap<Integer, TreeSet<Employee>> sortedData = new HashMap<>();

        for (String[] el : allLines) {
            Employee employee = new Employee();
            employee.setEmpId(Integer.parseInt(el[0].trim()));          //trim is used to delete any whitespaces
            employee.setProjectId(Integer.parseInt(el[1].trim()));

            String startDate = el[2];
            if (startDate.trim().equalsIgnoreCase("null")){  //null in lower or uppercase is equivalent to today
                employee.setDateFrom(LocalDate.now());
            } else {
                employee.setDateFrom(LocalDate.parse(startDate.trim()));
            }

            String endDate = el[3];
            if (endDate.trim().equalsIgnoreCase("null")){
                employee.setDateTo(LocalDate.now());
            } else {
                employee.setDateTo(LocalDate.parse(endDate.trim()));
            }

            if (!sortedData.containsKey(employee.getProjectId())) {
                sortedData.put(employee.getProjectId(), new TreeSet<>());
            }
            sortedData.get(employee.getProjectId()).add(employee);
        }
        return sortedData;
    }

    private static List<String[]> readInputData() {
        List<String[]> fileLines = new ArrayList<>();
        try {
            FileReader fileReader = new FileReader("employeesData.CSV");
            CSVReader csvReader = new CSVReader(fileReader);
            csvReader.readNext();

            String[] line = csvReader.readNext();
            while (line!= null) {
                fileLines.add(line);
                line= csvReader.readNext();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return fileLines;
    }

    private static void printMaxDurationForPair(HashMap<Integer, TreeSet <Employee>> table) {
        long maxDuration = 0;
        int emp1 = 0;
        int emp2 = 0;
        for (Map.Entry<Integer, TreeSet<Employee>> entry : table.entrySet()) {
            Employee first = entry.getValue().first();
            Employee second = entry.getValue().last();

            if (entry.getValue().size() == 2) {
                if (!second.getDateFrom().isAfter(first.getDateTo())){
                    if (second.getDateTo().isBefore(first.getDateTo()) ||
                        second.getDateTo().isEqual(first.getDateTo())) {
                            if ((first.getEmpId() == emp1 && second.getEmpId() == emp2) || (first.getEmpId() == emp2 && second.getEmpId() == emp1)) {
                                maxDuration += (ChronoUnit.DAYS.between(second.getDateFrom(), second.getDateTo()) + 1);
                            } else if ((ChronoUnit.DAYS.between(second.getDateFrom(), second.getDateTo()) + 1) > maxDuration){
                                maxDuration = (ChronoUnit.DAYS.between(second.getDateFrom(), second.getDateTo()) + 1) ;
                                emp1 = first.getEmpId();
                                emp2 = second.getEmpId();
                            }
                    } else {
                        if ((first.getEmpId() == emp1 && second.getEmpId() == emp2) || (first.getEmpId() == emp2 && second.getEmpId() == emp1)) {
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
        System.out.println("---Sample output---:\n"+emp1 + "," + emp2 + "," + maxDuration);
    }
}
