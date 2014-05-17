package com.example;

import java.util.Properties;

import javax.ejb.embeddable.EJBContainer;
import javax.naming.Context;

public class MyEmbededTomee {
    public static void main(String[] args) throws Exception {

        Properties p = new Properties();

        p.setProperty(EJBContainer.PROVIDER, "tomee-embedded");
        p.setProperty(
                "javax.ejb.embeddable.modules",
                "/stf/prj/tmp/tomee-embedded-trial/target/"
                    + "tomee-embedded-trial-0.0.1-SNAPSHOT");

        p.put("movieDatabase", "new://Resource?type=DataSource");
        p.put("movieDatabase.JdbcDriver", "org.hsqldb.jdbcDriver");
        p.put("movieDatabase.JdbcUrl", "jdbc:hsqldb:mem:moviedb");
        p.put("openejb.validation.output.level", "VERBOSE");

        Context context =
            EJBContainer.createEJBContainer(p).getContext();

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
