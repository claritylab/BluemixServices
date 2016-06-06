package de.rwthaachen.ensemble.backend;

import de.rwthaachen.ensemble.communication.AuthManager;

import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLEncoder;
import java.nio.charset.Charset;
//import java.util.logging.Logger;

import org.apache.commons.codec.binary.Base64;
import org.apache.http.HttpHeaders;
import org.apache.http.HttpResponse;
import org.apache.http.client.fluent.Executor;
import org.apache.http.client.fluent.Form;
import org.apache.http.client.fluent.Request;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

/**
 * Created by fp on 5/25/16.
 */
public class Backend {
    private int dialogId = 0;
    private String classifierId = "";
    private String[][] credentials = AuthManager.getUrlLookUpTable();

    public String query(String query) {
        return "";
    }

    public String testClassifier (String question) {

        Request request = null;

        try {
            URI converseURI = new URI("https://gateway.watsonplatform.net/natural-language-classifier/api/v1/classifiers/" + classifierId + "/classify?text=" + question.replaceAll(" ", "%20").replaceAll("\\?", "%3F")).normalize();
            request = Request.Get(converseURI);
            Executor executor = Executor.newInstance().auth(credentials[AuthManager.QaBackends.NLC.ordinal()][2],
                    credentials[AuthManager.QaBackends.NLC.ordinal()][1]);
            org.apache.http.client.fluent.Response response = executor.execute(request);
            HttpResponse httpResponse = response.returnResponse();

            return EntityUtils.toString(httpResponse.getEntity());
        } catch (Exception e) {
            return null;
        }
    }

    public String test2 (String path, String input, String clientID, String conversationID) {

        Request request = null;
        try {
            if (path.equals("conversation")) {
                request = conversation(input, conversationID, clientID);
            } else if (path.equals("profile")) {
                request = profile(clientID);
            }
            Executor executor = Executor.newInstance().auth(credentials[AuthManager.QaBackends.DIALOG.ordinal()][2],
                    credentials[AuthManager.QaBackends.DIALOG.ordinal()][1]);
            org.apache.http.client.fluent.Response response = executor.execute(request);
            HttpResponse httpResponse = response.returnResponse();

            return EntityUtils.toString(httpResponse.getEntity());
            //e.g. {"conversation_id":108188,"client_id":99897,"input": "","confidence":0,"response":["Hi, I'm Watson! I can help you order a pizza, what size would you like?"]}
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * Create a /profile request
     *
     * @param clientID Client ID to obtain profile for
     * @return the request
     * @throws URISyntaxException
     */
    private Request profile(String clientID) throws URISyntaxException {
        URI converseURI = new URI(credentials[AuthManager.QaBackends.DIALOG.ordinal()][2] + "/v1/dialogs/" + dialogId + "/profile?client_id="+ clientID).normalize();
        return Request.Get(converseURI);
    }

    private Request conversation(String input, String conversationID, String clientID) throws URISyntaxException {
        URI converseURI = new URI(credentials[AuthManager.QaBackends.DIALOG.ordinal()][2] + "/v1/dialogs/" + dialogId + "/conversation").normalize();
        return Request.Post(converseURI).bodyForm(
                Form.form()
                        .add("input", input)
                        .add("client_id", clientID)
                        .add("conversation_id", conversationID)
                        .build());
    }

    public String keyphrase (String text) {
        Request request = null;

        try {
            URI converseURI = new URI("http://gateway-a.watsonplatform.net/calls/text/TextGetRankedKeywords?apikey=" +
                    AuthManager.lookUpUrlSet(0)[1] + "&text=" + URLEncoder.encode(text, "UTF-8") + "&outputMode=json");
            request = Request.Get(converseURI);
            Executor executor = Executor.newInstance().auth(credentials[AuthManager.QaBackends.NLC.ordinal()][2],
                    credentials[AuthManager.QaBackends.NLC.ordinal()][1]);
            org.apache.http.client.fluent.Response response = executor.execute(request);
            HttpResponse httpResponse = response.returnResponse();

            return EntityUtils.toString(httpResponse.getEntity());
        } catch (Exception e) {
            return null;
        }
    }


}
