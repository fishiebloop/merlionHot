/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/J2EE/EJB30/SessionRemote.java to edit this template
 */
package ejb.stateless;

import entity.Room;
import entity.RoomType;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import javax.ejb.Remote;
import util.exception.RoomErrorException;

/**
 *
 * @author qiuyutong
 */
@Remote
public interface RoomSessionBeanRemote {

    public Long createRoom(Room newRoom, RoomType rt);

    public Room retrieveRoomByNumber(String roomNumber) throws RoomErrorException;

    public void updateRoomTypeOfRoom(Room room, RoomType newRt);

    public String deleteRoom(Room room);

    public List<Room> retrieveAllRooms() throws RoomErrorException;

    public void updateRoom(Room room);

    public boolean isRoomAvailable(Room room, LocalDate date, Long currentReservationId);
    // public int getAvailableRoomCountByTypeAndDate(RoomType roomType, Date startDate, Date endDate);

    public int getAvailableRoomCountForOnline(RoomType roomType, Date startDate, Date endDate);

    public int getAvailableRoomCountForWalkIn(RoomType roomType, Date startDate, Date endDate);
}
