/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package horsmanagementclient;

import ejb.stateless.EmployeeSessionBeanRemote;
import entity.Employee;
import java.util.Scanner;
import util.enumeration.EmployeeEnum;

/**
 *
 * @author eliseoh
 */
public class SystemAdministrationModule {
    private Scanner scanner = new Scanner(System.in);
    private EmployeeSessionBeanRemote employeeSessionBean;

    public SystemAdministrationModule() {
    }

    public SystemAdministrationModule(EmployeeSessionBeanRemote employeeSessionBean) {
        this.employeeSessionBean = employeeSessionBean;
    }
    
    public void menu() {
        Integer response = 0;
        
        while(true)
        {
            System.out.println("*** HoRS Management System :: System Administration Operation ***\n");
            System.out.println("1: Create New Employees");
            System.out.println("2: View All Employees");
            System.out.println("3: Create New Partner");
            System.out.println("4: View All Partner");
            System.out.println("5: Back\n");
            response = 0;
            
            while(response < 1 || response > 4)
            {
                System.out.print("> ");

                response = scanner.nextInt();

                if(response == 1)
                {
                    doCreateNewEmployee();
                }
                else if(response == 2)
                {
                    //doViewAllEmployees();
                }
                else if(response == 3)
                {
                    //doCreateNewPartner();
                }
                else if(response == 4)
                {
                    //doViewAllPartners();
                }
                else if(response == 5) {
                    break;
                }
                else
                {
                    System.out.println("Invalid option, please try again!\n");                
                }
            }
            
            if(response == 5)
            {
                break;
            }
        }
    }
    
    public void doCreateNewEmployee() {
        Employee staff = new Employee();
        System.out.println("*** HoRS Management System :: System Administration Operation :: Create Employee ***\n");

        scanner.nextLine();
        System.out.print("Enter Username> ");
        staff.setUsername(scanner.nextLine().trim());
        System.out.print("Enter Password> ");
        staff.setPassword(scanner.nextLine().trim());
        System.out.println("1: System Administrator");
        System.out.println("2: Guest Relation Officer");
        System.out.println("3: Operation Manager");
        System.out.println("4: Sales Manager");
        Boolean repeat = true;
        while (repeat) {
            System.out.print("Enter Employee Role> ");
            repeat = false;
            Integer role = scanner.nextInt();
            scanner.nextLine();
            if(role == 1)
            {
                staff.setRole(EmployeeEnum.SYSADMIN);
            } else if(role == 2) {
                staff.setRole(EmployeeEnum.GUESTOFF);
            } else if(role == 3) {
                staff.setRole(EmployeeEnum.OPMANAGER);
            } else if(role == 4) {
                staff.setRole(EmployeeEnum.SALESMANAGER);
            } else {
                System.out.println("Invalid option, please try again!\n");
                repeat = true;
            }
        }
        
        employeeSessionBean.createEmployee(staff);
    }
}
