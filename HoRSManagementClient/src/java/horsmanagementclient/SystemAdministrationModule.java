/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package horsmanagementclient;

import ejb.stateless.EmployeeSessionBeanRemote;
import ejb.stateless.PartnerSessionBeanRemote;
import entity.Employee;
import entity.Partner;
import java.util.List;
import java.util.Scanner;
import util.enumeration.EmployeeEnum;

/**
 *
 * @author eliseoh
 */
public class SystemAdministrationModule {
    private Scanner scanner = new Scanner(System.in);
    private EmployeeSessionBeanRemote employeeSessionBean;
    private PartnerSessionBeanRemote partnerSessionBean;

    public SystemAdministrationModule() {
    }

    public SystemAdministrationModule(EmployeeSessionBeanRemote employeeSessionBean, PartnerSessionBeanRemote partnerSessionBean) {
        this.employeeSessionBean = employeeSessionBean;
        this.partnerSessionBean = partnerSessionBean;
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
                scanner.nextLine();

                if(response == 1)
                {
                    doCreateNewEmployee();
                }
                else if(response == 2)
                {
                    doViewAllEmployees();
                }
                else if(response == 3)
                {
                    doCreateNewPartner();
                }
                else if(response == 4)
                {
                    doViewAllPartners();
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
    
     public void doViewAllEmployees() {
        System.out.println("*** HoRS Management System :: System Administration Operation :: View All Employees ***\n");
        
        List<Employee> list = employeeSessionBean.retrieveAllEmployees();
        if (list.size() == 0) {
            System.out.println("There are no employees!\n");
        } else {
            for (Employee e : list) {
                System.out.print("Employee Username: " + e.getUsername());
                if (e.getRole().equals(EmployeeEnum.GUESTOFF)) {
                    System.out.println(", Employee Role: Guest Relation Officer\n");
                } else if (e.getRole().equals(EmployeeEnum.OPMANAGER)) {
                    System.out.println(", Employee Role: Operation Manager\n");
                } else if (e.getRole().equals(EmployeeEnum.SALESMANAGER)) {
                    System.out.println(", Employee Role: Sales Manager\n");
                } else if (e.getRole().equals(EmployeeEnum.SYSADMIN)) {
                    System.out.println(", Employee Role: System Administrator\n");
                } else {
                    System.out.println(", Employee Role: No role given!\n");
                }
            }
        }
    }
    
    public void doCreateNewPartner() {
        Partner partner = new Partner();
        System.out.println("*** HoRS Management System :: System Administration Operation :: Create Partner ***\n");

        System.out.print("Enter Username> ");
        partner.setUsername(scanner.nextLine().trim());
        System.out.print("Enter Password> ");
        partner.setPassword(scanner.nextLine().trim());
        
        Long newPart = partnerSessionBean.createPartner(partner);
        if (newPart != null) {
            System.out.println("Partner created successfully!\n");
        } else {
            System.out.println("Error occurred during partner account creation!\n");
        }
    }
    
    public void doViewAllPartners() {
        System.out.println("*** HoRS Management System :: System Administration Operation :: View All Partners ***\n");
        
        List<Partner> list = partnerSessionBean.retrieveAllPartners();
        if (list.size() == 0) {
            System.out.println("There are no partners!\n");
        } else {
            for (Partner e : list) {
                System.out.println("Partner Username: " + e.getUsername() + ", Partner ID: " + e.getPartnerId() + "\n");
            }
        }
    }
}
