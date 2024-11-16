/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package horsmanagementclient;

import com.merlionhotel.utils.DateUtil;
import ejb.stateless.ExceptionReportSessionBeanRemote;
import ejb.stateless.GuestSessionBeanRemote;
import ejb.stateless.ReservationSessionBeanRemote;
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
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.NoResultException;
import util.enumeration.ExceptionTypeEnum;
import util.enumeration.RateTypeEnum;
import util.enumeration.RoomStatusEnum;
import util.exception.BeanValidationError;
import util.exception.DateValidationError;
import util.exception.NoAvailableRoomException;
import util.exception.RoomAllocationNotFoundException;

/**
 *
 * @author eliseoh
 */
public class FrontOfficeModule {

    private Scanner scanner = new Scanner(System.in);
    SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
    private ReservationSessionBeanRemote reservationBean;
    private RoomSessionBeanRemote roomBean;
    private RoomTypeSessionBeanRemote roomTypeBean;
    private RoomRateSessionBeanRemote rateBean;
    private GuestSessionBeanRemote guestBean;
    private RoomAllocationSessionBeanRemote roomAllocationBean;
    private ExceptionReportSessionBeanRemote exceptionReportBean;

    public FrontOfficeModule() {

    }

    public FrontOfficeModule(ReservationSessionBeanRemote reservationBean, RoomSessionBeanRemote roomBean, RoomTypeSessionBeanRemote roomTypeBean, RoomRateSessionBeanRemote rateBean, RoomAllocationSessionBeanRemote roomAllocationBean, ExceptionReportSessionBeanRemote exceptionReportBean, GuestSessionBeanRemote guestBean) {
        this.reservationBean = reservationBean;
        this.roomBean = roomBean;
        this.roomTypeBean = roomTypeBean;
        this.rateBean = rateBean;
        this.guestBean = guestBean;
        this.roomAllocationBean = roomAllocationBean;
        this.exceptionReportBean = exceptionReportBean;
    }

    public void menu() {
        Integer response = 0;

        while (true) {
            System.out.println("*** Front Office System :: Guest Relations Management ***\n");
            System.out.println("1: Walk-in Search Room");
            System.out.println("2: Check-in guest");
            System.out.println("3: Check-out guest");
            System.out.println("4: Logout\n");
            response = 0;

            while (response < 1 || response > 4) {
                System.out.print("> ");

                response = scanner.nextInt();
                scanner.nextLine();

                if (response == 1) {
                    try {
                        doSearchRoom();
                    } catch (DateValidationError ex) {
                        System.out.println(ex.getMessage());
                    }
                } else if (response == 2) {
                    doCheckin();
                } else if (response == 3) {
                    doCheckout();
                } else if (response == 4) {
                    break;
                }
                else {
                    System.out.println("Invalid option, please try again!\n");
                }
            }

            if (response == 4) {
                break;
            }
        }
    }

    public void doSearchRoom() throws DateValidationError {
        System.out.println("*** HoRS Management System :: Guest Relations :: Search/Reserve Room ***\n");
        Date out;
        Date in;

        while (true) {
            System.out.print("Enter Check-in Date! Please format in dd-MM-yyyy > ");
            String checkIn = scanner.nextLine().trim();
            try {
                in = DateUtil.convertToDate(checkIn);

                // check if the check-in date is before today's date
                LocalDate today = LocalDate.now();
                LocalDate checkInDate = in.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
                if (checkInDate.isBefore(today)) {
                    throw new DateValidationError("Check-in date cannot be before today!");
                }
                break;
            } catch (ParseException ex) {
                throw new DateValidationError("Date formatted wrongly! Try again!");
            }
        }

        while (true) {
            System.out.print("Enter Check-out Date! Please format in dd-MM-yyyy > ");
            String checkOut = scanner.nextLine().trim();
            try {
                out = DateUtil.convertToDate(checkOut);
                break;
            } catch (ParseException ex) {
                throw new DateValidationError("Date formatted wrongly! Try again!");
            }
        }

        long diffInMillies = out.getTime() - in.getTime();
        long numberOfNights = diffInMillies / (1000 * 60 * 60 * 24);
        if (in.equals(out)) {
            numberOfNights = 1;
        }

        if (!out.equals(in) && out.before(in)) {
            throw new DateValidationError("Dates entered wrongly!");
        }

        System.out.println("Number of nights: " + numberOfNights);
        List<RoomType> types = roomTypeBean.retrieveAvailableRoomTypes(in, out);
        if (types.isEmpty()) {
            System.out.println("No available room types.");
            return;
        }

        BigDecimal[] amounts = new BigDecimal[types.size()];

        int i = 1;
        for (RoomType type : types) {
            BigDecimal totalAmount = BigDecimal.ZERO;

            List<RoomRate> rates = type.getRoomrates();
            for (RoomRate rate : rates) {
                if (rate.getRateType().equals(RateTypeEnum.PUBLISHED)) {
                    BigDecimal ratePerNight = rate.getRatePerNight();
                    totalAmount = ratePerNight.multiply(BigDecimal.valueOf(numberOfNights));
                    break;
                }
            }

            amounts[i - 1] = totalAmount;

            System.out.println("Room Type " + i + ": " + type.getRoomTypeName());
            System.out.println("Total amount for " + numberOfNights + " nights: " + totalAmount);
            i++;
        }

        System.out.print("Reserve a room? Type Y to reserve, N to exit> ");
        String res = scanner.nextLine().trim();
        if (res.equalsIgnoreCase("Y")) {
            System.out.print("Enter main guest name> ");
            String name = scanner.nextLine().trim();
            System.out.print("Enter main guest email> ");
            String email = scanner.nextLine().trim();

            System.out.print("Choose room type to reserve (1 to " + types.size() + ")> ");
            int choiceIndex = scanner.nextInt();
            scanner.nextLine();

            if (choiceIndex >= 1 && choiceIndex <= types.size()) {
                RoomType chosenType = types.get(choiceIndex - 1);

                System.out.print("Enter number of rooms you want to reserve> ");
                int requestedRooms = scanner.nextInt();
                scanner.nextLine();

                int availableRooms = roomBean.getAvailableRoomCountForWalkIn(chosenType, in, out);

                if (requestedRooms <= availableRooms) {
                    System.out.println("Sufficient rooms available for reservation.");
                    BigDecimal chosenTotalAmount = amounts[choiceIndex - 1];

                    try {
                        createReservations(requestedRooms, chosenType, in, out, name, email, chosenTotalAmount);
                    } catch (BeanValidationError ex) {
                        System.out.println("Validation failed. Please correct the following errors:\n" + ex.getMessage());
                        System.out.println("Please re-enter your details.");
                    }
                } else {
                    System.out.println("Only " + availableRooms + " rooms are available for the chosen room type.");
                    System.out.print("Do you want to reserve the available rooms instead? Type Y for yes, N for no> ");
                    String partialRes = scanner.nextLine().trim();

                    if (partialRes.equalsIgnoreCase("Y")) {
                        BigDecimal chosenTotalAmount = amounts[choiceIndex - 1];

                        try {
                            createReservations(availableRooms, chosenType, in, out, name, email, chosenTotalAmount);
                        } catch (BeanValidationError ex) {
                            System.out.println("Validation failed. Please correct the following errors:\n" + ex.getMessage());
                            System.out.println("Please re-enter your details.");
                        }
                    } else {
                        System.out.println("Reservation canceled.");
                    }
                }
            } else {
                System.out.println("Invalid room type choice.");
            }
        } else {
            System.out.println("Reservation process exited.");
        }
    }

    private void createReservations(int numRooms, RoomType roomType, Date startDate, Date endDate, String guestName, String guestEmail, BigDecimal chosenTotalAmount) throws BeanValidationError {
        Guest guest = guestBean.retrieveGuestByEmail(guestEmail);
        boolean isNewGuest = false;

        if (guest == null) {
            guest = new Guest();
            guest.setName(guestName);
            guest.setEmail(guestEmail);
            guest = guestBean.createGuest(guest);
            isNewGuest = true;
            System.out.println("New guest created with email: " + guestEmail);
        } else {
            System.out.println("Existing guest found with email: " + guestEmail);
        }

        LocalDate startLocalDate = startDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        BigDecimal totalReservationFee = chosenTotalAmount.multiply(BigDecimal.valueOf(numRooms));

        try {
            for (int i = 0; i < numRooms; i++) {
                Reservation reservation = new Reservation();
                reservation.setCheckInDate(startDate);
                reservation.setCheckOutDate(endDate);
                reservation.setRoomType(roomType);
                reservation.setGuest(guest);
                reservation.setIsWalkIn(true);

                Long reserveID = reservationBean.createReservation(reservation);
                Reservation managedReservation = reservationBean.retrieveReservationById(reserveID);
                guest.getReservation().add(managedReservation);
                roomTypeBean.updateRoomType(roomType);
                guestBean.updateGuest(guest);
                System.out.println();
                System.out.println("Reserved " + roomType.getRoomTypeName() + " for " + guestName + " with reservation ID " + reserveID);
                System.out.println();

                LocalTime currentTime = LocalTime.now();
                if (startLocalDate.equals(LocalDate.now()) && currentTime.isAfter(LocalTime.of(2, 0))) {
                    System.out.println("Performing same-day check-in allocation...");
                    try {
                        Long allocationID = roomAllocationBean.createAllocation(managedReservation);
                        RoomAllocation allocation = roomAllocationBean.retrieveAllocationById(allocationID);
                        Room allocatedRoom = allocation.getRoom();
                        allocatedRoom.setStatus(RoomStatusEnum.OCCUPIED);
                        roomBean.updateRoom(allocatedRoom);

                        System.out.println("Allocated guest to room number " + allocatedRoom.getRoomNumber() + " of room type " + allocatedRoom.getRoomType().getRoomTypeName());
                        System.out.println();
                    } catch (NoAvailableRoomException ex) {
                        roomAllocationBean.createRoomAllocationException(reservation, ex);
                        if (ex.isUpgradeAvailable()) {
                            RoomAllocation upgradedAllocation = roomAllocationBean.retrieveAllocationByReservation(managedReservation);
                            Room upgradedRoom = upgradedAllocation.getRoom();
                            upgradedRoom.setStatus(RoomStatusEnum.OCCUPIED);
                            roomBean.updateRoom(upgradedRoom);

                            System.out.println("Room type unavailable. Guest was upgraded to room number " + upgradedRoom.getRoomNumber()
                                    + " of room type " + upgradedRoom.getRoomType().getRoomTypeName());
                        } else {
                            System.out.println("Room type unavailable and no upgrade available. No room could be allocated.");
                        }
                    } catch (RoomAllocationNotFoundException ex) {
                        System.out.println("Unexpected error: could not retrieve allocation.");
                    }
                }
            }
            System.out.println("Total reservation fee for " + numRooms + " rooms: " + totalReservationFee);
        } catch (Exception e) {
            System.out.println("Reservation process failed: " + e.getMessage());
            if (isNewGuest && guest.getReservation().isEmpty()) {
                guestBean.deleteGuest(guest);
                System.out.println("New guest with no reservations has been deleted.");
            }
        }
    }

    public void doCheckin() {
        System.out.println("*** HoRS Management System :: Guest Relations :: Check In ***\n");
        System.out.print("Enter guest email> ");
        String email = scanner.nextLine().trim();
        Guest g = guestBean.retrieveGuestByEmail(email);

        if (g == null) {
            System.out.println("No guest found with the email: " + email);
            return;  
        }

        List<Reservation> reservations = g.getReservation();
        if (reservations.isEmpty()) {
            System.out.println("No reservations found for the guest with email: " + email);
            return; 
        }

        for (Reservation r : reservations) {
            LocalDate checkinDate = r.getCheckInDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
            LocalTime currentTime = LocalTime.now();

            if (checkinDate.equals(LocalDate.now())) {
                try {
                    RoomAllocation allocation = roomAllocationBean.retrieveAllocationByReservation(r);
                    Room allocatedRoom = allocation.getRoom();

                    if (currentTime.isBefore(LocalTime.of(14, 0))) {
                        // Early check-in: only allow if room status is available
                        if (allocatedRoom.getStatus() == RoomStatusEnum.AVAIL) {
                            allocatedRoom.setStatus(RoomStatusEnum.OCCUPIED);
                            roomBean.updateRoom(allocatedRoom);
                            System.out.println("Early check-in successful. Allocated to room number " + allocatedRoom.getRoomNumber() + " of room type " + allocatedRoom.getRoomType().getRoomTypeName());
                        } else {
                            System.out.println("Early check-in not possible. Room is not available at this time.");
                        }
                    } else {
                        // Regular check-in: mark room as occupied
                        allocatedRoom.setStatus(RoomStatusEnum.OCCUPIED);
                        roomBean.updateRoom(allocatedRoom);
                        System.out.println("Checked in to room number " + allocatedRoom.getRoomNumber() + " of room type " + allocatedRoom.getRoomType().getRoomTypeName());
                    }
                } catch (RoomAllocationNotFoundException ex) {
                    System.out.println("Room allocation not found for reservation ID: " + r.getReservationId());

                    ExceptionReport exceptionReport = exceptionReportBean.retrieveExceptionByReservation(r);
                    if (exceptionReport != null && exceptionReport.getExceptionType() == ExceptionTypeEnum.NOHIGHERAVAIL) {
                        System.out.println("Unfortunately, no room was available for your reservation.");
                    }
                }
            } else {
                System.out.println("Check-in date is not today for reservation ID: " + r.getReservationId() + ". Cannot check in.");
            }
        }
    }

    public void doCheckout() {
        System.out.println("*** HoRS Management System :: Guest Relations :: Check Out ***\n");
        System.out.print("Enter guest email> ");
        String email = scanner.nextLine().trim();
        Guest g = guestBean.retrieveGuestByEmail(email);

        if (g == null) {
            System.out.println("No guest found with the email: " + email);
            return;  // Exit if no guest is found
        }

        List<Reservation> reservations = g.getReservation();
        if (reservations.isEmpty()) {
            System.out.println("No reservations found for the guest with email: " + email);
            return;  // Exit if there are no reservations
        }

        for (Reservation r : reservations) {
            try {
                RoomAllocation allocation = roomAllocationBean.retrieveAllocationByReservation(r);
                Room allocatedRoom = allocation.getRoom();
                String roomInfo = "Reservation ID: " + r.getReservationId() + ", Room Number: " + allocatedRoom.getRoomNumber();

                // check if the room is currently occupied
                if (allocatedRoom.getStatus() != RoomStatusEnum.OCCUPIED) {
                    continue;  // skip to the next reservation if the room is not occupied
                }

                System.out.print("Do you want to check out for " + roomInfo + "? (Enter 'Y' for Yes) > ");
                String ans = scanner.nextLine().trim();
                if (!ans.equalsIgnoreCase("Y")) {
                    System.out.println("Skipping check-out for " + roomInfo);
                    continue;  // skip to the next reservation if they don't want to check out
                }

                LocalDate checkoutDate = r.getCheckOutDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
                LocalTime currentTime = LocalTime.now();

                if (checkoutDate.equals(LocalDate.now())) {
                    if (currentTime.isAfter(LocalTime.of(12, 0))) {
                        // late check-out: only allow if no new reservation for this room today
                        boolean isRoomAvailableLater = roomBean.isRoomAvailable(allocatedRoom, LocalDate.now(), r.getReservationId());
                        if (isRoomAvailableLater) {
                            allocatedRoom.setStatus(RoomStatusEnum.AVAIL);
                            roomBean.updateRoom(allocatedRoom);
                            System.out.println("Late check-out successful. Room number " + allocatedRoom.getRoomNumber() + " is now available.");
                        } else {
                            System.out.println("Late check-out not possible. Room is already reserved for another guest today.");
                        }
                    } else {
                        // normal check-out
                        allocatedRoom.setStatus(RoomStatusEnum.AVAIL);
                        roomBean.updateRoom(allocatedRoom);
                        System.out.println("Checked out. Room number " + allocatedRoom.getRoomNumber() + " is now available.");
                    }
                } else {
                    System.out.println("Check-out date is not today for Reservation ID: " + r.getReservationId() + ". Cannot check out.");
                }
            } catch (RoomAllocationNotFoundException ex) {
                System.out.println("Room allocation not found for reservation ID: " + r.getReservationId());
            } catch (Exception ex) {
                System.out.println("An error occurred: " + ex.getMessage());
            }
        }
    }

    private Date parseDate(String dateStr, SimpleDateFormat formatter1) {
        try {
            DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
            return formatter.parse(dateStr);
        } catch (ParseException e) {
            System.out.println("Invalid date format. Please use yyyy-MM-dd.");
            return null;
        }
    }
}
