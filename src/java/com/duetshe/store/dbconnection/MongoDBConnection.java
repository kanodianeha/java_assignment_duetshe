package com.duetshe.store.dbconnection;

import com.duetshe.store.util.PropertiesFileReader;
import com.duetshe.store.util.StringConstants;
import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.MongoException;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

import java.io.IOException;
import java.util.Properties;

import org.bson.Document;

public class MongoDBConnection {
    private static MongoClient mongoClient;
    private static MongoDatabase database; 
    private static MongoDBConnection dbConnection; 
    
    private MongoDBConnection() {
    	if(mongoClient == null) {
    		mongoClient = createConnection();
    	}
    }
    
    private static MongoClient createConnection() {
    
    	// TODO: Connection properties should be different for different environments 
        Properties connectionProps;
        
		try {
			connectionProps = PropertiesFileReader.readPropertiesFile(
					StringConstants.DB_PROPS_FILE_NAME_QA);
	
	        ConnectionString connectionString = new ConnectionString(
	        		connectionProps.getProperty(StringConstants.DB_PROPS_URI));
	        
	        MongoClientSettings settings = MongoClientSettings.builder()
	                .applyConnectionString(connectionString)
	                .build();
	
	        //This registry is required for your Mongo document to POJO conversion
	        /** CodecRegistry codecRegistry = fromRegistries(MongoClient.getDefaultCodecRegistry(),
	                fromProviders(PojoCodecProvider.builder().automatic(true).build()));
	        MongoDatabase database = mongoClient.getDatabase("duetshe").withCodecRegistry(codecRegistry);
	        MongoCollection<Trade> collection = database.getCollection("trades", Trade.class);

	        //This assigns your collection to your list of Java objects
	        List<Trade> docs = collection.find(new Document(), Trade.class).into(new ArrayList<Trade>());
	        **/ 
	        
	        // Create a new client and connect to the server
	        mongoClient = MongoClients.create(settings);
//	        try () {
	            try {
	                // Send a ping to confirm a successful connection
	            	// TODO: This can be changed to get different DBs
	                database = mongoClient.getDatabase(
	                		connectionProps.getProperty(StringConstants.DB_PROPS_DATABASE));
	                database.runCommand(new Document("ping", 1));
	                System.out.println("Pinged your deployment. "
	                		+ "You successfully connected to MongoDB!");
	            } catch (MongoException e) {
	                e.printStackTrace();
	            }
//	        }
	        return mongoClient;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
    }
    
    public static MongoDBConnection getDBConnection() {
    	if(dbConnection == null) {
    		dbConnection = new MongoDBConnection();
    	}
    	return dbConnection;
    }
    
    public static MongoClient getMongoDBClient() {
    	if(mongoClient == null) {
    		mongoClient = createConnection();
    	}
    	return mongoClient;
    }
    
    // Utility method to get database instance
    public static MongoDatabase getDB() {
        return database;
    }
 
    // Utility method to get trade collection
    public static MongoCollection<Document> getTradeCollection() {
    	if(database == null) {
    		getDBConnection();
    	}
    	return database.getCollection(StringConstants.DB_PROPS_TRADES_COLLECTION);
    }
}

