package org.example.infraestructure.singleton;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;

public class MongoConnection {
    private static MongoConnection mongoConnection;
    public MongoClient mongoClient;
    public MongoDatabase mongoDatabase;
    private MongoConnection(){
        mongoClient = MongoClients.create("mongodb://localhost:27017");
        mongoDatabase = mongoClient.getDatabase("adapters");
    }
    public static MongoConnection getInstance(){
        if(mongoConnection == null){
            mongoConnection = new MongoConnection();
            return mongoConnection;
        }
        return mongoConnection;
    }
}
