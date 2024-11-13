/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/WebServices/EjbWebService.java to edit this template
 */
package ejb.session.ws;

import ejb.stateless.GuestSessionBeanLocal;
import ejb.stateless.PartnerSessionBeanLocal;
import ejb.stateless.ReservationSessionBeanLocal;
import ejb.stateless.RoomRateSessionBeanLocal;
import ejb.stateless.RoomSessionBeanLocal;
import ejb.stateless.RoomTypeSessionBeanLocal;
import javax.ejb.EJB;
import javax.jws.WebService;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author eliseoh
 */
@WebService(serviceName = "HolidayWebService")
@Stateless()
public class HolidayWebService {

    @EJB
    private RoomSessionBeanLocal roomSessionBean;

    @EJB
    private RoomRateSessionBeanLocal roomRateSessionBean;

    @EJB
    private ReservationSessionBeanLocal reservationSessionBean;

    @EJB
    private PartnerSessionBeanLocal partnerSessionBean;

    @EJB
    private GuestSessionBeanLocal guestSessionBean;
    @PersistenceContext(unitName = "MerlionHot-ejbPU")
    private EntityManager em;

    @EJB
    private RoomTypeSessionBeanLocal roomTypeSessionBean;
    
    
    /**
     * This is a sample web service operation
     */
    @WebMethod(operationName = "hello")
    public String hello(@WebParam(name = "name") String txt) {
        return "Hello " + txt + " !";
    }
    //need to detach and null bi direction
}
