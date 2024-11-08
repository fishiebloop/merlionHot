/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/J2EE/EJB30/StatelessEjbClass.java to edit this template
 */
package ejb.stateless;

import entity.RoomRate;
import entity.RoomType;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 *
 * @author qiuyutong
 */
@Stateless
public class RoomRateSessionBean implements RoomRateSessionBeanRemote, RoomRateSessionBeanLocal {

    @PersistenceContext(unitName = "MerlionHot-ejbPU")
    private EntityManager em;

    @Override
    public Long createRoomRate(RoomRate newRoomRate) {
        em.persist(newRoomRate);
        em.flush();
        return newRoomRate.getRoomRateId();
    }

    @Override
    public List<RoomRate> retrieveAllRoomRates() {
        Query query = em.createQuery("SELECT rr from RoomRate rr");
        return query.getResultList();
    }

    @Override
    public RoomRate retrieveRoomRateById(Long roomRateId) {
        Query query = em.createQuery("SELECT rr from RoomRate rr WHERE rr.roomRateId = :inID");
        query.setParameter("inID", roomRateId);
        return (RoomRate) query.getSingleResult();

    }

    @Override
    public RoomRate retrieveRoomRateByName(String roomRateName) {
        Query query = em.createQuery("SELECT rr from RoomRate rr WHERE rr.name = :inName");
        query.setParameter("inName", roomRateName);
        return (RoomRate) query.getSingleResult();
    }

    @Override
    public void updateRoomRate(RoomRate roomRate) {
        em.merge(roomRate);
    }

    @Override
    public void deleteRoomRate(RoomRate roomRate) {

    }
}
