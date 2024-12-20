/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/J2EE/EJB30/StatelessEjbClass.java to edit this template
 */
package ejb.stateless;

import entity.Room;
import entity.RoomType;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import util.enumeration.RateTypeEnum;
import util.enumeration.RoomStatusEnum;
import util.exception.RoomErrorException;

/**
 *
 * @author qiuyutong
 */
@Stateless
public class RoomSessionBean implements RoomSessionBeanRemote, RoomSessionBeanLocal {

    @PersistenceContext(unitName = "MerlionHot-ejbPU")
    private EntityManager em;

    @Override
    public Long createRoom(Room newRoom, RoomType rt) {
        em.persist(newRoom);
        rt = em.merge(rt);
        List<Room> li = rt.getRooms();
        li.add(newRoom);
        rt.setRooms(li);
        newRoom.setRoomType(rt);
        em.flush();
        return newRoom.getRoomId();
    }

    @Override
    public List<Room> retrieveAllRooms() throws RoomErrorException {
        Query query = em.createQuery("SELECT r from Room r");
        try {
            List<Room> roomList = query.getResultList();
            for (Room r : roomList) {
                r.getRoomAllocation().size();
            }
            return roomList;
        } catch (Exception ex) {
            throw new RoomErrorException("Error occured while retrieving Room Type List!");
        }
    }

    @Override
    public Room retrieveRoomById(Long roomId) {

        return null;

    }

    @Override
    public Room retrieveRoomByNumber(String roomNumber) throws RoomErrorException {
        Query query = em.createQuery("SELECT r from Room r WHERE r.roomNumber = :no");
        query.setParameter("no", roomNumber);
        try {
            Room r = (Room) query.getSingleResult();
            r.getRoomAllocation().size();
            return r;
        } catch (NoResultException | NonUniqueResultException ex) {
            throw new RoomErrorException("Cannot find room from room number!");
        }
    }

    @Override
    public void updateRoomTypeOfRoom(Room room, RoomType newRt) {
        room = em.merge(room);
        newRt = em.merge(newRt);
        RoomType oldRt = room.getRoomType();
        List<Room> oldRtList = oldRt.getRooms();
        List<Room> newRtList = new ArrayList<>();
        for (Room r : oldRtList) {
            if (!r.equals(room)) {
                newRtList.add(r);
            }
        }
        oldRt.setRooms(newRtList);

        room.setRoomType(newRt);
        List<Room> newList = newRt.getRooms();
        newList.add(room);
        newRt.setRooms(newList);
    }

    @Override
    public void updateRoom(Room room) {
        em.merge(room);
    }

    @Override
    public int getAvailableRoomCountForWalkIn(RoomType roomType, Date startDate, Date endDate) {
        return getAvailableRoomCountByRateType(roomType, startDate, endDate, RateTypeEnum.PUBLISHED);
    }

    @Override
    public int getAvailableRoomCountForOnline(RoomType roomType, Date startDate, Date endDate) {
        return getAvailableRoomCountByRateType(roomType, startDate, endDate, RateTypeEnum.NORMAL);
    }

    private int getAvailableRoomCountByRateType(RoomType roomType, Date startDate, Date endDate, RateTypeEnum rateType) {
        roomType = em.merge(roomType);

        // Check if the room type is disabled or if no active rate of the specified type exists
        Query rateCheckQuery = em.createQuery("SELECT COUNT(rr) FROM RoomRate rr WHERE rr.roomType = :roomType AND rr.isDisabled = false AND rr.rateType = :rateType");
        rateCheckQuery.setParameter("roomType", roomType);
        rateCheckQuery.setParameter("rateType", rateType);
        long activeRateCount = (long) rateCheckQuery.getSingleResult();
        if (roomType.getIsDisabled() || activeRateCount == 0) {
            return 0; // Room type or required rate type is disabled
        }

        // Query to get the total count of rooms for the specified RoomType that are not disabled
        Query totalRoomsQuery = em.createQuery("SELECT COUNT(r) FROM Room r WHERE r.roomType = :roomType AND r.isDisabled = false");
        totalRoomsQuery.setParameter("roomType", roomType);
        long totalRooms = (long) totalRoomsQuery.getSingleResult();

        // Query to count the number of overlapping reservations for this RoomType
        Query reservedRoomsQuery = em.createQuery("SELECT COUNT(res) FROM Reservation res "
                + "WHERE res.roomType = :roomType "
                + "AND ("
                + "(res.checkInDate < :endDate AND res.checkOutDate > :startDate) " // Overlaps with the requested dates
                + "OR (res.checkInDate = :startDate AND res.checkOutDate = :endDate AND res.roomAllocation.room.status = :occupiedStatus) " // Matches exactly with requested dates and is still occupied
                + "OR (res.checkOutDate = CURRENT_DATE AND :startDate = CURRENT_DATE AND res.roomAllocation.room.status = :occupiedStatus) " // Ends today and room is occupied
                + ")");
        reservedRoomsQuery.setParameter("roomType", roomType);
        reservedRoomsQuery.setParameter("startDate", startDate);
        reservedRoomsQuery.setParameter("endDate", endDate);
        reservedRoomsQuery.setParameter("occupiedStatus", RoomStatusEnum.OCCUPIED);

        long reservedRooms = (long) reservedRoomsQuery.getSingleResult();

        // Calculate available rooms by subtracting reserved rooms from total rooms
        int availableRooms = (int) (totalRooms - reservedRooms);
        return availableRooms > 0 ? availableRooms : 0; // Ensure it doesn't return a negative value
    }

    @Override
    public String deleteRoom(Room room) {
        Room managedRoom = em.find(Room.class, room.getRoomId());

        if (managedRoom == null) {
            return "Room not found";
        }

        Query query = em.createQuery("SELECT COUNT(a) FROM RoomAllocation a "
                + "WHERE a.room = :room "
                + "AND (a.reserveId.checkOutDate > CURRENT_DATE "
                + "OR (a.reserveId.checkOutDate = CURRENT_DATE AND a.room.status = :occupiedStatus))");
        query.setParameter("room", managedRoom);
        query.setParameter("occupiedStatus", RoomStatusEnum.OCCUPIED);
        Long count = (Long) query.getSingleResult();

        if (count == 0) {
            em.remove(managedRoom);
            em.flush();
            return "Room deleted successfully.";
        } else {
            managedRoom.setIsDisabled(true);
            em.merge(managedRoom);
            em.flush();
            return "Room has active allocations and has been disabled.";
        }
    }

    @Override
    public boolean isRoomAvailable(Room room, LocalDate date, Long currentReservationId) {
        ZonedDateTime endOfDay = date.atTime(LocalTime.MAX).atZone(ZoneId.systemDefault());

        Query query = em.createQuery("SELECT COUNT(a) FROM RoomAllocation a JOIN a.reserveId res "
                + "WHERE a.room = :room "
                + "AND res.reservationId != :currentReservationId "
                + "AND (res.checkInDate <= :endOfDay AND res.checkOutDate > :endOfDay) "
                + "AND a.room.status = :occupiedStatus"); // Ensure room is still occupied
        query.setParameter("room", room);
        query.setParameter("currentReservationId", currentReservationId);
        query.setParameter("endOfDay", Date.from(endOfDay.toInstant()));
        query.setParameter("occupiedStatus", RoomStatusEnum.OCCUPIED);

        Long count = (Long) query.getSingleResult();
        return count == 0;
    }
}
