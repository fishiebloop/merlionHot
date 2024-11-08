/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/J2EE/EJB30/SessionLocal.java to edit this template
 */
package ejb.stateless;

import entity.RoomRate;
import entity.RoomType;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author qiuyutong
 */
@Local
public interface RoomRateSessionBeanLocal {

    public Long createRoomRate(RoomRate newRoomRate);

    public List<RoomRate> retrieveAllRoomRates();

    public RoomRate retrieveRoomRateById(Long roomRateId);

    public void updateRoomRate(RoomRate roomRate);

    public void deleteRoomRate(RoomRate roomRate);

    public RoomRate retrieveRoomRateByName(String roomRateName);
    
}
