package doctorj;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.junit.Ignore;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.io.File;

@RunWith(JUnit4.class)
public class DocumentConverterTest {

    @Test
    public void testDummyDocumentConversion() 
      throws Exception {
        DocumentConverter converter = new DummyDocumentConverter();
        converter.initialize();
        converter.convertDocumentToPDF(new File("./data/example_docs/test1.odt"), new File("./data/output_docs/dummytest.pdf"));
    }

    @Test
    public void testLibreOfficeDocumentConversion() 
      throws Exception {
        DocumentConverter converter = new LibreOfficeDocumentConverter();
        converter.initialize();
        converter.convertDocumentToPDF(new File("./data/example_docs/test1.odt"), new File("./data/output_docs/libretest.pdf"));
    }

}