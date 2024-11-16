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
import entity.Guest;
import entity.Partner;
import java.util.Date;
import java.util.List;
import javax.annotation.Resource;
import javax.ejb.EJB;
import javax.ejb.EJBContext;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.persistence.NoResultException;
import util.exception.CannotUpgradeException;
import util.exception.NoAvailableRoomException;
import util.exception.ReservationErrorException;

/**
 *
 * @author qiuyutong
 */
@Stateless
public class ReservationSessionBean implements ReservationSessionBeanRemote, ReservationSessionBeanLocal {

    @EJB
    private RoomAllocationSessionBeanLocal roomAllocationSessionBean;

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
    public Reservation retrieveReservationById(Long id) throws ReservationErrorException {
        try {
            Query query = em.createQuery("SELECT r from Reservation r WHERE r.reservationId = :inID");
            query.setParameter("inID", id);
            return (Reservation) query.getSingleResult();
        } catch (NoResultException e) {
            throw new ReservationErrorException("Reservation with ID " + id + " not found.");
        }
    }
    
    @Override
    public Reservation createReservation(RoomType type, Guest guest, Partner partner, Date in, Date out) {
        Reservation r = new Reservation();
        r.setRoomType(type);
        type.getReservations().add(r);
        r.setGuest(guest);
        guest.getReservation().add(r);
        r.setPartner(partner);
        partner.getReservations().add(r);
        r.setCheckInDate(in);
        r.setCheckOutDate(out);
        r.setIsPartnerReservation(true);
        em.persist(r);
        em.merge(guest);
        em.merge(partner);
        em.merge(type);
        em.flush();
        return r;
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

    @Override
    public Reservation createSameDayReservation(Reservation newR) throws NoAvailableRoomException, CannotUpgradeException {
        Long newRId = createReservation(newR);
        newR = em.find(Reservation.class, newRId);
        roomAllocationSessionBean.createAllocation(newR);
        return newR;
    }

    @Override
    public Reservation createSameDayReservation(RoomType type, Guest guest, Partner partner, Date in, Date out) throws NoAvailableRoomException, CannotUpgradeException {
        Reservation r = createReservation(type, guest, partner, in, out);
        roomAllocationSessionBean.createAllocation(r);
        return r;
    }

    @Override
    public Reservation detachReservation(Reservation res) {
        em.detach(res);
        return res;
    }

}
