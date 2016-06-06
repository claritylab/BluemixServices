package de.rwthaachen.ensemble.communication;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;

public class RestServer {
    private final static int REST_SERVICE_PORT = 9093;

    public void start() throws Exception {
        Server server = new Server(REST_SERVICE_PORT);

        ServletContextHandler servletContextHandler = new ServletContextHandler(ServletContextHandler.SESSIONS);
        servletContextHandler.setContextPath("/");
        server.setHandler(servletContextHandler);

        ServletHolder servletHolder = servletContextHandler.addServlet(
                org.glassfish.jersey.servlet.ServletContainer.class, "/*"
        );
        servletHolder.setInitOrder(0);
        servletHolder.setInitParameter(
                "jersey.config.server.provider.classnames",
                RestHandler.class.getCanonicalName()
        );

        try {
            server.start();
            server.join();
        } finally {
            server.destroy();
        }
    }

}
