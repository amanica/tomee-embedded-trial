package com.example;

import java.util.Properties;

import javax.ejb.embeddable.EJBContainer;

public class ATomeeTest extends AOpenEjbTest {

    protected void setProperties(Properties properties) {
        super.setProperties(properties);
        properties.setProperty(EJBContainer.PROVIDER, "tomee-embedded");
        // properties.setProperty(
        // "javax.ejb.embeddable.modules", appBase);
    }

    // TODO: check if ports are available if not the find random ports
    // and use that
    public int getHttpPort() {
        return 8080;
    }

    public String getBaseUrl() {
        return "http://localhost:8080/";
    }
}