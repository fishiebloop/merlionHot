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
    @Future
    private Date checkInDate;
    @Temporal(TemporalType.TIMESTAMP)
    @Column(nullable = false)
    @Future
    private Date checkOutDate;
    @Column(length = 20, nullable = false)
    private Integer guestNo;
    
    
    @ManyToOne (optional = false)
    @JoinColumn(nullable = false)
    private Guest guest;
    @OneToMany(mappedBy = "reserveId")
    private List<RoomAllocation> roomAllocation;
    @OneToOne
    private RoomType roomType;

    public Reservation() {
        this.roomAllocation = new ArrayList<>();
    }

    public Reservation(Date checkInDate, Date checkOutDate, Integer guestNo) {
        this();
        this.checkInDate = checkInDate;
        this.checkOutDate = checkOutDate;
        this.guestNo = guestNo;
    }
    
    
    /**
     * @return the roomAllocation
     */
    public List<RoomAllocation> getRoomAllocation() {
        return roomAllocation;
    }

    /**
     * @param roomAllocation the roomAllocation to set
     */
    public void setRoomAllocation(List<RoomAllocation> roomAllocation) {
        this.roomAllocation = roomAllocation;
    }

    /**
     * @return the roomType
     */
    public RoomType getRoomType() {
        return roomType;
    }

    /**
     * @param roomType the roomType to set
     */
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
     * @return the guestNo
     */
    public Integer getGuestNo() {
        return guestNo;
    }

    /**
     * @param guestNo the guestNo to set
     */
    public void setGuestNo(Integer guestNo) {
        this.guestNo = guestNo;
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
    
}
