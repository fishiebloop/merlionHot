/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/J2EE/EJB30/SessionLocal.java to edit this template
 */
package ejb.stateless;

import entity.RoomRate;
import entity.RoomType;
import java.util.List;
import javax.ejb.Local;
import util.exception.BeanValidationError;
import util.exception.RoomRateErrorException;

/**
 *
 * @author qiuyutong
 */
@Local
public interface RoomRateSessionBeanLocal {

    public RoomRate createRoomRate(RoomRate newRoomRate);

    public List<RoomRate> retrieveAllRoomRates();

    public RoomRate retrieveRoomRateById(Long roomRateId);

    public void updateRoomRate(RoomRate roomRate);

    public String deleteRoomRate(RoomRate roomRate);

    Boolean validateRoomRate(RoomRate rr) throws BeanValidationError;

    public RoomRate retrieveRoomRateByName(String roomRateName) throws RoomRateErrorException;
    
}
