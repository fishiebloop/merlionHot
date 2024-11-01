/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package horsmanagementclient;

import ejb.stateless.RoomTypeSessionBeanRemote;
import entity.RoomType;
import java.util.Scanner;

/**
 *
 * @author eliseoh
 */
public class HotelOperationModule {
    private Scanner scanner = new Scanner(System.in);
    private RoomTypeSessionBeanRemote roomTypeBean;

    public HotelOperationModule() {
    }

    
    public HotelOperationModule(RoomTypeSessionBeanRemote roomTypeBean) {
        this.roomTypeBean = roomTypeBean;
    }
    
    public void menuOps() {
        Integer response = 0;
        
        while(true)
        {
            System.out.println("*** Hotel Operation System :: Operations Management ***\n");
            System.out.println("1: Manage Room Type");
            System.out.println("2: Manage Rooms");
            System.out.println("3: View Room Allocation Exception Report");
            System.out.println("4: Logout\n");
            response = 0;
            
            while(response < 1 || response > 3)
            {
                System.out.print("> ");

                response = scanner.nextInt();
                scanner.nextLine();

                if(response == 1)
                {
                    menuRoomType();
                }
                else if(response == 2)
                {
                    menuRoom();
                }
                else if(response == 3)
                {
                    //doViewAllocationException();
                }
                else if(response == 4)
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
                break;
            }
        }
    }
    
    public void menuRoomType() {
        Integer response = 0;
        
        while(true)
        {
            System.out.println("*** Hotel Operation System :: Room Type Operations ***\n");
            System.out.println("1: Create New Room Type");
            System.out.println("2: Update Room Type");
            System.out.println("3: Delete Room Type");
            System.out.println("4: View All Room Types");
            System.out.println("5: Back\n");
            response = 0;
            
            while(response < 1 || response > 4)
            {
                System.out.print("> ");

                response = scanner.nextInt();
                scanner.nextLine();

                if(response == 1)
                {
                    doCreateRoomType();
                }
                else if(response == 2)
                {
                    //doUpdateRoomType();
                }
                else if(response == 3)
                {
                    //doDeleteRoomType();
                }
                else if(response == 4)
                {
                    //doViewAllRoomType();
                }
                else if(response == 5)
                {
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
    
    public void menuRoom() {
        Integer response = 0;
        
        while(true)
        {
            System.out.println("*** Hotel Operation System :: Room Operations ***\n");
            System.out.println("1: Create New Room");
            System.out.println("2: Update Room");
            System.out.println("3: Delete Room");
            System.out.println("4: View All Rooms");
            System.out.println("5: Back\n");
            response = 0;
            
            while(response < 1 || response > 4)
            {
                System.out.print("> ");

                response = scanner.nextInt();
                scanner.nextLine();

                if(response == 1)
                {
                    //doCreateRoom();
                }
                else if(response == 2)
                {
                    //doUpdateRoom();
                }
                else if(response == 3)
                {
                    //doDeleteRoom();
                }
                else if(response == 4)
                {
                    //doViewAllRoom();
                }
                else if(response == 5)
                {
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
    
    public void doCreateRoomType() {
        RoomType type = new RoomType();
        System.out.println("*** HoRS Management System :: Room Type Operations :: Create New Room Type ***\n");
        
        System.out.print("Enter Room Type Name> ");
        type.setRoomTypeName(scanner.nextLine().trim());
        System.out.print("Enter Room Type Description> ");
        type.setDescription(scanner.nextLine().trim());
        System.out.print("Enter Room Type's Bed Description> ");
        type.setBed(scanner.nextLine().trim());
        System.out.print("Enter Room Type's Capacity> ");
        type.setCapacity(scanner.nextInt());
        scanner.nextLine();
        System.out.print("Enter Room Type's Amenities Description> ");
        type.setAmenities(scanner.nextLine().trim());
        
        Long newType = roomTypeBean.createRoomType(type);
        if (newType != null) {
            System.out.println("Room Type " + type.getRoomTypeName()+ " created successfully!\n");
        } else {
            System.out.println("Error occurred during room type creation!\n");
        }
    }
}
