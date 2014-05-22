package com.example;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.net.URL;
import java.util.Enumeration;
import java.util.Properties;

import javax.ejb.EJB;
import javax.ejb.embeddable.EJBContainer;

import org.apache.cxf.jaxrs.client.WebClient;
import org.apache.openejb.api.LocalClient;
import org.junit.Test;

@LocalClient
public class PingRSEmbeddedOpenEjbTest extends AOpenEjbTest {
    @EJB
    private Movies movies;

    protected void setProperties(Properties properties) {
        try {
            Enumeration<URL> ejbJars =
                this.getClass().getClassLoader().getResources(
                    "META-INF/ejb-jar.xml");
            while (ejbJars.hasMoreElements()) {
                URL url = ejbJars.nextElement();
                System.out.println("app = " + url);
            }

            ejbJars =
                this.getClass().getClassLoader().getResources(
                    "WEB-INF/ejb-jar.xml");
            while (ejbJars.hasMoreElements()) {
                URL url = ejbJars.nextElement();
                System.out.println("app = " + url);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        super.setProperties(properties);
        // loadProperties(properties, "jndi.properties");
        // properties.setProperty("javax.ejb.embeddable.modules",
        // "/stf/prj/tmp/tomee-embedded-trial/target/"
        // + "tomee-embedded-trial-0.0.1-SNAPSHOT");
        // properties.setProperty("javax.ejb.embeddable.modules",
        // "/stf/prj/tmp/tomee-embedded-trial/target/classes");
        // properties.setProperty("openejb.base",
        // "/stf/prj/tmp/tomee-embedded-trial/target/test-classes");

        properties.setProperty("openejb.validation.output.level", "VERBOSE");
        properties.setProperty("openejb.embedded.remotable", "true");
        properties.setProperty("openejb.log.factory", "slf4j");
        properties.setProperty("openejb.tempclassloader.skip", "annotations");
        properties.setProperty(
            "openejb.deployments.classpath.require.descriptor", "NONE");
        properties.setProperty(EJBContainer.APP_NAME, "myAppName");
        properties.put("movieDatabase", "new://Resource?type=DataSource");
        properties.put("movieDatabase.JdbcDriver", "org.hsqldb.jdbcDriver");
        properties.put("movieDatabase.JdbcUrl", "jdbc:hsqldb:mem:moviedb");

        /*
         * The following is only needed because my project's name starts with
         * tomee which causes openejb to automatically ignore it silently :(
         * org.apache.openejb.OpenEjbContainer$NoModulesFoundException: No modules found to deploy.
         */
        properties.setProperty("openejb.deployments.classpath.include",
            ".*-trial.*");
        properties.setProperty("openejb.deployments.package.include",
            ".*-trial.*");

    }

    @Test
    public void ping() {
        final String message =
            WebClient.create("http://localhost:4204").path(
                "/myAppName" +
                    // "/tomee-embedded-trial/MyRestApplication" +
                "/ping")
                .get(String.class);
        System.out.println("got message: " + message);
        assertEquals("pong Reservoir Dogs", message);
    }

}
