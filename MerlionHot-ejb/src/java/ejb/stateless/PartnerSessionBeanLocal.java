/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/J2EE/EJB30/SessionLocal.java to edit this template
 */
package ejb.stateless;

import entity.Partner;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author qiuyutong
 */
@Local
public interface PartnerSessionBeanLocal {

    public List<Partner> retrieveAllPartners();

    public Partner retrieveEmployeeById(Long partnerId);

    public Partner retrieveEmployeeByUsername(String partnerUsername);

    public Long createPartner(Partner newPartner);
    
}
