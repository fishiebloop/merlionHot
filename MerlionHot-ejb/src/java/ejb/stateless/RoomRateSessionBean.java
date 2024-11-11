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
import util.enumeration.RoomStatusEnum;

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
    public String deleteRoomRate(RoomRate rate) {
        // Retrieve the managed RoomRate instance
        RoomRate managedRate = em.find(RoomRate.class, rate.getRoomRateId());

        if (managedRate == null) {
            return "Room rate not found";
        }

        // Check for active reservations using the room rate's room type
        Query query = em.createQuery("SELECT COUNT(r) FROM Reservation r "
                + "WHERE r.roomType = :roomType "
                + "AND (r.checkOutDate > CURRENT_DATE "
                + "OR (r.checkOutDate = CURRENT_DATE AND r.roomAllocation.room.status = :occupiedStatus))");
        query.setParameter("roomType", managedRate.getRoomType());
        query.setParameter("occupiedStatus", RoomStatusEnum.OCCUPIED); // Assuming RoomStatusEnum.OCCUPIED exists
        Long count = (Long) query.getSingleResult();

        if (count == 0) {
            // No active reservations; delete the room rate
            em.remove(managedRate);
            em.flush();
            return "Room rate deleted successfully.";
        } else {
            // Mark room rate as disabled if it is in use
            managedRate.setIsDisabled(true);
            em.merge(managedRate);
            em.flush();
            return "Room rate is in use and has been disabled.";
        }
    }
}
