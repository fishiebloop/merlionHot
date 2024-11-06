/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/J2EE/EJB30/StatelessEjbClass.java to edit this template
 */
package ejb.stateless;

import entity.Room;
import entity.RoomType;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
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
    public List<Room> retrieveAllRooms() {
        
        return null;
        
    }
    
    @Override
    public Room retrieveRoomById(Long roomId) {
        
        return null;
        
    }

    @Override
    public Room retrieveRoomByNumber(Integer roomNumber) throws RoomErrorException {
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
    public void updateRoom(Room room) {
        
    } 
    
    @Override
    public void deleteRoom(Room room) {
        
    } 
}
