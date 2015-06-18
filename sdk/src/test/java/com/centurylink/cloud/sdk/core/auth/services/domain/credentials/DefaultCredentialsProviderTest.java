package com.centurylink.cloud.sdk.core.auth.services.domain.credentials;

import com.centurylink.cloud.sdk.core.exceptions.ClcException;
import org.testng.annotations.Test;

/**
 * @author Aliaksandr Krasitski
 */
public class DefaultCredentialsProviderTest {

    private String propertiesPath = "props.properties";

    private String userName = "user";
    private String userPassword = "password";

    private String userNameSysProp = "clc.username";
    private String userPasswordSysProp = "clc.userpass";

    @Test
    public void testDefaultConstructor() {
        new DefaultCredentialsProvider();
    }

    @Test(expectedExceptions = ClcException.class)
    public void testConstructorWithNullUserParams() {
        new DefaultCredentialsProvider(null, null);
    }

    @Test
    public void testConstructorWithUserParams() {
        System.setProperty(userNameSysProp, userName);
        System.setProperty(userPasswordSysProp, userPassword);

        CredentialsProvider provider = new DefaultCredentialsProvider(userNameSysProp, userPasswordSysProp);
        Credentials credentials = provider.getCredentials();

        assert userName.equals(credentials.getUsername());
        assert userPassword.equals(credentials.getPassword());

        System.clearProperty(userNameSysProp);
        System.clearProperty(userPasswordSysProp);
    }

    @Test
    public void testConstructorWithNullPath() {
        new DefaultCredentialsProvider(null);
    }

    @Test
    public void testConstructorWithPath() {
        new DefaultCredentialsProvider(propertiesPath);
    }
}
