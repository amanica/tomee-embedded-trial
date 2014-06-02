package com.example;

import java.util.Properties;
import java.util.ResourceBundle;

import javax.ejb.embeddable.EJBContainer;

/**
 * Unfortunately this requires the built war file content to exist
 * already which may only work with failsafe integration tests
 * that run after install.
 */
public class ATomeeIT extends AOpenEjbTest {

    protected void setProperties(Properties properties) {
        super.setProperties(properties);
        properties.setProperty(EJBContainer.PROVIDER, "tomee-embedded");

        String webappDir =
            ResourceBundle.getBundle("maven").getString("webappDir");
        System.out.println("webappDir=" + webappDir);
        properties.setProperty("javax.ejb.embeddable.modules",
            webappDir);

        // httpejbd.port seems to be ignored by tomee..
        properties.setProperty("tomee.ejbcontainer.http.port", "" + webPort);
    }

    public int getHttpPort() {
        return webPort;
    }

    public String getBaseUrl() {
        return "http://127.0.0.1:" + webPort + "/";
    }
}
