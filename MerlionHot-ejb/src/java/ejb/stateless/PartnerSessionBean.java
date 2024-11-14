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
import javax.persistence.Query;
import util.exception.InvalidLoginCredentialException;

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
    public Partner retrieveEmployeeByUsername(String username) throws InvalidLoginCredentialException{
        try {
            Partner p = (Partner) em.createQuery("SELECT p FROM Partner p WHERE p.username = :name").setParameter("name", username).getSingleResult();
            return p;
        } catch (Exception ex) {
            throw new InvalidLoginCredentialException("Cannot find user!");
        }
        
    }

    @Override
    public Long partnerLogin(String username, String password) throws InvalidLoginCredentialException {
        Partner p = retrieveEmployeeByUsername(username);
        if (!p.getPassword().equals(password)) {
            throw new InvalidLoginCredentialException("Invalid password!");
        }
        return p.getPartnerId();
    }

}
