package com.jetty;


import com.HttpRespDemo;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;

/**
 * 利用Jetty实现简单的嵌入式Httpserver，利用HttpServlet进行编程
 */
public class JettyServletServer {

    private static Log log = LogFactory.getLog(JettyServletServer.class);

    /**
     * <pre>
     * @param args
     * </pre>
     */
    public static void main(String[] args) {
        try {
            Server server = new Server(8080);

            ServletContextHandler context = new ServletContextHandler(
                    ServletContextHandler.SESSIONS);
            context.setContextPath("/");
            server.setHandler(context);

            context.addServlet(new ServletHolder(new HttpRespDemo()),
                    "/");
            log.info("server start.");
            server.start();
            server.join();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
