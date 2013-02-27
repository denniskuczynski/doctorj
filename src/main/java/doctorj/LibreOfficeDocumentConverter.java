package doctorj;

import com.sun.star.uno.UnoRuntime;
import java.io.File;

/* Use the LibreOffice API to convert a document to PDF */
public class LibreOfficeDocumentConverter implements DocumentConverter {

    private com.sun.star.frame.XComponentLoader xCompLoader = null;

    public void initialize()
        throws Exception {
        com.sun.star.uno.XComponentContext xContext = 
            com.sun.star.comp.helper.Bootstrap.bootstrap();

        com.sun.star.lang.XMultiComponentFactory xMCF = 
                xContext.getServiceManager();

        Object oDesktop = xMCF.createInstanceWithContext(
            "com.sun.star.frame.Desktop", xContext);

        this.xCompLoader = (com.sun.star.frame.XComponentLoader)
            UnoRuntime.queryInterface(com.sun.star.frame.XComponentLoader.class,
                oDesktop);
    }

    public void convertDocumentToPDF(File document, File outputDir) 
        throws Exception {
        String inputURL = getFileURL(document);
        String outputURL = getFileURL(outputDir);
        com.sun.star.frame.XStorable xStorable = openDocument(inputURL);
        processDocument(xStorable, inputURL, outputURL);
        closeDocument(xStorable);
    }

    private String getFileURL(File file) {
        return "file:///" + file.getAbsolutePath().replace( '\\', '/' );
    }

    // Return an object that will offer a simple way to store a document to a URL.    
    private com.sun.star.frame.XStorable openDocument(String inputURL)
        throws com.sun.star.io.IOException {
        com.sun.star.beans.PropertyValue propertyValues[] =
            new com.sun.star.beans.PropertyValue[1];
        propertyValues[0] = new com.sun.star.beans.PropertyValue();
        propertyValues[0].Name = "Hidden";
        propertyValues[0].Value = new Boolean(true);

        Object oDocToStore =
            this.xCompLoader.loadComponentFromURL(
                inputURL, "_blank", 0, propertyValues);
        
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

        // Appending the favoured extension to the origin document name
        int index1 = inputURL.lastIndexOf('/');
        int index2 = inputURL.lastIndexOf('.');
        String storeToURL = outputURL + inputURL.substring(index1, index2 + 1) + "pdf";

        // Storing and converting the document
        xStorable.storeToURL(storeToURL, propertyValues);
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
