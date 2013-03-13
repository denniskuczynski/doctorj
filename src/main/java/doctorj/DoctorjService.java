package doctorj;

import com.yammer.dropwizard.Service;
import com.yammer.dropwizard.config.Bootstrap;
import com.yammer.dropwizard.config.Environment;

public class DoctorjService extends Service<DoctorjConfiguration> {
    public static void main(String[] args) throws Exception {
        new DoctorjService().run(args);
    }

    @Override
    public void initialize(Bootstrap<DoctorjConfiguration> bootstrap) {
        bootstrap.setName("doctorj");
    }

    @Override
    public void run(DoctorjConfiguration configuration,
                    Environment environment) {
        try {
            DocumentConverter documentConverter = (DocumentConverter)Class.forName(
                System.getProperty("doctorj.converterClass", "doctorj.LibreOfficeDocumentConverter")).newInstance();
            documentConverter.initialize();
            ConversionManager.INSTANCE.initialize(documentConverter);
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(-1);
        }

        environment.addResource(new doctorj.resources.ConversionRequestResource());

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
    }

}