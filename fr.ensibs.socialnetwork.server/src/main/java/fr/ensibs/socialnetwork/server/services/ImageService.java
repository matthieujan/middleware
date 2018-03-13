package fr.ensibs.socialnetwork.server.services;

import java.util.HashMap;

public class ImageService {

    private HashMap<String,String> db = new HashMap<String, String>();

    public String get(String message) {
        return db.get(message);
    }

    public String put(String message) {
        String token = tokenGen();
        while(db.containsKey(token)){
            token = tokenGen();
        }
        db.put(token,message);
        return token;
    }

    private String tokenGen(){
        String token = "";
        return Math.random()+token;
    }
}
