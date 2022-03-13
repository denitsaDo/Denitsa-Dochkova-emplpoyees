package com.example.sirma_task.model;

import com.opencsv.bean.CsvBindByPosition;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Component;
import java.time.LocalDate;


@Component
@Getter
@Setter
@NoArgsConstructor
public class Employee implements Comparable <Employee>{


    @CsvBindByPosition(position = 0)
    private int empId;

    @CsvBindByPosition(position = 1)
    private int projectId;

    @CsvBindByPosition(position = 2)
    private LocalDate dateFrom;

    @CsvBindByPosition(position = 3)
    private LocalDate dateTo;


    @Override
    public int compareTo(Employee o) {          //we compare two Employees. Person already worked on this project do not enter SET
        if (this.getEmpId() == o.getEmpId()) {
            return 0;
        } else {
            if (this.getDateFrom().isBefore(o.getDateFrom())){ //compare elements in treeSet by startDate working on project
                return -1;
            } else {
                return 1;
            }
        }
    }
}
