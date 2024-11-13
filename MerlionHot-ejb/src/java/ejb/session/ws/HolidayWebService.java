/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/WebServices/EjbWebService.java to edit this template
 */
package ejb.session.ws;

import ejb.stateless.GuestSessionBeanLocal;
import ejb.stateless.PartnerSessionBeanLocal;
import ejb.stateless.ReservationSessionBeanLocal;
import ejb.stateless.RoomRateSessionBeanLocal;
import ejb.stateless.RoomSessionBeanLocal;
import ejb.stateless.RoomTypeSessionBeanLocal;
import entity.Guest;
import entity.Partner;
import entity.Reservation;
import entity.Room;
import entity.RoomRate;
import entity.RoomType;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import javax.ejb.EJB;
import javax.jws.WebService;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.xml.ws.RequestWrapper;
import javax.xml.ws.ResponseWrapper;
import util.exception.CannotUpgradeException;
import util.exception.DateValidationError;
import util.exception.InvalidLoginCredentialException;
import util.exception.NoAvailableRoomException;
import util.exception.ReservationErrorException;
import util.exception.RoomTypeErrorException;

/**
 *
 * @author eliseoh
 */
@WebService(serviceName = "HolidayWebService")
@Stateless()
public class HolidayWebService {

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
    public List<RoomType> retrieveAllAvailRoomTypeOnline(@WebParam(name = "in") Date in, @WebParam(name = "out") Date out) throws RoomTypeErrorException, DateValidationError {
        if (!out.equals(in) && out.before(in)) {
            throw new DateValidationError("Dates entered wrongly!");
        }
        List<RoomType> types = roomTypeSessionBean.retrieveAllAvailRoomTypeOnline(in, out);
        for (RoomType type : types) {
            em.detach(type);
            for (Reservation reservation : type.getReservations()) {
                em.detach(reservation);
                reservation.setRoomType(null);
            }
            for (RoomRate rate : type.getRoomrates()) {
                em.detach(rate);
                rate.setRoomType(null);
            }
            for (Room room : type.getRooms()) {
                em.detach(room);
                room.setRoomType(null);
            }
        }
        return types;
    }

    @WebMethod(operationName = "getAvailableRoomCountByTypeAndDate")
    public Integer getAvailableRoomCountByTypeAndDate(@WebParam(name = "roomTypeID") Long roomTypeID, @WebParam(name = "in") Date in, @WebParam(name = "out") Date out) throws RoomTypeErrorException {
        RoomType type = roomTypeSessionBean.retrieveRoomTypeById(roomTypeID);
        Integer num = roomSessionBean.getAvailableRoomCountByTypeAndDate(type, in, out);
        return num;
    }

    @WebMethod(operationName = "createReservation")
    public Reservation createReservation(@WebParam(name = "roomTypeID") Long roomTypeID, @WebParam(name = "in") Date in, @WebParam(name = "out") Date out, @WebParam(name = "guestNo") Integer guestNo, @WebParam(name = "guestEmail") String email, @WebParam(name = "partnerUser") String username) throws RoomTypeErrorException, InvalidLoginCredentialException {
        RoomType type = roomTypeSessionBean.retrieveRoomTypeById(roomTypeID);
        Guest guest = guestSessionBean.retrieveGuestByEmail(email);
        Partner partner = partnerSessionBean.retrieveEmployeeByUsername(username);
        Reservation r = reservationSessionBean.createReservation(type, guest, partner, in, out, guestNo);
        em.detach(r);
        r.setGuest(null);
        r.setPartner(null);
        r.setRoomAllocation(null);
        r.setRoomType(null);
        return r;
    }

    @WebMethod(operationName = "createSameDayReservation")
    public Reservation createSameDayReservation(@WebParam(name = "roomTypeID") Long roomTypeID, @WebParam(name = "in") Date in, @WebParam(name = "out") Date out, @WebParam(name = "guestNo") Integer guestNo, @WebParam(name = "guestEmail") String email, @WebParam(name = "partnerUser") String username) throws RoomTypeErrorException, InvalidLoginCredentialException, NoAvailableRoomException, CannotUpgradeException {
        RoomType type = roomTypeSessionBean.retrieveRoomTypeById(roomTypeID);
        Guest guest = guestSessionBean.retrieveGuestByEmail(email);
        Partner partner = partnerSessionBean.retrieveEmployeeByUsername(username);
        Reservation r = reservationSessionBean.createSameDayReservation(type, guest, partner, in, out, guestNo);
        em.detach(r);
        r.setGuest(null);
        r.setPartner(null);
        r.setRoomAllocation(null);
        r.setRoomType(null);
        return r;
    }

    @WebMethod(operationName = "getPriceOfRoomTypeOnline")
    public BigDecimal getPriceOfRoomTypeOnline(@WebParam(name = "in") Date in, @WebParam(name = "out") Date out, @WebParam(name = "roomTypeID") Long roomTypeID) throws RoomTypeErrorException {
        RoomType type = roomTypeSessionBean.retrieveRoomTypeById(roomTypeID);
        BigDecimal price = roomTypeSessionBean.getPriceOfRoomTypeOnline(in, out, type);
        return price;
    }

    @WebMethod(operationName = "retrieveReservationDetails")
    public Reservation retrieveReservationDetails(@WebParam(name = "reservationID") Long reservationID) throws ReservationErrorException{
        Reservation r = reservationSessionBean.retrieveReservationById(reservationID);
        em.detach(r);
        r.setGuest(null);
        r.setPartner(null);
        r.setRoomAllocation(null);
        r.setRoomType(null);
        return r;
    }

    @WebMethod(operationName = "retrieveAllPartnerReservation")
    public List<Reservation> retrieveAllPartnerReservation(@WebParam(name = "username") String username) throws InvalidLoginCredentialException {
        Partner p = partnerSessionBean.retrieveEmployeeByUsername(username);
        List<Reservation> reservations = p.getReservations();
        for (Reservation r : reservations) {
            em.detach(r);
            r.setGuest(null);
            r.setPartner(null);
            r.setRoomAllocation(null);
            r.setRoomType(null);
        }
        return reservations;
    }
}
