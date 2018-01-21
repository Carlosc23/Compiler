package GUI;


import java.util.ArrayList;

public class Ruler {
    public String name;

    public String getName() {
        return name;
    }

    public ArrayList<String> getTok() {
        return tok;
    }

    public ArrayList<String> tok = new ArrayList<String>();

    public void addRuler(String gg){
        tok.add(gg);
    }
    public void setName(String name){
        this.name=name;
    }
    public String toString(){
        String temp="\n";
        for(String j:tok){
            temp+="-->"+j+"\n";
        }
        String s="Parent: "+this.name+"\nChildren: "+temp;
        return s;
    }
}

