/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package horsmanagementclient;

import ejb.stateless.RoomRateSessionBeanRemote;
import ejb.stateless.RoomSessionBeanRemote;
import ejb.stateless.RoomTypeSessionBeanRemote;
import entity.Reservation;
import entity.Room;
import entity.RoomAllocation;
import entity.RoomRate;
import entity.RoomType;
import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import util.enumeration.RateTypeEnum;
import util.enumeration.RoomStatusEnum;
import util.exception.RoomErrorException;
import util.exception.RoomTypeErrorException;

/**
 *
 * @author eliseoh
 */
public class HotelOperationModule {

    private Scanner scanner = new Scanner(System.in);
    SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
    private RoomTypeSessionBeanRemote roomTypeBean;
    private RoomSessionBeanRemote roomBean;
    private RoomRateSessionBeanRemote rateBean;

    public HotelOperationModule() {
    }

    public HotelOperationModule(RoomTypeSessionBeanRemote roomTypeBean, RoomSessionBeanRemote roomBean, RoomRateSessionBeanRemote rateBean) {
        this.roomTypeBean = roomTypeBean;
        this.roomBean = roomBean;
        this.rateBean = rateBean;
    }

    public void menuOps() {
        Integer response = 0;

        while (true) {
            System.out.println("*** Hotel Operation System :: Operations Management ***\n");
            System.out.println("1: Create New Room Type");
            System.out.println("2: View All Room Types");
            System.out.println("3: Create New Rooms");
            System.out.println("4: Update Room");
            System.out.println("5: Delete Room");
            System.out.println("6: View All Rooms");
            System.out.println("7: View Room Allocation Exception Report");
            System.out.println("8: Logout\n");
            response = 0;

            while (response < 1 || response > 8) {
                System.out.print("> ");

                response = scanner.nextInt();
                scanner.nextLine();

                if (response == 1) {
                    doCreateRoomType();
                } else if (response == 2) {
                    doViewAllRoomTypes();
                } else if (response == 3) {
                    doCreateRoom();
                } else if (response == 4) {
                    doUpdateRoom();
                } else if (response == 5) {
                    doDeleteRoom();
                } else if (response == 6) {
                    doViewAllRooms();
                } else if (response == 7) {
                    //doViewRoomAllocationReport();
                } else if (response == 8) {
                    break;
                } else {
                    System.out.println("Invalid option, please try again!\n");
                }
            }

            if (response == 8) {
                break;
            }
        }
    }

    public void menuSales() {
        Integer response = 0;

        while (true) {
            System.out.println("*** Hotel Operation System :: Sales Management ***\n");
            System.out.println("1: Create New Room Rate");
            System.out.println("2: View All Room Rates");
            System.out.println("3: View Room Rate Details");
            System.out.println("4: Logout");
            response = 0;

            while (response < 1 || response > 8) {
                System.out.print("> ");

                response = scanner.nextInt();
                scanner.nextLine();

                if (response == 1) {
                    doCreateRoomRate();
                } else if (response == 2) {
                    doViewAllRoomRates();
                } else if (response == 3) {
                    doViewRoomRateDetails();
                } else if (response == 4) {
                    break;
                } else {
                    System.out.println("Invalid option, please try again!\n");
                }
            }

            if (response == 4) {
                break;
            }
        }
    }

    public void doViewAllRoomTypes() {
        Integer res = 0;

        try {
            List<RoomType> li = roomTypeBean.retrieveAllRoomTypes();
            if (li.size() < 1) {
                System.out.println("No Room Types created!\n");
            } else {
                System.out.println("List of Room Types: ");
                for (int i = 1; i <= li.size(); i++) {
                    System.out.println(i + ": " + li.get(i - 1).getRoomTypeName());
                }
                System.out.println();

                while (true) {
                    System.out.println("Enter numbers 1 to " + li.size() + " to view details of a Room Type. Enter " + (li.size() + 1) + " to complete operation.");
                    res = 0;
                    while (res < 1 || res > li.size()) {
                        System.out.print("> ");
                        res = scanner.nextInt();
                        scanner.nextLine();
                        if (res > 0 && res <= li.size()) {
                            RoomType type = li.get(res - 1);
                            doViewRoomTypeDetails(type);
                        } else if (res == li.size() + 1) {
                            break;
                        } else {
                            System.out.println("Invalid option, please try again!\n");
                        }
                    }
                    if (res == li.size() + 1) {
                        break;
                    }
                }
            }
        } catch (RoomTypeErrorException ex) {
            System.out.println(ex.getMessage());
        }
    }

    public void doViewRoomTypeDetails(RoomType type) {
        Integer response = 0;

        System.out.println("Room Type Name: " + type.getRoomTypeName());
        System.out.println("Room Type Description: " + type.getDescription());
        System.out.println("Room Type Bed Description: " + type.getBed());
        System.out.println("Room Type Capacity: " + type.getCapacity());
        System.out.println("Room Type Amenities: " + type.getAmenities());
        List<Room> rooms = type.getRooms();
        if (rooms.size() > 0) {
            System.out.println("Rooms under room type:");
            for (Room r : rooms) {
                System.out.println("Room Number: " + r.getRoomNumber());
            }
        }

        List<RoomRate> roomRate = type.getRoomrates();
        if (roomRate.size() > 0) {
            System.out.println("Room rates under room type:");
            for (RoomRate r : roomRate) {
                System.out.println(r.getRateType() + ": $" + r.getRatePerNight());
            }
        }

        List<Reservation> reserve = type.getReservations();
        System.out.println("Number of reservations under room type: " + reserve.size() + "\n");

        while (true) {
            System.out.println("*** Hotel Operation System :: Room Type Management ***\n");
            System.out.println("1: Update Room Type");
            System.out.println("2: Delete Room Types");
            System.out.println("3: Exit");
            System.out.print("> ");

            response = scanner.nextInt();
            scanner.nextLine();

            if (response == 1) {
                doUpdateRoomType(type);
            } else if (response == 2) {
                doDeleteRoomType(type);
            } else if (response == 3) {
                System.out.println("Exiting Room Type Management.");
                break;  // Break out of the main while loop
            } else {
                System.out.println("Invalid option, please try again!\n");
            }
        }
    }

    public void doDeleteRoomType(RoomType type) {
        if (type.getReservations().size() > 0) {
            System.out.println("Still have pending reservations, unable to delete Room Type, will disable room type instead.\n");
        }
        roomTypeBean.deleteRoomType(type);
    }

    public void doDeleteRoom() {
        System.out.println("*** HoRS Management System :: Room Operations :: Delete Room ***\n");
        System.out.print("Enter Room Number> ");
        Integer response = 0;
        try {
            Room r = roomBean.retrieveRoomByNumber(scanner.nextInt());
            scanner.nextLine();
            if (r.getRoomAllocation().size() > 0) {
                System.out.println("Still have pending room allocations, unable to delete Room, will disable room instead.\n");
            }
            roomBean.deleteRoom(r);

        } catch (RoomErrorException ex) {
            System.out.println(ex.getMessage());
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
            System.out.println("Room Type " + type.getRoomTypeName() + " created successfully!\n");
        } else {
            System.out.println("Error occurred during room type creation!\n");
        }
    }

    public void doCreateRoom() {
        Room r = new Room();
        System.out.println("*** HoRS Management System :: Room Operations :: Create New Room ***\n");

        try {
            List<RoomType> li = roomTypeBean.retrieveAllRoomTypes();
            if (li.size() < 1) {
                System.out.println("No Room Types created!\n");
            } else {
                Integer i = 1;
                for (RoomType roomType : li) {
                    if (!roomType.getIsDisabled()) {
                        System.out.println(i + ": " + roomType.getRoomTypeName());
                        i++;
                    }
                }
                System.out.println();
                System.out.print("Choose Room Type> ");

                Integer rtNo = scanner.nextInt();
                scanner.nextLine();
                RoomType rt = li.get(rtNo - 1);

                System.out.print("Enter Room Number> ");
                r.setRoomNumber(scanner.nextInt());
                scanner.nextLine();

                Long newRoom = roomBean.createRoom(r, rt);
                if (newRoom != null) {
                    System.out.println("Room Number " + r.getRoomNumber() + " created successfully!\n");
                } else {
                    System.out.println("Error occurred during room type creation!\n");
                }
            }
        } catch (RoomTypeErrorException ex) {
            System.out.println(ex.getMessage() + "\n");
        }
    }

    //doesnt allow update of associations
    public void doUpdateRoomType(RoomType rt) {
        System.out.println("*** HoRS Management System :: Room Type Operations :: Update Room Type ***\n");
        Integer response = 0;

        while (true) {
            System.out.println("1: Change Room Type Name");
            System.out.println("2: Change Room Type Description");
            System.out.println("3: Change Bed Description");
            System.out.println("4: Change Room Type Capacity");
            System.out.println("5: Change Amenities Description");
            System.out.println("6: Exit\n");
            response = 0;

            while (response < 1 || response > 6) {
                System.out.print("> ");

                response = scanner.nextInt();
                scanner.nextLine();

                if (response == 1) {
                    System.out.print("Enter new Room Type Name> ");
                    rt.setRoomTypeName(scanner.nextLine().trim());
                    roomTypeBean.updateRoomType(rt);
                    System.out.println("\n Successful update!");
                } else if (response == 2) {
                    System.out.print("Enter new Room Type Description> ");
                    rt.setDescription(scanner.nextLine().trim());
                    roomTypeBean.updateRoomType(rt);
                    System.out.println("\n Successful update!");
                } else if (response == 3) {
                    System.out.print("Enter new Room Type Bed Description> ");
                    rt.setBed(scanner.nextLine().trim());
                    roomTypeBean.updateRoomType(rt);
                    System.out.println("\n Successful update!");
                } else if (response == 4) {
                    System.out.print("Enter new Room Type Capacity> ");
                    rt.setCapacity(scanner.nextInt());
                    roomTypeBean.updateRoomType(rt);
                    System.out.println("\n Successful update!");
                } else if (response == 5) {
                    System.out.print("Enter new Room Type Amenities Description> ");
                    rt.setAmenities(scanner.nextLine().trim());
                    roomTypeBean.updateRoomType(rt);
                    System.out.println("\n Successful update!");
                } else if (response == 6) {
                    break;
                } else {
                    System.out.println("Invalid option, please try again!\n");
                }
            }

            if (response == 6) {
                break;
            }
        }
    }

    //cannot update room allocation, need review to see if needed
    public void doUpdateRoom() {
        System.out.println("*** HoRS Management System :: Room Operations :: Update Room ***\n");
        System.out.print("Enter Room Number> ");
        Integer response = 0;

        try {
            Room r = roomBean.retrieveRoomByNumber(scanner.nextInt());
            scanner.nextLine();
            while (true) {
                System.out.println("1: Change Room Number");
                System.out.println("2: Change Room Status");
                System.out.println("3: Change Room Type");
                System.out.println("4: Exit\n");
                response = 0;

                while (response < 1 || response > 4) {
                    System.out.print("> ");

                    response = scanner.nextInt();
                    scanner.nextLine();

                    if (response == 1) {
                        System.out.print("Enter new Room number> ");
                        r.setRoomNumber(scanner.nextInt());
                        scanner.nextLine();
                        roomBean.updateRoom(r);
                        System.out.println("\n Successful update!");
                    } else if (response == 2) {
                        System.out.println("1: Available");
                        System.out.println("2: Occupied");
                        System.out.print("Enter new Room Status> ");
                        Integer no = scanner.nextInt();
                        scanner.nextLine();

                        if (no == 1) {
                            r.setStatus(RoomStatusEnum.AVAIL);
                            roomBean.updateRoom(r);
                            System.out.println("\n Successful update!");
                        } else if (no == 2) {
                            r.setStatus(RoomStatusEnum.OCCUPIED);
                            roomBean.updateRoom(r);
                            System.out.println("\n Successful update!");
                        } else {
                            System.out.println("Unknown Input!");
                        }
                    } else if (response == 3) {
                        try {
                            List<RoomType> li = roomTypeBean.retrieveAllRoomTypes();
                            if (li.size() < 1) {
                                System.out.println("No Room Types created!\n");
                            } else {
                                System.out.println("Choose new room type:  ");
                                for (int i = 1; i <= li.size(); i++) {
                                    System.out.println(i + ": " + li.get(i - 1).getRoomTypeName());
                                }
                                System.out.println();

                                System.out.print("> ");
                                Integer rmType = scanner.nextInt();
                                scanner.nextLine();
                                if (rmType > 0 && rmType <= li.size()) {
                                    RoomType newRt = li.get(rmType - 1);
                                    roomBean.updateRoomTypeOfRoom(r, newRt);
                                } else {
                                    System.out.println("Unknown Input!");
                                }
                            }
                        } catch (RoomTypeErrorException ex) {
                            System.out.println(ex.getMessage());
                        }
                    } else if (response == 4) {
                        break;
                    } else {
                        System.out.println("Invalid option, please try again!\n");
                    }
                }

                if (response == 4) {
                    break;
                }
            }
        } catch (RoomErrorException ex) {
            System.out.println(ex.getMessage());
        }
    }

    public void doViewAllRooms() {
        try {
            List<Room> roomList = roomBean.retrieveAllRooms();
            for (Room r : roomList) {
                System.out.println("Room Number: " + r.getRoomNumber() + " , Room Type: " + r.getRoomType().getRoomTypeName() + " . Room Status: " + r.getStatus());
                List<RoomAllocation> alloList = r.getRoomAllocation();
                System.out.println("Number of room allocations: " + alloList.size());
            }
        } catch (RoomErrorException ex) {
            System.out.println(ex.getMessage());
        }
    }

    public void doCreateRoomRate() {
        RoomRate r = new RoomRate();
        System.out.println("*** HoRS Management System :: Room Rate Operations :: Create New Room Rate ***\n");
        try {
            List<RoomType> li = roomTypeBean.retrieveAllRoomTypes();
            if (li.isEmpty()) {
                System.out.println("No Room Types created!\n");
                return;
            }

            System.out.println("List of Room Types");
            int i = 1;
            for (RoomType roomType : li) {
                if (!roomType.getIsDisabled()) {
                    System.out.println(i + ": " + roomType.getRoomTypeName());
                    i++;
                }
            }

            System.out.print("Choose Room Type> ");
            Integer rtNo = scanner.nextInt();
            scanner.nextLine();

            RoomType rt = li.get(rtNo - 1);

            System.out.println("List of Rate Types");
            int j = 1;
            for (RateTypeEnum rateEnum : RateTypeEnum.values()) {
                System.out.println(j + ": " + rateEnum);
                j++;
            }

            System.out.print("Choose Rate Type> ");
            int response = scanner.nextInt();
            scanner.nextLine();
            RateTypeEnum rateType = RateTypeEnum.values()[response - 1];

            // Check if a RoomRate of the chosen RateType already exists for this RoomType
            boolean rateExists = roomTypeBean.roomRateExistsForType(rt.getRoomTypeId(), rateType);
            if (rateExists) {
                System.out.println("A room rate of type " + rateType + " already exists for the selected room type.\n");
                return;
            }

            r.setRoomType(rt);
            r.setRateType(rateType);

            System.out.print("Enter room rate name> ");
            r.setName(scanner.nextLine().trim());

            System.out.print("Enter rate per night> ");
            r.setRatePerNight(new BigDecimal(scanner.nextLine().trim()));

            if (rateType.equals(RateTypeEnum.PEAK) || rateType.equals(RateTypeEnum.PROMOTION)) {
                System.out.print("Enter start date (yyyy-MM-dd)> ");
                r.setStartDate(parseDate(scanner.nextLine().trim(), formatter));
                System.out.print("Enter end date (yyyy-MM-dd)> ");
                r.setEndDate(parseDate(scanner.nextLine().trim(), formatter));
            }

            // Persist the RoomRate if no duplicate rate type exists
            Long newRateId = rateBean.createRoomRate(r);
            if (newRateId != null) {
                RoomRate roomrate = rateBean.retrieveRoomRateById(newRateId);
                rt.getRoomrates().add(roomrate);
                roomTypeBean.updateRoomType(rt);
                System.out.println("Room Rate " + r.getName() + " created successfully!\n");
            } else {
                System.out.println("Error occurred during room rate creation!\n");
            }
        } catch (RoomTypeErrorException ex) {
            System.out.println(ex.getMessage() + "\n");
        }
    }

// Helper method for parsing dates
    private Date parseDate(String dateStr, SimpleDateFormat formatter1) {
        try {
            DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
            return formatter.parse(dateStr);
        } catch (ParseException e) {
            System.out.println("Invalid date format. Please use yyyy-MM-dd.");
            return null;
        }
    }

    public void doViewAllRoomRates() {
        System.out.println("*** HoRS Management System :: Room Rate Operations :: View All Room Rates ***\n");
        List<RoomRate> rates = rateBean.retrieveAllRoomRates();

        System.out.printf("%-20s %-20s %-15s %-10s%n", "Name", "Room Type", "Rate Type", "Rate/Night");
        System.out.println("-----------------------------------------------------------------------");

        for (RoomRate rate : rates) {
            System.out.printf("%-20s %-20s %-15s %-10.2f%n",
                    rate.getName(),
                    rate.getRoomType().getRoomTypeName(),
                    rate.getRateType(),
                    rate.getRatePerNight().doubleValue());
        }
    }

    public void doViewRoomRateDetails() {
        System.out.println("*** HoRS Management System :: Room Rate Operations :: View Room Rate Details ***\n");
        System.out.println("Enter room rate name> ");
        String name = scanner.nextLine().trim();
        RoomRate rate = rateBean.retrieveRoomRateByName(name);
        System.out.println("Room Rate Name: " + name);
        System.out.println("Room Type: " + rate.getRoomType().getRoomTypeName());
        System.out.println("Rate Type: " + rate.getRateType());
        System.out.println("Rate Per Night: " + rate.getRatePerNight());
        if (rate.getRateType().equals(RateTypeEnum.PEAK) || rate.getRateType().equals(RateTypeEnum.PROMOTION)) {
            System.out.println("Start Date: " + rate.getStartDate());
            System.out.println("End Date: " + rate.getEndDate());

        }
        System.out.println("Choose action: ");
        System.out.println("1: Update Room Rate");
        System.out.println("2: Delete Room Rate");
        System.out.println("3: Back");
        int res = scanner.nextInt();
        if (res == 1) {
            doUpdateRoomRate(rate);
        } else if (res == 2) {
            doDeleteRoomRate(rate);
        } else {
            return;
        }
    }

    public void doUpdateRoomRate(RoomRate rate) {
        System.out.println("*** HoRS Management System :: Room Rate Operations :: Update Room Rate ***\n");
        Integer response = 0;

        while (true) {
            System.out.println("1: Change Room Rate Name");
            System.out.println("2: Change Room Type");
            System.out.println("3: Change Rate Type");
            System.out.println("4: Change Rate Per Night");
            System.out.println("5: Change Start Date");
            System.out.println("6: Change End Date");
            System.out.println("7: Exit\n");
            response = 0;

            while (response < 1 || response > 7) {
                System.out.print("> ");

                response = scanner.nextInt();
                scanner.nextLine();

                if (response == 1) {
                    System.out.print("Enter new Room Rate Name> ");
                    rate.setName(scanner.nextLine().trim());
                    rateBean.updateRoomRate(rate);
                    System.out.println("\n Successful update!");
                } else if (response == 2) {
                    try {
                        List<RoomType> li = roomTypeBean.retrieveAllRoomTypes();
                        System.out.println("List of Room Types");

                        if (li.size() < 1) {
                            System.out.println("No Room Types created!\n");
                        } else {
                            Integer i = 1;
                            for (RoomType roomType : li) {
                                if (!roomType.getIsDisabled()) {
                                    System.out.println(i + ": " + roomType.getRoomTypeName());
                                    i++;
                                }
                            }
                            System.out.println();
                            System.out.print("Choose new Room Type> ");

                            Integer rtNo = scanner.nextInt();
                            scanner.nextLine();
                            RoomType rt = li.get(rtNo - 1);
                            scanner.nextLine();
                            rate.setRoomType(rt);
                            rateBean.updateRoomRate(rate);
                            rt.getRoomrates().add(rate);
                            roomTypeBean.updateRoomType(rt);
                            System.out.println("\n Successful update!");
                        }
                    } catch (RoomTypeErrorException ex) {
                        System.out.println(ex.getMessage() + "\n");
                    }
                } else if (response == 3) {
                    System.out.println("List of Rate Types");

                    for (RateTypeEnum rateEnum : RateTypeEnum.values()) {
                        int j = 1;
                        System.out.println(j + ": " + rateEnum);
                        j++;
                    }
                    System.out.println("Choose new Rate Type> ");
                    int res = scanner.nextInt();
                    scanner.nextLine();
                    List<RateTypeEnum> rateTypes = Arrays.asList(RateTypeEnum.values());
                    RateTypeEnum rateType = rateTypes.get(response - 1);
                    rate.setRateType(rateType);
                    rateBean.updateRoomRate(rate);
                    System.out.println("\n Successful update!");
                } else if (response == 4) {
                    System.out.print("Enter new Rate Per Night> ");
                    String rateInput = scanner.nextLine();
                    BigDecimal ratePerNight = new BigDecimal(rateInput);
                    rate.setRatePerNight(ratePerNight);
                    rateBean.updateRoomRate(rate);
                    System.out.println("\n Successful update!");
                } else if (response == 5) {
                    System.out.print("Enter start date (yyyy-MM-dd)> ");
                    Date startDate = parseDate(scanner.nextLine(), formatter);
                    rate.setStartDate(startDate);
                    rateBean.updateRoomRate(rate);
                    System.out.println("\n Successful update!");
                } else if (response == 6) {
                    System.out.print("Enter end date (yyyy-MM-dd)> ");
                    Date endDate = parseDate(scanner.nextLine(), formatter);
                    rate.setEndDate(endDate);
                    rateBean.updateRoomRate(rate);
                    System.out.println("\n Successful update!");
                } else if (response == 7) {
                    break;
                } else {
                    System.out.println("Invalid option, please try again!\n");
                }
            }

            if (response == 7) {
                break;
            }
        }
    }

    public void doDeleteRoomRate(RoomRate rate) {
        scanner.nextLine();
        String res = "";
        System.out.println("*** HoRS Management System :: Room Rate Operations :: Delete Room Rate ***\n");
        System.out.print("Enter Y to delete room rate, N to exit> ");
        res = scanner.nextLine().trim();

        if (res.equalsIgnoreCase("Y")) {
            String resultMessage = rateBean.deleteRoomRate(rate);
            System.out.println(resultMessage); // Display the result to the client
        } else {
            System.out.println("Room rate deletion canceled.");
        }
    }
}
