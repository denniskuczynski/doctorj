package doctorj;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ExecutorService;

import java.io.File;

public enum ConversionManager {
    INSTANCE;

    private static final String STATUS_NOT_FOUND  = "NotFound";
    private static final String STATUS_ERROR  = "Error";
    private static final String STATUS_IN_PROCESS = "InProcess";
    private static final String STATUS_COMPLETE   = "Complete";

    private static final String OUTPUT_DIRECTORY = "./data/output_docs";

    private DocumentConverter documentConverter = null;
    private ConcurrentHashMap<String, String> requestStatusMap = null;
    private ExecutorService executor = null;

    public void initialize(DocumentConverter documentConverter)
        throws Exception {
        this.documentConverter = documentConverter;
        this.requestStatusMap = new ConcurrentHashMap<String, String>();
        this.executor = Executors.newSingleThreadExecutor();
    }

    public void shutdown()
        throws Exception {
        this.executor.shutdown();
    }
    
    public String getConversionRequestStatus(String id) {
        String status = this.requestStatusMap.get(id);
        if (status == null) {
            return STATUS_NOT_FOUND;
        } else {
            return status;
        }
    }

    public String addConversionRequest(final File file) {
        final String id = java.util.UUID.randomUUID().toString();
        this.requestStatusMap.put(id, STATUS_IN_PROCESS);
        this.executor.submit(new Runnable() {
            @Override
            public void run() {
                System.out.println("Converting to PDF: "+file.getAbsolutePath());
                try {
                    ConversionManager.this.documentConverter.convertDocumentToPDF(file, getOutputFile(id));
                    ConversionManager.this.requestStatusMap.put(id, STATUS_COMPLETE);
                } catch(Exception e) {
                    ConversionManager.this.requestStatusMap.put(id, STATUS_ERROR);
                    e.printStackTrace();
                }
            }
        });
        return id;
    }

    public File getConversionRequestFile(String id) {
        return getOutputFile(id);
    }

    public String deleteConversionRequest(String id) {
        File file = getOutputFile(id);
        try {
            if (file.exists()) { file.delete(); }
        } catch(Exception e) {
            System.out.println("Error deleting file: "+file.getAbsolutePath());
        }
        String status = this.requestStatusMap.remove(id);
        if (status == null) {
            return STATUS_NOT_FOUND;
        } else {
            return status;
        }
    }

    private File getOutputFile(String id) {
        return new File(OUTPUT_DIRECTORY+"/"+id+".pdf");
    }
}