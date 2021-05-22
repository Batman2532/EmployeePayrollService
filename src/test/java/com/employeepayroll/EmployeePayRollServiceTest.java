package com.employeepayroll;

import org.junit.jupiter.api.Test;

public class EmployeePayRollServiceTest {
    static EmployeePayrollService employeePayrollService = new EmployeePayrollService();

    @Test
    public void printWelcomeMessage() {
        employeePayrollService.printWelcomeMessage();
    }
}
