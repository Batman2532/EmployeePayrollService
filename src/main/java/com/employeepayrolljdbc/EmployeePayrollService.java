package com.employeepayrolljdbc;

import java.sql.Date;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

public class EmployeePayrollService {
    private List<EmployeePayrollData> employeePayrollList;
    private EmployeePayrollDBService employeePayrollDBService;

    public enum IOServices {CONSOLE_ID, FILE_IO, DB_IO, REST_IO}

    public EmployeePayrollService()
    {
        employeePayrollDBService = EmployeePayrollDBService.getInstance();
    }

    public EmployeePayrollService(List<EmployeePayrollData> employeePayrollList){
        this();
        this.employeePayrollList = employeePayrollList;
    }


    public List<EmployeePayrollData> readEmployeePayrollData(IOServices ioService)
    {
        if(ioService.equals(IOServices.DB_IO))
            this.employeePayrollList = new EmployeePayrollDBService().readData();
        return this.employeePayrollList;
    }


    public List<EmployeePayrollData> readEmployeePayrollDataForDataRange( LocalDate startDate, LocalDate endDate){
        return employeePayrollDBService.getEmployeePayrollDataForDateRange(startDate,endDate);
    }

    public boolean checkEmployeePayrollInSyncWithDB(String name)
    {
        List<EmployeePayrollData> employeePayrollDataList = employeePayrollDBService.getEmployeePayrollData(name);
        return employeePayrollDataList.equals(employeePayrollDBService.getEmployeePayrollData(name));
    }

    public void updateEmployeeSalary(String name, Double salary){
        int result = employeePayrollDBService.updateEmployeePayrollData(name,salary);
        if (result == 0) return;
        EmployeePayrollData employeePayrollData;
        employeePayrollData= this.getEmployeePayrollData(name);
        if (employeePayrollData != null)
            employeePayrollData.salary = salary;
    }

    private EmployeePayrollData getEmployeePayrollData(String name){
        return this.employeePayrollList.stream().filter(employeePayrollData ->
                employeePayrollData.name.equals(name)).findFirst().orElse(null);
    }

    public void updateEmployeeBasicPay(String name, Double basic_pay){
        int result = employeePayrollDBService.updateEmployeePayrollDataForBasicPay(name,basic_pay);
        if (result == 0) return;
        EmployeePayrollData employeePayrollData;
        employeePayrollData= this.getEmployeePayrollData(name);
        if (employeePayrollData != null)
            employeePayrollData.basicPay = basic_pay;
    }

    public Map<String, Double> getAverageSalaryGroupByGender(){
        return employeePayrollDBService.readAverageSalaryGroupByGender();
    }

    public Map<String, Double> getMinimumSalaryGroupByGender(){
        return employeePayrollDBService.readMinimumSalaryGroupByGender();
    }

    public Map<String, Double> getMaximumSalaryGroupByGender(){
        return employeePayrollDBService.readMaximumSalaryGroupByGender();
    }

    public Map<String, Double> getCountOfEmployeeGroupByGender(){
        return employeePayrollDBService.readCountOfEmployeesGroupByGender();
    }
}
