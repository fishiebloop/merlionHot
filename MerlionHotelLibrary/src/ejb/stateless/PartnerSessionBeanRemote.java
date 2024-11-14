/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/J2EE/EJB30/SessionRemote.java to edit this template
 */
package ejb.stateless;

import entity.Partner;
import entity.Reservation;
import java.util.List;
import javax.ejb.Remote;
import util.exception.InvalidLoginCredentialException;

/**
 *
 * @author qiuyutong
 */
@Remote
public interface PartnerSessionBeanRemote {
    public Long createPartner(Partner newPartner);
    public List<Partner> retrieveAllPartners();
    public List<Reservation> retrieveReservationsFromPID(Long partnerId) throws InvalidLoginCredentialException;
    public Partner retrieveEmployeeByUsername(String username) throws InvalidLoginCredentialException;
    public Partner retrievePartnerById(Long partnerId);
}
