/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/J2EE/EJB30/SessionRemote.java to edit this template
 */
package ejb.stateless;

import entity.Employee;
import java.util.List;
import javax.ejb.Remote;
import util.exception.EmployeeErrorException;

/**
 *
 * @author qiuyutong
 */
@Remote
public interface EmployeeSessionBeanRemote {
    public Long createEmployee(Employee newEmployee);

    public List<Employee> retrieveAllEmployees();

    public Employee retrieveEmployeeById(Long employee);
    
    public Employee employeeAuth(String userName, String password) throws EmployeeErrorException;

    public Employee retrieveEmployeeByUsername(String employeeUsername) throws EmployeeErrorException;

}
