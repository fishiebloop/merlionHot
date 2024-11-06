/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/J2EE/EJB30/SessionRemote.java to edit this template
 */
package ejb.stateless;

import entity.RoomType;
import java.util.List;
import javax.ejb.Remote;
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
}
