/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package entity;

import java.io.Serializable;
import java.util.List;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

/**
 *
 * @author eliseoh
 */
@Entity
public class Guest implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long guestId;
    private String name;
    private String email;
    @OneToMany(mappedBy="guest", orphanRemoval = true)
    private List<Reservation> reservation;
    

    public Guest() {
    }

    public Guest(Long guestId, String name, String email, List<Reservation> reservation) {
        this.guestId = guestId;
        this.name = name;
        this.email = email;
        this.reservation = reservation;
    }
    
    
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (guestId != null ? guestId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Guest)) {
            return false;
        }
        Guest other = (Guest) object;
        if ((this.guestId == null && other.guestId != null) || (this.guestId != null && !this.guestId.equals(other.guestId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.Guest[ id=" + guestId + " ]";
    }

    /**
     * @return the guestId
     */
    public Long getGuestId() {
        return guestId;
    }

    /**
     * @param guestId the guestId to set
     */
    public void setGuestId(Long guestId) {
        this.guestId = guestId;
    }

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return the email
     */
    public String getEmail() {
        return email;
    }

    /**
     * @param email the email to set
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * @return the reservation
     */
    public List<Reservation> getReservation() {
        return reservation;
    }

    /**
     * @param reservation the reservation to set
     */
    public void setReservation(List<Reservation> reservation) {
        this.reservation = reservation;
    }
    
}
