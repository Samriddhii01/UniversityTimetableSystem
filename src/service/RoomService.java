package service;
import java.util.*; import model.Room;
public class RoomService {
 private final List<Room> list=new ArrayList<>();
 public boolean addRoom(Room x){if(findRoomById(x.getRoomId())!=null)return false;list.add(x);return true;}
 public Room findRoomById(String id){for(Room x:list)if(x.getRoomId().equalsIgnoreCase(id))return x;return null;}
 public List<Room> getAllRooms(){return new ArrayList<>(list);}
}
