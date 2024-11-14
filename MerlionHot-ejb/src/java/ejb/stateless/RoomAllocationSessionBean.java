/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/J2EE/EJB30/StatelessEjbClass.java to edit this template
 */
package ejb.stateless;

import entity.ExceptionReport;
import entity.Reservation;
import entity.Room;
import entity.RoomAllocation;
import entity.RoomType;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import javax.annotation.Resource;
import javax.ejb.EJB;
import javax.ejb.EJBContext;
import javax.ejb.Schedule;
import javax.ejb.Singleton;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import util.enumeration.ExceptionTypeEnum;
import util.enumeration.RateTypeEnum;
import util.enumeration.RoomStatusEnum;
import util.exception.CannotUpgradeException;
import util.exception.NoAvailableRoomException;
import util.exception.RoomAllocationNotFoundException;

@Stateless
public class RoomAllocationSessionBean implements RoomAllocationSessionBeanRemote, RoomAllocationSessionBeanLocal {

    @EJB
    private ExceptionReportSessionBeanRemote exceptionReportBean;

    @EJB
    private ReservationSessionBeanRemote reservationBean;

    @EJB
    private RoomSessionBeanRemote roomBean;

    @PersistenceContext(unitName = "MerlionHot-ejbPU")
    private EntityManager em;

    @Override
    public RoomAllocation retrieveAllocationById(Long id) throws RoomAllocationNotFoundException {
        try {
            Query query = em.createQuery("SELECT a FROM RoomAllocation a WHERE a.allocationId = :inID");
            query.setParameter("inID", id);
            return (RoomAllocation) query.getSingleResult();
        } catch (NoResultException e) {
            throw new RoomAllocationNotFoundException("Room allocation not found for ID: " + id);
        }
    }

    @Override
    public RoomAllocation retrieveAllocationByReservation(Reservation r) throws RoomAllocationNotFoundException {
        try {
            Query query = em.createQuery("SELECT a FROM RoomAllocation a WHERE a.reserveId = :inReserve");
            query.setParameter("inReserve", r);
            return (RoomAllocation) query.getSingleResult();
        } catch (NoResultException e) {
            throw new RoomAllocationNotFoundException("Room allocation not found for reservation: " + r.getReservationId());
        }
    }

    @Override
//@TransactionAttribute(TransactionAttributeType.REQUIRED)
    public Long createAllocation(Reservation reservation) throws NoAvailableRoomException, CannotUpgradeException {
        reservation = em.merge(reservation);
        RoomType requestedType = reservation.getRoomType();
        Date startDate = reservation.getCheckInDate();
        Date endDate = reservation.getCheckOutDate();

        // Step 1: Attempt to find an available room of the requested room type
        Room availableRoom = reservation.getIsWalkIn()
                ? findAvailableRoomForWalkIn(requestedType, startDate, endDate)
                : findAvailableRoomForOnline(requestedType, startDate, endDate);

        if (availableRoom != null) {
            // Room of requested type is available; allocate it directly
            return allocateRoom(reservation, availableRoom);
        } else {
            // Step 2: No room of requested type available, check for the next higher room type
            RoomType nextHigherType = requestedType.getNextHigherRoomType();
            if (nextHigherType != null) {
                Room upgradedRoom = reservation.getIsWalkIn()
                        ? findAvailableRoomForWalkIn(nextHigherType, startDate, endDate)
                        : findAvailableRoomForOnline(nextHigherType, startDate, endDate);

                if (upgradedRoom != null) {
                    // Allocate the guest to the next higher room type and log an exception
                    Long allocationId = allocateRoom(reservation, upgradedRoom);
                    throw new NoAvailableRoomException("Upgraded to next higher room type: " + nextHigherType.getRoomTypeName() + " due to lack of availability.");
                }
            }

            // Step 3: No available room of requested or next higher type; log an exception without allocation
            reservation.setIsDisabled(true);
            throw new CannotUpgradeException("No next higher room type.");
        }
    }

    @Override
    public Room findAvailableRoomForWalkIn(RoomType type, Date startDate, Date endDate) {
        // Verify room type and published rate availability
        Query rateQuery = em.createQuery("SELECT COUNT(rr) FROM RoomRate rr WHERE rr.roomType = :roomType AND rr.rateType = :publishedRate AND rr.isDisabled = false");
        rateQuery.setParameter("roomType", type);
        rateQuery.setParameter("publishedRate", RateTypeEnum.PUBLISHED);
        long publishedRateCount = (long) rateQuery.getSingleResult();

        if (type.getIsDisabled() || publishedRateCount == 0) {
            return null; // Room type or published rate is disabled, so no rooms are available for walk-in
        }

        // Find an available room of the specified type that has no overlapping allocations
        Query query = em.createQuery("SELECT r FROM Room r WHERE r.roomType = :roomType AND r.isDisabled = false AND r.status = :inStatus "
                + "AND r NOT IN (SELECT a.room FROM RoomAllocation a JOIN a.reserveId res WHERE "
                + "(res.checkInDate < :endDate AND res.checkOutDate > :startDate) "
                + "OR (res.checkInDate = :startDate AND res.checkOutDate = :endDate AND a.room.status = :occupiedStatus) "
                + "OR (res.checkOutDate = CURRENT_DATE AND :startDate = CURRENT_DATE AND a.room.status = :occupiedStatus))");
        query.setParameter("roomType", type);
        query.setParameter("startDate", startDate);
        query.setParameter("endDate", endDate);
        query.setParameter("inStatus", RoomStatusEnum.AVAIL);
        query.setParameter("occupiedStatus", RoomStatusEnum.OCCUPIED);

        List<Room> availableRooms = query.getResultList();
        return availableRooms.isEmpty() ? null : availableRooms.get(0);
    }

    @Override
    public Room findAvailableRoomForOnline(RoomType type, Date startDate, Date endDate) {
        // Verify room type and online rate availability
        Query rateQuery = em.createQuery("SELECT COUNT(rr) FROM RoomRate rr WHERE rr.roomType = :roomType "
                + "AND rr.rateType IN (:peakRate, :normalRate, :promotionRate) AND rr.isDisabled = false");
        rateQuery.setParameter("roomType", type);
        rateQuery.setParameter("peakRate", RateTypeEnum.PEAK);
        rateQuery.setParameter("normalRate", RateTypeEnum.NORMAL);
        rateQuery.setParameter("promotionRate", RateTypeEnum.PROMOTION);
        long onlineRateCount = (long) rateQuery.getSingleResult();

        if (type.getIsDisabled() || onlineRateCount == 0) {
            return null; // Room type or required online rates are disabled, so no rooms are available for online reservation
        }

        // Find an available room of the specified type that has no overlapping allocations
        Query query = em.createQuery("SELECT r FROM Room r WHERE r.roomType = :roomType AND r.isDisabled = false AND r.status = :inStatus "
                + "AND r NOT IN (SELECT a.room FROM RoomAllocation a JOIN a.reserveId res WHERE "
                + "(res.checkInDate < :endDate AND res.checkOutDate > :startDate) "
                + "OR (res.checkInDate = :startDate AND res.checkOutDate = :endDate AND a.room.status = :occupiedStatus) "
                + "OR (res.checkOutDate = CURRENT_DATE AND :startDate = CURRENT_DATE AND a.room.status = :occupiedStatus))");
        query.setParameter("roomType", type);
        query.setParameter("startDate", startDate);
        query.setParameter("endDate", endDate);
        query.setParameter("inStatus", RoomStatusEnum.AVAIL);
        query.setParameter("occupiedStatus", RoomStatusEnum.OCCUPIED);

        List<Room> availableRooms = query.getResultList();
        return availableRooms.isEmpty() ? null : availableRooms.get(0);
    }

    /* @Override
    public Room findAvailableRoom(RoomType type, Date startDate, Date endDate) {
        Query query = em.createQuery("SELECT r FROM Room r WHERE r.roomType = :roomType AND r.isDisabled = false AND r.status = :inStatus AND r NOT IN ("
                + "SELECT a.room FROM RoomAllocation a JOIN a.reserveId res WHERE "
                + "res.checkInDate <= :endDate AND res.checkOutDate >= :startDate)");
        query.setParameter("roomType", type);
        query.setParameter("startDate", startDate);
        query.setParameter("endDate", endDate);
        query.setParameter("inStatus", RoomStatusEnum.AVAIL);

        List<Room> availableRooms = query.getResultList();
        return availableRooms.isEmpty() ? null : availableRooms.get(0);
    } */
    @Override
    public Long allocateRoom(Reservation reservation, Room room) {
        RoomAllocation allocation = new RoomAllocation();
        allocation.setRoom(room);
        allocation.setReserveId(reservation);
        room.getRoomAllocation().add(allocation);
        reservation.setRoomAllocation(allocation);

        em.persist(allocation);
        em.flush();
        roomBean.updateRoom(room);
        reservationBean.updateReservation(reservation);

        return allocation.getAllocationId();
    }

    @Override
    //@TransactionAttribute(TransactionAttributeType.REQUIRED)
    public void performRoomAllocations() {
        Date today = new Date();

        // Reset today to 12 am (midnight)
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(today);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);

        today = calendar.getTime();

        List<Reservation> reservations = em.createQuery("SELECT r FROM Reservation r WHERE r.checkInDate = :today", Reservation.class)
                .setParameter("today", today)
                .getResultList();

        for (Reservation reservation : reservations) {
            try {
                createAllocation(reservation);
                System.out.println("Room allocated for reservation ID: " + reservation.getReservationId());
            } catch (NoAvailableRoomException | CannotUpgradeException e) {
                // Log the exception in the report
                createRoomAllocationException(reservation, e);
                System.out.println("Exception occurred for reservation ID: " + reservation.getReservationId() + " - " + e.getMessage());
            }
        }
    }

    @Override
    public void createRoomAllocationException(Reservation r, Exception exception) {
        ExceptionReport report = new ExceptionReport();
        report.setReservation(r);
        if (exception instanceof NoAvailableRoomException) {
            report.setExceptionType(ExceptionTypeEnum.HIGHERAVAIL);
        } else {
            report.setExceptionType(ExceptionTypeEnum.NOHIGHERAVAIL);
        }
        report.setTimestamp(new Date());
        report.setMessage(exception.getMessage());
        report.setResolved(false);
        exceptionReportBean.createExceptionReport(report);

    }

}
