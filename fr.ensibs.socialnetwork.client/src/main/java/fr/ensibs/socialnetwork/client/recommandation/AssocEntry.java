package fr.ensibs.socialnetwork.client.recommandation;

import net.jini.core.entry.Entry;

import java.rmi.MarshalledObject;

public class AssocEntry implements Entry{
    public Integer type;
    public String source;
    public String target;

    public AssocEntry(){};

    public AssocEntry(Integer type,String source, String target){
        this.type = type;
        this.source = source;
        this.target = target;
    }
}
