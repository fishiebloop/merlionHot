package ejb.stateless;

import entity.RoomType;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Stateless
public class RoomTypeSessionBean implements RoomTypeSessionBeanRemote, RoomTypeSessionBeanLocal {

    @PersistenceContext(unitName = "MerlionHot-ejbPU")
    private EntityManager em;

    @Override
    public Long createRoomType(RoomType newRoomType) {
        em.persist(newRoomType);
        em.flush();
        return newRoomType.getRoomTypeId();    
    }

    @Override
    public List<RoomType> retrieveAllRoomTypes() {
        
        return null;
        
    }
    
    @Override
    public RoomType retrieveRoomTypeById(Long roomTypeId) {
        
        return null;
        
    }

    @Override
    public RoomType retrieveRoomTypeByName(String roomTypeName) {
        
        return null;
        
    }
    
    @Override
    public void updateRoomType(RoomType roomType) {
        
    } 
    
    @Override
    public void deleteRoomType(RoomType roomType) {
        
    } 
}
