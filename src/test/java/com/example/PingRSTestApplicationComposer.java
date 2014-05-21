package com.example;

import static org.junit.Assert.assertEquals;

import java.io.IOException;

import org.apache.cxf.jaxrs.client.WebClient;
import org.apache.openejb.jee.SingletonBean;
import org.apache.openejb.junit.ApplicationComposer;
import org.apache.openejb.testing.EnableServices;
import org.apache.openejb.testing.Module;
import org.junit.Test;
import org.junit.runner.RunWith;

@EnableServices(value = {"jaxrs" // , "ejbd"
})
@RunWith(ApplicationComposer.class)
public class PingRSTestApplicationComposer {
    @Module
    public SingletonBean app() {

        return (SingletonBean) new SingletonBean(
            PingRS.class
        ).localBean();
    }

    @Test
    public void ping() throws IOException {
        final String message =
            WebClient.create("http://localhost:4204").path(
                "/" + getClass().getSimpleName() + "/ping")
                .get(String.class);
        System.out.println("got message: " + message);
        assertEquals("pong", message);
    }

}
