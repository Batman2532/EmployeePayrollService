package com.employeepayroll;

import java.util.Scanner;
import java.util.ArrayList;
import java.util.List;

public class EmployeePayrollService {
    /* Welcome Message */
    public void printWelcomeMessage() {
        System.out.println("Welcome to the Employee PayRoll Service Program");
    }
    private final List<EmployeePayrollData> employeePayrollList;

    public EmployeePayrollService() {
        employeePayrollList = new ArrayList<>();
    }

    public static void main(String[] args) {
        EmployeePayrollService employeePayrollService = new EmployeePayrollService();
        employeePayrollService.printWelcomeMessage();
        Scanner consoleInputReader = new Scanner(System.in);
        employeePayrollService.readEmployeePayrollData(consoleInputReader);
        employeePayrollService.writeEmployeePayrollData();
    }

    /* Read Employee Payroll data from console */
    private void readEmployeePayrollData(Scanner consoleInputReader) {
        System.out.println("Enter Employee ID: ");
        int id = consoleInputReader.nextInt();
        System.out.println("Enter Employee Name ");
        String name = consoleInputReader.next();
        System.out.println("Enter Employee Salary ");
        double salary = consoleInputReader.nextDouble();
        employeePayrollList.add(new EmployeePayrollData(id, name, salary));
    }

    /* Write Employee Payroll data to console */
    private void writeEmployeePayrollData() {
        System.out.println("\nWriting Employee Payroll Roaster to Console\n" + employeePayrollList);
    }

}
