package com.employeepayrolljdbc;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EmployeePayrollDBService {
    private static PreparedStatement employeePayrollDataStatement;
    private static EmployeePayrollDBService employeePayrollDBService;

    public EmployeePayrollDBService() {
    }

    public static EmployeePayrollDBService getInstance(){
        if(employeePayrollDBService == null)
            employeePayrollDBService = new EmployeePayrollDBService();
        return employeePayrollDBService;
    }


    private Connection getConnection() throws SQLException {
        String jdbcURL = "jdbc:mysql://localhost:3306/payroll_service?useSSL=false";
        String userName = "root";
        String password = "";
        Connection connection;
        System.out.println("Connecting to database:"+jdbcURL);
        connection = DriverManager.getConnection(jdbcURL,userName,password);
        System.out.println("Connection is successful!!!"+connection);
        return connection;
    }

    public List<EmployeePayrollData> readData() {
        String sql = "SELECT * FROM employee_payroll";
        List<EmployeePayrollData> employeePayrollList = new ArrayList<>();
        try {
            Connection connection = this.getConnection();
            Statement statement = connection.createStatement();
            ResultSet result = statement.executeQuery(sql);
            while(result.next()){
                int id = result.getInt("id");
                String name = result.getString("name");
                double salary = result.getDouble("salary");
                double basicPay = result.getDouble("basic_pay");
                LocalDate startDate = result.getDate("startdate").toLocalDate();
                employeePayrollList.add(new EmployeePayrollData(id,name,salary,basicPay,startDate));
            }
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return employeePayrollList;
    }
    int updateEmployeePayrollDataForBasicPay(String name, double basicPay){
        return this.updateEmployeeDataUsingStatementForBasicPay(name, basicPay);
    }

    private int updateEmployeeDataUsingStatementForBasicPay(String name, double basicPay) {
        String sql = String.format("UPDATE employee_payroll set basic_pay = %.2f where name = '%s';", basicPay,name);
        try(Connection connection = this.getConnection()){
            Statement statement = connection.createStatement();
            return statement.executeUpdate(sql);
        }catch (SQLException e){
            e.printStackTrace();
        }
        return 0;
    }

    int updateEmployeePayrollData(String name, Double salary){
        return this.updateEmployeeDataUsingStatement(name,salary);
    }

    private int updateEmployeeDataUsingStatement(String name, Double salary){
        String sql = String.format("UPDATE employee_payroll set salary = %.2f where name = '%s';", salary,name);
        try(Connection connection = this.getConnection()){
            Statement statement = connection.createStatement();
            return statement.executeUpdate(sql);
        }catch (SQLException e){
            e.printStackTrace();
        }
        return 0;
    }

    private List<EmployeePayrollData> getEmployeePayrollData(ResultSet result){
        List<EmployeePayrollData> employeePayrollList = new ArrayList<>();
        try{
            while(result.next()){
                int id = result.getInt("id");
                String name = result.getString("name");
                double salary = result.getDouble("salary");
                double basicPay = result.getDouble("basic_pay");
                LocalDate startDate = result.getDate("startdate").toLocalDate();
                employeePayrollList.add(new EmployeePayrollData(id,name,salary,basicPay,startDate));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return employeePayrollList;
    }

    public List<EmployeePayrollData> getEmployeePayrollDataForDateRange(LocalDate startDate, LocalDate endDate)
    {
        String sql = String.format("SELECT * FROM  employee_payroll WHERE startdate BETWEEN '%s' AND '%s';", Date.valueOf(startDate), Date.valueOf(endDate));
        return this.getEmployeePayrollDataUsingDB(sql);
    }

    private List<EmployeePayrollData> getEmployeePayrollDataUsingDB(String sql)
    {
        List<EmployeePayrollData> employeePayrollList = new ArrayList<>();
        try (Connection connection = this.getConnection())
        {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(sql);
            employeePayrollList = this.getEmployeePayrollData(resultSet);
        } catch (SQLException e)
        {
            e.printStackTrace();
        }
        return employeePayrollList;
    }

    public List<EmployeePayrollData> getEmployeePayrollData(String name)
    {
        List<EmployeePayrollData> employeePayrollList = null;
        if (employeePayrollDataStatement == null)
            this.prepareStatementForEmployeeData();
        try
        {
            employeePayrollDataStatement.setString(1, name);
            ResultSet resultSet = employeePayrollDataStatement.executeQuery();
            employeePayrollList = this.getEmployeePayrollData(resultSet);
        } catch (SQLException e)
        {
            e.printStackTrace();
        }

        return employeePayrollList;
    }

    public Map<String, Double> readAverageSalaryGroupByGender(){
        String sql = "SELECT gender, AVG(salary) as salary FROM employee_payroll GROUP BY gender;";
        Map<String, Double> averageSalaryGroupByGender = new HashMap<>();
        try (Connection connection = this.getConnection())
        {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(sql);
            while (resultSet.next())
            {
                String gender = resultSet.getString("gender");
                double salary = resultSet.getDouble("salary");
                averageSalaryGroupByGender.put(gender, salary);
            }
        } catch (SQLException e)
        {
            e.printStackTrace();
        }
        return averageSalaryGroupByGender;
    }

    public Map<String, Double> readMinimumSalaryGroupByGender(){
        String sql = "SELECT gender, MIN(salary) as salary FROM employee_payroll GROUP BY gender;";
        Map<String, Double> minimumSalaryGroupByGender = new HashMap<>();
        try (Connection connection = this.getConnection())
        {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(sql);
            while (resultSet.next())
            {
                String gender = resultSet.getString("gender");
                double salary = resultSet.getDouble("salary");
                minimumSalaryGroupByGender.put(gender, salary);
            }
        } catch (SQLException e)
        {
            e.printStackTrace();
        }
        return minimumSalaryGroupByGender;
    }

    public Map<String, Double> readMaximumSalaryGroupByGender() {
        String sql = "SELECT gender, Max(salary) as salary FROM employee_payroll GROUP BY gender;";
        Map<String, Double >maximumSalaryGroupByGender = new HashMap<>();
        try (Connection connection = this.getConnection())
        {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(sql);
            while (resultSet.next())
            {
                String gender = resultSet.getString("gender");
                double salary = resultSet.getDouble("salary");
                maximumSalaryGroupByGender.put(gender, salary);
            }
        } catch (SQLException e)
        {
            e.printStackTrace();
        }
        return maximumSalaryGroupByGender;
    }

    public Map<String, Double> readCountOfEmployeesGroupByGender() {
        String sql = "SELECT gender, COUNT(*) as count FROM employee_payroll GROUP BY gender;";
        Map<String, Double >countOfEmployeeGroupByGender = new HashMap<>();
        try (Connection connection = this.getConnection())
        {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(sql);
            while (resultSet.next())
            {
                String gender = resultSet.getString("gender");
                double count = resultSet.getDouble("count");
                countOfEmployeeGroupByGender.put(gender, count);
            }
        } catch (SQLException e)
        {
            e.printStackTrace();
        }
        return countOfEmployeeGroupByGender;
    }

    private void prepareStatementForEmployeeData()
    {
        try
        {
            Connection connection = this.getConnection();
            String sql = "SELECT * FROM employee_payroll WHERE name = ?";
            employeePayrollDataStatement = connection.prepareStatement(sql);
        } catch (SQLException e)
        {
            e.printStackTrace();
        }
    }
}

