package fr.ensibs.socialnetwork.client.recommandation;

import net.jini.core.entry.Entry;

import java.io.Serializable;

public class ListEntry implements Entry, Serializable {
    public String value;
    public String source;
    public Integer type;
    public String[] list;

    public ListEntry(){};

    public ListEntry(String value,String source,Integer type, String[] list){
        this.value = value;
        this.source = source;
        this.type = type;
        this.list = list;
    }

    public String toString(){
        String ret = "ListEntry : value - "+value+" / source - "+source+" / type - "+type+" / list : ";
        if(list != null){
            for(String s : list){
                ret += s+' ';
            }
        }else{
            ret+= " null";
        }
        return ret;
    }

}
