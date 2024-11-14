/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/J2EE/EJB30/SessionLocal.java to edit this template
 */
package ejb.stateless;

import entity.Partner;
import entity.Reservation;
import java.util.List;
import javax.ejb.Local;
import util.exception.InvalidLoginCredentialException;

/**
 *
 * @author qiuyutong
 */
@Local
public interface PartnerSessionBeanLocal {

    public List<Partner> retrieveAllPartners();

    public List<Reservation> retrieveReservationsFromPID(Long partnerId) throws InvalidLoginCredentialException;

    public Partner retrieveEmployeeByUsername(String username) throws InvalidLoginCredentialException;

    public Long createPartner(Partner newPartner);

    Long partnerLogin(String username, String password) throws InvalidLoginCredentialException;
    public Partner retrievePartnerById(Long partnerId);
    
}
