package intermediate;

import java.util.ArrayList;

/*
@author: Carlos Calderon
@version: 2.0
@since: 2.0
Class that represents a template to intermediate code
*/
public class Intercode {

    ArrayList<Quadruple> listQuad;
    int tempVariables = 0;

    public Intercode() {
        listQuad = new ArrayList<>();
    }
    public void addQuadruple(Quadruple q){
        listQuad.add(q);
    }

}
