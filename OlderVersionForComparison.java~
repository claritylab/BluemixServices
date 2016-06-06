package de.rwthaachen.bluemixbackend;

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
//import com.ibm.json.java.JSONArray;
//import org.apache.wink.json4j.JSON;
//import org.apache.wink.json4j.JSONArray;
//import org.apache.wink.json4j.JSONException;
//import org.apache.wink.json4j.JSONObject;

//import com.eclipsesource.json.*;
import com.eclipsesource.json.Json;
import com.eclipsesource.json.JsonArray;
import com.eclipsesource.json.JsonObject;
import com.eclipsesource.json.JsonValue;

//Problems:
//Conflict between javax.ws.rs.core.Response and org.apache.http.client.fluent.Response

@javax.ws.rs.ApplicationPath("resources")
@Path("/services")
public class BluemixBackend extends javax.ws.rs.core.Application{
	
	private enum ServiceNames {AL, NLC, DIALOG, RR, STT, TTS, DC, CI};
	private String[][] credentials;
	
	private String dialogId = "";
	private String classifierId = "...";
	
	private static final String CONVERSATION = "/conversation";
	private static final String PROFILE = "/profile";
	
	@Context
    private UriInfo context;
	
	private String VCAP_SERVICES;
	
	public BluemixBackend() {
		credentials = new String[8][3];
		
		VCAP_SERVICES = System.getenv("VCAP_SERVICES");
		//System.out.println("Authentication information: " + VCAP_SERVICES);
		
		if(VCAP_SERVICES != null) {
	      	JsonValue vcapJson = Json.parse(VCAP_SERVICES);

		    // Obtain service credentials when running on Bluemix
		    if (vcapJson != null && vcapJson.isObject()) {
		    	extractCredentials("alchemy", vcapJson, 0);
		     	extractCredentials("natural_language_classifier", vcapJson, 1);
		     	extractCredentials("dialog", vcapJson, 2);
		     	extractCredentials("retrieve_and_rank", vcapJson, 3);
		     	extractCredentials("speech_to_text", vcapJson, 4);
		     	extractCredentials("text_to_speech", vcapJson, 5);
		     	extractCredentials("document_conversion", vcapJson, 6);
		     	extractCredentials("concept_insights", vcapJson, 7);
		    }
		}
		
		// Obtain service credentials locally when running on-premise
		if(credentials[0][0] == null || credentials[0][1] == null || credentials[0][2] == null) {
			credentials[ServiceNames.AL.ordinal()][0] = "";
			credentials[ServiceNames.AL.ordinal()][1] = "";
			credentials[ServiceNames.AL.ordinal()][2] = "";
		}
		if(credentials[1][0] == null || credentials[1][1] == null || credentials[1][2] == null) {
			credentials[ServiceNames.NLC.ordinal()][0] = "...";
			credentials[ServiceNames.NLC.ordinal()][1] = "...";
			credentials[ServiceNames.NLC.ordinal()][2] = "...";
			System.out.println("Couldn't receive credentials for 1");
		}
		
		if(credentials[2][0] == null || credentials[2][1] == null || credentials[2][2] == null) {
			credentials[ServiceNames.DIALOG.ordinal()][0] = "...";
			credentials[ServiceNames.DIALOG.ordinal()][1] = "...";
			credentials[ServiceNames.DIALOG.ordinal()][2] = "...";
			System.out.println("Couldn't receive credentials for 2");
		}
		
		if(credentials[3][0] == null || credentials[3][1] == null || credentials[3][2] == null) {
			credentials[ServiceNames.RR.ordinal()][0] = "...";
			credentials[ServiceNames.RR.ordinal()][1] = "...";
			credentials[ServiceNames.RR.ordinal()][2] = "...";
			System.out.println("Couldn't receive credentials for 3");
		}
		
		if(credentials[4][0] == null || credentials[4][1] == null || credentials[4][2] == null) {
			credentials[ServiceNames.STT.ordinal()][0] = "...";
			credentials[ServiceNames.STT.ordinal()][1] = "...";
			credentials[ServiceNames.STT.ordinal()][2] = "...";
			System.out.println("Couldn't receive credentials for 4");
		}
		
		if(credentials[5][0] == null || credentials[5][1] == null || credentials[5][2] == null) {
			credentials[ServiceNames.TTS.ordinal()][0] = "...";
			credentials[ServiceNames.TTS.ordinal()][1] = "...";
			credentials[ServiceNames.TTS.ordinal()][2] = "...";
			System.out.println("Couldn't receive credentials for 5");
		}
		
		if(credentials[6][0] == null || credentials[6][1] == null || credentials[6][2] == null) {
			credentials[ServiceNames.DC.ordinal()][0] = "...";
			credentials[ServiceNames.DC.ordinal()][1] = "...";
			credentials[ServiceNames.DC.ordinal()][2] = "...";
			System.out.println("Couldn't receive credentials for 6");
		}
		
		if(credentials[7][0] == null || credentials[7][1] == null || credentials[7][2] == null) {
			credentials[ServiceNames.CI.ordinal()][0] = "...";
			credentials[ServiceNames.CI.ordinal()][1] = "...";
			credentials[ServiceNames.CI.ordinal()][2] = "...";
			System.out.println("Couldn't receive credentials for 7");
		}
		
	}
	
	private void extractCredentials(String Service_Name, JsonValue vcapJson, int index) {	  	

		        	JsonValue vcapServiceValue = vcapJson.asObject().get(Service_Name);
		        	if (vcapServiceValue != null && vcapServiceValue.isArray()) {
		        		JsonArray vcapServiceArray = vcapServiceValue.asArray();
		        		if(vcapServiceArray != null && vcapServiceArray.size()>0) {
		        			JsonValue vcapServiceArrayEntry = vcapServiceArray.get(0);
		        			if(vcapServiceArrayEntry != null && vcapServiceArrayEntry.isObject()) {
		        				JsonValue vcapServiceCredentials = vcapServiceArrayEntry.asObject().get("credentials");
		        				if (vcapServiceCredentials != null && vcapServiceCredentials.isObject()) {
		        					JsonObject vcapServiceCredentialsObject = vcapServiceCredentials.asObject();
		        					JsonValue temp = vcapServiceCredentialsObject.get("username");
		        					if (temp != null) {
		        						credentials[index][0] = temp.toString().replaceAll("\"", "");
		        					}
		        					temp = vcapServiceCredentialsObject.get("password");
		        					if (temp != null) {
		        						credentials[index][1] = temp.toString().replaceAll("\"", "");
		        					}
		        					temp = vcapServiceCredentialsObject.get("url");
		        					if (temp != null) {
		        						credentials[index][2] = temp.toString().replaceAll("\"", "");
		        					}
		        				}
		        			}
		        			
		        		}
		        	}
		}
	
	@POST
	@Path("/test")
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	public String test (@FormParam("question") String question) {
		return "uno" + question;
	}
	
	@POST
	@Path("/classifier")
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	public String testClassifier (@FormParam("question") String question) {
		
		Request request = null;
		
		try {
			URI converseURI = new URI("https://gateway.watsonplatform.net/natural-language-classifier/api/v1/classifiers/" + classifierId + "/classify?text=" + question.replaceAll(" ", "%20").replaceAll("\\?", "%3F")).normalize();
			request = Request.Get(converseURI);
			Executor executor = Executor.newInstance().auth(credentials[ServiceNames.NLC.ordinal()][0],
					credentials[ServiceNames.NLC.ordinal()][1]);
			org.apache.http.client.fluent.Response response = executor.execute(request);
			HttpResponse httpResponse = response.returnResponse();

			return EntityUtils.toString(httpResponse.getEntity());
		} catch (Exception e) {
			return null;
		}
	}
	
	@POST
	@Path("/dialog")
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	public String test2 (@FormParam("path") String path, @FormParam("input") String input,
			@FormParam("clientID") String clientID, @FormParam("conversationID") String conversationID) {

		Request request = null;
		try {
			if (path.equals(CONVERSATION)) {
				request = conversation(input, conversationID, clientID);
			} else if (path.equals(PROFILE)) {
				request = profile(clientID);
			}
			Executor executor = Executor.newInstance().auth(credentials[ServiceNames.DIALOG.ordinal()][0], 
					credentials[ServiceNames.DIALOG.ordinal()][1]);
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
	 * @param req the HTTP request
	 * @return the request
	 * @throws URISyntaxException 
	 */
	private Request profile(String clientID) throws URISyntaxException {
		URI converseURI = new URI(credentials[ServiceNames.DIALOG.ordinal()][2] + "/v1/dialogs/" + dialogId + "/profile?client_id="+ clientID).normalize();
		return Request.Get(converseURI);
	}

	private Request conversation(String input, String conversationID, String clientID) throws URISyntaxException {
		URI converseURI = new URI(credentials[ServiceNames.DIALOG.ordinal()][2] + "/v1/dialogs/" + dialogId+ "/conversation").normalize();
		return Request.Post(converseURI).bodyForm(
			Form.form()
				.add("input", input)
				.add("client_id", clientID)
				.add("conversation_id", conversationID)
				.build());
	}

	@GET
	public String getInformation() {
		return "&nbsp;&nbsp;&nbsp;&nbsp;Welcome to <span class=\"blue\">PalmQA</span>";
	}
	
	private Response credentialsError() {
		return Response.status(Response.Status.BAD_REQUEST).header("Pragma", "no-cache").header("Cache-Control", "no-cache").entity("{\"error\" : \"Credentials not found.\"}").build();
	}

}
