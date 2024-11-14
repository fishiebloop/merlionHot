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
        Query query = em.createQuery("SELECT rt FROM RoomType rt WHERE rt.isDisabled = true "
                + "AND NOT EXISTS (SELECT r FROM Reservation r WHERE r.roomType = rt "
                + "AND (r.checkOutDate > CURRENT_DATE "
                + "OR (r.checkOutDate = CURRENT_DATE AND r.roomAllocation.room.status = :occupiedStatus)))");
        query.setParameter("occupiedStatus", RoomStatusEnum.OCCUPIED);

        List<RoomType> roomTypesToDelete = query.getResultList();

        for (RoomType roomType : roomTypesToDelete) {
            // Check if the room type has active reservations as per the deleteRoomType logic
            Long activeReservationCount = (Long) em.createQuery("SELECT COUNT(r) FROM Reservation r "
                    + "WHERE r.roomType = :roomType "
                    + "AND (r.checkOutDate > CURRENT_DATE "
                    + "OR (r.checkOutDate = CURRENT_DATE AND r.roomAllocation.room.status = :occupiedStatus))")
                    .setParameter("roomType", roomType)
                    .setParameter("occupiedStatus", RoomStatusEnum.OCCUPIED)
                    .getSingleResult();

            if (activeReservationCount == 0) {
                // Dissociate next higher room type relationships
                em.createQuery("UPDATE RoomType rt SET rt.nextHigherRoomType = NULL WHERE rt.nextHigherRoomType = :roomType")
                        .setParameter("roomType", roomType)
                        .executeUpdate();

                // Delete associated rooms and rates
                roomType.getRooms().forEach(em::remove);
                roomType.getRoomrates().forEach(em::remove);
                em.remove(roomType);
            }
        }
        em.flush();
    }

    private void deleteUnusedRoomRates() {
        Query query = em.createQuery("SELECT rr FROM RoomRate rr WHERE rr.isDisabled = true "
                + "AND NOT EXISTS (SELECT r FROM Reservation r WHERE r.roomType = rr.roomType "
                + "AND ((rr.rateType = :publishedRate AND r.isWalkIn = true) "
                + "OR (rr.rateType IN (:normalRate, :peakRate, :promotionRate) AND r.isWalkIn = false)) "
                + "AND (r.checkOutDate > CURRENT_DATE "
                + "OR (r.checkOutDate = CURRENT_DATE AND r.roomAllocation.room.status = :occupiedStatus)))");
        query.setParameter("occupiedStatus", RoomStatusEnum.OCCUPIED);
        query.setParameter("publishedRate", RateTypeEnum.PUBLISHED);
        query.setParameter("normalRate", RateTypeEnum.NORMAL);
        query.setParameter("peakRate", RateTypeEnum.PEAK);
        query.setParameter("promotionRate", RateTypeEnum.PROMOTION);

        List<RoomRate> ratesToDelete = query.getResultList();

        for (RoomRate rate : ratesToDelete) {
            // Check if there are active reservations that would prevent deletion, per deleteRoomRate logic
            Long activeReservationCount = (Long) em.createQuery("SELECT COUNT(r) FROM Reservation r "
                    + "WHERE r.roomType = :roomType "
                    + "AND r.isWalkIn = :isWalkIn "
                    + "AND (r.checkOutDate > CURRENT_DATE "
                    + "OR (r.checkOutDate = CURRENT_DATE AND r.roomAllocation.room.status = :occupiedStatus))")
                    .setParameter("roomType", rate.getRoomType())
                    .setParameter("isWalkIn", rate.getRateType() == RateTypeEnum.PUBLISHED)
                    .setParameter("occupiedStatus", RoomStatusEnum.OCCUPIED)
                    .getSingleResult();

            if (activeReservationCount == 0) {
                em.remove(rate);
            }
        }
        em.flush();
    }

    private void deleteUnusedRooms() {
        Query query = em.createQuery("SELECT r FROM Room r WHERE r.isDisabled = true "
                + "AND NOT EXISTS (SELECT ra FROM RoomAllocation ra WHERE ra.room = r "
                + "AND (ra.reservation.checkOutDate > CURRENT_DATE "
                + "OR (ra.reservation.checkOutDate = CURRENT_DATE AND r.status = :occupiedStatus)))");
        query.setParameter("occupiedStatus", RoomStatusEnum.OCCUPIED);

        List<Room> roomsToDelete = query.getResultList();

        for (Room room : roomsToDelete) {
            // Check if there are active allocations that would prevent deletion, per deleteRoom logic
            Long activeAllocationCount = (Long) em.createQuery("SELECT COUNT(a) FROM RoomAllocation a "
                    + "WHERE a.room = :room "
                    + "AND (a.reservation.checkOutDate > CURRENT_DATE "
                    + "OR (a.reservation.checkOutDate = CURRENT_DATE AND a.room.status = :occupiedStatus))")
                    .setParameter("room", room)
                    .setParameter("occupiedStatus", RoomStatusEnum.OCCUPIED)
                    .getSingleResult();

            if (activeAllocationCount == 0) {
                em.remove(room);
            }
        }
        em.flush();
    }
}
