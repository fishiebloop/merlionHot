/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package horsreservationclient;

import ejb.stateless.GuestSessionBeanRemote;
import entity.Guest;
import java.util.Date;
import java.util.Scanner;
import util.exception.BeanValidationError;
import util.exception.GuestErrorException;
import util.exception.InvalidLoginCredentialException;
import com.merlionhotel.utils.DateUtil;
import ejb.stateless.ReservationSessionBeanRemote;
import ejb.stateless.RoomAllocationSessionBeanRemote;
import ejb.stateless.RoomSessionBeanRemote;
import ejb.stateless.RoomTypeSessionBeanRemote;
import entity.OnlineGuest;
import entity.Reservation;
import entity.RoomType;
import java.math.BigDecimal;
import java.text.ParseException;
import java.util.List;
import util.exception.DateValidationError;
import util.exception.RoomTypeErrorException;

/**
 *
 * @author eliseoh
 */
public class MainApp {
    
    private Guest currentGuest;
    private static Scanner scanner = new Scanner(System.in);
    private GuestSessionBeanRemote guestSessionBean;
    private RoomTypeSessionBeanRemote roomTypeBean;
    private ReservationSessionBeanRemote reservationBean;
    private RoomSessionBeanRemote roomSessionBean;
    private RoomAllocationSessionBeanRemote allocationBean;

    public MainApp() {
    }

    public MainApp(GuestSessionBeanRemote guestSessionBean, RoomTypeSessionBeanRemote roomTypeBean, 
                   ReservationSessionBeanRemote reservationBean, RoomSessionBeanRemote roomSessionBean, RoomAllocationSessionBeanRemote allocationBean) {
        
        this.guestSessionBean = guestSessionBean;
        this.roomTypeBean = roomTypeBean;
        this.reservationBean = reservationBean;
        this.roomSessionBean = roomSessionBean;
        this.allocationBean = allocationBean;
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
                        loggedInMenu();
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
                        loggedInMenu();
                    }
                    catch(BeanValidationError ex) 
                    {
                        System.out.println("Validation failed. Please correct the following errors:\n" + ex.getMessage());
                        System.out.println("Please re-enter your details.");
                    }
                }
                else if (response == 3)
                {
                    try {
                        doSearchHotel();
                    } catch (RoomTypeErrorException | DateValidationError ex) {
                        System.out.println( ex.getMessage() + "\n"); 
                    }
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
        OnlineGuest g = new OnlineGuest();
        
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
    
    private void doSearchHotel() throws RoomTypeErrorException, DateValidationError {
        Integer res; 
        RoomType type = null;
        BigDecimal totalPrice = BigDecimal.ZERO;
        Date out; 
        Date in;
        System.out.println("*** HoRS Reservation System :: Search Hotel ***\n");
        while(true) {
            System.out.print("Enter Check in Date! Please format in dd-MM-yyyy > ");
            String checkIn = scanner.nextLine().trim();
            try {
                in = DateUtil.convertToDate(checkIn);
                break;
            } catch (ParseException ex) {
                throw new DateValidationError("Date formatted wrongly! Try again!");
            }
        }
        
        while(true) {
            System.out.print("Enter Check out Date! Please format in dd-MM-yyyy > ");
            String checkOut = scanner.nextLine().trim();
            try {
                out = DateUtil.convertToDate(checkOut);
                break;
            } catch (ParseException ex) {
                throw new DateValidationError("Date formatted wrongly! Try again!");
            }
        }
        
        System.out.print("Enter No of Guest(s) > ");
        Integer guests = scanner.nextInt();

        //same day booking must have check in time after current time
        if (!out.equals(in) && out.before(in)) {
            throw new DateValidationError("Dates entered wrongly!");
        }
        
        Boolean reselect = true;
        do {
            List<RoomType> li = roomTypeBean.retrieveAllAvailRoomTypeOnline(in, out);
            if (li.size() < 1) {
                System.out.println("No Room Types Available!\n"); 
            } else {
                System.out.println("List of Room Types: ");
                for (int i = 1; i <= li.size(); i++) {
                    System.out.println(i + ": " + li.get(i-1).getRoomTypeName());
                    System.out.println("    Description: " + li.get(i-1).getDescription());
                    System.out.println("    Bed Description: " + li.get(i-1).getBed() + "\n" + ", Capacity: " + li.get(i-1).getCapacity());
                    System.out.println("    Amenities: " + li.get(i-1).getAmenities() + "\n");
                    //print out price
                    System.out.println("    Price for entire stay: $" + roomTypeBean.getPriceOfRoomTypeOnline(in, out, li.get(i-1)) + "\n");
                    System.out.println();
                }
                System.out.println();
                
                System.out.print("Enter Room Type for reservation > ");
                res = 0;
                res = scanner.nextInt();
                scanner.nextLine();
                if (res > 0 && res <= li.size()) {
                   type = li.get(res-1);
                   Integer avail = roomSessionBean.getAvailableRoomCountByTypeAndDate(type, in, out);
                   Integer noRoomsNeeded = (int) Math.ceil((float) guests / type.getCapacity());
                    if (noRoomsNeeded > avail) {
                        System.out.print("Current room chosen only has " + avail + " rooms left, would you want to book all remaining rooms? (Enter Y for Yes) >");
                        String ans = scanner.nextLine().trim();
                        if (ans.equals("Y")) {
                            Integer i = avail * type.getCapacity();
                            totalPrice = totalPrice.add(reserveRoom(type, in, out, i));
                            guests = guests - i;
                        } 
                    } else {
                        reselect = false;
                    }
                   break;
                } else if (res == li.size() + 1) {
                    break;
                } else
                {
                    System.out.println("Invalid option, please try again!\n");                
                }
            }
        } while (reselect);

            if (currentGuest == null) {
                System.out.println("*** You are not logged in yet! Log in/Register to reserve a room! ***\n");
                System.out.println("1: Login");
                System.out.println("2: Register");

                Integer response = 0;
                while(true) {
                    System.out.print("> ");
                    response = scanner.nextInt();
                    scanner.nextLine(); 

                    if(response == 1)
                    {
                        try
                        {
                            doLogin(); 
                            System.out.println("Login successful! Logged in as: "+ currentGuest.getName()+ "\n");
                            break;
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
                            break;
                        }
                        catch(BeanValidationError ex) 
                        {
                            System.out.println("Validation failed. Please correct the following errors:\n" + ex.getMessage());
                            System.out.println("Please re-enter your details.");
                        }
                    }
                    else if (response == 3)
                    {
                        break;
                    }
                    else
                    {
                        System.out.println("Invalid option, please try again!\n");                
                    }
                }

            }
            totalPrice = totalPrice.add(reserveRoom(type, in, out, guests));
            System.out.println("Booking successful! Total price for all rooms purchased: $" + totalPrice); 
            loggedInMenu();
        }
        
    
    private BigDecimal reserveRoom(RoomType rt, Date in, Date out, Integer guests) {
        Integer capacity = rt.getCapacity();
        Integer noRooms = (int) Math.ceil((float) guests / capacity);
        BigDecimal price = BigDecimal.ZERO;
        if (noRooms > 1) {
            System.out.println("As no. of guest exceeds maximum capacity per room, system will be making reservation for " + noRooms + " rooms!");
        }
        for (int i = 0; i < noRooms; i++) {
            Reservation newR = new Reservation(in, out, (i < noRooms - 1) ? capacity : guests - (capacity * (noRooms - 1)));
            newR.setGuest(currentGuest);
            newR.setRoomType(rt);

            if (!DateUtil.sameDayLaterThan2AM(new Date(), in)) {
                Long newRId = reservationBean.createReservation(newR);
                System.out.println("Reservation No: " + newRId);
                //newR = reservationBean.detachReservation(newR);
                price = price.add(roomTypeBean.getPriceOfRoomTypeOnline(in, out, rt));
            } else {
                //past allocation same day reservation, allocate directly 
                try {
                    newR = reservationBean.createSameDayReservation(newR);
                    System.out.println("Reservation No: " + newR.getReservationId());
                    //newR = reservationBean.detachReservation(newR);
                    price = price.add(roomTypeBean.getPriceOfRoomTypeOnline(in, out, rt));
                } catch (Exception ex) {
                    System.out.println("Exception thrown: " + ex.getMessage());
                }
            }
        }
        return price;
    }
    
    private void loggedInMenu() {
        Integer response = 0;
        while(true) {
            System.out.println("*** Welcome, " + currentGuest.getName() + "! ***\n");
            System.out.println("1: Search Hotel ");
            System.out.println("2: View All My Reservations");
            System.out.println("3: View Reservation Details");
            System.out.println("4: Log Out\n");

            response = 0;
            
            while(response < 1 || response > 3)
            {
                System.out.print("> ");
                response = scanner.nextInt();
                scanner.nextLine(); 

                if(response == 1)
                {
                    try {
                        doSearchHotel();
                    } catch (RoomTypeErrorException | DateValidationError ex) {
                        System.out.println( ex.getMessage() + "\n"); 
                    }
                }
                else if (response == 2)
                {
                    doViewAllReservations();
                }
                else if (response == 3)
                {
                    doViewReservationDetails();
                }
                else if (response == 4)
                {
                    currentGuest = null;
                    break;
                }
                else
                {
                    System.out.println("Invalid option, please try again!\n");                
                }
            }
            if(response == 4)
            {
                System.out.println("Logging Out.");
                break;
            }
        }
    }
    
    private void doViewAllReservations() {
        try {
            List<Reservation> li = guestSessionBean.retrieveAllReservations(currentGuest.getGuestId());
            if (li.size() > 0) {
                System.out.println("List of Reservations: "); 
                for (Reservation r : li) {
                    System.out.println( "Reservation ID: " + r.getReservationId() + " Room Type: " + r.getRoomType().getRoomTypeName()); 
                }
                
                System.out.print("Want to view specific reservation's details? (Enter 'Y' for Yes) > "); 
                String ans = scanner.nextLine().trim();
                if (ans.equals("Y")) {
                    doViewReservationDetails();
                }
            } else {
                System.out.println("No reservations yet! "); 
            }
            
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
    }
    
    private void doViewReservationDetails() {
        System.out.print("Enter Reservation ID of your booking > "); 
        Long id = scanner.nextLong();
        scanner.nextLine();
        try {
            Reservation r = reservationBean.retrieveReservationByIdForGuest(id, currentGuest);
            System.out.println( "Reservation ID: " + r.getReservationId()); 
            System.out.println("Room Type: " + r.getRoomType().getRoomTypeName() + ", Number of guests: " + r.getGuestNo()); 
            System.out.println( "Check In Date: " + r.getCheckInDate() + ", Check Out Date: " + r.getCheckOutDate()); 
            System.out.println("Total Price: $" + "\n"); 
            
            System.out.println( "Amenities: " + r.getRoomType().getAmenities() + ", Bed: " + r.getRoomType().getBed()); 
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
    }
}
