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
 * @author qiuyutong

 */
@Remote
public interface GuestSessionBeanRemote {

    public Guest createGuest(Guest g)throws BeanValidationError;
    public List<Reservation> retrieveAllReservations(Long guestId) throws GuestErrorException;
    public Boolean validateGuest(Guest g) throws BeanValidationError;
    public Guest guestAuth(String email, String password) throws GuestErrorException;

    public Guest retrieveGuestByEmail(String guestEmail);

    public void updateGuest(Guest guest);

    public void deleteGuest(Guest guest);

    Guest createGuestWeb(String name, String email) throws BeanValidationError;
}
