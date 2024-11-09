/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package horsreservationclient;

import ejb.stateless.GuestSessionBeanRemote;
import entity.Guest;
import java.util.Scanner;
import util.exception.BeanValidationError;
import util.exception.GuestErrorException;
import util.exception.InvalidLoginCredentialException;

/**
 *
 * @author eliseoh
 */
public class MainApp {
    
    private Guest currentGuest;
    private static Scanner scanner = new Scanner(System.in);
    private GuestSessionBeanRemote guestSessionBean;

    public MainApp() {
    }

    public MainApp(GuestSessionBeanRemote guestSessionBean) {
        this.guestSessionBean = guestSessionBean;
    }
    
    
    
    public void runApp() {
        Integer response = 0;
        while(true) {
            System.out.println("*** Welcome to the HoRS Reservation Application ***\n");
            System.out.println("1: Login");
            System.out.println("2: Register");
            System.out.println("3: Search Hotel Room");
            System.out.println("4: Exit\n");
            response = 0;
            
            while(response < 1 || response > 4)
            {
                System.out.print("> ");
                response = scanner.nextInt();
                scanner.nextLine(); 

                if(response == 1)
                {
                    try
                    {
                        doLogin(); 
                        System.out.println("Login successful! Logged in as: "+ currentGuest.getName()+ "\n");
                        //loggedInMenu();
                    }
                    catch(InvalidLoginCredentialException | GuestErrorException ex) 
                    {
                        System.out.println("Login failed: " + ex.getMessage() + "\n");
                    }
                }
                else if (response == 2)
                {
                    try
                    {
                        doRegister(); 
                        System.out.println("Register was successful! Logged in as: "+ currentGuest.getName()+ "\n");
                        //loggedInMenu();
                    }
                    catch(BeanValidationError ex) 
                    {
                        System.out.println("Validation failed. Please correct the following errors:\n" + ex.getMessage());
                        System.out.println("Please re-enter your details.");
                    }
                }
                else if (response == 3)
                {
                    //doSearchHotel();
                }
                else if (response == 4)
                {
                    break;
                }
                else
                {
                    System.out.println("Invalid option, please try again!\n");                
                }
            }
            if(response == 4)
            {
                System.out.println("Exiting application.");
                break;
            }
        }
    }
    
    private void doLogin() throws InvalidLoginCredentialException, GuestErrorException {
        String email = "";
        String password = "";
        
        System.out.println("*** HoRS Reservation System :: Login ***\n");
        System.out.print("Enter email> ");
        email = scanner.nextLine().trim();
        System.out.print("Enter password> ");
        password = scanner.nextLine().trim();
        
        if(email.length() > 0 && password.length() > 0)
        {
            currentGuest = guestSessionBean.guestAuth(email, password);      
        }
        else
        {
            throw new InvalidLoginCredentialException("Missing login credential!");
        }
    } 
    
    private void doRegister() throws BeanValidationError{
        Guest g = new Guest();
        
        System.out.println("*** HoRS Reservation System :: Register ***\n");
        System.out.print("Enter email> ");
        g.setEmail(scanner.nextLine().trim());
        System.out.print("Enter password> ");
        g.setPassword(scanner.nextLine().trim());
        System.out.print("Enter Name> ");
        g.setName(scanner.nextLine().trim());
            guestSessionBean.validateGuest(g);
            currentGuest = guestSessionBean.createGuest(g);
    }
    
    //private void doSearchHotel() {
        
    //}
    
    //private void loggedInMenu() {
        
    //}
}
