/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package holidayreservationsystemseclient;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import ws.holiday.BeanValidationError_Exception;
import ws.holiday.CannotUpgradeException;
import ws.holiday.CannotUpgradeException_Exception;
import ws.holiday.DateValidationError_Exception;
import ws.holiday.HolidayWebService;
import ws.holiday.HolidayWebService_Service;
import ws.holiday.InvalidLoginCredentialException;
import ws.holiday.NoAvailableRoomException_Exception;
import ws.holiday.Reservation;
import ws.holiday.ReservationDTO;
import ws.holiday.ReservationErrorException_Exception;
import ws.holiday.Room;
import ws.holiday.RoomAllocationNotFoundException_Exception;
import ws.holiday.RoomType;
import ws.holiday.RoomTypeErrorException_Exception;

/**
 *
 * @author eliseoh
 */
public class MainApp {
    private static Scanner scanner = new Scanner(System.in);
    private Long partnerId;
    
    public XMLGregorianCalendar convertToXMLGregorianCalendar(Date date) throws DatatypeConfigurationException {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return DatatypeFactory.newInstance().newXMLGregorianCalendar((GregorianCalendar) calendar);
    }
    
    public static boolean sameDayLaterThan2AM(XMLGregorianCalendar date, XMLGregorianCalendar checkIn) {
        // Convert XMLGregorianCalendar to Calendar
        Calendar calendarDate = date.toGregorianCalendar();
        Calendar calendarCheckIn = checkIn.toGregorianCalendar();

        // Check if date and checkIn are on the same day
        boolean sameDay = calendarDate.get(Calendar.YEAR) == calendarCheckIn.get(Calendar.YEAR) &&
                          calendarDate.get(Calendar.DAY_OF_YEAR) == calendarCheckIn.get(Calendar.DAY_OF_YEAR);

        // Create a calendar for 2 AM on the same day
        Calendar twoAM = (Calendar) calendarCheckIn.clone(); // Clone the checkIn calendar
        twoAM.set(Calendar.HOUR_OF_DAY, 2);  // Set to 2 AM
        twoAM.set(Calendar.MINUTE, 0);
        twoAM.set(Calendar.SECOND, 0);
        twoAM.set(Calendar.MILLISECOND, 0);

        // Check if it's the same day and the time is later than 2 AM
        return sameDay && calendarDate.after(twoAM);
    }
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
                scanner.nextLine();

                if(response == 1)
                {
                    try
                    {
                        doLogin(); 
                        System.out.println("Login successful!");
                        loggedInMenu();
                    }
                    catch(InvalidLoginCredentialException ex) 
                    {
                        System.out.println("Login failed: " + ex.getMessage() + "\n");
                    }
                }
                else if (response == 2)
                {
                    try {
                        doSearchRoom();
                    } catch (RoomTypeErrorException_Exception | DateValidationError_Exception  | BeanValidationError_Exception ex) {
                        System.out.println( ex.getMessage() + "\n"); 
                    }
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
    
    private void doLogin() throws InvalidLoginCredentialException {
        System.out.println("*** Holiday Reservation System :: Partner Login ***\n");
        
        System.out.print("Enter username> ");
        String username = scanner.nextLine().trim();
        System.out.print("Enter password> ");
        String password = scanner.nextLine().trim();
            HolidayWebService_Service service = new HolidayWebService_Service();
            partnerId = service.getHolidayWebServicePort().partnerLogin(username, password);
    }
    
    private void doSearchRoom() throws RoomTypeErrorException_Exception, DateValidationError_Exception, BeanValidationError_Exception {
        Integer res; 
        RoomType type = null;
        BigDecimal totalPrice = BigDecimal.ZERO;
        Date out; 
        Date in;
        HolidayWebService_Service service = new HolidayWebService_Service();
        HolidayWebService port = service.getHolidayWebServicePort();
        
        SimpleDateFormat dateTimeFormat = new SimpleDateFormat("dd-MM-yyyy");
        System.out.println("*** Holiday Reservation System :: Search Room ***\n");
        while(true) {
            System.out.print("Enter Check in Date! Please format in dd-MM-yyyy > ");
            String checkIn = scanner.nextLine().trim();
            try {
                // Parse the input string into a Date object
                in = dateTimeFormat.parse(checkIn);
                
                String fullDateTimeString = dateTimeFormat.format(in);
                in = dateTimeFormat.parse(fullDateTimeString);
                
                break;  // Exit the loop after successful conversion
            } catch (ParseException ex) {
                System.out.println("Date formatted wrongly! Try again!");
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        
        while(true) {
            System.out.print("Enter Check out Date! Please format in dd-MM-yyyy > ");
            String checkOut = scanner.nextLine().trim();
            try {
                // Parse the input string into a Date object
                out = dateTimeFormat.parse(checkOut);
                
                String fullDateTimeString = dateTimeFormat.format(in);
                in = dateTimeFormat.parse(fullDateTimeString);
                
                break;
            } catch (ParseException ex) {
                System.out.println("Date formatted wrongly! Try again!");
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        XMLGregorianCalendar xmlIn = null;
        XMLGregorianCalendar xmlOut = null;
        try {
            xmlIn = convertToXMLGregorianCalendar(in);
            xmlOut = convertToXMLGregorianCalendar(out);
        } catch (Exception ex) {
            System.out.println("Date formatted wrongly! Try again!");
        }

            System.out.print("Enter customer email > ");
            String email = scanner.nextLine().trim();
            if (!port.guestExist(email)) {
                System.out.print("Please provide guest's name > ");
                String name = scanner.nextLine().trim();
                try {
                    port.createGuest(name, email);
                } catch (BeanValidationError_Exception ex) {
                    System.out.println("Email formatted wrongly!");
                }
            }

            Boolean reselect = true;
            do {
                List<RoomType> li = port.retrieveAllAvailRoomTypeOnline(xmlIn, xmlOut);
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
                        System.out.println("    Price for entire stay: $" + port.getPriceOfRoomTypeOnline(xmlIn, xmlOut, li.get(i-1).getRoomTypeId()) + "\n");
                        System.out.println();
                    }
                    System.out.println();

                    System.out.print("Enter number of rooms for reservation > ");
                    Integer noRooms = scanner.nextInt();
                    scanner.nextLine();
                    
                    for (int i = 0; i < noRooms; i++) {
                        System.out.print("Enter Room Type for reservation > ");
                        res = 0;
                        res = scanner.nextInt();        
                        scanner.nextLine();
                        if (res > 0 && res <= li.size()) {
                            if (partnerId == null) {
                                System.out.println("*** You are not logged in yet! Log in to reserve a room! ***\n");
                                try
                                {
                                    doLogin(); 
                                    System.out.println("Login successful!");
                                }
                                catch(InvalidLoginCredentialException ex) 
                                {
                                    System.out.println("Login failed: " + ex.getMessage() + "\n");
                                } 
                            }
                            type = li.get(res-1);
                            totalPrice = totalPrice.add(reserveRoom(type, xmlIn, xmlOut, email, partnerId));
                            System.out.println("Booking successful! Total price for all rooms purchased: $" + totalPrice); 
                            loggedInMenu();
                            reselect = false;
                        } else {
                            System.out.println("Invalid option, please try again!\n"); 
                        }
                    }
                }
            } while (reselect);
        
    }

    private BigDecimal reserveRoom(RoomType rt, XMLGregorianCalendar in, XMLGregorianCalendar out, String email, Long partnerId) {
        HolidayWebService_Service service = new HolidayWebService_Service();
        HolidayWebService port = service.getHolidayWebServicePort();
        BigDecimal price = BigDecimal.ZERO;
 
        try {
            XMLGregorianCalendar now = convertToXMLGregorianCalendar(new Date());

                if (!sameDayLaterThan2AM(now, in)) {
                    try {
                        Reservation newR = port.createReservation(rt.getRoomTypeId(),in, out, email, partnerId);
                        System.out.println("Reservation No: " + newR.getReservationId());
                        try {
                            price = price.add(port.getPriceOfRoomTypeOnline(in, out, rt.getRoomTypeId()));
                        } catch (RoomTypeErrorException_Exception ex) {
                            System.out.println("Error getting price of room!");
                        }
                    }catch (Exception ex) {
                        System.out.println("Exception thrown: " + ex.getMessage());
                    }
                    
                } else {
                    //past allocation same day reservation, allocate directly 
                    try {
                        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
                        System.out.println("RoomType: " + rt.getRoomTypeName());
                        System.out.println("In Date: " + dateFormat.format(in.toGregorianCalendar().getTime()));
                        System.out.println("Out Date: " + dateFormat.format(out.toGregorianCalendar().getTime()));
                        System.out.println("Guest Email: " + email);
                        System.out.println("Partner ID: " + partnerId);
                        Reservation newR = port.createSameDayReservation(rt.getRoomTypeId(),in, out, email, partnerId);
                        System.out.println("Reservation No: " + newR.getReservationId());
                        try {
                            price = price.add(port.getPriceOfRoomTypeOnline(in, out, rt.getRoomTypeId()));
                        } catch (RoomTypeErrorException_Exception ex) {
                            System.out.println("Error getting price of room!");
                        }
                    } catch (RoomTypeErrorException_Exception | InvalidLoginCredentialException | NoAvailableRoomException_Exception | CannotUpgradeException_Exception ex) {
                        System.out.println("Exception thrown: " + ex.getMessage());
                        ex.printStackTrace();  // For more detailed output
                    }
                }
            return price;
        } catch (DatatypeConfigurationException ex) {
            System.out.println("Date format wrong!");
            return BigDecimal.ZERO;
        } 
    }   
    
    private void loggedInMenu() {
        Integer response = 0;
        while(true) {
            System.out.println("*** Welcome to Holiday Reservation System! ***\n");
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
                        doSearchRoom();
                    } catch (RoomTypeErrorException_Exception | DateValidationError_Exception | BeanValidationError_Exception ex) {
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
                    partnerId = null;
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
        HolidayWebService_Service service = new HolidayWebService_Service();
        HolidayWebService port = service.getHolidayWebServicePort();
        try {
            List<ReservationDTO> li = port.retrieveAllPartnerReservation(partnerId);
            if (li.size() > 0) {
                System.out.println("List of Reservations: "); 
                for (ReservationDTO r : li) {
                    System.out.println( "Reservation ID: " + r.getReservationId() + " Room Type: " + r.getRoomTypeName()); 
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
        HolidayWebService_Service service = new HolidayWebService_Service();
        HolidayWebService port = service.getHolidayWebServicePort();
        System.out.print("Enter Reservation ID of your booking > "); 
        Long id = scanner.nextLong();
        scanner.nextLine();
        try {
            ReservationDTO r = port.retrieveReservationDetails(id);
            System.out.println( "Reservation ID: " + r.getReservationId()); 
            System.out.println( "Guest email: " + r.getGuestEmail() + ", Guest Name: " + r.getGuestName());
            System.out.println("Room Type: " + r.getRoomTypeName()); 
            System.out.println( "Check In Date: " + r.getCheckIn() + ", Check Out Date: " + r.getCheckOut()); 
            
            try {
                Room ra = port.retrieveAllocationRoom(id);
                System.out.println( "Room Number " + ra.getRoomNumber());
            } catch (ReservationErrorException_Exception ex) {
                System.out.println("Cannot find reservation");
            } catch (RoomAllocationNotFoundException_Exception ex) {
                System.out.println("No room allocation yet.");
            }

        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
    }
}
