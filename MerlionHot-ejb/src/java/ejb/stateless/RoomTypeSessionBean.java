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
    public RoomType createRoomType(RoomType newRoomType) {
        em.persist(newRoomType);
        em.flush();
        return newRoomType;
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

    /*@Override
    public RoomType retrieveRoomTypeById(Long roomTypeId) {
        Query query = em.createQuery("SELECT rt from RoomType rt WHERE rt.roomTypeId = :inID");
        query.setParameter("inID", roomTypeId);
        return (RoomType) query.getSingleResult();
    }*/
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
                + "WHERE rt.isDisabled = false " // Only include room types that are not disabled
                + "AND EXISTS (SELECT rr FROM RoomRate rr WHERE rr.roomType = rt AND rr.isDisabled = false AND rr.rateType = :publishedRate) "
                + "AND (SELECT COUNT(res) FROM Reservation res "
                + "WHERE res.roomType = rt "
                + "AND (res.checkInDate < :endDate AND res.checkOutDate > :startDate) "
                + "AND (res.checkOutDate != CURRENT_DATE OR res.roomAllocation.room.status != :occupiedStatus)) < "
                + "(SELECT COUNT(r) FROM Room r WHERE r.roomType = rt AND r.isDisabled = false)" // Ensure reservation count is less than room count
        );
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
                    + "WHERE rt.isDisabled = false " // Only include room types that are not disabled
                    + "AND EXISTS (SELECT rr FROM RoomRate rr WHERE rr.roomType = rt AND rr.isDisabled = false AND rr.rateType = :normalRate) "
                    + "AND (SELECT COUNT(res) FROM Reservation res "
                    + "WHERE res.isDisabled = false AND res.roomType = rt "
                    + "AND (res.checkInDate < :endDate AND res.checkOutDate > :startDate) "
                    + "AND (res.checkOutDate != CURRENT_DATE OR res.roomAllocation.room.status != :occupiedStatus)) < "
                    + "(SELECT COUNT(r) FROM Room r WHERE r.roomType = rt AND r.isDisabled = false)" // Ensure reservation count is less than room count
            );
            query.setParameter("startDate", checkIn);
            query.setParameter("endDate", checkOut);
            query.setParameter("normalRate", RateTypeEnum.NORMAL);
            query.setParameter("occupiedStatus", RoomStatusEnum.OCCUPIED);

                /*Integer noRooms = (int) Math.ceil((float) guests / rt.getCapacity());
                long count = (long) q.getSingleResult();
                if (count + noRooms <= rt.getRooms().size()) {
                    avail.add(rt);
                }*/
            return query.getResultList();
        } catch (Exception ex) {
            throw new RoomTypeErrorException("Error occured while retrieving Available Room Type List!" + ex.getMessage());
        }
    }

    @Override
    public String deleteRoomType(RoomType roomType) {
        // Retrieve the managed RoomType instance
        RoomType managedRoomType = em.find(RoomType.class, roomType.getRoomTypeId());

        if (managedRoomType == null) {
            return "Room type not found";
        }

        // Check for active reservations
        Query query = em.createQuery("SELECT COUNT(r) FROM Reservation r "
                + "WHERE r.roomType = :roomType "
                + "AND (r.checkOutDate > CURRENT_DATE "
                + "OR (r.checkOutDate = CURRENT_DATE AND r.roomAllocation.room.status = :occupiedStatus))");
        query.setParameter("roomType", managedRoomType);
        query.setParameter("occupiedStatus", RoomStatusEnum.OCCUPIED);
        Long count = (Long) query.getSingleResult();

        if (count == 0) {
            // No active reservations; delete room type, associated rooms, and room rates
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
            // Active reservations exist; disable room type, associated rooms, and room rates
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
        rt = em.merge(rt);  // Ensures the RoomType is properly merged into the current persistence context

        // Create a query that includes date filtering for PEAK and PROMOTION rates
        String queryStr = "SELECT rr.ratePerNight FROM RoomRate rr WHERE rr.roomType = :rt "
                          + "AND rr.isDisabled = false ";

        // Add date range filtering only for PEAK and PROMOTION rates
        queryStr += "AND ((rr.rateType = util.enumeration.RateTypeEnum.PROMOTION "
                    + "AND rr.startDate <= :checkOut AND rr.endDate >= :checkIn) "
                    + "OR (rr.rateType = util.enumeration.RateTypeEnum.PEAK "
                    + "AND rr.startDate <= :checkOut AND rr.endDate >= :checkIn) "
                    + "OR (rr.rateType NOT IN (util.enumeration.RateTypeEnum.PROMOTION, "
                    + "util.enumeration.RateTypeEnum.PEAK))) ";

        // Order by rateType priority (PROMOTION > PEAK > NORMAL > others)
        queryStr += "ORDER BY CASE "
                    + "WHEN rr.rateType = util.enumeration.RateTypeEnum.PROMOTION THEN 1 "
                    + "WHEN rr.rateType = util.enumeration.RateTypeEnum.PEAK THEN 2 "
                    + "WHEN rr.rateType = util.enumeration.RateTypeEnum.NORMAL THEN 3 "
                    + "ELSE 4 END";

        Query q = em.createQuery(queryStr)
                    .setParameter("rt", rt)
                    .setParameter("checkIn", checkIn)
                    .setParameter("checkOut", checkOut)
                    .setMaxResults(1);

        // Get the selected rate based on rateType priority
        BigDecimal dailyPrice = (BigDecimal) q.getSingleResult();

        if (checkIn.equals(checkOut)) {
            // If the dates are the same, count as 1 night
            totalPrice = totalPrice.add(dailyPrice);
        } else {
            // Loop through each day between checkIn and checkOut to accumulate the price
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(checkIn);

            while (!calendar.getTime().after(checkOut)) {
                // Add the price for each day
                totalPrice = totalPrice.add(dailyPrice);
                calendar.add(Calendar.DAY_OF_MONTH, 1); // Move to the next day
            }
        }

        return totalPrice; 
    }
    
}
