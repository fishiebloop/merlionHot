/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/J2EE/EJB30/SessionLocal.java to edit this template
 */
package ejb.stateless;

import entity.Room;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author qiuyutong
 */
@Local
public interface RoomSessionBeanLocal {

    public Long createRoom(Room newRoom);

    public List<Room> retrieveAllRooms();

    public Room retrieveRoomById(Long roomId);

    public Room retrieveRoomByNumber(String roomNumber);

    public void updateRoom(Room room);

    public void deleteRoom(Room room);
    
}
