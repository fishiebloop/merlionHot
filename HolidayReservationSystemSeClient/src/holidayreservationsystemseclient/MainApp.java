/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package holidayreservationsystemseclient;

import java.util.Scanner;

/**
 *
 * @author eliseoh
 */
public class MainApp {
    private static Scanner scanner = new Scanner(System.in);
    
    public void runApp() {
        Integer response;
        
        while(true)
        {
            System.out.println("*** Welcome to Holiday.com, Holiday Reservation System ***\n");
            System.out.println("1: Login");
            System.out.println("2: Search Room");
            System.out.println("3: Exit\n");
            response = 0;
            
            while(response < 1 || response > 3)
            {
                System.out.print("> ");

                response = scanner.nextInt();

                if(response == 1)
                {
                    doLogin();
                }
                else if (response == 2)
                {
                    //doSearchRoom();
                }
                else if (response == 3)
                {
                    break;
                }
                else
                {
                    System.out.print("Invalid option, please try again!\n");                
                }
            }
            
            if(response == 3)
            {
                break;
            }
        }
    }
    
    private void doLogin() {
        System.out.println("*** Holiday Reservation System :: Partner Login ***\n");
        
        System.out.print("Enter username> ");
        String username = scanner.nextLine().trim();
        System.out.print("Enter password> ");
        String password = scanner.nextLine().trim();
    }
}
