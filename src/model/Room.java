package model;

public class Room implements IRoom{

    protected String roomNumber;
    protected Double roomPrice;
    protected RoomType roomType;

    public Room (String roomNumber, Double roomPrice, RoomType roomType) {
        super();
        this.roomNumber = roomNumber;
        this.roomPrice = roomPrice;
        this.roomType = roomType;
    }

    @Override
    public String getRoomNumber() {
        return roomNumber;
    }

    @Override
    public Double getRoomPrice() {
        return roomPrice;
    }

    @Override
    public RoomType getRoomType() {
        return roomType;
    }

    @Override
    public boolean isFree() {
        return (roomPrice == 0.0);
    }

    public String roomNumberAndType() {
        return "Room number: " + roomNumber + ", Room type: " + roomType;
    }
    @Override
    public String toString() {
        return roomNumberAndType() + ", Room price: " + roomPrice;
    }

    @Override
    /*
      Returns whether the argument object represents the same Room as this.
      Reference: https://www.geeksforgeeks.org/equals-hashcode-methods-java/

      @param obj The object to compare with
      @return A boolean indicating if this and obj represent the same Room.
     */
    public boolean equals(Object obj) {
        if (this == obj) { return true; }
        if (obj == null || obj.getClass() != this.getClass()) { return false; }
        Room room = (Room) obj;
        return (room.roomNumber.equals(this.roomNumber));
    }
}
