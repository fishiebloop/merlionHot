package ejb.stateless;

import entity.RoomType;
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
    public Long createRoomType(RoomType newRoomType) {
        em.persist(newRoomType);
        em.flush();
        return newRoomType.getRoomTypeId();
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
    public RoomType retrieveRoomTypeById(Long roomTypeId) {
        Query query = em.createQuery("SELECT rt from RoomType rt WHERE rt.roomTypeId = :inID");
        query.setParameter("inID", roomTypeId);
        return (RoomType) query.getSingleResult();

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
    public void deleteRoomType(RoomType roomType) {
        roomType = em.merge(roomType);
        if (roomType.getReservations().size() > 0) {
            roomType.setIsDisabled(true);
        } else {
            em.remove(roomType);
        }
    }

    @Override
    public boolean roomRateExistsForType(Long roomTypeId, RateTypeEnum rateType) {
        Query query = em.createQuery("SELECT COUNT(rr) FROM RoomRate rr WHERE rr.roomType.roomTypeId = :roomTypeId AND rr.rateType = :rateType AND rr.isDisabled = false");
        query.setParameter("roomTypeId", roomTypeId);
        query.setParameter("rateType", rateType);

        Long count = (Long) query.getSingleResult();
        return count > 0;
    }
}
