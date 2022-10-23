package model;

public class FreeRoom extends Room{
    public FreeRoom() {
        super();
        this.roomPrice = 0.0;
    }

    @Override
    public String toString() {
        return super.roomNumberAndType() + ", Free";
    }
}
