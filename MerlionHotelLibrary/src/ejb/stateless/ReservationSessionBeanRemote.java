/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/J2EE/EJB30/SessionRemote.java to edit this template
 */
package ejb.stateless;

import entity.Reservation;
import entity.Guest;
import entity.RoomType;
import java.util.Date;
import javax.ejb.Remote;
import util.exception.CannotUpgradeException;
import util.exception.NoAvailableRoomException;
import util.exception.ReservationErrorException;

/**
 *
 * @author qiuyutong
 */
@Remote
public interface ReservationSessionBeanRemote {

    public void updateReservation(Reservation reservation);

    public Long createReservation(Reservation reservation);

    public Reservation retrieveReservationById(Long id);
  
    Reservation createReservation(Reservation newR, Guest guest, RoomType rt);

    Reservation retrieveReservationByIdForGuest(Long id, Guest g) throws ReservationErrorException;

    //public Reservation createNotSameDayReservation(Reservation newR, Guest guest, RoomType rt) throws NoAvailableRoomException, CannotUpgradeException;

    public Reservation createSameDayReservation(Reservation newR) throws NoAvailableRoomException, CannotUpgradeException;

    Reservation detachReservation(Reservation res);
    
}

