package MatchingEngine.Trading;

public class Order {

    private final long id;
    private long quantity;
    private Side side;
    private long price;
    private final long ownerid;

    public Order(long id, Side side, long quantity, long price, long ownerid) {
        this.id = id;
        this.side = side;
        this.quantity = quantity;
        this.price = price;
        this.ownerid = ownerid;
    }

    // can the current order be traded against the macthPrice?
    public boolean acceptsPrice(long matchPrice) { // market orders accept all prices!
        return price == 0 || side.getComparator().compare(matchPrice, price) <= 0;
    }

    public long getId() {
        return id;
    }

    public long getQuantity() {
        return quantity;
    }

    public void setQuantity(long quantity) {
        this.quantity = quantity;
    }

    public Side getSide() {
        return side;
    }

    public void setSide(Side side) {
        this.side = side;
    }

    public long getPrice() {
        return price;
    }

    public void setPrice(long price) {
        this.price = price;
    }

    public long getOwnerid() {
        return ownerid;
    }
}
