package com.tongtong.admin.entity;

public class Address {
    private String street;
    private String area;
    private String city;
    private String state;
    private String country;

    private Contact contact;

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public Contact getContact() {
        return contact;
    }

    public void setContact(Contact contact) {
        this.contact = contact;
    }

    @Override
    public String toString() {
        return "{" +
                "\"street\":" + (street == null ? "null" : "\"" + street + "\"") + ", " +
                "\"area\":" + (area == null ? "null" : "\"" + area + "\"") + ", " +
                "\"city\":" + (city == null ? "null" : "\"" + city + "\"") + ", " +
                "\"state\":" + (state == null ? "null" : "\"" + state + "\"") + ", " +
                "\"country\":" + (country == null ? "null" : "\"" + country + "\"") + ", " +
                "\"contact\":" + (contact == null ? "null" : contact) +
                "}";
    }
}
