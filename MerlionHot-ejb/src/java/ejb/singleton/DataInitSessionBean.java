/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/J2EE/EJB31/SingletonEjbClass.java to edit this template
 */
package ejb.singleton;

import ejb.stateless.EmployeeSessionBeanLocal;
import ejb.stateless.PartnerSessionBeanLocal;
import ejb.stateless.RoomRateSessionBeanLocal;
import ejb.stateless.RoomSessionBeanLocal;
import ejb.stateless.RoomTypeSessionBeanLocal;
import entity.Employee;
import entity.Partner;
import entity.Room;
import entity.RoomRate;
import entity.RoomType;
import java.math.BigDecimal;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.Singleton;
import javax.ejb.LocalBean;
import javax.ejb.Startup;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import util.enumeration.EmployeeEnum;
import util.enumeration.RateTypeEnum;
import util.enumeration.RoomStatusEnum;
import util.exception.EmployeeErrorException;

/**
 *
 * @author eliseoh
 */
@Singleton
@LocalBean
@Startup
public class DataInitSessionBean {

    @EJB
    private PartnerSessionBeanLocal partnerSessionBean;

    @EJB
    private RoomSessionBeanLocal roomSessionBean;

    @EJB
    private RoomRateSessionBeanLocal roomRateSessionBean;

    @EJB
    private RoomTypeSessionBeanLocal roomTypeSessionBean;

    @PersistenceContext(unitName = "MerlionHot-ejbPU")
    private EntityManager em;

    @EJB
    private EmployeeSessionBeanLocal employeeSessionBeanLocal;

    public DataInitSessionBean() {
    }

    @PostConstruct
    public void postConstruct() {
        try {
            employeeSessionBeanLocal.retrieveEmployeeByUsername("sysadmin");
        } catch (EmployeeErrorException ex) {
            initializeData();
        }
    }

    private void initializeData() {

        employeeSessionBeanLocal.createEmployee(new Employee("sysadmin", "password", EmployeeEnum.SYSADMIN));
        employeeSessionBeanLocal.createEmployee(new Employee("opmanager", "password", EmployeeEnum.OPMANAGER));
        employeeSessionBeanLocal.createEmployee(new Employee("salesmanager", "password", EmployeeEnum.SALESMANAGER));
        employeeSessionBeanLocal.createEmployee(new Employee("guestrelo", "password", EmployeeEnum.GUESTOFF));

        try {
            RoomType grandSuite = roomTypeSessionBean.createRoomType(new RoomType("Grand Suite"));
            RoomType junior = roomTypeSessionBean.createRoomType(new RoomType("Junior Suite", grandSuite));
            RoomType fam = roomTypeSessionBean.createRoomType(new RoomType("Family Room", junior));
            RoomType pre = roomTypeSessionBean.createRoomType(new RoomType("Premier Room", fam));
            RoomType deluxe = roomTypeSessionBean.createRoomType(new RoomType("Deluxe Room", pre));

            roomRateSessionBean.createRoomRate(new RoomRate("Grand Suite Normal", RateTypeEnum.NORMAL, BigDecimal.valueOf(250), grandSuite), grandSuite);
            roomRateSessionBean.createRoomRate(new RoomRate("Grand Suite Published", RateTypeEnum.PUBLISHED, BigDecimal.valueOf(500), grandSuite), grandSuite);
            roomRateSessionBean.createRoomRate(new RoomRate("Junior Suite Normal", RateTypeEnum.NORMAL, BigDecimal.valueOf(200), junior), junior);
            roomRateSessionBean.createRoomRate(new RoomRate("Junior Suite Published", RateTypeEnum.PUBLISHED, BigDecimal.valueOf(400), junior), junior);
            roomRateSessionBean.createRoomRate(new RoomRate("Family Room Normal", RateTypeEnum.NORMAL, BigDecimal.valueOf(150), fam), fam);
            roomRateSessionBean.createRoomRate(new RoomRate("Family Room Published", RateTypeEnum.PUBLISHED, BigDecimal.valueOf(300), fam), fam);
            roomRateSessionBean.createRoomRate(new RoomRate("Premier Room Normal", RateTypeEnum.NORMAL, BigDecimal.valueOf(100), pre), pre);
            roomRateSessionBean.createRoomRate(new RoomRate("Premier Room Published", RateTypeEnum.PUBLISHED, BigDecimal.valueOf(200), pre), pre);
            roomRateSessionBean.createRoomRate(new RoomRate("Deluxe Room Normal", RateTypeEnum.NORMAL, BigDecimal.valueOf(50), deluxe), deluxe);
            roomRateSessionBean.createRoomRate(new RoomRate("Deluxe Room Published", RateTypeEnum.PUBLISHED, BigDecimal.valueOf(100), deluxe), deluxe);

            roomSessionBean.createRoom(new Room("0101", RoomStatusEnum.AVAIL, deluxe), deluxe);
            roomSessionBean.createRoom(new Room("0201", RoomStatusEnum.AVAIL, deluxe), deluxe);
            roomSessionBean.createRoom(new Room("0301", RoomStatusEnum.AVAIL, deluxe), deluxe);
            roomSessionBean.createRoom(new Room("0401", RoomStatusEnum.AVAIL, deluxe), deluxe);
            roomSessionBean.createRoom(new Room("0501", RoomStatusEnum.AVAIL, deluxe), deluxe);

            roomSessionBean.createRoom(new Room("0102", RoomStatusEnum.AVAIL, pre), pre);
            roomSessionBean.createRoom(new Room("0202", RoomStatusEnum.AVAIL, pre), pre);
            roomSessionBean.createRoom(new Room("0302", RoomStatusEnum.AVAIL, pre), pre);
            roomSessionBean.createRoom(new Room("0402", RoomStatusEnum.AVAIL, pre), pre);
            roomSessionBean.createRoom(new Room("0502", RoomStatusEnum.AVAIL, pre), pre);

            roomSessionBean.createRoom(new Room("0103", RoomStatusEnum.AVAIL, fam), fam);
            roomSessionBean.createRoom(new Room("0203", RoomStatusEnum.AVAIL, fam), fam);
            roomSessionBean.createRoom(new Room("0303", RoomStatusEnum.AVAIL, fam), fam);
            roomSessionBean.createRoom(new Room("0403", RoomStatusEnum.AVAIL, fam), fam);
            roomSessionBean.createRoom(new Room("0503", RoomStatusEnum.AVAIL, fam), fam);

            roomSessionBean.createRoom(new Room("0104", RoomStatusEnum.AVAIL, junior), junior);
            roomSessionBean.createRoom(new Room("0204", RoomStatusEnum.AVAIL, junior), junior);
            roomSessionBean.createRoom(new Room("0304", RoomStatusEnum.AVAIL, junior), junior);
            roomSessionBean.createRoom(new Room("0404", RoomStatusEnum.AVAIL, junior), junior);
            roomSessionBean.createRoom(new Room("0504", RoomStatusEnum.AVAIL, junior), junior);

            roomSessionBean.createRoom(new Room("0105", RoomStatusEnum.AVAIL, grandSuite), grandSuite);
            roomSessionBean.createRoom(new Room("0205", RoomStatusEnum.AVAIL, grandSuite), grandSuite);
            roomSessionBean.createRoom(new Room("0305", RoomStatusEnum.AVAIL, grandSuite), grandSuite);
            roomSessionBean.createRoom(new Room("0405", RoomStatusEnum.AVAIL, grandSuite), grandSuite);
            roomSessionBean.createRoom(new Room("0505", RoomStatusEnum.AVAIL, grandSuite), grandSuite);

        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
    }

}
