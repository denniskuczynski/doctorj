package doctorj;

import java.io.File;
import java.io.FileOutputStream;
import java.io.ByteArrayInputStream;

/* Placeholder for the converter.  (I use this to test on OS X without a 32bit JDK...) */
public class DummyDocumentConverter implements DocumentConverter {

    public void initialize() 
        throws Exception {
        //no-op
    }

    public void convertDocumentToPDF(File document, File outputFile) 
        throws Exception {
        if (!outputFile.exists()) { outputFile.createNewFile(); }        
        try (FileOutputStream fos = new FileOutputStream(outputFile, false);
            ByteArrayInputStream dummyStream = new ByteArrayInputStream("DUMMY".getBytes())) {
            org.apache.commons.io.IOUtils.copy(dummyStream, fos); 
        }
    }
}