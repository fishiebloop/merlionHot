/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package entity;

import java.io.Serializable;
import java.util.List;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import util.enumeration.EmployeeEnum;
import util.enumeration.RoomStatusEnum;

/**
 *
 * @author eliseoh
 */
@Entity
public class Room implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long roomId;
    private Integer roomNumber;
    @Enumerated(EnumType.STRING)
    private RoomStatusEnum status;
    @ManyToMany
    @JoinTable(
        name = "room_allocation_rooms", // name of the join table
        joinColumns = @JoinColumn(name = "room_id"), // foreign key for Room
        inverseJoinColumns = @JoinColumn(name = "allocation_id") // foreign key for RoomAllocation
    )
    private List<RoomAllocation> roomAllocation;
    
    public Room() {
    }

    public Room(Long roomId, Integer roomNumber, RoomStatusEnum status) {
        this.roomId = roomId;
        this.roomNumber = roomNumber;
        this.status = status;
    }

    
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (roomId != null ? roomId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Room)) {
            return false;
        }
        Room other = (Room) object;
        if ((this.roomId == null && other.roomId != null) || (this.roomId != null && !this.roomId.equals(other.roomId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.Room[ id=" + roomId + " ]";
    }

    /**
     * @return the roomId
     */
    public Long getRoomId() {
        return roomId;
    }

    /**
     * @param roomId the roomId to set
     */
    public void setRoomId(Long roomId) {
        this.roomId = roomId;
    }

    /**
     * @return the roomNumber
     */
    public Integer getRoomNumber() {
        return roomNumber;
    }

    /**
     * @param roomNumber the roomNumber to set
     */
    public void setRoomNumber(Integer roomNumber) {
        this.roomNumber = roomNumber;
    }

    /**
     * @return the status
     */
    public RoomStatusEnum getStatus() {
        return status;
    }

    /**
     * @param status the status to set
     */
    public void setStatus(RoomStatusEnum status) {
        this.status = status;
    }
    
}
