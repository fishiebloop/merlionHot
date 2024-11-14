/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/J2EE/EJB31/SingletonEjbClass.java to edit this template
 */
package ejb.singleton;

import entity.Room;
import entity.RoomRate;
import entity.RoomType;
import java.util.List;
import javax.ejb.Schedule;
import javax.ejb.Singleton;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import util.enumeration.RateTypeEnum;
import util.enumeration.RoomStatusEnum;

/**
 *
 * @author qiuyutong
 */
@Singleton
public class ScheduledCleanupSessionBean {

    @PersistenceContext
    private EntityManager em;

    @Schedule(hour = "2", minute = "0", second = "0", info = "Daily Cleanup Timer") // Runs every day at 2 AM
    public void performCleanup() {
        deleteUnusedRoomTypes();
        deleteUnusedRoomRates();
        deleteUnusedRooms();
    }

    private void deleteUnusedRoomTypes() {
        // Find all disabled room types without active reservations
        Query query = em.createQuery("SELECT rt FROM RoomType rt WHERE rt.isDisabled = true "
                + "AND NOT EXISTS (SELECT r FROM Reservation r WHERE r.roomType = rt "
                + "AND (r.checkOutDate > CURRENT_DATE "
                + "OR (r.checkOutDate = CURRENT_DATE AND r.roomAllocation.room.status = :occupiedStatus)))");
        query.setParameter("occupiedStatus", RoomStatusEnum.OCCUPIED);

        List<RoomType> roomTypesToDelete = query.getResultList();

        for (RoomType roomType : roomTypesToDelete) {
            // Step 1: Dissociate this room type as "next higher room type" in other RoomType entities
            Query dissociateQuery = em.createQuery("UPDATE RoomType rt SET rt.nextHigherRoomType = NULL WHERE rt.nextHigherRoomType = :roomType");
            dissociateQuery.setParameter("roomType", roomType);
            dissociateQuery.executeUpdate();

            // Step 2: Delete associated rooms and room rates
            for (Room room : roomType.getRooms()) {
                em.remove(room);
            }
            for (RoomRate rate : roomType.getRoomrates()) {
                em.remove(rate);
            }
            em.remove(roomType);
        }
        em.flush();
    }

    private void deleteUnusedRoomRates() {
        // Find all disabled room rates without active reservations, matching the criteria in deleteRoomRate
        Query query = em.createQuery("SELECT rr FROM RoomRate rr WHERE rr.isDisabled = true "
                + "AND NOT EXISTS (SELECT r FROM Reservation r WHERE r.roomType = rr.roomType "
                + "AND ((rr.rateType = :publishedRate AND r.isWalkIn = true) " // Check for walk-in reservations for published rates
                + "OR (rr.rateType IN (:normalRate, :peakRate, :promotionRate) AND r.isWalkIn = false)) " // Check online reservations for other rate types
                + "AND (r.checkOutDate > CURRENT_DATE "
                + "OR (r.checkOutDate = CURRENT_DATE AND r.roomAllocation.room.status = :occupiedStatus)))");
        query.setParameter("occupiedStatus", RoomStatusEnum.OCCUPIED);
        query.setParameter("publishedRate", RateTypeEnum.PUBLISHED);
        query.setParameter("normalRate", RateTypeEnum.NORMAL);
        query.setParameter("peakRate", RateTypeEnum.PEAK);
        query.setParameter("promotionRate", RateTypeEnum.PROMOTION);

        List<RoomRate> ratesToDelete = query.getResultList();

        for (RoomRate rate : ratesToDelete) {
            em.remove(rate);
        }
        em.flush();
    }

    private void deleteUnusedRooms() {
        // Find all disabled rooms without active allocations, matching the criteria in deleteRoom
        Query query = em.createQuery("SELECT r FROM Room r WHERE r.isDisabled = true "
                + "AND NOT EXISTS (SELECT ra FROM RoomAllocation ra WHERE ra.room = r "
                + "AND (ra.reservation.checkOutDate > CURRENT_DATE "
                + "OR (ra.reservation.checkOutDate = CURRENT_DATE AND r.status = :occupiedStatus)))");
        query.setParameter("occupiedStatus", RoomStatusEnum.OCCUPIED);

        List<Room> roomsToDelete = query.getResultList();

        for (Room room : roomsToDelete) {
            em.remove(room);
        }
        em.flush();
    }
}
