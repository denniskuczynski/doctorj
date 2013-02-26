package doctorj.resources;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.DELETE;
import javax.ws.rs.PathParam;
import javax.ws.rs.Path;
import javax.ws.rs.Consumes;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;

import javax.servlet.http.HttpServletRequest;

import java.io.File;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.FileOutputStream;
import java.io.IOException;

@Path("/convert")
public class ConversionRequestResource {

    @Path("/{id}")
    @GET
    @Produces("text/plain")
    public String getConversionRequestStatus(@PathParam(value = "id") final String id) {
        return doctorj.ConversionManager.INSTANCE.getConversionRequestStatus(id);
    }

    @POST
    @Consumes("application/octet-stream")
    @Produces("text/plain")
    public String addConversionRequest(@Context HttpServletRequest httpRequest) 
        throws IOException {
        File temp = File.createTempFile("tempfile", ".tmp");
        try (FileOutputStream fos = new FileOutputStream(temp)) {
          org.apache.commons.io.IOUtils.copy(httpRequest.getInputStream(), fos); 
        }
        return doctorj.ConversionManager.INSTANCE.addConversionRequest(temp);
    }

    @Path("/{id}")
    @DELETE 
    @Produces("text/plain")
    public String deleteConversionRequest(@PathParam(value = "id") final String id) {
        return doctorj.ConversionManager.INSTANCE.deleteConversionRequest("1");
    }
}