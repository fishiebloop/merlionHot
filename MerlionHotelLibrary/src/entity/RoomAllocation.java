/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package entity;

import java.io.Serializable;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;

/**
 *
 * @author eliseoh
 */
@Entity
public class RoomAllocation implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long allocationId;
    @OneToOne (optional = false)
    @JoinColumn(nullable = false)
    private Reservation reserveId;
    @ManyToOne(optional = false)
    @JoinColumn(nullable = false)
    private Room room;

    public RoomAllocation() {
    }

    public RoomAllocation(Reservation reserveId, List<Room> rooms) {
        this.reserveId = reserveId;
        this.room = room;
    }
    

    public Long getAllocationId() {
        return allocationId;
    }

    public void setAllocationId(Long allocationId) {
        this.allocationId = allocationId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (allocationId != null ? allocationId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the allocationId fields are not set
        if (!(object instanceof RoomAllocation)) {
            return false;
        }
        RoomAllocation other = (RoomAllocation) object;
        if ((this.allocationId == null && other.allocationId != null) || (this.allocationId != null && !this.allocationId.equals(other.allocationId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.RoomAllocation[ id=" + allocationId + " ]";
    }

    /**
     * @return the reserveId
     */
    public Reservation getReserveId() {
        return reserveId;
    }

    /**
     * @param reserveId the reserveId to set
     */
    public void setReserveId(Reservation reserveId) {
        this.reserveId = reserveId;
    }

    /**
     * @return the rooms
     */
    public Room getRoom() {
        return room;
    }

    /**
     * @param rooms the rooms to set
     */
    public void setRoom(Room room) {
        this.room = room;
    }
    
}
