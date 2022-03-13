# Denitsa-Dochkova-emplpoyees

Create an application using Spring that identifies the pair of employees who have worked together on common projects for the longest period of time.

Input data:
A CSV file with data in the following format:

EmpID, ProjectID, DateFrom, DateTo


Sample data:
143, 12, 2013-11-01, 2014-01-05
218, 10, 2012-05-16, NULL        //DateTo can be NULL, equivalent to today
143, 10, 2009-01-01, 2011-04-27

Sample output:
143, 218, 8

USED LINKS:

https://www.baeldung.com/java-csv-file-array    Reading a CSV File into an Array

https://www.baeldung.com/java-date-difference   Using ChronoUNIT

