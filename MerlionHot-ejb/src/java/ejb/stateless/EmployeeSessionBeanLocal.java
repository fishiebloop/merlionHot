/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/J2EE/EJB30/SessionLocal.java to edit this template
 */
package ejb.stateless;

import entity.Employee;
import java.util.List;
import javax.ejb.Local;
import util.exception.EmployeeErrorException;

/**
 *
 * @author eliseoh
 */
@Local
public interface EmployeeSessionBeanLocal {

    public Long createEmployee(Employee newEmployee);

    public List<Employee> retrieveAllEmployees();

    public Employee retrieveEmployeeById(Long employee);
    
    public Employee employeeAuth(String userName, String password) throws EmployeeErrorException;

    public Employee retrieveEmployeeByUsername(String employeeUsername) throws EmployeeErrorException;

}
