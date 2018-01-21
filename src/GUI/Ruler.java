package GUI;


import java.util.ArrayList;

public class Ruler {
    private String name;

    public String getName() {
        return name;
    }

    public ArrayList<String> getTok() {
        return tok;
    }

    private ArrayList<String> tok = new ArrayList<>();

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
        return "Parent: "+ this.name+"\nChildren: "+temp;
    }
}

