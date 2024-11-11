/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package horsreservationclient;

import ejb.stateless.GuestSessionBeanRemote;
import ejb.stateless.ReservationSessionBeanRemote;
import ejb.stateless.RoomTypeSessionBeanRemote;
import javax.ejb.EJB;

/**
 *
 * @author eliseoh
 */
public class Main {

    @EJB
    private static ReservationSessionBeanRemote reservationSessionBean;

    @EJB
    private static RoomTypeSessionBeanRemote roomTypeSessionBean;

    @EJB
    private static GuestSessionBeanRemote guestSessionBean;

    

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        MainApp mainApp = new MainApp(guestSessionBean, roomTypeSessionBean, reservationSessionBean);
        mainApp.runApp();
    }
 
}