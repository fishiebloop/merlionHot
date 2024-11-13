/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/J2EE/EJB30/SessionLocal.java to edit this template
 */
package ejb.stateless;

import entity.Guest;
import entity.Partner;
import entity.Reservation;
import entity.RoomType;
import java.util.Date;
import javax.ejb.Local;
import util.exception.CannotUpgradeException;
import util.exception.NoAvailableRoomException;
import util.exception.ReservationErrorException;

/**
 *
 * @author qiuyutong
 */
@Local
public interface ReservationSessionBeanLocal {

    // Reservation createReservation(Reservation newR, Guest guest, RoomType rt);

    Reservation retrieveReservationByIdForGuest(Long id, Guest g) throws ReservationErrorException;

    //public Reservation createNotSameDayReservation(Reservation newR, Guest guest, RoomType rt) throws NoAvailableRoomException, CannotUpgradeException;
    // public Reservation createSameDayReservation(Reservation newR) throws NoAvailableRoomException, CannotUpgradeException;

    public Reservation createSameDayReservation(Reservation newR) throws NoAvailableRoomException, CannotUpgradeException;
    Reservation detachReservation(Reservation res);
    
     public Reservation retrieveReservationById(Long id) throws ReservationErrorException;

    public Reservation createReservation(RoomType type, Guest guest, Partner partner, Date in, Date out, Integer guestNo);

    public Reservation createSameDayReservation(RoomType type, Guest guest, Partner partner, Date in, Date out, Integer guestNo) throws NoAvailableRoomException, CannotUpgradeException;

}
