package de.rwthaachen.ensemble;

import de.rwthaachen.ensemble.communication.RestServer;

/**
 * This Main class creates an EnsembleServer that uses an EnsembleHandler to query the ensemble
 * The handler asks the Dispatcher for an answer, which collects answers from all backends that are afterwards
 * merged by the Merger using a specific Strategy like a MajorityVote
 * Finally, all question/answer sets are persisted by the Database Manager
 *
 * Alternatively, a Jetty-based RESTful interface can be used
 *
 * To use: "docker-compose up" to start the data backends, web qa and YodaQA, then launch this ensemble
 * Query via Python client, which sends a question and displays an answer
 * Soon this ensemble will also be usable directly from Lucida
 *
 */
public class Main {
    private static RestServer restServer;

    public static void main(String[] args) throws Exception {
        System.out.println("Command line arguments:");
        for (String s: args) {
            System.out.println(s);
            // if(s.toLowerCase().equals("rest")) {}
        }
        restServer = new RestServer();
        restServer.start();

    }
}
