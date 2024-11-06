/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/J2EE/EJB30/SessionLocal.java to edit this template
 */
package ejb.stateless;

import entity.Room;
import entity.RoomType;
import java.util.List;
import javax.ejb.Local;
import util.exception.RoomErrorException;

/**
 *
 * @author qiuyutong
 */
@Local
public interface RoomSessionBeanLocal {

    public Long createRoom(Room newRoom, RoomType rt);

    public List<Room> retrieveAllRooms() throws RoomErrorException;

    public Room retrieveRoomById(Long roomId);

    public Room retrieveRoomByNumber(Integer roomNumber) throws RoomErrorException;

    public void updateRoomTypeOfRoom(Room room, RoomType newRt);

    public void deleteRoom(Room room);
    
    public void updateRoom(Room room);
    
}
