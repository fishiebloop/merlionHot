package ejb.stateless;

import entity.Reservation;
import entity.RoomType;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
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
    public RoomType retrieveRoomTypeById(Long roomTypeId) throws RoomTypeErrorException{
        
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
    public List<RoomType> retrieveAllAvailRoomType(Date checkIn, Date checkOut, Integer guests) throws RoomTypeErrorException {
        try {
            List<RoomType> all = retrieveAllRoomTypes();
            List<RoomType> avail = new ArrayList<>();
            for (RoomType rt : all) {
                Query q = em.createQuery("SELECT COUNT(r) FROM Reservation r WHERE r.roomType = :rt AND r.checkInDate < :out AND r.checkOutDate > :in");
                q.setParameter("rt", rt);
                q.setParameter("in", checkIn);
                q.setParameter("out", checkOut);

                Integer noRooms = (int) Math.ceil((float) guests / rt.getCapacity());
                long count = (long) q.getSingleResult();
                if (count + noRooms <= rt.getRooms().size()) {
                    avail.add(rt);
                }
            }
            return avail;
        } catch (Exception ex) {
            throw new RoomTypeErrorException("Error occured while retrieving Available Room Type List!" + ex.getMessage());
        }
        
    }
}
