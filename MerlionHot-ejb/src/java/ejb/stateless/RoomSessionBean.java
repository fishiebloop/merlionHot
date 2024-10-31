/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/J2EE/EJB30/StatelessEjbClass.java to edit this template
 */
package ejb.stateless;

import entity.Room;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author qiuyutong
 */
@Stateless
public class RoomSessionBean implements RoomSessionBeanRemote, RoomSessionBeanLocal {

    @PersistenceContext(unitName = "MerlionHot-ejbPU")
    private EntityManager em;

    @Override
    public Long createRoom(Room newRoom) {
        em.persist(newRoom);
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
    public Room retrieveRoomByNumber(String roomNumber) {
        
        return null;
        
    }
    
    @Override
    public void updateRoom(Room room) {
        
    } 
    
    @Override
    public void deleteRoom(Room room) {
        
    } 
}
