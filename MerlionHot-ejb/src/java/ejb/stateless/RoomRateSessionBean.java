/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/J2EE/EJB30/StatelessEjbClass.java to edit this template
 */
package ejb.stateless;

import entity.RoomRate;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author qiuyutong
 */
@Stateless
public class RoomRateSessionBean implements RoomRateSessionBeanRemote, RoomRateSessionBeanLocal {

    @PersistenceContext(unitName = "MerlionHot-ejbPU")
    private EntityManager em;

    @Override
    public Long createRoomRate(RoomRate newRoomRate) {
        em.persist(newRoomRate);
        em.flush();
        return newRoomRate.getRoomRateId();
    }

    @Override
    public List<RoomRate> retrieveAllRoomRates() {

        return null;

    }

    @Override
    public RoomRate retrieveRoomRateById(Long roomRateId) {

        return null;

    }

    @Override
    public RoomRate retrieveRoomRateByName(String roomRateName) {

        return null;

    }

    @Override
    public void updateRoomRate(RoomRate roomRate) {

    }

    @Override
    public void deleteRoomRate(RoomRate roomRate) {

    }
}
