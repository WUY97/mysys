package com.tongtong.oms.admin.api;

import com.tongtong.common.config.AppConfig;
import com.tongtong.common.config.ServiceID;
import com.tongtong.oms.admin.entity.Address;
import com.tongtong.oms.admin.entity.Contact;
import com.tongtong.oms.admin.entity.OperationStatus;
import com.tongtong.oms.admin.entity.UserProfile;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class UserProfileApiClient extends ApiClient {

    @Override
    protected ServiceID getServiceID() {
        return ServiceID.UserProfileSvc;
    }

    @Override
    protected ServiceID getEndpointServiceID() {
        return ServiceID.AuthSvc;
    }

    @Override
    protected String getServiceURI() {
        return AppConfig.USERS_PROFILE_RESOURCE_PATH;
    }

    @Override
    protected Object createObject(String id) {
        return createUserProfile(id);
    }

    @Override
    protected String getObjectId(int index) {
        return getUserId(index);
    }

    public OperationStatus insertBootstrapData() {
        String users[] = {"admin", "john"};
        OperationStatus operationStatus = null;
        for (String userId : users) {
            operationStatus = insertData(userId, createUserProfile(userId));
            if (!operationStatus.isSuccess()) {
                return operationStatus;
            }
        }
        operationStatus.setMessage("Inserted User profile bootstrap data");
        return operationStatus;
    }

    private Contact createContact(String id) {
        Contact contact = new Contact();
        contact.setName(id.substring(0, 1).toUpperCase() + id.substring(1));
        contact.setEmail(id + "@test.com");
        contact.setTelephone("0001234567");
        return contact;
    }

    private Address createAddress(String id) {
        Address address = new Address();
        address.setStreet(id + ", 12th cross, 2nd main");
        address.setArea("Indira Nagar");
        address.setCity("Bangalore");
        address.setState("KA");
        address.setCountry("IN");
        address.setContact(createContact(id));
        return address;
    }

    private UserProfile createUserProfile(String id) {
        UserProfile userProfile = new UserProfile();
        userProfile.setId(id);
        userProfile.setName(id.substring(0, 1).toUpperCase() + id.substring(1));
        userProfile.setEmail(id + "@test.com");
        userProfile.setContact(createContact(id));
        userProfile.setAddress(createAddress("1"));
        List<Address> shipAddresses = new ArrayList<>();
        shipAddresses.add(createAddress("2"));
        shipAddresses.add(createAddress("3"));
        userProfile.setShippingAddresses(shipAddresses);
        return userProfile;
    }

}
