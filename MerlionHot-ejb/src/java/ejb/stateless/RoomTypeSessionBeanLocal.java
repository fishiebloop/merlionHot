/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/J2EE/EJB30/SessionLocal.java to edit this template
 */
package ejb.stateless;

import entity.RoomType;
import java.math.BigDecimal;
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

    public RoomType createRoomType(RoomType newRoomType)  throws RoomTypeErrorException;

    public List<RoomType> retrieveAllRoomTypes() throws RoomTypeErrorException;

    public RoomType retrieveRoomTypeById(Long roomTypeId) throws RoomTypeErrorException;

    public RoomType retrieveRoomTypeByName(String roomTypeName) throws RoomTypeErrorException;

    public void updateRoomType(RoomType roomType);

    public String deleteRoomType(RoomType roomType);

    List<RoomType> retrieveAllAvailRoomTypeOnline(Date checkIn, Date checkOut) throws RoomTypeErrorException;

    BigDecimal getPriceOfRoomTypeOnline(Date checkIn, Date checkOut, RoomType rt);
    
}