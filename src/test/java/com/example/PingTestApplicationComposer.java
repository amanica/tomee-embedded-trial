package com.example;

import static org.junit.Assert.assertEquals;

import java.io.IOException;

import org.apache.cxf.jaxrs.client.WebClient;
import org.apache.openejb.jee.SingletonBean;
import org.apache.openejb.junit.ApplicationComposer;
import org.apache.openejb.junit.EnableServices;
import org.apache.openejb.junit.Module;
import org.junit.Test;
import org.junit.runner.RunWith;

@EnableServices(value = "jaxrs")
@RunWith(ApplicationComposer.class)
public class PingTestApplicationComposer {
    @Module
    public SingletonBean app() {

        return (SingletonBean) new SingletonBean(
            PingRS.class
        // MyRestApplication.class
        ).localBean();
    }

    @Test
    public void get() throws IOException {
        final String message =
            WebClient.create("http://localhost:4204"
                // ).path(
                + "/PingTestApplicationComposer"
                // + "/MyRestApplication"
                + "/ping"
                ).get(String.class);
        assertEquals("pong", message);
    }

    // @Test
    // public void post() throws IOException {
    // final String message =
    // WebClient.create("http://localhost:4204").path(
    // "/PingTestApplicationComposer/ping/")
    // .post("Hi REST!", String.class);
    // assertEquals("hi rest!", message);
    // }
}
