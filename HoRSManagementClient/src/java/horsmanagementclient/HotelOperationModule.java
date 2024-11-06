/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package horsmanagementclient;

import ejb.stateless.RoomSessionBeanRemote;
import ejb.stateless.RoomTypeSessionBeanRemote;
import entity.Room;
import entity.RoomType;
import java.util.List;
import java.util.Scanner;
import util.exception.RoomTypeErrorException;

/**
 *
 * @author eliseoh
 */
public class HotelOperationModule {
    private Scanner scanner = new Scanner(System.in);
    private RoomTypeSessionBeanRemote roomTypeBean;
    private RoomSessionBeanRemote roomBean;

    public HotelOperationModule() {
    }

    
    public HotelOperationModule(RoomTypeSessionBeanRemote roomTypeBean, RoomSessionBeanRemote roomBean) {
        this.roomTypeBean = roomTypeBean;
        this.roomBean = roomBean;
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
                    doUpdateRoomType();
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
                    doCreateRoom();
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
    
    public void doCreateRoom() {
        Room r = new Room();
        System.out.println("*** HoRS Management System :: Room Operations :: Create New Room ***\n"); 

        try{
            List<RoomType> li = roomTypeBean.retrieveAllRoomTypes();
            if (li.size() < 1) {
                System.out.println("No Room Types created!\n");
            } else {
                for (int i = 1; i <= li.size(); i++) {
                    System.out.println(i + ": " + li.get(i-1).getRoomTypeName());
                }
                System.out.println();
                System.out.print("Choose Room Type> ");
                
                Integer rtNo = scanner.nextInt();
                scanner.nextLine();
                RoomType rt = li.get(rtNo-1);
                
                System.out.print("Enter Room Number> ");
                r.setRoomNumber(scanner.nextInt());
                scanner.nextLine();

                Long newRoom = roomBean.createRoom(r, rt);
                if (newRoom != null) {
                    System.out.println("Room Number " + r.getRoomNumber()+ " created successfully!\n");
                } else {
                    System.out.println("Error occurred during room type creation!\n");
                }
            }
        } catch (RoomTypeErrorException ex) {
            System.out.println( ex.getMessage() +"\n");
        }
    }
    
    public void doUpdateRoomType() {
        System.out.println("*** HoRS Management System :: Room Type Operations :: Update Room Type ***\n"); 

        try{
            List<RoomType> li = roomTypeBean.retrieveAllRoomTypes();
            if (li.size() < 1) {
                System.out.println("No Room Types created!\n");
            } else {
                for (int i = 1; i <= li.size(); i++) {
                    System.out.println(i + ": " + li.get(i-1).getRoomTypeName());
                }
                System.out.println();
                System.out.print("Choose Room Type> ");
                
                Integer rtNo = scanner.nextInt();
                scanner.nextLine();
                RoomType rt = li.get(rtNo-1);
                Integer response = 0;
                while (response == 0) {
                    System.out.println("Would you like to change Room Type Name? ");
                    System.out.println("1: Yes ");
                    System.out.println("2: No ");
                    System.out.print("> ");
                    response = scanner.nextInt();
                    scanner.nextLine();
                    if (response == 1) {
                        System.out.print("Enter new Room Type Name> ");
                        rt.setRoomTypeName(scanner.nextLine().trim());
                    } else if (response != 2) {
                        System.out.println("Invalid response! Try Again!");
                        response = 0;
                    }
                }
                
                response = 0;
                while (response == 0) {
                    System.out.println("Would you like to change Room Type Description? ");
                    System.out.println("1: Yes ");
                    System.out.println("2: No ");
                    System.out.print("> ");
                    response = scanner.nextInt();
                    scanner.nextLine();
                    if (response == 1) {
                        System.out.print("Enter new Room Type Description> ");
                        rt.setDescription(scanner.nextLine().trim());
                    } else if (response != 2) {
                        response = 0;
                    }
                }
                
                response = 0;
                while (response == 0) {
                    System.out.println("Would you like to change Room Type Bed Description? ");
                    System.out.println("1: Yes ");
                    System.out.println("2: No ");
                    System.out.print("> ");
                    response = scanner.nextInt();
                    scanner.nextLine();
                    if (response == 1) {
                        System.out.print("Enter new Room Type Bed Description> ");
                        rt.setBed(scanner.nextLine().trim());
                    } else if (response != 2) {
                        response = 0;
                    }
                }
                
                response = 0;
                while (response == 0) {
                    System.out.println("Would you like to change Room Type Capacity? ");
                    System.out.println("1: Yes ");
                    System.out.println("2: No ");
                    System.out.print("> ");
                    response = scanner.nextInt();
                    scanner.nextLine();
                    if (response == 1) {
                        System.out.print("Enter new Room Type Capacity> ");
                        rt.setCapacity(scanner.nextInt());
                        scanner.nextLine();
                    } else if (response != 2) {
                        response = 0;
                    }
                }
                
                response = 0;
                while (response == 0) {
                    System.out.println("Would you like to change Room Type Amenities Description? ");
                    System.out.println("1: Yes ");
                    System.out.println("2: No ");
                    System.out.print("> ");
                    response = scanner.nextInt();
                    scanner.nextLine();
                    if (response == 1) {
                        System.out.print("Enter new Room Type Amenities Description> ");
                        rt.setAmenities(scanner.nextLine().trim());
                    } else if (response != 2) {
                        response = 0;
                    }
                }
                roomTypeBean.updateRoomType(rt);
            }
        } catch (RoomTypeErrorException ex) {
            System.out.println( ex.getMessage() +"\n");
        }
    }
}
