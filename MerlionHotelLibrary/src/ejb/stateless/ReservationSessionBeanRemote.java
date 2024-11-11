/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/J2EE/EJB30/SessionRemote.java to edit this template
 */
package ejb.stateless;

import entity.Reservation;
import javax.ejb.Remote;

/**
 *
 * @author qiuyutong
 */
@Remote
public interface ReservationSessionBeanRemote {

    public void updateReservation(Reservation reservation);

    public Long createReservation(Reservation reservation);

    public Reservation retrieveReservationById(Long id);
    
}

