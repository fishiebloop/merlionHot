/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package entity;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

/**
 *
 * @author eliseoh
 */
@Entity
@DiscriminatorValue("OnlineGuest")
public class OnlineGuest extends Guest implements Serializable {

    private static final long serialVersionUID = 1L;
    @Column(length = 20, nullable = true)
    private String password;
    
    public OnlineGuest() {
    }

    public OnlineGuest(String password, String name, String email) {
        super(name, email);
        this.password = password;
    }
    
    /*@Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof OnlineGuest)) {
            return false;
        }
        OnlineGuest other = (OnlineGuest) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.OnlineGuest[ id=" + id + " ]";
    }*/

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
    
}
