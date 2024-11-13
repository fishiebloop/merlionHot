/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/J2EE/EJB30/SessionRemote.java to edit this template
 */
package ejb.stateless;

import entity.Reservation;
import entity.Room;
import entity.RoomAllocation;
import entity.RoomType;
import java.util.Date;
import javax.ejb.Remote;
import util.exception.CannotUpgradeException;
import util.exception.NoAvailableRoomException;
import util.exception.RoomAllocationNotFoundException;

/**
 *
 * @author qiuyutong
 */
@Remote
public interface RoomAllocationSessionBeanRemote {

    public Room findAvailableRoom(RoomType type, Date startDate, Date endDate);

    public Long allocateRoom(Reservation reservation, Room room);
    
    public void createRoomAllocationException(Reservation r, Exception exception);
    public void performRoomAllocations();
    public RoomAllocation retrieveAllocationById(Long id) throws RoomAllocationNotFoundException;
    public RoomAllocation retrieveAllocationByReservation(Reservation r) throws RoomAllocationNotFoundException;

    public Long createAllocation(Reservation reservation) throws NoAvailableRoomException, CannotUpgradeException;
    
}
