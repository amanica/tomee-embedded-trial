package com.example;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.Random;

import javax.ws.rs.core.MediaType;

import org.apache.cxf.jaxrs.client.WebClient;
import org.apache.openejb.config.EjbModule;
import org.apache.openejb.jee.Beans;
import org.apache.openejb.jee.EjbJar;
import org.apache.openejb.jee.Empty;
import org.apache.openejb.jee.SingletonBean;
import org.apache.openejb.jee.StatelessBean;
import org.apache.openejb.jee.Webservices;
import org.apache.openejb.jee.jpa.unit.Persistence;
import org.apache.openejb.jee.jpa.unit.PersistenceUnit;
import org.apache.openejb.junit.ApplicationComposer;
import org.apache.openejb.testing.EnableServices;
import org.apache.openejb.testing.Module;
import org.hibernate.ejb.HibernatePersistence;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;

@EnableServices(value = {"jaxrs", "ejbd"}, httpDebug = true)
@RunWith(ApplicationComposer.class)
public class PingRSApplicationComposerTest {
    private static final Random RANDOM = new Random();
    protected int webPort;

    public PingRSApplicationComposerTest() throws Exception {
        // force port because other tests mess with the property
        webPort = getAvailablePort();
        System.setProperty("httpejbd.port", "" + webPort);
        System.out.println("@@@@@@@@@ webPort = " + webPort);

    }

    // @Classes(value = {MyRestApplication.class})
    @Module
    public EjbModule app() throws Exception {
        // setupPort();

        SingletonBean beanMovies = new SingletonBean(Movies.class);
        beanMovies.setLocalBean(new Empty());

        final StatelessBean beanPingRS =
            new StatelessBean(PingRS.class);
        beanPingRS.setLocalBean(new Empty());

        final StatelessBean beanPing2RS =
            new StatelessBean(Ping2RS.class);
        beanPing2RS.setLocalBean(new Empty());

        final StatelessBean beanDuckRS =
            new StatelessBean(DuckRS.class);
        beanPing2RS.setLocalBean(new Empty());

        final EjbJar ejbJar = new EjbJar();
        ejbJar.addEnterpriseBean(beanMovies);
        ejbJar.addEnterpriseBean(beanPingRS);
        ejbJar.addEnterpriseBean(beanPing2RS);
        ejbJar.addEnterpriseBean(beanDuckRS);

        final Beans beans = new Beans();
        // beans.addManagedClass(PlcBaseJpaDAO.class);

        final EjbModule jar = new EjbModule(ejbJar);
        jar.getProperties().setProperty("httpejbd.port", "" + webPort);
        jar.setBeans(beans);

        Webservices webservices = new Webservices();
        jar.setWebservices(webservices);

        return jar;
    }

    @Module
    // a persistence.xml
        public
        Persistence persistence() {

        final PersistenceUnit unit = new PersistenceUnit("movie-unit");
        unit.setProvider(HibernatePersistence.class);
        unit.setJtaDataSource("movieDatabase");
        unit.addClass(com.example.Movie.class);
        unit.setProperty("hibernate.hbm2ddl.auto", "create-drop");
        unit.setProperty("hibernate.dialect",
            "org.hibernate.dialect.HSQLDialect");
        unit.setExcludeUnlistedClasses(true);

        final Persistence persistence = new Persistence(unit);
        persistence.setVersion("2.0");
        return persistence;
    }

    @Test
    public void ping() throws IOException {

        final String message =
            WebClient.create(
                "http://localhost:"
                    + webPort
                    // System.getProperty("httpejbd.port" // ,
                // "4204"
                // )
                )
                .path(
                    "/" + getClass().getSimpleName() + "/" +
                        // "/MyRestApplication" +
                        "/ping")
                .get(String.class);
        System.out.println("got message: " + message);
        assertEquals("pong Reservoir Dogs", message);
    }

    @Ignore
    @Test
    public void giveMeADuck() {
        String message =
            WebClient.create("http://localhost:" + webPort).path(
                "/" + getClass().getSimpleName() + "/" +
                    "/duck")
                .accept(MediaType.APPLICATION_JSON_TYPE)
                .get(String.class);
        System.out.println("got message: " + message);
        assertEquals("{\n" +
            "  \"name\" : \"Donald\",\n" +
            "  \"nicknames\" : [ \"Don\" ],\n" +
            "  \"nephews\" : [ \"Huey\", \"Dewey\", \"Louie\" ]\n" +
            "}", message);
    }

    /*
     * http://stackoverflow.com/questions/7144401/how-can-i-find-an-open-ports-in-range-of-ports
     */
    private int getAvailablePort() throws IOException {
        int port = 0;
        do {
            port = RANDOM.nextInt(20000) + 10000;
        } while (!isPortAvailable(port));

        return port;
    }

    private boolean isPortAvailable(final int port) throws IOException {
        ServerSocket ss = null;
        try {
            ss = new ServerSocket(port);
            ss.setReuseAddress(true);
            return true;
        } catch (final IOException e) {
        } finally {
            if (ss != null) {
                ss.close();
            }
        }

        return false;
    }

}
