/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/J2EE/EJB30/StatelessEjbClass.java to edit this template
 */
package ejb.stateless;

import entity.Room;
import entity.RoomType;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
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
    
    public void updateRoom(Room room) {
        em.merge(room);
    }
    
    @Override
    public void deleteRoom(Room room) {
        room = em.merge(room);
        if (room.getRoomAllocation().size() > 0) {
            room.setStatus(RoomStatusEnum.DISABLED);
        } else {
            em.remove(room);
        }
    } 
}
