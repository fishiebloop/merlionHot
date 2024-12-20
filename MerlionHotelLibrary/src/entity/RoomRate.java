package entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.Enumeration;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import com.merlionhotel.utils.DateUtil;
import java.time.LocalDate;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.validation.constraints.Digits;
import util.enumeration.RateTypeEnum;

@Entity
public class RoomRate implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long roomRateId;
    @Column(nullable = false, length = 64, unique = true)
    private String name;
    @Enumerated(EnumType.STRING)
    private RateTypeEnum rateType;
    @Column(nullable = false, scale=2)
    @Digits(integer = 5,fraction = 2)
    private BigDecimal ratePerNight;
    @Temporal(TemporalType.TIMESTAMP)
    @Column
    private Date startDate;
    @Temporal(TemporalType.TIMESTAMP)
    @Column
    private Date endDate;
    @ManyToOne (optional = false)
    @JoinColumn(nullable = false)
    private RoomType roomType;
    @Column(nullable = false)
    private Boolean isDisabled = false;

    public Boolean getIsDisabled() {
        return isDisabled;
    }

    public RoomRate() {
    }

    
    public RoomRate(String name, RateTypeEnum rateType, BigDecimal ratePerNight, RoomType roomType) {
        this.name = name;
        this.rateType = rateType;
        this.ratePerNight = ratePerNight;
        this.roomType = roomType;
    }
    
    public void setIsDisabled(Boolean isDisabled) {
        this.isDisabled = isDisabled;
    }

    public RoomType getRoomType() {
        return roomType;
    }

    public void setRoomType(RoomType roomType) {
        this.roomType = roomType;
    }

    public Long getRoomRateId() {
        return roomRateId;
    }

    public void setRoomRateId(Long roomRateId) {
        this.roomRateId = roomRateId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public RateTypeEnum getRateType() {
        return rateType;
    }

    public void setRateType(RateTypeEnum rateType) {
        this.rateType = rateType;
    }

    public BigDecimal getRatePerNight() {
        return ratePerNight;
    }

    public void setRatePerNight(BigDecimal ratePerNight) {
        this.ratePerNight = ratePerNight;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += ( roomRateId != null ? roomRateId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof RoomRate)) {
            return false;
        }
        RoomRate other = (RoomRate) object;
        if ((this.roomRateId == null && other.roomRateId != null) || (this.roomRateId != null && !this.roomRateId.equals(other.roomRateId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.RoomRate[ roomRateId=" + roomRateId + " ]";
    }
    
}
