package com.example;

import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.URL;
import java.util.Properties;
import java.util.Random;

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

    private static final Random RANDOM = new Random();
    private Context context;
    private EJBContainer container;

    protected int webPort;

    @Before
    public final void setupAOpenEjbTest() throws Exception {
        webPort = getAvailablePort();
        System.out.println("webPort = " + webPort);
        // needed here for users of our subclasses
        setUpBeforeInjection();
        injectContext();
        setUpAfterInjection();
    }

    @After
    public final void closeContext() {
        System.out.println("closeContext()");
        try {
            tearDownBeforeClosingContext();
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (context != null) {
            try {
                context.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
            context = null;
        }
        if (container != null) {
            try {
                container.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
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

        try {
            Context context = getContext(properties);
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
        } catch (Exception e) {
            System.out.println("Could not load context.");
            e.printStackTrace();
            closeContext();
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
        properties.setProperty("httpejbd.port", "" + webPort);
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
