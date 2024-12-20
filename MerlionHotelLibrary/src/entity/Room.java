/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
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
    @Column(length = 4, nullable = false, unique = true)
    private String roomNumber;
    @Enumerated(EnumType.STRING)
    private RoomStatusEnum status;
    @OneToMany (mappedBy = "room", fetch = FetchType.EAGER)
    private List<RoomAllocation> roomAllocation;
    @ManyToOne(optional = false)
    @JoinColumn(nullable = false, name = "roomTypeId")
    private RoomType roomType;
    @Column(nullable = false)
    private Boolean isDisabled = false;

    public boolean getIsDisabled() {
        return isDisabled;
    }

    public void setIsDisabled(boolean isDisabled) {
        this.isDisabled = isDisabled;
    }
    
    public Room() {
        List<RoomAllocation> rooms = new ArrayList<>();
        this.status = RoomStatusEnum.AVAIL;
    }

    public Room(String roomNumber) {
        this();
        this.roomNumber = roomNumber;
    }

    public Room(String roomNumber, RoomStatusEnum status, RoomType roomType) {
        this.roomNumber = roomNumber;
        this.status = status;
        this.roomType = roomType;
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
    public String getRoomNumber() {
        return roomNumber;
    }

    /**
     * @param roomNumber the roomNumber to set
     */
    public void setRoomNumber(String roomNumber) {
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
    
}
