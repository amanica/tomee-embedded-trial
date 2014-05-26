package com.example;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Properties;

import javax.ejb.embeddable.EJBContainer;
import javax.naming.Context;
import javax.naming.NamingException;

import org.apache.openejb.api.LocalClient;
import org.junit.After;
import org.junit.Before;

/**
 * Subclasses must be annotated with @org.apache.openejb.api.LocalClient
 */
public abstract class AOpenEjbTest {

    private Context context;
    private EJBContainer container;

    @Before
    public final void setupAOpenEjbTest() throws Exception {
        // needed here for users of our subclasses
        setUpBeforeInjection();
        injectContext();
        setUpAfterInjection();
    }

    @After
    public final void closeContext() throws Exception {
        tearDownBeforeClosingContext();
        if (context != null) {
            context.close();
            context = null;
        }
        if (container != null) {
            container.close();
            container = null;
        }
    }

    private void injectContext() throws Exception {
        if (context == null) {
            context = inject(this);
        }
    }

    private Context inject(final Object target)
        throws NamingException {
        Properties properties = new Properties();
        setProperties(properties);

        Context context = getContext(properties);
        try {
            if (target.getClass().getAnnotation(LocalClient.class) == null) {
                System.out.println("***** Subclass of AbstractEjbTest is "
                    + "not annotated with " + "@LocalClient:\n" + target);
            } else {

                if (getClass().getClassLoader().getResource(
                    "META-INF/application-client.xml") == null) {
                    System.out.println("*****  META-INF/application-client.xml"
                        + " does not exist");
                } else {
                    context.bind("inject", target);
                }
            }
        } catch (NamingException e) {
            System.out.println("Could not load context.");
            e.printStackTrace();
        }
        return context;
    }

    protected Context getContext(Properties properties) {
        container = EJBContainer.createEJBContainer(properties);
        return container.getContext();
        //
        // try {
        // return new InitialContext(properties);
        // } catch (NamingException e) {
        // throw new RuntimeException("Could not create new InitialContext", e);
        // }
    }

    /**
     * You should call super if you override this..
     */
    protected void setProperties(Properties properties) {
        loadProperties(properties, "jndi.properties");
        properties.setProperty(Context.INITIAL_CONTEXT_FACTORY,
            "org.apache.openejb.client.LocalInitialContextFactory");
        // Enabling option 'openejb.embedded.remotable' requires class
        // 'org.apache.openejb.server.ServiceManager'
        URL resource = Thread.currentThread().getContextClassLoader()
            .getResource("org/apache/openejb/server/ServiceManager.class");
        if (resource != null) {
            properties.setProperty("openejb.embedded.remotable", "true");
        }
    }

    /**
     * Override this in stead of using @Before if you want to be sure that this
     * runs before injection, but call super.setUpBeforeInjection() for in case
     * one of your parents does something here.
     */
    public void setUpBeforeInjection() throws Exception {
    }

    /**
     * Override this in stead of using @Before if you want to be sure that
     * this runs after injection, but call super.setUpAfterInjection()
     * for in case one of your parents does something here.
     */
    public void setUpAfterInjection() throws Exception {
    }

    /**
     * Override this in stead of using @After if you want to be sure that this
     * runs before the context is destroyed,
     * but call super.tearDownBeforeClosingContext() for in case
     * one of your parents does something here.
     */
    public void tearDownBeforeClosingContext() throws Exception {
    }

    protected static void loadProperties(final Properties properties,
        final String resourcePath) {
        try {
            ClassLoader classLoader = Thread.currentThread()
                .getContextClassLoader();
            InputStream resourceAsStream = classLoader
                .getResourceAsStream(resourcePath);
            if (resourceAsStream == null) {

                System.out.println("Could not load properties from {}." +
                    resourcePath);
                System.out.println("Thread.currentThread(): "
                    + Thread.currentThread());
                System.out.println("Thread.currentThread().getName(): "
                    + Thread.currentThread().getName());
                System.out
                    .println("Thread.currentThread().getContextClassLoader(): "
                        + Thread.currentThread().getContextClassLoader());
                // System.out.println(
                // "EbrFactory.class.getClassLoader(): "
                // + EbrFactory.class.getClassLoader());
                return;
            }

            properties.load(resourceAsStream);
        } catch (IOException e) {
            System.out.println("Could not load properties from " + resourcePath
                + ".");
            e.printStackTrace();
        }
    }
}
