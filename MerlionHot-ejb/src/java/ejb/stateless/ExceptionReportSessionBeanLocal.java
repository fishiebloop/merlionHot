/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/J2EE/EJB30/SessionLocal.java to edit this template
 */
package ejb.stateless;

import entity.ExceptionReport;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author qiuyutong
 */
@Local
public interface ExceptionReportSessionBeanLocal {

    List<ExceptionReport> retrieveHigherAvailList();

    List<ExceptionReport> retrieveNoUpgradeList();
    
}
