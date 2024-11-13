/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/J2EE/EJB30/SessionLocal.java to edit this template
 */
package ejb.stateless;


import javax.ejb.Local;
import entity.Guest;
import entity.Reservation;
import java.util.List;
import javax.ejb.Local;
import util.exception.BeanValidationError;
import util.exception.GuestErrorException;

/**
 *
 * @author eliseoh
 */
@Local
public interface GuestSessionBeanLocal {

    Guest guestAuth(String email, String password) throws GuestErrorException;
    
    public Boolean validateGuest(Guest g) throws BeanValidationError;

    Guest createGuest(Guest g)throws BeanValidationError;

    List<Reservation> retrieveAllReservations(Long guestId) throws GuestErrorException;

    public Guest retrieveGuestById(Long id);
    
    public Guest retrieveGuestByEmail(String guestEmail);
}
