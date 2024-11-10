/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/J2EE/EJB30/SessionRemote.java to edit this template
 */
package ejb.stateless;

import entity.Guest;
import entity.Reservation;
import entity.RoomType;
import javax.ejb.Remote;
import util.exception.ReservationErrorException;

/**
 *
 * @author qiuyutong
 */
@Remote
public interface ReservationSessionBeanRemote {

    Reservation createReservation(Reservation newR, Guest guest, RoomType rt);

    Reservation retrieveReservationByIdForGuest(Long id, Guest g) throws ReservationErrorException;
    
}

