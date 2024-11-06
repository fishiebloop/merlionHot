/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/J2EE/EJB30/SessionRemote.java to edit this template
 */
package ejb.stateless;

import entity.Room;
import entity.RoomType;
import javax.ejb.Remote;

/**
 *
 * @author qiuyutong
 */
@Remote
public interface RoomSessionBeanRemote {
    public Long createRoom(Room newRoom, RoomType rt);
}
