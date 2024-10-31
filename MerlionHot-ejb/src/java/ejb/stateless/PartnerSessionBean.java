/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/J2EE/EJB30/StatelessEjbClass.java to edit this template
 */
package ejb.stateless;

import entity.Partner;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author qiuyutong
 */
@Stateless
public class PartnerSessionBean implements PartnerSessionBeanRemote, PartnerSessionBeanLocal {

    @PersistenceContext(unitName = "MerlionHot-ejbPU")
    private EntityManager em;

    @Override
    public Long createPartner(Partner newPartner) {
        em.persist(newPartner);
        em.flush();
        return newPartner.getPartnerId();
        
    }

    @Override
    public List<Partner> retrieveAllPartners() {
        
        List<Partner> li = em.createQuery("Select p FROM Partner p").getResultList();
        return li;
        
    }
    
    @Override
    public Partner retrieveEmployeeById(Long partnerId) {
        
        return null;
        
    }

    @Override
    public Partner retrieveEmployeeByUsername(String partnerUsername) {
        
        return null;
        
    }
    
    
  
}
