package doctorj;

import java.io.File;

public interface DocumentConverter {

    public void initialize() throws Exception;
    public void convertDocumentToPDF(File document, File outputDir) throws Exception;
}