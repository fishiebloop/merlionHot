package entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.validation.constraints.Digits;

@Entity
public class RoomType implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long roomTypeId;
    @Column(nullable = false, unique = true, length = 100)
    private String roomTypeName;
    @Column(length = 255)
    private String description;
    @Column(length = 30)
    private String bed;
    @Column(nullable = true)
    @Digits(integer = 2, fraction = 0)
    private int capacity;
    @Column(length = 255)
    private String amenities;
    @Column(nullable = false)
    private Boolean isDisabled = false;

    @OneToMany (mappedBy = "roomType", cascade = CascadeType.REMOVE, orphanRemoval = true, fetch = FetchType.EAGER)
    private List<Room> rooms;
    @OneToMany (cascade = CascadeType.REMOVE, orphanRemoval = true, mappedBy = "roomType", fetch = FetchType.EAGER) 
    private List<RoomRate> roomrates;
    @OneToMany (mappedBy = "roomType", fetch = FetchType.EAGER)
    private List<Reservation> reservations;
    @OneToOne
    private RoomType nextHigherRoomType;

    public RoomType getNextHigherRoomType() {
        return nextHigherRoomType;
    }

    public void setNextHigherRoomType(RoomType nextHigherRoomType) {
        this.nextHigherRoomType = nextHigherRoomType;
    }
    
    public RoomType() {
        this.rooms = new ArrayList<>();
        this.roomrates = new ArrayList<>();
        this.reservations = new ArrayList<>();
        this.isDisabled = false;
    }
    
    public RoomType(String roomTypeName) {
        this();
        this.roomTypeName = roomTypeName;
    }
    
    public RoomType(String roomTypeName, RoomType nextHigherRoomType) {
        this();
        this.roomTypeName = roomTypeName;
        this.nextHigherRoomType = nextHigherRoomType;
    }

    public Long getRoomTypeId() {
        return roomTypeId;
    }

    public void setRoomTypeId(Long roomTypeId) {
        this.roomTypeId = roomTypeId;
    }

    public String getRoomTypeName() {
        return roomTypeName;
    }

    public void setRoomTypeName(String roomTypeName) {
        this.roomTypeName = roomTypeName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getBed() {
        return bed;
    }

    public void setBed(String bed) {
        this.bed = bed;
    }

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public String getAmenities() {
        return amenities;
    }

    public void setAmenities(String amenities) {
        this.amenities = amenities;
    }

    public List<Room> getRooms() {
        return rooms;
    }

    public void setRooms(List<Room> rooms) {
        this.rooms = rooms;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (roomTypeId != null ? roomTypeId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof RoomType)) {
            return false;
        }
        RoomType other = (RoomType) object;
        if ((this.roomTypeId == null && other.roomTypeId != null) || (this.roomTypeId != null && !this.roomTypeId.equals(other.roomTypeId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.RoomType[ roomTypeId=" + roomTypeId + " ]";
    }

    /**
     * @return the roomrates
     */
    public List<RoomRate> getRoomrates() {
        return roomrates;
    }

    /**
     * @param roomrates the roomrates to set
     */
    public void setRoomrates(List<RoomRate> roomrates) {
        this.roomrates = roomrates;
    }

    /**
     * @return the reservations
     */
    public List<Reservation> getReservations() {
        return reservations;
    }

    /**
     * @param reservations the reservations to set
     */
    public void setReservations(List<Reservation> reservations) {
        this.reservations = reservations;
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

}
