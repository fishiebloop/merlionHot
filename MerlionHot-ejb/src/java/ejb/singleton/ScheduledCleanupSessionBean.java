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
import util.enumeration.RoomStatusEnum;

/**
 *
 * @author qiuyutong
 */
@Singleton
public class ScheduledCleanupSessionBean {

    @PersistenceContext(unitName = "MerlionHot-ejbPU")
    private EntityManager em;

    @Singleton
    public class RoomTypeCleanupJob {

        @PersistenceContext
        private EntityManager em;

        @Schedule(hour = "2", minute = "0", second = "0", info = "Daily Cleanup Timer") // Runs every day at 2 AM
        public void performCleanup() {
            deleteUnusedRoomTypes(); // Room types are deleted along with their associated rooms and room rates
            deleteUnusedRoomRates(); // Standalone cleanup for disabled room rates
            deleteUnusedRooms();     // Standalone cleanup for disabled rooms
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
                // Delete associated rooms and room rates
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
            // Find all disabled room rates without active reservations
            Query query = em.createQuery("SELECT rr FROM RoomRate rr WHERE rr.isDisabled = true "
                    + "AND NOT EXISTS (SELECT r FROM Reservation r WHERE r.roomType = rr.roomType "
                    + "AND (r.checkOutDate > CURRENT_DATE "
                    + "OR (r.checkOutDate = CURRENT_DATE AND r.roomAllocation.room.status = :occupiedStatus)))");
            query.setParameter("occupiedStatus", RoomStatusEnum.OCCUPIED);

            List<RoomRate> ratesToDelete = query.getResultList();

            for (RoomRate rate : ratesToDelete) {
                em.remove(rate);
            }
            em.flush();
        }

        private void deleteUnusedRooms() {
            // Find all disabled rooms without active allocations
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
}