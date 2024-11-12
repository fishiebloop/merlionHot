/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/J2EE/EJB30/SessionRemote.java to edit this template
 */
package ejb.stateless;

import entity.RoomType;
import java.util.Date;
import java.util.List;
import javax.ejb.Remote;
import util.enumeration.RateTypeEnum;
import util.exception.RoomTypeErrorException;

/**
 *
 * @author qiuyutong
 */
@Remote
public interface RoomTypeSessionBeanRemote {
    public Long createRoomType(RoomType newRoomType);
    public RoomType retrieveRoomTypeByName(String roomTypeName) throws RoomTypeErrorException;
    public List<RoomType> retrieveAllRoomTypes() throws RoomTypeErrorException;
    public void updateRoomType(RoomType roomType);
    public void deleteRoomType(RoomType roomType);

    public List<RoomType> retrieveAvailableRoomTypes(Date startDate, Date endDate);

    public boolean roomRateExistsForType(Long roomTypeId, RateTypeEnum rateType);

    public RoomType retrieveRoomTypeById(Long roomTypeId) throws RoomTypeErrorException;
    public RoomType retrieveRoomTypeById(Long roomTypeId)
  
  //modify to be same as above 
    List<RoomType> retrieveAllAvailRoomType(Date checkIn, Date checkOut, Integer guests) throws RoomTypeErrorException;

}
