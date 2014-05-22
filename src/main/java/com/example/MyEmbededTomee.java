package com.example;

import java.util.Properties;

import javax.ejb.embeddable.EJBContainer;
import javax.naming.Context;

public class MyEmbededTomee {
    // // @Parameter(defaultValue = "${project.packaging}")
    // protected String packaging = "war";
    //
    // // @Parameter(
    // // defaultValue =
    // "${project.build.directory}/${project.build.finalName}")
    // protected File warFile;
    //
    // // @Parameter(property = "tomee-embedded-plugin.http", defaultValue =
    // // "8080")
    // private int httpPort;
    //
    // // @Parameter(property = "tomee-embedded-plugin.ajp", defaultValue =
    // "8009")
    // private int ajpPort = 8009;
    //
    // // @Parameter(property = "tomee-embedded-plugin.stop", defaultValue =
    // // "8005")
    // private int stopPort;
    //
    // // @Parameter(property = "tomee-embedded-plugin.host",
    // // defaultValue = "localhost")
    // private String host;
    //
    // // @Parameter(property = "tomee-embedded-plugin.lib",
    // // defaultValue = "${project.build.directory}/apache-tomee-embedded")
    // protected String dir;
    //
    // // @Parameter
    // private File serverXml;

    public static void main(String[] args) throws Exception {
        new MyEmbededTomee().run();
    }

    private void run() throws Exception {
        Properties p = new Properties();

        p.setProperty(EJBContainer.PROVIDER, "tomee-embedded");
        p.setProperty(
            "javax.ejb.embeddable.modules",
            "/stf/prj/tmp/tomee-embedded-trial/target/"
                + "tomee-embedded-trial-0.0.1-SNAPSHOT");

        // p.put("movieDatabase", "new://Resource?type=DataSource");
        // p.put("movieDatabase.JdbcDriver", "org.hsqldb.jdbcDriver");
        // p.put("movieDatabase.JdbcUrl", "jdbc:hsqldb:mem:moviedb");
        // p.put("openejb.validation.output.level", "VERBOSE");

        Context context =
            EJBContainer.createEJBContainer(p).getContext();

        for (int i = 0; i < 10; i++) {
            try {
                System.out.println(" " + i);
                Thread.sleep(200000);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        context.close();

        // final Container container = new Container();
        // final Configuration config = getConfig();
        // container.setup(config);
        // try {
        // container.start();
        //
        // Runtime.getRuntime().addShutdownHook(new Thread() {
        // @Override
        // public void run() {
        // try {
        // container.undeploy(warFile.getAbsolutePath());
        // container.stop();
        // } catch (Exception e) {
        // e.printStackTrace();
        // System.out.println("can't stop TomEE");
        // }
        // }
        // });
        //
        // container.deploy(warFile.getName(), warFile);
        //
        // System.out.println(
        // "TomEE embedded started on " + config.getHost() + ":"
        // + config.getHttpPort());
        // container.await();
        // System.out.println("*** started ***");
        //
        // try {
        // for (int i = 0; i < 10; i++) {
        // System.out.println(" " + i);
        // Thread.sleep(20000);
        // }
        // } catch (Exception e) {
        // e.printStackTrace();
        // }
        //
        // System.out.println("*** closing ***");
        // container.stop();
        // } catch (Exception e) {
        // e.printStackTrace();
        // System.out.println("*** can't start TomEE");
        // }

    }

    // private Configuration getConfig() { // lazy way but it works fine
    // final Configuration config = new Configuration();
    // for (Field field : getClass().getDeclaredFields()) {
    // try {
    // final Field configField =
    // Configuration.class.getDeclaredField(field.getName());
    // field.setAccessible(true);
    // configField.setAccessible(true);
    //
    // final Object value = field.get(this);
    // if (value != null) {
    // configField.set(config, value);
    // System.out.println("using " + field.getName() + " = "
    // + value);
    // }
    // } catch (NoSuchFieldException nsfe) {
    // // ignored
    // } catch (Exception e) {
    // System.out.println("can't initialize attribute "
    // + field.getName());
    // }
    //
    // }
    // return config;
    // }
}
