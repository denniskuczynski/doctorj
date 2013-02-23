package doctorj;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ExecutorService;

import java.io.File;

public enum ConversionManager {
    INSTANCE;

    private static final int CONVERSION_THREADS = 1;

    private static final String STATUS_NOT_FOUND  = "NotFound";
    private static final String STATUS_ERROR  = "Error";
    private static final String STATUS_IN_PROCESS = "InProcess";
    private static final String STATUS_COMPLETE   = "Complete";

    private DocumentConverter documentConverter = null;
    private ConcurrentHashMap<String, String> requestStatusMap = null;
    private ExecutorService executor = null;
    
    public void initialize()
        throws Exception {
        documentConverter = new DocumentConverter();
        documentConverter.initialize();

        requestStatusMap = new ConcurrentHashMap<String, String>();
        executor = Executors.newFixedThreadPool(CONVERSION_THREADS);
    }
    
    public String getConversionRequestStatus(String id) {
        String status = requestStatusMap.get(id);
        if (status == null) {
            return STATUS_NOT_FOUND;
        } else {
            return status;
        }
    }

    public String addConversionRequest(final File file) {
        final String id = java.util.UUID.randomUUID().toString();
        requestStatusMap.put(id, STATUS_IN_PROCESS);
        executor.submit(new Runnable() {
            @Override
            public void run() {
                System.out.println("Converting to PDF: "+file.getAbsolutePath());
                try {
                    documentConverter.convertDocumentToPDF(file, new File("./data/output"));
                    requestStatusMap.put(id, STATUS_COMPLETE);
                } catch(Exception e) {
                    requestStatusMap.put(id, STATUS_ERROR);
                }
            }
        });
        return id;
    }

    public String deleteConversionRequest(String id) {
        File file = new File("./data/output/"+id+".pdf");
        try {
            if (file.exists()) {
                file.delete();
            }
        } catch(Exception e) {
            System.out.println("Error deleting file: "+file.getAbsolutePath());
        }
        return requestStatusMap.remove(id);
    } 
}