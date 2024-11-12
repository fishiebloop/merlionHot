package ejb.stateless;

import entity.ExceptionReport;
import entity.Reservation;
import java.util.Date;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

@Stateless
public class ExceptionReportSessionBean implements ExceptionReportSessionBeanLocal, ExceptionReportSessionBeanRemote {

    @PersistenceContext(unitName = "MerlionHot-ejbPU")
    private EntityManager em;

    @Override
    public void createExceptionReport(ExceptionReport report) {
        em.persist(report);
        em.flush();
    }
    
    @Override
    public ExceptionReport retrieveExceptionByReservation(Reservation r) {
        Query query = em.createQuery("SELECT er FROM ExceptionReport er WHERE er.reservation = :inReserve ");
        query.setParameter("inReserve", r);
        return (ExceptionReport) query.getSingleResult();
    }
}
