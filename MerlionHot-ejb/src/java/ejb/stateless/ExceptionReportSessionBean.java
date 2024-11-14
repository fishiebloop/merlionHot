package ejb.stateless;

import entity.ExceptionReport;
import entity.Reservation;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TemporalType;
import util.enumeration.ExceptionTypeEnum;

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
        try {
            Query query = em.createQuery("SELECT er FROM ExceptionReport er WHERE er.reservation = :inReserve");
            query.setParameter("inReserve", r);
            return (ExceptionReport) query.getSingleResult();
        } catch (NoResultException ex) {
            return null;
        }
    }

    @Override
    public List<ExceptionReport> retrieveHigherAvailList() {
        // Get the current date and time
        Date now = new Date();

        // Calculate today's 12 am (start of the day)
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(now);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        Date todayStart = calendar.getTime();

        // Retrieve reports created between 12 am today and now
        return em.createQuery(
                "SELECT e FROM ExceptionReport e WHERE e.timestamp BETWEEN :todayStart AND :now "
                + "AND e.exceptionType = :exceptionType",
                ExceptionReport.class)
                .setParameter("todayStart", todayStart, TemporalType.TIMESTAMP)
                .setParameter("now", now, TemporalType.TIMESTAMP)
                .setParameter("exceptionType", ExceptionTypeEnum.HIGHERAVAIL)
                .getResultList();
    }

    @Override
    public List<ExceptionReport> retrieveNoUpgradeList() {
        // Get the current date and time
        Date now = new Date();

        // Calculate today's 12 am (start of the day)
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(now);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        Date todayStart = calendar.getTime();

        // Retrieve reports created between 12 am today and now
        return em.createQuery(
                "SELECT e FROM ExceptionReport e WHERE e.timestamp BETWEEN :todayStart AND :now "
                + "AND e.exceptionType = :exceptionType",
                ExceptionReport.class)
                .setParameter("todayStart", todayStart, TemporalType.TIMESTAMP)
                .setParameter("now", now, TemporalType.TIMESTAMP)
                .setParameter("exceptionType", ExceptionTypeEnum.HIGHERAVAIL)
                .getResultList();
    }

}
