package semantic;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/*
@author: Carlos Calderon
@version: 1.2
@since: 1.0
*/
public class SymbolTable {
    /* Attributes */
    private Integer scope_id;
    private SymbolTable parent;
    private ArrayList<SymbolTable> children;
    private Map<String, Symbol> scopes;

    public Integer getScope_id() {
        return scope_id;
    }

    public void setScope_id(Integer scope_id) {
        this.scope_id = scope_id;
    }

    public SymbolTable getParent() {
        return parent;
    }

    public void setParent(SymbolTable parent) {
        this.parent = parent;
    }

    public ArrayList<SymbolTable> getChildren() {
        return children;
    }

    public void setChildren(ArrayList<SymbolTable> children) {
        this.children = children;
    }

    public Map<String, Symbol> getscopes() {
        return scopes;
    }

    public void setscopes(Map<String, Symbol> scopes) {
        this.scopes = scopes;
    }

    /**
     * @param scope_id
     * @param parent
     */
    public SymbolTable(Integer scope_id, SymbolTable parent){
        this.scope_id = scope_id;
        this.parent = parent;
        scopes = new HashMap<String, Symbol>();
        children = new ArrayList<SymbolTable>();
    }

    /**
     * @param id
     * @return
     */
    public Symbol searchSymbol(String id){

        return scopes.get(id);
    }

    /**
     * @param id
     * @param symb
     */
    public void insert(String id, Symbol symb){
        scopes.put(id, symb);
    }

    /**
     * @param id
     * @param level
     * @return
     */
    public Integer verify(String id, Integer level){
        if(this.scopes.containsKey(id)){
            System.out.println("Lo contiene");
            return level+1;
        } else {
            if(this.parent != null){
                return this.parent.verify(id, level+1);
            }else{
                return 0;
            }
        }
    }

    /**
     * @param id
     * @return
     */
    public Boolean lookupGlobal(String id) {
        return this.scopes.containsKey(id);
    }

    /**
     * @param id
     * @param level
     * @return
     */
    public String getType(String id, Integer level){
        if(level == 1){
            return scopes.get(id).getReturnType();
        } else {
            level -= 1;
            return parent.getType(id, level);
        }
    }

    /**
     * @param id
     * @param level
     * @return
     */
    public Boolean isArray(String id, Integer level){
        if(level == 1){
            return scopes.get(id).getArray();
        } else {
            level -= 1;
            return parent.isArray(id, level);
        }
    }

}