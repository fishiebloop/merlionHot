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
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import ws.holiday.DateValidationError_Exception;
import ws.holiday.HolidayWebService;
import ws.holiday.HolidayWebService_Service;
import ws.holiday.InvalidLoginCredentialException;
import ws.holiday.RoomType;
import ws.holiday.RoomTypeErrorException_Exception;

/**
 *
 * @author eliseoh
 */
public class MainApp {
    private static Scanner scanner = new Scanner(System.in);
    private Long partnerId;
    
    public XMLGregorianCalendar convertToXMLGregorianCalendar(Date date) throws Exception {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return DatatypeFactory.newInstance().newXMLGregorianCalendar((GregorianCalendar) calendar);
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

                if(response == 1)
                {
                    doLogin();
                }
                else if (response == 2)
                {
                    try {
                        doSearchRoom();
                    } catch (RoomTypeErrorException_Exception | DateValidationError_Exception ex) {
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
    
    private void doLogin() {
        System.out.println("*** Holiday Reservation System :: Partner Login ***\n");
        
        System.out.print("Enter username> ");
        String username = scanner.nextLine().trim();
        System.out.print("Enter password> ");
        String password = scanner.nextLine().trim();
        try {
            HolidayWebService_Service service = new HolidayWebService_Service();
            partnerId = service.getHolidayWebServicePort().partnerLogin(username, password);
        } catch (InvalidLoginCredentialException e) {
            // Handle the SOAP fault
            System.out.println("Login failed: " + e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    private void doSearchRoom() throws RoomTypeErrorException_Exception, DateValidationError_Exception {
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
        try {
            XMLGregorianCalendar xmlIn = convertToXMLGregorianCalendar(in);
            XMLGregorianCalendar xmlOut = convertToXMLGregorianCalendar(out);

            System.out.print("Enter No of Guest(s) > ");
            Integer guests = scanner.nextInt();

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

                    System.out.print("Enter Room Type for reservation > ");
                    res = 0;
                    res = scanner.nextInt();
                    scanner.nextLine();
                    if (res > 0 && res <= li.size()) {
                       type = li.get(res-1);
                       Integer avail = port.getAvailableRoomCountByTypeAndDate(type.getRoomTypeId(), xmlIn, xmlOut);
                       Integer noRoomsNeeded = (int) Math.ceil((float) guests / type.getCapacity());
                        if (noRoomsNeeded > avail) {
                            System.out.print("Current room chosen only has " + avail + " rooms left, would you want to book all remaining rooms? (Enter Y for Yes) >");
                            String ans = scanner.nextLine().trim();
                            if (ans.equals("Y")) {
                                Integer i = avail * type.getCapacity();
                                //totalPrice = totalPrice.add(reserveRoom(type, in, out, i));
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

                if (partnerId == null) {
                    System.out.println("*** You are not logged in yet! Log in to reserve a room! ***\n");
                    System.out.println("1: Login");
                    doLogin(); 
                    System.out.println("Login successful!" + "\n");
                }

                //totalPrice = totalPrice.add(reserveRoom(type, in, out, guests));
                System.out.println("Booking successful! Total price for all rooms purchased: $" + totalPrice); 
        } catch (Exception ex) {
            System.out.println("Date formatted wrongly! Try again!");
        }
    }    
}
