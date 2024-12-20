/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/J2EE/EJB30/SessionRemote.java to edit this template
 */
package ejb.stateless;

import entity.ExceptionReport;
import entity.Reservation;
import java.util.List;
import javax.ejb.Remote;

/**
 *
 * @author qiuyutong
 */
@Remote
public interface ExceptionReportSessionBeanRemote {

    public void createExceptionReport(ExceptionReport report);

    public ExceptionReport retrieveExceptionByReservation(Reservation r);

    List<ExceptionReport> retrieveHigherAvailList();

    List<ExceptionReport> retrieveNoUpgradeList();
    
}
