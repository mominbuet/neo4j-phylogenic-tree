/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.neo4jprotein;
//http://weblog4j.com/2013/07/22/neo4j-a-next-generation-graph-database-and-java/
import org.apache.commons.httpclient.Header;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.PutMethod;
import org.apache.commons.httpclient.methods.StringRequestEntity;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Relationship;
import org.neo4j.graphdb.RelationshipType;

/**
 *
 * @author Gun2sh
 */
public class neoStart {

    private static void registerShutdownHook(final GraphDatabaseService graphDb) {
        // Registers a shutdown hook for the Neo4j instance so that it
        // shuts down nicely when the VM exits (even if you "Ctrl-C" the
        // running application).
        Runtime.getRuntime().addShutdownHook(new Thread() {
            @Override
            public void run() {
                graphDb.shutdown();
            }
        });
    }

    /**
     * @param args the command line arguments
     */
    private static enum RelTypes implements RelationshipType {

        KNOWS
    }

    public static void main(String[] args) {
        GraphDatabaseService graphDb;
        Node firstNode;
        Node secondNode;
        Relationship relationship;
//        GraphDatabaseService graphDb = new GraphDatabaseFactory()
//    .newEmbeddedDatabaseBuilder(  )
//    .setConfig( GraphDatabaseSettings.nodestore_mapped_memory_size, "10M" )
//    .setConfig( GraphDatabaseSettings.string_block_size, "60" )
//    .setConfig( GraphDatabaseSettings.array_block_size, "300" )
//    .newGraphDatabase();
//        graphDb = new GraphDatabaseFactory().newEmbeddedDatabase("C:\\Users\\Gun2sh\\Documents\\Neo4j\\default.graphdb");
//        
//        try (Transaction tx = graphDb.beginTx()) {
//            // Database operations go here
//            tx.success();
//        }
//        firstNode = graphDb.createNode();
//        firstNode.setProperty("message", "Hello, ");
//        secondNode = graphDb.createNode();
//        secondNode.setProperty("message", "World!");
//
//        relationship = firstNode.createRelationshipTo(secondNode, RelTypes.KNOWS);
//        relationship.setProperty("message", "brave Neo4j ");
//        
//        System.out.print( firstNode.getProperty( "message" ) );
//        registerShutdownHook(graphDb);
        System.out.println("Status: " + createNode());
    }
    static String SERVER_ROOT_URI = "http://localhost:7474";

    static public void addProperty(String nodeURI,
            String propertyName,
            String propertyValue) {
        String output = null;

        try {
            String nodePointUrl = nodeURI + "/properties/" + propertyName;
            HttpClient client = new HttpClient();
            PutMethod mPut = new PutMethod(nodePointUrl);

            /**
             * set headers
             */
            Header mtHeader = new Header();
            mtHeader.setName("content-type");
            mtHeader.setValue("application/json");
            mtHeader.setName("accept");
            mtHeader.setValue("application/json");
            mPut.addRequestHeader(mtHeader);

            /**
             * set json payload
             */
            String jsonString = "" + propertyValue + "";
            StringRequestEntity requestEntity = new StringRequestEntity(jsonString,
                    "application/json",
                    "UTF-8");
            mPut.setRequestEntity(requestEntity);
            int satus = client.executeMethod(mPut);
            output = mPut.getResponseBodyAsString();

            mPut.releaseConnection();
            System.out.println("satus : " + satus);
            System.out.println("output : " + output);
        } catch (Exception e) {
            System.out.println("Exception in creating node in neo4j : " + e);
        }

    }

    static public int getServerStatus() {
        int status = 500;
        try {

            HttpClient client = new HttpClient();
            GetMethod mGet = new GetMethod(SERVER_ROOT_URI);
            status = client.executeMethod(mGet);
            mGet.releaseConnection();
        } catch (Exception e) {
            System.out.println("Exception in connecting to neo4j : " + e);
        }

        return status;
    }

    static public String createNode() {
        String output = null;
        String location = null;
        try {
            String nodePointUrl = SERVER_ROOT_URI + "/db/data/node/1";
            HttpClient client = new HttpClient();
            PostMethod mPost = new PostMethod(nodePointUrl);

            /**
             * set headers
             */
            Header mtHeader = new Header();
            mtHeader.setName("content-type");
            mtHeader.setValue("application/json");
            mtHeader.setName("accept");
            mtHeader.setValue("application/json");
            mPost.addRequestHeader(mtHeader);

            /**
             * set json payload
             */
            StringRequestEntity requestEntity = new StringRequestEntity("{}",
                    "application/json",
                    "UTF-8");
            mPost.setRequestEntity(requestEntity);
            int satus = client.executeMethod(mPost);
            output = mPost.getResponseBodyAsString();
            Header locationHeader = mPost.getResponseHeader("location");
            location = locationHeader.getValue();
            mPost.releaseConnection();
            System.out.println("satus : " + satus);
            System.out.println("location : " + location);
            System.out.println("output : " + output);
        } catch (Exception e) {
            System.out.println("Exception in creating node in neo4j : " + e);
        }

        return location;
    }
}
