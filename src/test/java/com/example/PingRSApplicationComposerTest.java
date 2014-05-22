package com.example;

import static org.junit.Assert.assertEquals;

import java.io.IOException;

import org.apache.cxf.jaxrs.client.WebClient;
import org.apache.openejb.config.EjbModule;
import org.apache.openejb.jee.Beans;
import org.apache.openejb.jee.EjbJar;
import org.apache.openejb.jee.Empty;
// import org.apache.openejb.jee.Persistence;
// import org.apache.openejb.jee.Persistence.PersistenceUnit;
import org.apache.openejb.jee.SingletonBean;
import org.apache.openejb.jee.StatelessBean;
import org.apache.openejb.jee.jpa.unit.Persistence;
import org.apache.openejb.jee.jpa.unit.PersistenceUnit;
import org.apache.openejb.junit.ApplicationComposer;
import org.apache.openejb.testing.EnableServices;
import org.apache.openejb.testing.Module;
import org.hibernate.ejb.HibernatePersistence;
import org.junit.Test;
import org.junit.runner.RunWith;

@EnableServices(value = {"jaxrs", "ejbd"})
@RunWith(ApplicationComposer.class)
public class PingRSApplicationComposerTest {
    @Module
    public EjbModule app() throws Exception {
        SingletonBean beanMovies = new SingletonBean(Movies.class);
        beanMovies.setLocalBean(new Empty());

        final StatelessBean beanPingRS =
            new StatelessBean(PingRS.class);
        beanPingRS.setLocalBean(new Empty());

        final EjbJar ejbJar = new EjbJar();
        ejbJar.addEnterpriseBean(beanMovies);
        ejbJar.addEnterpriseBean(beanPingRS);

        final Beans beans = new Beans();
        // beans.addManagedClass(PlcBaseJpaDAO.class);

        final EjbModule jar = new EjbModule(ejbJar);
        jar.setBeans(beans);

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
            WebClient.create("http://localhost:4204").path(
                "/" + getClass().getSimpleName() + "/ping")
                .get(String.class);
        System.out.println("got message: " + message);
        assertEquals("pong Reservoir Dogs", message);
    }

}
