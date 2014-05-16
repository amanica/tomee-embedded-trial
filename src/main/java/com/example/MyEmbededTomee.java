package com.example;

import java.util.Properties;

import javax.ejb.embeddable.EJBContainer;
import javax.naming.Context;

public class MyEmbededTomee {
    public static void main(String[] args) throws Exception {

        Properties properties = new Properties();

        properties.setProperty(EJBContainer.PROVIDER, "tomee-embedded");
        properties
            .setProperty(
                "javax.ejb.embeddable.modules",
                "/stf/prj/tmp/tomee-embed-run-webapp/target/tomee-embed-run-webapp-0.0.1-SNAPSHOT");

        Context context =
            EJBContainer.createEJBContainer(properties).getContext();

        System.out.println("*** started ***");

        try {
            for (int i = 0; i < 10; i++) {
                System.out.println(" " + i);
                Thread.sleep(20000);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        System.out.println("*** closing ***");
        context.close();
    }
}
