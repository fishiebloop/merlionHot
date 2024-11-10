/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/J2EE/EJB30/SessionLocal.java to edit this template
 */
package ejb.stateless;

import entity.RoomType;
import java.util.Date;
import java.util.List;
import javax.ejb.Local;
import util.exception.RoomTypeErrorException;

/**
 *
 * @author qiuyutong
 */
@Local
public interface RoomTypeSessionBeanLocal {

    public Long createRoomType(RoomType newRoomType);

    public List<RoomType> retrieveAllRoomTypes() throws RoomTypeErrorException;

    public RoomType retrieveRoomTypeById(Long roomTypeId) throws RoomTypeErrorException;

    public RoomType retrieveRoomTypeByName(String roomTypeName) throws RoomTypeErrorException;

    public void updateRoomType(RoomType roomType);

    public void deleteRoomType(RoomType roomType);

    List<RoomType> retrieveAllAvailRoomType(Date checkIn, Date checkOut, Integer guests) throws RoomTypeErrorException;
    
}