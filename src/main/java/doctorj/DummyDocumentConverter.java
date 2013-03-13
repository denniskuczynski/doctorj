package doctorj;

import java.io.File;
import java.io.FileOutputStream;
import java.io.ByteArrayInputStream;
import java.util.concurrent.TimeUnit;

import com.yammer.metrics.Metrics;
import com.yammer.metrics.core.Timer;
import com.yammer.metrics.core.TimerContext;

/* Placeholder for the converter.  (I use this to test on OS X without a 32bit JDK...) */
public class DummyDocumentConverter implements DocumentConverter {

    private Timer timer;

    public void initialize() 
        throws Exception {
        timer = Metrics.newTimer(DummyDocumentConverter.class, "convertDocumentToPDFs", TimeUnit.MILLISECONDS, TimeUnit.SECONDS);
    }

    public void convertDocumentToPDF(File document, File outputFile) 
        throws Exception {
        final TimerContext context = timer.time();
        try {
            if (!outputFile.exists()) { outputFile.createNewFile(); }        
            try (FileOutputStream fos = new FileOutputStream(outputFile, false);
                ByteArrayInputStream dummyStream = new ByteArrayInputStream("DUMMY".getBytes())) {
                com.google.common.io.ByteStreams.copy(dummyStream, fos); 
            }
        } finally {
            context.stop();
        }
    }
}