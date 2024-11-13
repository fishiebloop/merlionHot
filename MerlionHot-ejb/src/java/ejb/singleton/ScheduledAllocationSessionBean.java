/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/J2EE/EJB31/SingletonEjbClass.java to edit this template
 */
package ejb.singleton;

import ejb.stateless.RoomAllocationSessionBeanRemote;
import javax.ejb.EJB;
import javax.ejb.Schedule;
import javax.ejb.Singleton;

/**
 *
 * @author qiuyutong
 */
@Singleton
public class ScheduledAllocationSessionBean implements ScheduledAllocationSessionBeanRemote, ScheduledAllocationSessionBeanLocal {

    @EJB
    private RoomAllocationSessionBeanRemote roomAllocationBean;

    
    @Schedule(hour = "23", minute = "17", second = "0", persistent = false)
    public void allocateRoomsForToday() {
        // Your room allocation logic here
        roomAllocationBean.performRoomAllocations();
        System.out.println("Room allocation process started at 2 am.");
        
    }
}
