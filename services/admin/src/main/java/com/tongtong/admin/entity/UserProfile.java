package com.tongtong.admin.entity;

import java.util.Arrays;
import java.util.List;

public class UserProfile {
    private String id;
    private String name;
    private String email;

    private Contact contact;
    private Address address;
    private List<Address> shippingAddresses;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Contact getContact() {
        return contact;
    }

    public void setContact(Contact contact) {
        this.contact = contact;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public List<Address> getShippingAddresses() {
        return shippingAddresses;
    }

    public void setShippingAddresses(List<Address> shippingAddresses) {
        this.shippingAddresses = shippingAddresses;
    }

    @Override
    public String toString() {
        return "{" +
                "\"id\":" + (id == null ? "null" : "\"" + id + "\"") + ", " +
                "\"name\":" + (name == null ? "null" : "\"" + name + "\"") + ", " +
                "\"email\":" + (email == null ? "null" : "\"" + email + "\"") + ", " +
                "\"contact\":" + (contact == null ? "null" : contact) + ", " +
                "\"address\":" + (address == null ? "null" : address) + ", " +
                "\"shippingAddresses\":" + (shippingAddresses == null ? "null" : Arrays.toString(shippingAddresses.toArray())) +
                "}";
    }
}
