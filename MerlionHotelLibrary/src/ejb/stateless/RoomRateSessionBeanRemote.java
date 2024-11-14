/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/J2EE/EJB30/SessionRemote.java to edit this template
 */
package ejb.stateless;

import entity.RoomRate;
import entity.RoomType;
import java.util.List;
import javax.ejb.Remote;
import util.exception.RoomRateErrorException;
import util.exception.BeanValidationError;
import util.exception.BeanValidationError;
import util.exception.RoomRateErrorException;

/**
 *
 * @author qiuyutong
 */
@Remote
public interface RoomRateSessionBeanRemote {
    public RoomRate createRoomRate(RoomRate newRoomRate);
    public void updateRoomRate(RoomRate newRoomRate);
    public List<RoomRate> retrieveAllRoomRates();
    public RoomRate retrieveRoomRateByName(String roomRateName) throws RoomRateErrorException;
    public RoomRate retrieveRoomRateById(Long roomRateId);
    public String deleteRoomRate(RoomRate rate);

    Boolean validateRoomRate(RoomRate rr) throws BeanValidationError;
}
