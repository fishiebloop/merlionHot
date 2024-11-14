/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/WebServices/EjbWebService.java to edit this template
 */
package ejb.session.ws;

import ejb.stateless.GuestSessionBeanLocal;
import ejb.stateless.PartnerSessionBeanLocal;
import ejb.stateless.ReservationSessionBeanLocal;
import ejb.stateless.RoomAllocationSessionBeanLocal;
import ejb.stateless.RoomRateSessionBeanLocal;
import ejb.stateless.RoomSessionBeanLocal;
import ejb.stateless.RoomTypeSessionBeanLocal;
import entity.Guest;
import entity.Partner;
import entity.Reservation;
import entity.Room;
import entity.RoomAllocation;
import entity.RoomRate;
import entity.RoomType;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.ejb.EJB;
import javax.jws.WebService;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.xml.datatype.XMLGregorianCalendar;
import javax.xml.ws.RequestWrapper;
import javax.xml.ws.ResponseWrapper;
import util.exception.BeanValidationError;
import util.exception.CannotUpgradeException;
import util.exception.DateValidationError;
import util.exception.InvalidLoginCredentialException;
import util.exception.NoAvailableRoomException;
import util.exception.ReservationErrorException;
import util.exception.RoomAllocationNotFoundException;
import util.exception.RoomTypeErrorException;

/**
 *
 * @author eliseoh
 */
@WebService(serviceName = "HolidayWebService")
@Stateless()
public class HolidayWebService {

    @EJB
    private RoomAllocationSessionBeanLocal roomAllocationSessionBean;

    @EJB
    private RoomSessionBeanLocal roomSessionBean;

    @EJB
    private RoomRateSessionBeanLocal roomRateSessionBean;

    @EJB
    private ReservationSessionBeanLocal reservationSessionBean;

    @EJB
    private PartnerSessionBeanLocal partnerSessionBean;

    @EJB
    private GuestSessionBeanLocal guestSessionBean;

    @PersistenceContext(unitName = "MerlionHot-ejbPU")
    private EntityManager em;

    @EJB
    private RoomTypeSessionBeanLocal roomTypeSessionBean;

    @WebMethod(operationName = "partnerLogin")
    public Long partnerLogin(@WebParam(name = "username") String username,
                               @WebParam(name = "password") String password) throws InvalidLoginCredentialException {
        return partnerSessionBean.partnerLogin(username, password);
    }

    @WebMethod(operationName = "retrieveAllAvailRoomTypeOnline")
    public List<RoomType> retrieveAllAvailRoomTypeOnline(@WebParam(name = "in") XMLGregorianCalendar in, @WebParam(name = "out") XMLGregorianCalendar out) throws RoomTypeErrorException, DateValidationError {
        Date inDate = in.toGregorianCalendar().getTime();
        Date outDate = out.toGregorianCalendar().getTime();
        if (!outDate.equals(inDate) && outDate.before(inDate)) {
            throw new DateValidationError("Dates entered wrongly!");
        }
        List<RoomType> types = roomTypeSessionBean.retrieveAllAvailRoomTypeOnline(inDate, outDate);
        for (RoomType rt: types) {
            em.detach(rt);
            rt.setRooms(null);
            rt.setReservations(null);
            rt.setRoomrates(null);
        }
        return types;
    }

    @WebMethod(operationName = "getAvailableRoomCountByTypeAndDate")
    public Integer getAvailableRoomCountByTypeAndDate(@WebParam(name = "roomTypeID") Long roomTypeID, @WebParam(name = "in")XMLGregorianCalendar in, @WebParam(name = "out") XMLGregorianCalendar out) throws RoomTypeErrorException {
        Date inDate = in.toGregorianCalendar().getTime();
        Date outDate = out.toGregorianCalendar().getTime();
        RoomType type = roomTypeSessionBean.retrieveRoomTypeById(roomTypeID);
        Integer num = roomSessionBean.getAvailableRoomCountForOnline(type, inDate, outDate);
        return num;
    }

    @WebMethod(operationName = "createReservation")
    public Reservation createReservation(@WebParam(name = "roomTypeID") Long roomTypeID, @WebParam(name = "in") XMLGregorianCalendar in, @WebParam(name = "out") XMLGregorianCalendar out, @WebParam(name = "guestEmail") String email, @WebParam(name = "parterId") Long partnerId) throws RoomTypeErrorException, InvalidLoginCredentialException {
        Date inDate = in.toGregorianCalendar().getTime();
        Date outDate = out.toGregorianCalendar().getTime();
        RoomType type = roomTypeSessionBean.retrieveRoomTypeById(roomTypeID);
        Guest guest = guestSessionBean.retrieveGuestByEmail(email);
        Partner partner = partnerSessionBean.retrievePartnerById(partnerId);
        Reservation r = reservationSessionBean.createReservation(type, guest, partner, inDate, outDate);
        em.detach(r);
        r.setGuest(null);
        r.setPartner(null);
        r.setRoomAllocation(null);
        r.setRoomType(null);
        return r;
    }

    @WebMethod(operationName = "createSameDayReservation")
    public Reservation createSameDayReservation(@WebParam(name = "roomTypeID") Long roomTypeID, @WebParam(name = "in") XMLGregorianCalendar in, @WebParam(name = "out") XMLGregorianCalendar out, @WebParam(name = "guestEmail") String email, @WebParam(name = "partnerUser") Long partnerId) throws RoomTypeErrorException, InvalidLoginCredentialException, NoAvailableRoomException, CannotUpgradeException {
        Date inDate = in.toGregorianCalendar().getTime();
        Date outDate = out.toGregorianCalendar().getTime();

        RoomType type = roomTypeSessionBean.retrieveRoomTypeById(roomTypeID);
        if (type == null) {
            throw new RoomTypeErrorException("Room type not found for ID: " + roomTypeID);
        }
        
        Guest guest = guestSessionBean.retrieveGuestByEmail(email);
        if (guest == null) {
            throw new InvalidLoginCredentialException("Guest not found for email: " + email);
        }
        
        Partner partner = partnerSessionBean.retrievePartnerById(partnerId);
        if (partner == null) {
            throw new InvalidLoginCredentialException("Partner not found for ID: " + partnerId);
        }

        Reservation r = reservationSessionBean.createSameDayReservation(type, guest, partner, inDate, outDate);
        if (r == null) {
            throw new NoAvailableRoomException("Failed to create reservation, no rooms available.");
        }
        em.detach(r);
        r.setGuest(null);
        r.setPartner(null);
        r.setRoomAllocation(null);
        r.setRoomType(null);
        return r;
    }

    @WebMethod(operationName = "getPriceOfRoomTypeOnline")
    public BigDecimal getPriceOfRoomTypeOnline(@WebParam(name = "in") XMLGregorianCalendar in, @WebParam(name = "out") XMLGregorianCalendar out, @WebParam(name = "roomTypeID") Long roomTypeID) throws RoomTypeErrorException {
        Date inDate = in.toGregorianCalendar().getTime();
        Date outDate = out.toGregorianCalendar().getTime();
        RoomType type = roomTypeSessionBean.retrieveRoomTypeById(roomTypeID);
        BigDecimal price = roomTypeSessionBean.getPriceOfRoomTypeOnline(inDate, outDate, type);
        return price;
    }

    @WebMethod(operationName = "retrieveReservationDetails")
    public ReservationDTO retrieveReservationDetails(@WebParam(name = "reservationID") Long reservationID) throws ReservationErrorException{
        Reservation r = reservationSessionBean.retrieveReservationById(reservationID);
        r.getRoomType();
        if (r == null) {
            throw new ReservationErrorException("Reservation not found for ID: " + reservationID);
        }

        // Check if RoomType is available
        if (r.getRoomType() == null) {
            throw new ReservationErrorException("RoomType is not available for this reservation ID: " + reservationID);
        }

        // Check if Guest is available
        if (r.getGuest() == null) {
            throw new ReservationErrorException("Guest details are missing for this reservation ID: " + reservationID);
        }

        // Check if CheckInDate and CheckOutDate are set
        if (r.getCheckInDate() == null || r.getCheckOutDate() == null) {
            throw new ReservationErrorException("Check-in or check-out date is missing for this reservation ID: " + reservationID);
        }

        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        String checkIn = dateFormat.format(r.getCheckInDate());
        String checkOut = dateFormat.format(r.getCheckOutDate());
        ReservationDTO dto = new ReservationDTO(
            r.getReservationId(),
            r.getRoomType().getRoomTypeName(),
            r.getGuest().getName(),
            r.getGuest().getEmail(),
            checkIn, checkOut
        );

        return dto;
    }

    @WebMethod(operationName = "retrieveAllPartnerReservation")
    public List<ReservationDTO> retrieveAllPartnerReservation(@WebParam(name = "partnerId") Long partnerId) throws InvalidLoginCredentialException, ReservationErrorException {
        List<Reservation> reservations = partnerSessionBean.retrieveReservationsFromPID(partnerId);
        List<ReservationDTO> reservationDTOs = new ArrayList<>();
        for (Reservation r : reservations) {
            ReservationDTO dto = retrieveReservationDetails(r.getReservationId());
            reservationDTOs.add(dto);
        }
        return reservationDTOs;
    }
    
    @WebMethod(operationName = "createGuest")
    public Guest createGuest(@WebParam(name = "guestName") String guestName, @WebParam(name = "guestEmail") String guestEmail) throws BeanValidationError {
        Guest g = guestSessionBean.createGuestWeb(guestName, guestEmail);
        return g;
    }
    
    @WebMethod(operationName = "guestExist")
    public Boolean guestExist(@WebParam(name = "guestEmail") String guestEmail) {
        Guest g = guestSessionBean.retrieveGuestByEmail(guestEmail);
        if (g == null) {
            return false;
        }
        return true;
    }
    
    @WebMethod(operationName = "retrieveAllocationRoom")
    public Room retrieveAllocationRoom(@WebParam(name = "reservationId") Long reservationId) throws ReservationErrorException, RoomAllocationNotFoundException {
        Reservation re = reservationSessionBean.retrieveReservationById(reservationId);
        RoomAllocation ra = roomAllocationSessionBean.retrieveAllocationByReservation(re);
        Room r = ra.getRoom();
        if (r == null) {
            throw new RoomAllocationNotFoundException("Room allocation not found for the given reservation ID.");
        }
        em.detach(r);
        for (RoomAllocation ra1 : r.getRoomAllocation()) {
            em.detach(ra1); // Detach the RoomAllocation entity
            ra1.setRoom(null); // Break the association to avoid cyclic reference
            ra1.setReserveId(null);
        }
        em.detach(r.getRoomType());
        r.setRoomType(null);  // Break association with RoomType
        return r;
    }
}
