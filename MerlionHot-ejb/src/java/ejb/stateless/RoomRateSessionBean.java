/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/J2EE/EJB30/StatelessEjbClass.java to edit this template
 */
package ejb.stateless;

import entity.RoomRate;
import entity.RoomType;
import java.util.List;
import java.util.Set;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import util.enumeration.RateTypeEnum;
import util.enumeration.RoomStatusEnum;
import util.exception.RoomTypeErrorException;
import util.exception.BeanValidationError;
import util.exception.RoomRateErrorException;

/**
 *
 * @author qiuyutong
 */
@Stateless
public class RoomRateSessionBean implements RoomRateSessionBeanRemote, RoomRateSessionBeanLocal {

    @PersistenceContext(unitName = "MerlionHot-ejbPU")
    private EntityManager em;

    //throw room rate bean validation
    @Override
    public RoomRate createRoomRate(RoomRate newRoomRate) {
        em.persist(newRoomRate);
        em.flush();
        return newRoomRate;
    }

    @Override
    public List<RoomRate> retrieveAllRoomRates() {
        Query query = em.createQuery("SELECT rr from RoomRate rr");
        return query.getResultList();
    }

    @Override
    public RoomRate retrieveRoomRateById(Long roomRateId) {
        Query query = em.createQuery("SELECT rr from RoomRate rr WHERE rr.roomRateId = :inID");
        query.setParameter("inID", roomRateId);
        return (RoomRate) query.getSingleResult();

    }

    @Override
    public RoomRate retrieveRoomRateByName(String roomRateName) throws RoomRateErrorException {
        try {
            Query query = em.createQuery("SELECT rr from RoomRate rr WHERE rr.name = :inName");
            query.setParameter("inName", roomRateName);
            return (RoomRate) query.getSingleResult();
        } catch (NoResultException | NonUniqueResultException ex) {
            throw new RoomRateErrorException("Cannot find room rate from name!");
        }
    }

    @Override
    public void updateRoomRate(RoomRate roomRate) {
        em.merge(roomRate);
    }

    @Override
    public String deleteRoomRate(RoomRate rate) {
        // Retrieve the managed RoomRate instance
        RoomRate managedRate = em.find(RoomRate.class, rate.getRoomRateId());

        if (managedRate == null) {
            return "Room rate not found";
        }

        // Determine the rate type
        RateTypeEnum rateType = managedRate.getRateType();

        // Construct the base query for active reservations based on rate type
        String reservationQueryStr = "SELECT COUNT(r) FROM Reservation r "
                + "WHERE r.roomType = :roomType "
                + "AND r.isWalkIn = :isWalkIn " // Check if reservation matches the type associated with the rate
                + "AND (r.checkOutDate > CURRENT_DATE "
                + "OR (r.checkOutDate = CURRENT_DATE AND r.roomAllocation.room.status = :occupiedStatus))";

        Query query = em.createQuery(reservationQueryStr);
        query.setParameter("roomType", managedRate.getRoomType());
        query.setParameter("occupiedStatus", RoomStatusEnum.OCCUPIED);

        // Determine whether to filter for walk-in or online reservations
        boolean isWalkIn;
        if (rateType == RateTypeEnum.PUBLISHED) {
            isWalkIn = true;  // PUBLISHED rates apply only to walk-in reservations
        } else {
            isWalkIn = false; // Other rates apply only to online reservations
        }
        query.setParameter("isWalkIn", isWalkIn);

        // Check if there are active reservations that would prevent deletion
        Long activeReservationCount = (Long) query.getSingleResult();

        if (activeReservationCount == 0) {
            // No active reservations; delete the room rate
            em.remove(managedRate);
            em.flush();
            return "Room rate deleted successfully.";
        } else {
            // Active reservations exist; disable the room rate instead
            managedRate.setIsDisabled(true);
            em.merge(managedRate);
            em.flush();
            return "Room rate is in use and has been disabled.";
        }
    }

    @Override
    public Boolean validateRoomRate(RoomRate rr) throws BeanValidationError {
        ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory();
        Validator validator = validatorFactory.getValidator();

        // Validate the Guest object and store violations
        Set<ConstraintViolation<RoomRate>> violations = validator.validate(rr);

        if (!violations.isEmpty()) {
            StringBuilder errorMessage = new StringBuilder("Validation Errors:\n");
            for (ConstraintViolation<RoomRate> violation : violations) {
                errorMessage.append("Field: ")
                        .append(violation.getPropertyPath())
                        .append(", Invalid Value: ")
                        .append(violation.getInvalidValue())
                        .append(", Message: ")
                        .append(violation.getMessage())
                        .append("\n");
            }
            throw new BeanValidationError(errorMessage.toString());
        }

        return true;
    }
}
