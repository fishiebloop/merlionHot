/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/J2EE/EJB30/StatelessEjbClass.java to edit this template
 */
package ejb.stateless;

import entity.Guest;
import entity.OnlineGuest;
import entity.Reservation;
import java.util.List;
import java.util.Set;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import util.exception.BeanValidationError;
import util.exception.GuestErrorException;

/**
 *
 * @author eliseoh
 */
@Stateless
public class GuestSessionBean implements GuestSessionBeanRemote, GuestSessionBeanLocal {

    @PersistenceContext(unitName = "MerlionHot-ejbPU")
    private EntityManager em;

    @Override
    public Guest guestAuth(String email, String password) throws GuestErrorException {
        try {
            OnlineGuest g = (OnlineGuest) em.createQuery("SELECT g FROM Guest g WHERE g.email = :e").setParameter("e", email).getSingleResult();
            if (!g.getPassword().equals(password)) {
                throw new GuestErrorException("Wrong password entered!");
            }
            return g;
        } catch (Exception ex) {
            throw new GuestErrorException("Wrong email entered!");
        }
    }

    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")
    @Override
    public Boolean validateGuest(Guest g) throws BeanValidationError {
        // Initialize ValidatorFactory and Validator
        ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory();
        Validator validator = validatorFactory.getValidator();

        // Validate the Guest object and store violations
        Set<ConstraintViolation<Guest>> violations = validator.validate(g);

        if (!violations.isEmpty()) {
            StringBuilder errorMessage = new StringBuilder("Validation Errors:\n");
            for (ConstraintViolation<Guest> violation : violations) {
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

    @Override
    public Guest createGuest(Guest g) {
        em.persist(g);
        return g;
    }

    @Override
    public List<Reservation> retrieveAllReservations(Long guestId) throws GuestErrorException {
        try {
            Guest g = em.find(Guest.class, guestId);
            g.getReservation().size();
            return g.getReservation();
        } catch (Exception ex) {
            throw new GuestErrorException("Unable to find Guest!");
        }
    }

    @Override
    public Guest retrieveGuestByEmail(String guestEmail) {
        try {
            Query query = em.createQuery("SELECT g FROM Guest g WHERE g.email = :inEmail");
            query.setParameter("inEmail", guestEmail);

            return (Guest) query.getSingleResult();
        } catch (NoResultException e) {
            return null;  // Return null if no guest with that email is found
        }
    }

    @Override
    public void updateGuest(Guest guest) {
        em.merge(guest);
    }

    @Override
    public void deleteGuest(Guest guest) {
        guest = em.merge(guest);
        em.remove(guest);
    }

}
