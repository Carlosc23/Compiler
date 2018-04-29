package intermediate;

import java.util.ArrayList;
import java.util.HashMap;

/*
@author: Carlos Calderon
@version: 2.0
@since: 2.0
Class that represents a template to intermediate code
*/
public class Intercode {

    ArrayList<Quadruple> listQuad;
    HashMap<String,String> listRegisters= new HashMap<String,String>();

    public Intercode() {
        listQuad = new ArrayList<>();
    }
    public void addQuadruple(Quadruple q){
        listQuad.add(q);
    }

}
