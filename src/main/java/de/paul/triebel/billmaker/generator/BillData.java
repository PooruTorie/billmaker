package de.paul.triebel.billmaker.generator;

import java.time.LocalDate;
import java.util.ArrayList;

public class BillData {

    private final ArrayList<BillElementData> billElements = new ArrayList<>();
    private String name;
    private String customerNumber;
    private String billNumber;
    private String taxNumber;
    private String street;
    private String city;
    private String plz;
    private String country;
    private LocalDate date;
    private LocalDate start;
    private LocalDate end;
    private LocalDate billEnd;

    public ArrayList<BillElementData> getBillElements() {
        return billElements;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCustomerNumber() {
        return customerNumber;
    }

    public void setCustomerNumber(String customerNumber) {
        this.customerNumber = customerNumber;
    }

    public String getTaxNumber() {
        return taxNumber;
    }

    public void setTaxNumber(String taxNumber) {
        this.taxNumber = taxNumber;
    }

    public String getBillNumber() {
        return billNumber;
    }

    public void setBillNumber(String billNumber) {
        this.billNumber = billNumber;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public LocalDate getEnd() {
        return end;
    }

    public void setEnd(LocalDate end) {
        this.end = end;
    }

    public LocalDate getBillEnd() {
        return billEnd;
    }

    public void setBillEnd(LocalDate billEnd) {
        this.billEnd = billEnd;
    }

    public LocalDate getStart() {
        return start;
    }

    public void setStart(LocalDate start) {
        this.start = start;
    }

    public String getPlz() {
        return plz;
    }

    public void setPlz(String plz) {
        this.plz = plz;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public float getSum() {
        float sum = 0;
        for (BillElementData elementData : billElements) {
            sum += elementData.getSum();
        }
        return sum;
    }
}
