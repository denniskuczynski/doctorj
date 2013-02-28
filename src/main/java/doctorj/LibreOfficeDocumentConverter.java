package doctorj;

import com.sun.star.uno.UnoRuntime;
import java.io.File;

/* Use the LibreOffice API to convert a document to PDF */
public class LibreOfficeDocumentConverter implements DocumentConverter {

    private static final int MAX_LOAD_ATTEMPTS = 3;

    private com.sun.star.uno.XComponentContext xContext = null;
    private com.sun.star.frame.XComponentLoader xCompLoader = null;

    public void initialize()
        throws Exception {
        refreshDesktopInstance();
    }

    public void refreshDesktopInstance() 
        throws com.sun.star.comp.helper.BootstrapException, com.sun.star.uno.Exception {
        this.xContext = com.sun.star.comp.helper.Bootstrap.bootstrap();
        com.sun.star.lang.XMultiComponentFactory xMCF = xContext.getServiceManager();
        Object oDesktop = xMCF.createInstanceWithContext(
            "com.sun.star.frame.Desktop", this.xContext);
        this.xCompLoader = (com.sun.star.frame.XComponentLoader)
            UnoRuntime.queryInterface(com.sun.star.frame.XComponentLoader.class,
                oDesktop);
    }

    public void convertDocumentToPDF(File document, File outputFile) 
        throws Exception {
        String inputURL = getFileURL(document);
        String outputURL = getFileURL(outputFile);
        com.sun.star.frame.XStorable xStorable = openDocument(inputURL);
        processDocument(xStorable, inputURL, outputURL);
        closeDocument(xStorable);
    }

    private String getFileURL(File file) {
        return "file:///" + file.getAbsolutePath().replace( '\\', '/' );
    }

    private Object getDocToStore(String inputURL) 
        throws com.sun.star.comp.helper.BootstrapException, com.sun.star.uno.Exception {
        com.sun.star.beans.PropertyValue propertyValues[] =
            new com.sun.star.beans.PropertyValue[1];
        propertyValues[0] = new com.sun.star.beans.PropertyValue();
        propertyValues[0].Name = "Hidden";
        propertyValues[0].Value = new Boolean(true);

        int attempts = 0;
        while (true) {
            try {
                return this.xCompLoader.loadComponentFromURL(
                    inputURL, "_blank", 0, propertyValues);
            } catch (com.sun.star.lang.DisposedException e) {
                attempts = attempts + 1;
                if (attempts < MAX_LOAD_ATTEMPTS) {
                  refreshDesktopInstance();
                } else {
                  throw e;
                }
            }
        }
    }

    // Return an object that will offer a simple way to store a document to a URL.    
    private com.sun.star.frame.XStorable openDocument(String inputURL)
        throws com.sun.star.comp.helper.BootstrapException, com.sun.star.uno.Exception {
        Object oDocToStore = getDocToStore(inputURL);
        com.sun.star.frame.XStorable xStorable =
            (com.sun.star.frame.XStorable)UnoRuntime.queryInterface(
                com.sun.star.frame.XStorable.class, oDocToStore );
        System.out.println("Opened document: "+inputURL);
        return xStorable;
    }

    private void processDocument(com.sun.star.frame.XStorable xStorable, String inputURL, String outputURL) 
        throws com.sun.star.io.IOException {
        com.sun.star.beans.PropertyValue propertyValues[] = 
            new com.sun.star.beans.PropertyValue[2];
        propertyValues[0] = new com.sun.star.beans.PropertyValue();
        propertyValues[0].Name = "Overwrite";
        propertyValues[0].Value = new Boolean(true);
        propertyValues[1] = new com.sun.star.beans.PropertyValue();
        propertyValues[1].Name = "FilterName";
        propertyValues[1].Value = "writer_pdf_Export";

        // Storing and converting the document
        xStorable.storeToURL(outputURL, propertyValues);
        System.out.println("Wrote file to: "+outputURL);
    }

    // Closing the converted document. Use XCloseable.clsoe if the
    // interface is supported, otherwise use XComponent.dispose    
    private void closeDocument(com.sun.star.frame.XStorable xStorable)
       throws com.sun.star.util.CloseVetoException {
        com.sun.star.util.XCloseable xCloseable =
            (com.sun.star.util.XCloseable)UnoRuntime.queryInterface(
                com.sun.star.util.XCloseable.class, xStorable);
        if ( xCloseable != null ) {
            xCloseable.close(false);
        } else {
            com.sun.star.lang.XComponent xComp =
                (com.sun.star.lang.XComponent)UnoRuntime.queryInterface(
                    com.sun.star.lang.XComponent.class, xStorable);

            xComp.dispose();
        }
        System.out.println("Closed document");
    }
}
