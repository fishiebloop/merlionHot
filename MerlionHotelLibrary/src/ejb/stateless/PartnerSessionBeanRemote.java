/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/J2EE/EJB30/SessionRemote.java to edit this template
 */
package ejb.stateless;

import entity.Partner;
import java.util.List;
import javax.ejb.Remote;

/**
 *
 * @author qiuyutong
 */
@Remote
public interface PartnerSessionBeanRemote {
    public Long createPartner(Partner newPartner);
    public List<Partner> retrieveAllPartners();
}
