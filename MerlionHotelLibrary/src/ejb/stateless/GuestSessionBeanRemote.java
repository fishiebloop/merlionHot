/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/J2EE/EJB30/SessionRemote.java to edit this template
 */
package ejb.stateless;

import entity.Guest;
import entity.Reservation;
import java.util.List;
import javax.ejb.Remote;
import util.exception.BeanValidationError;
import util.exception.GuestErrorException;

/**
 *
 * @author eliseoh
 */
@Remote
public interface GuestSessionBeanRemote {

    Guest guestAuth(String email, String password) throws GuestErrorException;
    
    public Boolean validateGuest(Guest g) throws BeanValidationError;

    Guest createGuest(Guest g);

    List<Reservation> retrieveAllReservations(Long guestId) throws GuestErrorException;
}
