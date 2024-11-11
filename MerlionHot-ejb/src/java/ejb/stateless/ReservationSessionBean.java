/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/J2EE/EJB30/StatelessEjbClass.java to edit this template
 */
package ejb.stateless;

import entity.Reservation;
import entity.RoomType;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 *
 * @author qiuyutong
 */
@Stateless
public class ReservationSessionBean implements ReservationSessionBeanRemote, ReservationSessionBeanLocal {

    @PersistenceContext(unitName = "MerlionHot-ejbPU")
    private EntityManager em;
    
    @Override
    public Long createReservation(Reservation reservation) {
        em.persist(reservation);
        em.flush();
        return reservation.getReservationId();
    }
    
    @Override
    public void updateReservation(Reservation reservation) {
        em.merge(reservation);
    }
    
    @Override
    public Reservation retrieveReservationById(Long id) {
        Query query = em.createQuery("SELECT r from Reservation r WHERE r.reservationId = :inID");
        query.setParameter("inID", id);
        return (Reservation) query.getSingleResult();

    }
    
}
