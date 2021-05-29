package com.employeepayroll;

import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.util.Enumeration;

public class EmployeePayrollJDBC {
    public static void main(String[] args) {
        String jdbcURL = "jdbc:mysql://localhost:3306/payroll_service?useSSl=false";
        String userName = "root";
        String password = "";
        Connection connection;
        try{
            Class.forName("com.mysql.cj.jdbc.Driver");
            System.out.println("Driver is loaded");
        }catch (ClassNotFoundException e){
            throw new IllegalStateException("cannot find the driver in the classpath",e);
        }
        listDrivers();
        try{
            System.out.println("connecting to database"+jdbcURL);
            connection = DriverManager.getConnection(jdbcURL, userName, password);
            System.out.println("connection is successful!!!!!"+connection);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private static void listDrivers() {
        Enumeration<Driver> driverList = DriverManager.getDrivers();
        while(driverList.hasMoreElements()){
            Driver driverClass = (Driver) driverList.nextElement();
            System.out.println("  "+driverClass.getClass().getName());
        }

    }
}
