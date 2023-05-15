package com.tongtong.oms.user.service;

import com.tongtong.oms.user.config.TestConfig;
import com.tongtong.oms.user.dao.UserProfileMockDao;
import com.tongtong.oms.user.entity.UserProfile;
import junit.framework.Assert;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

public class UserProfileServiceImplTest extends TestCase {

    private UserProfileServiceImpl userProfileServiceBean;

    public UserProfileServiceImplTest(String testName )
    {
        super( testName );
    }

    public static Test suite()
    {
        return new TestSuite( UserProfileServiceImplTest.class );
    }

    public void setUp() throws Exception {
        super.setUp();
        userProfileServiceBean = new UserProfileServiceImpl();
        userProfileServiceBean.setUserProfileDaoBean(new UserProfileMockDao());
    }

    public void testDummy() {
        Assert.assertEquals(true, true);
    }

    public void testCreateUserProfile() {
        UserProfile up = TestConfig.createUserProfile(TestConfig.TEST_USER_PROFILE_ID_1);
        boolean success = userProfileServiceBean.saveUserProfile(up);
        Assert.assertEquals(true, success);
    }

    public void testFetchUserProfile() {
        // Fetch userProfile
        UserProfile up = TestConfig.createUserProfile(TestConfig.TEST_USER_PROFILE_ID_1);
        UserProfile userProfile = userProfileServiceBean.getUserProfile(TestConfig.TEST_USER_PROFILE_ID_1);
        Assert.assertNotNull(userProfile);
        Assert.assertEquals(up.getId(), userProfile.getId());
        Assert.assertEquals(up.getShippingAddresses().size(),
                            userProfile.getShippingAddresses().size());
        Assert.assertEquals(up.getShippingAddresses().get(0).getStreet(),
                userProfile.getShippingAddresses().get(0).getStreet());
        Assert.assertEquals(up.getAddress().getContact().getName(),
                userProfile.getAddress().getContact().getName());
    }

    public void testRemoveUserProfile() {
        // Remove userProfile
        boolean success = userProfileServiceBean.removeUserProfile(TestConfig.TEST_USER_PROFILE_ID_1);
        Assert.assertTrue(success);
    }

}
