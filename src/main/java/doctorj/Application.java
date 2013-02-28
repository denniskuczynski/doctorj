package doctorj;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import com.sun.jersey.spi.container.servlet.ServletContainer;
 
public class Application 
{
    public static void main(String[] args) throws Exception
    {
        DocumentConverter documentConverter = (DocumentConverter)Class.forName(
            System.getProperty("doctorj.converterClass", "doctorj.LibreOfficeDocumentConverter")).newInstance();
        documentConverter.initialize();
        ConversionManager.INSTANCE.initialize(documentConverter);
        
        ServletHolder sh = new ServletHolder(ServletContainer.class);
        sh.setInitParameter("com.sun.jersey.config.property.resourceConfigClass", "com.sun.jersey.api.core.PackagesResourceConfig");
        sh.setInitParameter("com.sun.jersey.config.property.packages", "doctorj.resources");

        ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);
        context.setContextPath("/");
        context.addServlet(sh, "/*");
 
        Server server = new Server(8080);
        server.setHandler(context);

        Runtime.getRuntime().addShutdownHook(new Thread() {
            public void run() {
                //Capture Control-C and exit gracefully
                System.out.println("Shutting down Doctorj...");
                try {
                    ConversionManager.INSTANCE.shutdown();
                    System.out.println("Shutdown doctorj.ConversionManager");
                } catch(Exception e) {
                    System.out.println("Unable to gracefully shutdown ConversionManager");
                    e.printStackTrace();
                }
                System.out.println("Doctorj shutdown complete");
            }
        });

        server.start();
        server.join();
    }
}