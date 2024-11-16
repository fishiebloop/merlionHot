package ejb.stateless;

import entity.Reservation;
import entity.Room;
import entity.RoomRate;
import entity.RoomType;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
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
import util.exception.RoomTypeErrorException;

@Stateless
public class RoomTypeSessionBean implements RoomTypeSessionBeanRemote, RoomTypeSessionBeanLocal {

    @PersistenceContext(unitName = "MerlionHot-ejbPU")
    private EntityManager em;

    @Override
    public RoomType createRoomType(RoomType type) throws RoomTypeErrorException {
        if (isRoomTypeNameUnique(type.getRoomTypeName())) {
            em.persist(type);
            em.flush();
            return type;
        } else {
            throw new RoomTypeErrorException("Room type name must be unique.");
        }
    }

    private boolean isRoomTypeNameUnique(String roomTypeName) {
        Query query = em.createQuery("SELECT COUNT(rt) FROM RoomType rt WHERE rt.roomTypeName = :name");
        query.setParameter("name", roomTypeName);
        Long count = (Long) query.getSingleResult();
        return count == 0; // true if the name is unique
    }

    @Override
    public List<RoomType> retrieveAllRoomTypes() throws RoomTypeErrorException {
        Query query = em.createQuery("SELECT rt from RoomType rt");
        try {
            List<RoomType> rtLi = query.getResultList();
            for (RoomType rt : rtLi) {
                rt.getRooms().size();
                rt.getReservations().size();
                rt.getRoomrates().size();
            }
            return rtLi;
        } catch (Exception ex) {
            throw new RoomTypeErrorException("Error occured while retrieving Room Type List!");
        }

    }

    @Override
    public RoomType retrieveRoomTypeById(Long roomTypeId) throws RoomTypeErrorException {

        try {
            RoomType rt = em.find(RoomType.class, roomTypeId);
            rt.getRooms().size();
            rt.getReservations().size();
            rt.getRoomrates().size();
            return rt;
        } catch (Exception ex) {
            throw new RoomTypeErrorException("Error occured while retrieving Room Type List!");
        }

    }

    @Override
    public RoomType retrieveRoomTypeByName(String roomTypeName) throws RoomTypeErrorException {
        Query query = em.createQuery("SELECT rt from RoomType rt WHERE rt.roomTypeName = :name");
        query.setParameter("name", roomTypeName);
        try {
            RoomType rt = (RoomType) query.getSingleResult();
            rt.getRooms().size();
            rt.getReservations().size();
            rt.getRoomrates().size();
            return rt;
        } catch (NoResultException | NonUniqueResultException ex) {
            throw new RoomTypeErrorException("Cannot find room type from name!");
        }

    }

    @Override
    public List<RoomType> retrieveAvailableRoomTypes(Date startDate, Date endDate) {
        Query query = em.createQuery("SELECT rt FROM RoomType rt "
                + "WHERE rt.isDisabled = false "
                + "AND EXISTS (SELECT rr FROM RoomRate rr WHERE rr.roomType = rt AND rr.isDisabled = false AND rr.rateType = :publishedRate) "
                + "AND (SELECT COUNT(res) FROM Reservation res "
                + "WHERE res.roomType = rt "
                + "AND ("
                + "(res.checkInDate < :endDate AND res.checkOutDate > :startDate) "
                + "OR (res.checkInDate = :startDate AND res.checkOutDate = :endDate AND res.roomAllocation.room.status = :occupiedStatus) "
                + "OR (res.checkOutDate = CURRENT_DATE AND :startDate = CURRENT_DATE AND res.roomAllocation.room.status = :occupiedStatus) "
                + ")) < "
                + "(SELECT COUNT(r) FROM Room r WHERE r.roomType = rt AND r.isDisabled = false)");
        query.setParameter("startDate", startDate);
        query.setParameter("endDate", endDate);
        query.setParameter("publishedRate", RateTypeEnum.PUBLISHED);
        query.setParameter("occupiedStatus", RoomStatusEnum.OCCUPIED);

        return query.getResultList();
    }

//no reservations, !isDisabled, available?
    @Override
    public void updateRoomType(RoomType roomType) {
        em.merge(roomType);
    }

    @Override
    public boolean roomRateExistsForType(Long roomTypeId, RateTypeEnum rateType) {
        Query query = em.createQuery("SELECT COUNT(rr) FROM RoomRate rr WHERE rr.roomType.roomTypeId = :roomTypeId AND rr.rateType = :rateType AND rr.isDisabled = false");
        query.setParameter("roomTypeId", roomTypeId);
        query.setParameter("rateType", rateType);

        Long count = (Long) query.getSingleResult();
        return count > 0;

    }

    @Override
    public List<RoomType> retrieveAllAvailRoomTypeOnline(Date checkIn, Date checkOut) throws RoomTypeErrorException {
        try {
            Query query = em.createQuery("SELECT rt FROM RoomType rt "
                    + "WHERE rt.isDisabled = false "
                    + "AND EXISTS (SELECT rr FROM RoomRate rr WHERE rr.roomType = rt AND rr.isDisabled = false AND rr.rateType = :normalRate) "
                    + "AND (SELECT COUNT(res) FROM Reservation res "
                    + "WHERE res.isDisabled = false AND res.roomType = rt "
                    + "AND ("
                    + "(res.checkInDate < :endDate AND res.checkOutDate > :startDate) "
                    + "OR (res.checkInDate = :startDate AND res.checkOutDate = :endDate AND res.roomAllocation.room.status = :occupiedStatus) "
                    + "OR (res.checkOutDate = CURRENT_DATE AND :startDate = CURRENT_DATE AND res.roomAllocation.room.status = :occupiedStatus) "
                    + ")) < "
                    + "(SELECT COUNT(r) FROM Room r WHERE r.roomType = rt AND r.isDisabled = false)");
            query.setParameter("startDate", checkIn);
            query.setParameter("endDate", checkOut);
            query.setParameter("normalRate", RateTypeEnum.NORMAL);
            query.setParameter("occupiedStatus", RoomStatusEnum.OCCUPIED);

            return query.getResultList();
        } catch (Exception ex) {
            throw new RoomTypeErrorException("Error occurred while retrieving Available Room Type List: " + ex.getMessage());
        }
    }

    @Override
    public String deleteRoomType(RoomType roomType) {
        RoomType managedRoomType = em.find(RoomType.class, roomType.getRoomTypeId());

        if (managedRoomType == null) {
            return "Room type not found";
        }

        Query query = em.createQuery("SELECT COUNT(r) FROM Reservation r "
                + "WHERE r.roomType = :roomType "
                + "AND (r.checkOutDate > CURRENT_DATE "
                + "OR (r.checkOutDate = CURRENT_DATE AND r.roomAllocation.room.status = :occupiedStatus))");
        query.setParameter("roomType", managedRoomType);
        query.setParameter("occupiedStatus", RoomStatusEnum.OCCUPIED);
        Long activeReservationCount = (Long) query.getSingleResult();

        if (activeReservationCount == 0) {
            Query dissociateQuery = em.createQuery("UPDATE RoomType rt SET rt.nextHigherRoomType = NULL WHERE rt.nextHigherRoomType = :roomType");
            dissociateQuery.setParameter("roomType", managedRoomType);
            dissociateQuery.executeUpdate();

            for (RoomRate rate : managedRoomType.getRoomrates()) {
                em.remove(rate);
            }
            for (Room room : managedRoomType.getRooms()) {
                em.remove(room);
            }
            em.remove(managedRoomType);
            em.flush();
            return "Room type, associated rooms, and room rates deleted successfully.";
        } else {
            managedRoomType.setIsDisabled(true);
            em.merge(managedRoomType);

            for (RoomRate rate : managedRoomType.getRoomrates()) {
                rate.setIsDisabled(true);
                em.merge(rate);
            }

            for (Room room : managedRoomType.getRooms()) {
                room.setIsDisabled(true);
                em.merge(room);
            }

            em.flush();
            return "Room type has active reservations and has been disabled along with associated rooms and room rates.";
        }
    }

    public BigDecimal getPriceOfRoomTypeOnline(Date checkIn, Date checkOut, RoomType rt) {
        BigDecimal totalPrice = BigDecimal.ZERO;
        rt = em.merge(rt); 

        // check if it is a same-day stay 
        if (checkIn.equals(checkOut)) {
            // Query to get the rate for the single day
            String queryStr = "SELECT rr.ratePerNight FROM RoomRate rr WHERE rr.roomType = :rt "
                    + "AND rr.isDisabled = false "
                    + "AND ((rr.rateType = util.enumeration.RateTypeEnum.PROMOTION "
                    + "AND rr.startDate <= :currentDay AND rr.endDate >= :currentDay) "
                    + "OR (rr.rateType = util.enumeration.RateTypeEnum.PEAK "
                    + "AND rr.startDate <= :currentDay AND rr.endDate >= :currentDay) "
                    + "OR (rr.rateType = util.enumeration.RateTypeEnum.NORMAL)) "
                    + "ORDER BY CASE "
                    + "WHEN rr.rateType = util.enumeration.RateTypeEnum.PROMOTION THEN 1 "
                    + "WHEN rr.rateType = util.enumeration.RateTypeEnum.PEAK THEN 2 "
                    + "WHEN rr.rateType = util.enumeration.RateTypeEnum.NORMAL THEN 3 "
                    + "ELSE 4 END";

            Query q = em.createQuery(queryStr)
                    .setParameter("rt", rt)
                    .setParameter("currentDay", checkIn)
                    .setMaxResults(1);

            BigDecimal dailyPrice = (BigDecimal) q.getSingleResult();
            totalPrice = totalPrice.add(dailyPrice); // Count as 1 night
        } else {
            // loop through each day between checkIn and checkOut (excluding checkOut)
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(checkIn);

            while (!calendar.getTime().equals(checkOut)) {
                Date currentDay = calendar.getTime();

                // calculate rate for the current day
                String queryStr = "SELECT rr.ratePerNight FROM RoomRate rr WHERE rr.roomType = :rt "
                        + "AND rr.isDisabled = false "
                        + "AND ((rr.rateType = util.enumeration.RateTypeEnum.PROMOTION "
                        + "AND rr.startDate <= :currentDay AND rr.endDate >= :currentDay) "
                        + "OR (rr.rateType = util.enumeration.RateTypeEnum.PEAK "
                        + "AND rr.startDate <= :currentDay AND rr.endDate >= :currentDay) "
                        + "OR (rr.rateType = util.enumeration.RateTypeEnum.NORMAL)) "
                        + "ORDER BY CASE "
                        + "WHEN rr.rateType = util.enumeration.RateTypeEnum.PROMOTION THEN 1 "
                        + "WHEN rr.rateType = util.enumeration.RateTypeEnum.PEAK THEN 2 "
                        + "WHEN rr.rateType = util.enumeration.RateTypeEnum.NORMAL THEN 3 "
                        + "ELSE 4 END";

                Query q = em.createQuery(queryStr)
                        .setParameter("rt", rt)
                        .setParameter("currentDay", currentDay)
                        .setMaxResults(1);

                BigDecimal dailyPrice = (BigDecimal) q.getSingleResult();
                
                // add the price for day
                totalPrice = totalPrice.add(dailyPrice);

                calendar.add(Calendar.DAY_OF_MONTH, 1); // Move to the next day
            }
        }

        return totalPrice;
    }
}
