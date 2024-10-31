/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/J2EE/EJB30/StatelessEjbClass.java to edit this template
 */
package ejb.stateless;

import entity.Employee;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import util.exception.EmployeeErrorException;

/**
 *
 * @author eliseoh
 */
@Stateless
public class EmployeeSessionBean implements EmployeeSessionBeanRemote, EmployeeSessionBeanLocal {

    @PersistenceContext(unitName = "MerlionHot-ejbPU")
    private EntityManager em;

    @Override
    public Long createEmployee(Employee newEmployee) {
        em.persist(newEmployee);
        em.flush();
        return newEmployee.getEmployeeId();
    }

    public Employee employeeAuth(String userName, String password) throws EmployeeErrorException {
        Employee e = retrieveEmployeeByUsername(userName);
        if (e.getPassword().equals(password)) {
            return e;
        } else {
            throw new EmployeeErrorException("Password Error");
        }
    }

    @Override
    public List<Employee> retrieveAllEmployees() {

        List<Employee> li = em.createQuery("Select e FROM Employee e").getResultList();
        return li;

    }

    @Override
    public Employee retrieveEmployeeById(Long employeeId) {

        return null;

    }

    @Override
    public Employee retrieveEmployeeByUsername(String employeeUsername) throws EmployeeErrorException {
        Query query = em.createQuery("SELECT s FROM Employee s WHERE s.username = :inUsername");
        query.setParameter("inUsername", employeeUsername);

        try {
            return (Employee) query.getSingleResult();
        } catch (NoResultException | NonUniqueResultException ex) {
            throw new EmployeeErrorException("Employee Username " + employeeUsername + " does not exist!");
        }

    }

}

