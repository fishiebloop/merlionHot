/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package horsmanagementclient;

import ejb.stateless.ExceptionReportSessionBeanRemote;
import ejb.stateless.RoomAllocationSessionBeanRemote;
import ejb.stateless.RoomRateSessionBeanRemote;
import ejb.stateless.RoomSessionBeanRemote;
import ejb.stateless.RoomTypeSessionBeanRemote;
import entity.ExceptionReport;
import entity.Guest;
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
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import util.enumeration.RateTypeEnum;
import util.enumeration.RoomStatusEnum;
import util.exception.RoomErrorException;
import util.exception.RoomRateErrorException;
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
    private ExceptionReportSessionBeanRemote exceptionReportSessionBean;
    private RoomAllocationSessionBeanRemote roomAllocationSessionBean;

    public HotelOperationModule() {
    }

    public HotelOperationModule(RoomTypeSessionBeanRemote roomTypeBean, RoomSessionBeanRemote roomBean, RoomRateSessionBeanRemote rateBean, ExceptionReportSessionBeanRemote exceptionReportSessionBean, RoomAllocationSessionBeanRemote roomAllocationSessionBean) {
        this.roomTypeBean = roomTypeBean;
        this.roomBean = roomBean;
        this.rateBean = rateBean;
        this.exceptionReportSessionBean = exceptionReportSessionBean;
        this.roomAllocationSessionBean = roomAllocationSessionBean;
    }

    public void menuOps() {
        Integer response = 0;

        while (true) {
            System.out.println("*** Hotel Operation System :: Operations Management ***\n");
            System.out.println("1: Create New Room Type");
            System.out.println("2: View All Room Types");
            System.out.println("3: View Room Type Details");
            System.out.println("4: Create New Rooms");
            System.out.println("5: Update Room");
            System.out.println("6: Delete Room");
            System.out.println("7: View All Rooms");
            System.out.println("8: View Room Allocation Exception Report");
            System.out.println("9: Allocate room for reservations");
            System.out.println("10: Logout\n");
            response = 0;

            while (response < 1 || response > 10) {
                System.out.print("> ");

                response = scanner.nextInt();
                scanner.nextLine();
                if (response == 1) {
                    doCreateRoomType();
                } else if (response == 2) {
                    doViewAllRoomTypes();
                } else if (response == 3) {
                    doViewRoomTypeDetails();
                } else if (response == 4) {
                    doCreateRoom();
                } else if (response == 5) {
                    doUpdateRoom();
                } else if (response == 6) {
                    doDeleteRoom();
                } else if (response == 7) {
                    doViewAllRooms();
                } else if (response == 8) {
                    doViewRoomAllocationReport();
                } else if (response == 9) {
                    roomAllocationSessionBean.performRoomAllocations();
                    System.out.println("Room allocation process.");
                } else if (response == 10) {
                    break;
                } else {
                    System.out.println("Invalid option, please try again!\n");
                }
            }

            if (response == 10) {
                break;
            }
        }
    }

    public void doViewRoomAllocationReport() {
        Integer response = 0;

        System.out.println("*** Hotel Operation System :: Operations Management :: View Room Allocation Report ***\n");
        List<ExceptionReport> higherAvail = exceptionReportSessionBean.retrieveHigherAvailList();
        if (higherAvail.size() < 1) {
            System.out.println("No reservations allocated to next higher room type.");
        } else {
            System.out.println("List of reservations allocated to next higher room type:");
            Integer i = 1;
            for (ExceptionReport er : higherAvail) {
                Reservation r = er.getReservation();
                Guest g = r.getGuest();
                System.out.println(i + ".");
                System.out.println("    Reservation ID: " + r.getReservationId());
                System.out.println("    Guest Name: " + g.getName());
                System.out.println("    Guest Email: " + g.getEmail());
                System.out.println("    Allocated Room Type: " + r.getRoomType().getRoomTypeName());
                System.out.println("    Allocated Room Number: " + r.getRoomAllocation().getRoom().getRoomNumber());
                System.out.println();
                i++;
            }
        }

        List<ExceptionReport> noHigherAvail = exceptionReportSessionBean.retrieveNoUpgradeList();
        if (noHigherAvail.size() < 1) {
            System.out.println("No reservations with no allocated rooms.");
        } else {
            System.out.println("List of reservations with no allocated rooms:");
            Integer i = 1;
            for (ExceptionReport er : noHigherAvail) {
                Reservation r = er.getReservation();
                Guest g = r.getGuest();
                System.out.println(i + ".");
                System.out.println("    Reservation ID: " + r.getReservationId());
                System.out.println("    Guest Name: " + g.getName());
                System.out.println("    Guest Email: " + g.getEmail());
                System.out.println();
                i++;
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

    public void doViewRoomTypeDetails() {
        System.out.println("Enter Room Type ID (You may retrieve ID details from 'View All Room Types')> ");
        Long id = scanner.nextLong();
        scanner.nextLine();

        try {
            RoomType type = roomTypeBean.retrieveRoomTypeById(id);
            getRoomTypeDetails(type);
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
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

                    System.out.println("Room Type ID: " + li.get(i - 1).getRoomTypeId() + ", Room Type Name: " + li.get(i - 1).getRoomTypeName());
                }
                System.out.println();

                System.out.print("Want to view specific Room Type details? (Enter 'Y' for Yes) > ");
                String ans = scanner.nextLine().trim();
                if (ans.equals("Y")) {
                    doViewRoomTypeDetails();
                }
            }
        } catch (RoomTypeErrorException ex) {
            System.out.println(ex.getMessage());
        }
    }

    public void getRoomTypeDetails(RoomType type) {
        Integer response = 0;

        System.out.println("Room Type Name: " + type.getRoomTypeName());
        System.out.println("Room Type Description: " + type.getDescription());
        System.out.println("Room Type Bed Description: " + type.getBed());
        System.out.println("Room Type Capacity: " + type.getCapacity());
        System.out.println("Room Type Amenities: " + type.getAmenities());
        System.out.println("Disabled?: " + (type.getIsDisabled() ? "Yes" : "No"));
        System.out.println("Next Higher Room Type: "
                + (type.getNextHigherRoomType() != null ? type.getNextHigherRoomType().getRoomTypeName() : "None"));
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

    public void doUpdateRoomType(RoomType rt) {
        System.out.println("*** HoRS Management System :: Room Type Operations :: Update Room Type ***\n");

        while (true) {
            System.out.println("1: Change Room Type Name");
            System.out.println("2: Change Room Type Description");
            System.out.println("3: Change Bed Description");
            System.out.println("4: Change Room Type Capacity");
            System.out.println("5: Change Amenities Description");
            System.out.println("6: Change Next Higher Room Type");
            System.out.println("7: Exit\n");
            System.out.print("> ");

            int response = scanner.nextInt();
            scanner.nextLine();

            if (response == 1) {
                System.out.print("Enter new Room Type Name> ");
                String newRoomTypeName = scanner.nextLine().trim();

                // Check if room type name already exists
                try {
                    RoomType existingRoomType = roomTypeBean.retrieveRoomTypeByName(newRoomTypeName);
                    System.out.println("Error: Room type name '" + newRoomTypeName + "' already exists. Update canceled.");
                } catch (RoomTypeErrorException ex) {
                    rt.setRoomTypeName(newRoomTypeName);
                    roomTypeBean.updateRoomType(rt);
                    System.out.println("Successful update!");
                }
            } else if (response == 2) {
                System.out.print("Enter new Room Type Description> ");
                rt.setDescription(scanner.nextLine().trim());
                roomTypeBean.updateRoomType(rt);
                System.out.println("Successful update!");
            } else if (response == 3) {
                System.out.print("Enter new Bed Description> ");
                rt.setBed(scanner.nextLine().trim());
                roomTypeBean.updateRoomType(rt);
                System.out.println("Successful update!");
            } else if (response == 4) {
                System.out.print("Enter new Room Type Capacity> ");
                rt.setCapacity(scanner.nextInt());
                scanner.nextLine();
                roomTypeBean.updateRoomType(rt);
                System.out.println("Successful update!");
            } else if (response == 5) {
                System.out.print("Enter new Amenities Description> ");
                rt.setAmenities(scanner.nextLine().trim());
                roomTypeBean.updateRoomType(rt);
                System.out.println("Successful update!");
            } else if (response == 6) {
                System.out.print("Enter Next Higher Room Type ID> ");
                Long id = scanner.nextLong();
                try {
                    RoomType nextHigher = roomTypeBean.retrieveRoomTypeById(id);
                    rt.setNextHigherRoomType(nextHigher);
                    roomTypeBean.updateRoomType(rt);
                    System.out.println("Successful update!");
                } catch (RoomTypeErrorException e) {
                    System.out.println("Room type does not exist!");
                }
            } else if (response == 7) {
                break;
            } else {
                System.out.println("Invalid option, please try again!");
            }
        }
    }

    public void doDeleteRoomType(RoomType roomType) {
        scanner.nextLine();
        String res = "";
        System.out.println("*** HoRS Management System :: Room Type Operations :: Delete Room Type ***\n");
        System.out.print("Enter Y to delete room type, N to exit> ");
        res = scanner.nextLine().trim();

        if (res.equalsIgnoreCase("Y")) {
            String resultMessage = roomTypeBean.deleteRoomType(roomType);
            System.out.println(resultMessage); // Display the result to the client
        } else {
            System.out.println("Room type deletion canceled.");
        }
    }

    public void doDeleteRoom() {
        System.out.println("*** HoRS Management System :: Room Operations :: Delete Room ***\n");
        System.out.print("Enter room number> ");
        Integer roomNum = scanner.nextInt();
        try {
            Room room = roomBean.retrieveRoomByNumber(roomNum);
            String res = "";
            scanner.nextLine();
            System.out.print("Enter Y to delete room, N to exit> ");
            res = scanner.nextLine().trim();
            if (res.equalsIgnoreCase("Y")) {
                String resultMessage = roomBean.deleteRoom(room);

                System.out.println(resultMessage); // Display the result to the client
            } else {
                System.out.println("Room deletion canceled.");
            }

        } catch (RoomErrorException ex) {
            System.out.println("Room " + roomNum + " not found!");
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
        try {
            System.out.print("Enter Room Type's Capacity> ");
            type.setCapacity(scanner.nextInt());
        } catch (InputMismatchException ex) {
            System.out.println("Please key in an integer!");
            return;
        }
        scanner.nextLine();
        System.out.print("Enter Room Type's Amenities Description> ");
        type.setAmenities(scanner.nextLine().trim());
        System.out.println("Enter Next Higher Room Type ID (You may retrieve ID details from 'View All Room Types', please key in -1 if no next higher room type)> ");
        Long id = scanner.nextLong();
        if (id == -1) {
            try {
                type = roomTypeBean.createRoomType(type);
                System.out.println("Room Type " + type.getRoomTypeName() + " created successfully!\n");
                return;
            } catch (RoomTypeErrorException e) {
                System.out.println("Error: " + e.getMessage() + "\n");
                return;
            }
        }
        try {
            RoomType nextHigher = roomTypeBean.retrieveRoomTypeById(id);
            type.setNextHigherRoomType(nextHigher);

            try {
                type = roomTypeBean.createRoomType(type);
                System.out.println("Room Type " + type.getRoomTypeName() + " created successfully!\n");
            } catch (RoomTypeErrorException e) {
                System.out.println("Error: " + e.getMessage() + "\n");
            }
        } catch (RoomTypeErrorException e) {
            System.out.println("Room type does not exist!");
            return;
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
            //ex.printStackTrace();
        }
    }

    public void doUpdateRoomRate(RoomRate rate) {
        System.out.println("*** HoRS Management System :: Room Rate Operations :: Update Room Rate ***\n");

        while (true) {
            System.out.println("1: Change Room Rate Name");
            System.out.println("2: Change Room Type");
            System.out.println("3: Change Rate Type");
            System.out.println("4: Change Rate Per Night");
            System.out.println("5: Change Start Date");
            System.out.println("6: Change End Date");
            System.out.println("7: Exit\n");
            System.out.print("> ");

            int response = scanner.nextInt();
            scanner.nextLine();

            if (response == 1) {
                System.out.print("Enter new Room Rate Name> ");
                String newRoomRateName = scanner.nextLine().trim();

                // Check if room rate name already exists
                try {
                    RoomRate existingRate = rateBean.retrieveRoomRateByName(newRoomRateName);
                    System.out.println("Error: Room rate name '" + newRoomRateName + "' already exists. Update canceled.");
                } catch (RoomRateErrorException ex) {
                    rate.setName(newRoomRateName);
                    rateBean.updateRoomRate(rate);
                    System.out.println("Successful update!");
                }
            } else if (response == 2) {
                // Select new room type
                try {
                    List<RoomType> roomTypes = roomTypeBean.retrieveAllRoomTypes();
                    System.out.println("List of Room Types:");
                    for (int i = 0; i < roomTypes.size(); i++) {
                        System.out.println((i + 1) + ": " + roomTypes.get(i).getRoomTypeName());
                    }
                    int choice = scanner.nextInt();
                    scanner.nextLine();
                    RoomType newRoomType = roomTypes.get(choice - 1);
                    rate.setRoomType(newRoomType);
                    rateBean.updateRoomRate(rate);
                    System.out.println("Successful update!");
                } catch (RoomTypeErrorException ex) {
                    System.out.println(ex.getMessage());
                }
            } else if (response == 3) {
                System.out.println("Choose Rate Type:");
                for (RateTypeEnum type : RateTypeEnum.values()) {
                    System.out.println(type.ordinal() + 1 + ": " + type);
                }
                int choice = scanner.nextInt();
                scanner.nextLine();
                RateTypeEnum newRateType = RateTypeEnum.values()[choice - 1];

                // Check if rate type already exists for this room type
                if (roomTypeBean.roomRateExistsForType(rate.getRoomType().getRoomTypeId(), newRateType)) {
                    System.out.println("Error: Rate type '" + newRateType + "' already exists for this room type. Update canceled.");
                } else {
                    rate.setRateType(newRateType);
                    rateBean.updateRoomRate(rate);
                    System.out.println("Successful update!");
                }
            } else if (response == 4) {
                System.out.print("Enter new Rate Per Night> ");
                rate.setRatePerNight(scanner.nextBigDecimal());
                scanner.nextLine();
                rateBean.updateRoomRate(rate);
                System.out.println("Successful update!");
            } else if (response == 5) {
                System.out.print("Enter Start Date (yyyy-MM-dd)> ");
                String startDateStr = scanner.nextLine().trim();
                Date startDate = parseDate(startDateStr, new SimpleDateFormat("yyyy-MM-dd"));
                if (startDate != null) {
                    rate.setStartDate(startDate);
                    rateBean.updateRoomRate(rate);
                    System.out.println("Successful update!");
                }
            } else if (response == 6) {
                System.out.print("Enter End Date (yyyy-MM-dd)> ");
                String endDateStr = scanner.nextLine().trim();
                Date endDate = parseDate(endDateStr, new SimpleDateFormat("yyyy-MM-dd"));
                if (endDate != null) {
                    rate.setEndDate(endDate);
                    rateBean.updateRoomRate(rate);
                    System.out.println("Successful update!");
                }
            } else if (response == 7) {
                break;
            } else {
                System.out.println("Invalid option, please try again!");
            }
        }
    }

    public void doUpdateRoom() {
        System.out.println("*** HoRS Management System :: Room Operations :: Update Room ***\n");
        System.out.print("Enter Room Number> ");

        try {
            Room r = roomBean.retrieveRoomByNumber(scanner.nextInt());
            scanner.nextLine();

            while (true) {
                System.out.println("1: Change Room Number");
                System.out.println("2: Change Room Status");
                System.out.println("3: Change Room Type");
                System.out.println("4: Exit\n");
                System.out.print("> ");

                int response = scanner.nextInt();
                scanner.nextLine();

                if (response == 1) {
                    System.out.print("Enter new Room number> ");
                    Integer newRoomNumber = scanner.nextInt();
                    scanner.nextLine();

                    // Check if room number already exists
                    try {
                        Room existingRoom = roomBean.retrieveRoomByNumber(newRoomNumber);
                        System.out.println("Error: Room number " + newRoomNumber + " already exists. Update canceled.");
                    } catch (RoomErrorException ex) {
                        r.setRoomNumber(newRoomNumber);
                        roomBean.updateRoom(r);
                        System.out.println("Successful update!");
                    }
                } else if (response == 2) {
                    System.out.println("1: Available");
                    System.out.println("2: Occupied");
                    System.out.print("Enter new Room Status> ");
                    int statusOption = scanner.nextInt();
                    scanner.nextLine();

                    if (statusOption == 1) {
                        r.setStatus(RoomStatusEnum.AVAIL);
                    } else if (statusOption == 2) {
                        r.setStatus(RoomStatusEnum.OCCUPIED);
                    } else {
                        System.out.println("Unknown Input!");
                    }
                    roomBean.updateRoom(r);
                    System.out.println("Successful update!");
                } else if (response == 3) {
                    try {
                        List<RoomType> roomTypes = roomTypeBean.retrieveAllRoomTypes();
                        System.out.println("List of Room Types:");
                        for (int i = 0; i < roomTypes.size(); i++) {
                            System.out.println((i + 1) + ": " + roomTypes.get(i).getRoomTypeName());
                        }
                        int choice = scanner.nextInt();
                        scanner.nextLine();
                        RoomType newRoomType = roomTypes.get(choice - 1);
                        r.setRoomType(newRoomType);
                        roomBean.updateRoom(r);
                        System.out.println("Successful update!");
                    } catch (RoomTypeErrorException ex) {
                        System.out.println(ex.getMessage());
                    }
                } else if (response == 4) {
                    break;
                } else {
                    System.out.println("Invalid option, please try again!");
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
                System.out.println("Room Number: " + r.getRoomNumber() + ", Room Type: " + r.getRoomType().getRoomTypeName() + ", Room Status: " + r.getStatus());
                List<RoomAllocation> alloList = r.getRoomAllocation();
                System.out.println("Number of room allocations: " + alloList.size());
                System.out.println("Disabled?: " + (r.getIsDisabled() ? "Yes" : "No"));
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

            String rateName = rt.getRoomTypeName() + " " + rateType;
            r.setName(rateName);

            System.out.print("Enter rate per night> ");
            r.setRatePerNight(new BigDecimal(scanner.nextLine().trim()));

            if (rateType.equals(RateTypeEnum.PEAK) || rateType.equals(RateTypeEnum.PROMOTION)) {
                System.out.print("Enter start date (yyyy-MM-dd)> ");
                r.setStartDate(parseDate(scanner.nextLine().trim(), formatter));
                System.out.print("Enter end date (yyyy-MM-dd)> ");
                r.setEndDate(parseDate(scanner.nextLine().trim(), formatter));
            }

            // Persist the RoomRate if no duplicate rate type exists
            RoomRate newRateId = rateBean.createRoomRate(r);
            if (newRateId.getRoomRateId() != null) {
                RoomRate roomrate = rateBean.retrieveRoomRateById(newRateId.getRoomRateId());
                rt.getRoomrates().add(roomrate);
                roomTypeBean.updateRoomType(rt);
                System.out.println("Room Rate: " + r.getName() + " created successfully!\n");
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

        System.out.printf("%-20s %-20s %-15s %-10s %-10s%n", "Name", "Room Type", "Rate Type", "Rate/Night", "Disabled?");
        System.out.println("-------------------------------------------------------------------------------");

        for (RoomRate rate : rates) {
            System.out.printf("%-20s %-20s %-15s %-10.2f %-10s%n",
                    rate.getName(),
                    rate.getRoomType().getRoomTypeName(),
                    rate.getRateType(),
                    rate.getRatePerNight().doubleValue(),
                    rate.getIsDisabled() ? "Yes" : "No");
        }
    }

    public void doViewRoomRateDetails() {
        System.out.println("*** HoRS Management System :: Room Rate Operations :: View Room Rate Details ***\n");
        System.out.println("Enter room rate name (You may retrieve room rate names in 'View All Room Rates'> ");
        String name = scanner.nextLine().trim();
        try {
            RoomRate rate = rateBean.retrieveRoomRateByName(name);
            System.out.println("Room Rate Name: " + name);
            System.out.println("Room Type: " + rate.getRoomType().getRoomTypeName());
            System.out.println("Rate Type: " + rate.getRateType());
            System.out.println("Rate Per Night: " + rate.getRatePerNight());
            if (rate.getRateType().equals(RateTypeEnum.PEAK) || rate.getRateType().equals(RateTypeEnum.PROMOTION)) {
                System.out.println("Start Date: " + rate.getStartDate());
                System.out.println("End Date: " + rate.getEndDate());

            }
            System.out.println("Disabled?: " + (rate.getIsDisabled() ? "Yes" : "No"));
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
        } catch (RoomRateErrorException ex) {
            System.out.println(ex.getMessage());
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
