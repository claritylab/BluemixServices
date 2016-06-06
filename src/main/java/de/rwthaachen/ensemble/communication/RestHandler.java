package de.rwthaachen.ensemble.communication;

import de.rwthaachen.ensemble.backend.Backend;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * REST interface for Ensemble
 */
@Path("/")
public class RestHandler extends Handler{

    private Backend backend = new Backend();

    /**
     * Constructor that creates StatusMonitor and JBT backends
     */
    public RestHandler() {}

    /**
     * Query to backend, e.g. http://localhost:9093/ask?text=This%20is%20a%20test%20text%20about%20m-learning%20and%20more%20m-learning%20and%20its%20ramifications%20for%20e-learning.%20While%20it%20is%20not%20clear%20how%20long%20this%20text%20can%20be%20I%20would%20like%20to%20stress%20some%20important%20keyphrases%20like%20MOOCs,%20mobile%20learning%20and%20mobile%20devices.%20This%20should%20provide%20some%20insights%20into%20whether%20keyphrases%20are%20actually%20extracted.
     * @param text Text to extract keyphrases from
     * @return Response
     */
    @GET
    @Path("ask")
    @Produces(MediaType.APPLICATION_JSON)
    public String countTermJson(@QueryParam("text") String text){
        System.out.println(text);
        return backend.keyphrase(text);
    }


}
