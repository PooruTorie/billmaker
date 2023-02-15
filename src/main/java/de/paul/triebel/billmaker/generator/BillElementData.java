package de.paul.triebel.billmaker.generator;

public class BillElementData {

    private final String title;
    private final String description;
    private final float price;
    private final int count;

    public BillElementData(String title, String description, float price, int count) {
        this.title = title;
        this.description = description;
        this.price = price;
        this.count = count;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public float getPrice() {
        return price;
    }

    public int getCount() {
        return count;
    }

    public float getSum() {
        return price * count;
    }
}
