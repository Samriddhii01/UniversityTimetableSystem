package model;
public class Room {
    private final String roomId, roomName, roomType; private final int capacity;
    public Room(String roomId,String roomName,int capacity,String roomType){
        if(capacity<1) throw new IllegalArgumentException("Capacity must be positive.");
        this.roomId=roomId;this.roomName=roomName;this.capacity=capacity;this.roomType=roomType;
    }
    public String getRoomId(){return roomId;} public String getRoomName(){return roomName;}
    public int getCapacity(){return capacity;} public String getRoomType(){return roomType;}
    public String toString(){return roomId+" - "+roomName+" ("+capacity+")";}
}
