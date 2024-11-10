/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/J2EE/EJB30/StatelessEjbClass.java to edit this template
 */
package ejb.stateless;

import entity.Guest;
import entity.Reservation;
import entity.RoomType;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import util.exception.ReservationErrorException;

/**
 *
 * @author qiuyutong
 */
@Stateless
public class ReservationSessionBean implements ReservationSessionBeanRemote, ReservationSessionBeanLocal {

    @PersistenceContext(unitName = "MerlionHot-ejbPU")
    private EntityManager em;

    @Override
    public Reservation createReservation(Reservation newR, Guest guest, RoomType rt) {
        guest = em.merge(guest);
        rt = em.merge(rt);
        em.persist(newR);
        newR.setGuest(guest);
        List<Reservation> li = guest.getReservation();
        li.add(newR);
        guest.setReservation(li);
        
        newR.setRoomType(rt);
        li = rt.getReservations();
        li.add(newR);
        rt.setReservations(li);
        em.flush();
        return newR;
    }

    @Override
    public Reservation retrieveReservationByIdForGuest(Long id, Guest g) throws ReservationErrorException {
        try {
            Reservation r = em.find(Reservation.class, id);
            g = em.merge(g);
            if (r.getGuest().getGuestId() != g.getGuestId()) {
                throw new ReservationErrorException("Reservation not under " + g.getName());
            } 
            return r;
        } catch (Exception ex) {
            throw new ReservationErrorException("Cannot find reservation!");
        }
    }
    
    
}
