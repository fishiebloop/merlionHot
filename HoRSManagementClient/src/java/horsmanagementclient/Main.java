/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package horsmanagementclient;

import ejb.stateless.EmployeeSessionBeanRemote;
import ejb.stateless.ExceptionReportSessionBeanRemote;
import ejb.stateless.GuestSessionBeanRemote;
import ejb.stateless.PartnerSessionBeanRemote;
import ejb.stateless.ReservationSessionBeanRemote;
import ejb.stateless.RoomAllocationSessionBeanRemote;
import ejb.stateless.RoomRateSessionBeanRemote;
import ejb.stateless.RoomSessionBeanRemote;
import ejb.stateless.RoomTypeSessionBeanRemote;
import entity.Employee;
import java.util.Scanner;
import javax.ejb.EJB;
import util.enumeration.EmployeeEnum;
import util.exception.EmployeeErrorException;
import util.exception.InvalidLoginCredentialException;

/**
 *
 * @author eliseoh
 */
public class Main {

    @EJB
    private static RoomAllocationSessionBeanRemote roomAllocationSessionBean;
    @EJB
    private static ReservationSessionBeanRemote reservationSessionBean;
    @EJB
    private static ExceptionReportSessionBeanRemote exceptionReportSessionBean;
    @EJB
    private static RoomSessionBeanRemote roomSessionBean;
    @EJB
    private static EmployeeSessionBeanRemote employeeSessionBean;
    @EJB
    private static PartnerSessionBeanRemote partnerSessionBean;
    @EJB
    private static RoomTypeSessionBeanRemote roomTypeSessionBean;
    @EJB
    private static RoomRateSessionBeanRemote roomRateSessionBean;
    @EJB
    private static GuestSessionBeanRemote guestSessionBean;
    
    
    
    
    
    
    
    private Employee currentEmployee;
    private HotelOperationModule hotelOpsModule;
    private FrontOfficeModule frontOffModule;
    private SystemAdministrationModule sysAdminModule;
    private static Scanner scanner = new Scanner(System.in);
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        
        Main mainApp = new Main();
        mainApp.runApp();
    }
    
    private void runApp() {
        Integer response = 0;
        while(true) {
            System.out.println("*** Welcome to the HoRS Management Application ***\n");
            System.out.println("1: Login");
            System.out.println("2: Exit\n");
            response = 0;
            
            while(response < 1 || response > 2)
            {
                System.out.print("> ");
                response = scanner.nextInt();
                scanner.nextLine(); 

                if(response == 1)
                {
                    try
                    {
                        doLogin(); 
                        System.out.println("Login successful! Logged in as: "+ currentEmployee.getUsername()+ "\n");
                        if (currentEmployee.getRole().equals(EmployeeEnum.SYSADMIN)) {
                            sysAdminModule = new SystemAdministrationModule(employeeSessionBean, partnerSessionBean);
                            sysAdminModule.menu();
                        } else if (currentEmployee.getRole().equals(EmployeeEnum.OPMANAGER)) {
                            hotelOpsModule = new HotelOperationModule(roomTypeSessionBean, roomSessionBean, roomRateSessionBean, exceptionReportSessionBean, roomAllocationSessionBean);
                            hotelOpsModule.menuOps();
                        } else if (currentEmployee.getRole().equals(EmployeeEnum.SALESMANAGER)) {
                            hotelOpsModule = new HotelOperationModule(roomTypeSessionBean, roomSessionBean, roomRateSessionBean, exceptionReportSessionBean, roomAllocationSessionBean);
                            hotelOpsModule.menuSales();
                        } else if (currentEmployee.getRole().equals(EmployeeEnum.GUESTOFF)) {
                            frontOffModule = new FrontOfficeModule(reservationSessionBean, roomSessionBean, roomTypeSessionBean, roomRateSessionBean, roomAllocationSessionBean, exceptionReportSessionBean, guestSessionBean);
                            frontOffModule.menu();
                        } 

                    }
                    catch(InvalidLoginCredentialException | EmployeeErrorException ex) 
                    {
                        System.out.println("Login failed: " + ex.getMessage() + "\n");
                    }
                }
                else if (response == 2)
                {
                    break;
                }
                else
                {
                    System.out.println("Invalid option, please try again!\n");                
                }
            }
            if(response == 2)
            {
                System.out.println("Exiting application.");
                break;
            }
        }
    }
    
    private void doLogin() throws InvalidLoginCredentialException, EmployeeErrorException {
        String username = "";
        String password = "";
        
        System.out.println("*** HoRS Management System :: Login ***\n");
        System.out.print("Enter username> ");
        username = scanner.nextLine().trim();
        System.out.print("Enter password> ");
        password = scanner.nextLine().trim();
        
        if(username.length() > 0 && password.length() > 0)
        {
            currentEmployee = employeeSessionBean.employeeAuth(username, password);      
        }
        else
        {
            throw new InvalidLoginCredentialException("Missing login credential!");
        }
    } 
    
}

