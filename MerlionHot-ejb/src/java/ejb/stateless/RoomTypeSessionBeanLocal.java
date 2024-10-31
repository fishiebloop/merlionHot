/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/J2EE/EJB30/SessionLocal.java to edit this template
 */
package ejb.stateless;

import entity.RoomType;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author qiuyutong
 */
@Local
public interface RoomTypeSessionBeanLocal {

    public Long createRoomType(RoomType newRoomType);

    public List<RoomType> retrieveAllRoomTypes();

    public RoomType retrieveRoomTypeById(Long roomTypeId);

    public RoomType retrieveRoomTypeByName(String roomTypeName);

    public void updateRoomType(RoomType roomType);

    public void deleteRoomType(RoomType roomType);
    
}