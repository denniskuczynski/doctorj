package doctorj;

import java.io.File;

/* Placeholder for the converter.  (I use this to test on OS X without a 32bit JDK...) */
public class DummyDocumentConverter implements DocumentConverter {

    public void initialize() 
        throws Exception {
        //no-op
    }

    public void convertDocumentToPDF(File document, File outputDir) 
        throws Exception {
        //no-op
    }
}