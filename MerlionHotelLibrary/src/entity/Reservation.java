/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.Digits;
import javax.validation.constraints.Future;

/**
 *
 * @author eliseoh
 */
@Entity
public class Reservation implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long reservationId;
    @Temporal(TemporalType.TIMESTAMP)
    @Column(nullable = false)
    private Date checkInDate;
    @Temporal(TemporalType.TIMESTAMP)
    @Column(nullable = false)
    private Date checkOutDate;
    @Temporal(TemporalType.TIMESTAMP)
    @Column(nullable = false)
    private Date reserveDate;
    @Column(nullable = false)
    private Boolean isDisabled = false;
    
    @ManyToOne (optional = false)
    @JoinColumn(nullable = false)
    private Guest guest;
    @OneToOne(mappedBy = "reserveId")
    private RoomAllocation roomAllocation;
    @ManyToOne
    private Partner partner;
   @ManyToOne (fetch = FetchType.EAGER, optional = false)
    private RoomType roomType;
    private Boolean isPartnerReservation = false;
    private Boolean isWalkIn = false;

    public Boolean getIsWalkIn() {
        return isWalkIn;
    }

    public void setIsWalkIn(Boolean isWalkIn) {
        this.isWalkIn = isWalkIn;
    }

    public Boolean getIsPartnerReservation() {
        return isPartnerReservation;
    }

    public void setIsPartnerReservation(Boolean isPartnerReservation) {
        this.isPartnerReservation = isPartnerReservation;
    }
    
    public Reservation() {
       this.reserveDate = new Date();
       this.isDisabled = false;
    }

    public Reservation(Date checkInDate, Date checkOutDate) {
        this();
        this.checkInDate = checkInDate;
        this.checkOutDate = checkOutDate;
    }
    /**
     * @return the reserveDate
     */
    public Date getReserveDate() {
        return reserveDate;
    }

    /**
     * @param reserveDate the reserveDate to set
     */
    public void setReserveDate(Date reserveDate) {
        this.reserveDate = reserveDate;
    }
    
    /**
     * @return the partner
     */
    public Partner getPartner() {
        return partner;
    }

    /**
     * @param partner the partner to set
     */
    public void setPartner(Partner partner) {
        this.partner = partner;
    }
    
    public RoomAllocation getRoomAllocation() {
        return roomAllocation;
    }

    public void setRoomAllocation(RoomAllocation roomAllocation) {
        this.roomAllocation = roomAllocation;
    }

    public RoomType getRoomType() {
        return roomType;
    }

    public void setRoomType(RoomType roomType) {
        this.roomType = roomType;
    }
   

    
    public Long getReservationId() {
        return reservationId;
    }

    public void setReservationId(Long reservationId) {
        this.reservationId = reservationId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (reservationId != null ? reservationId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the reservationId fields are not set
        if (!(object instanceof Reservation)) {
            return false;
        }
        Reservation other = (Reservation) object;
        if ((this.reservationId == null && other.reservationId != null) || (this.reservationId != null && !this.reservationId.equals(other.reservationId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.Reservation[ id=" + reservationId + " ]";
    }

    /**
     * @return the checkInDate
     */
    public Date getCheckInDate() {
        return checkInDate;
    }

    /**
     * @param checkInDate the checkInDate to set
     */
    public void setCheckInDate(Date checkInDate) {
        this.checkInDate = checkInDate;
    }

    /**
     * @return the checkOutDate
     */
    public Date getCheckOutDate() {
        return checkOutDate;
    }

    /**
     * @param checkOutDate the checkOutDate to set
     */
    public void setCheckOutDate(Date checkOutDate) {
        this.checkOutDate = checkOutDate;
    }

    /**
     * @return the guest
     */
    public Guest getGuest() {
        return guest;
    }

    /**
     * @param guest the guest to set
     */
    public void setGuest(Guest guest) {
        this.guest = guest;
    }

    /**
     * @return the isDisabled
     */
    public Boolean getIsDisabled() {
        return isDisabled;
    }

    /**
     * @param isDisabled the isDisabled to set
     */
    public void setIsDisabled(Boolean isDisabled) {
        this.isDisabled = isDisabled;
    }

    public Partner getPartner(Object object) {
        return partner;
    }
    
}
