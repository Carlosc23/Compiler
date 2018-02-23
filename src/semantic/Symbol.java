package semantic;

import java.util.ArrayList;
/*
Class that represents a Symbol. Useful in SymbolTable.
@author: Carlos Calderon
@version: 1.2
@since: 1.0
*/

public class Symbol {
    /* Attributes*/
    private Boolean isStruct;
    private Boolean isMethod;
    private Boolean isVar;
    private Boolean isArray;
    private Integer arraySize;
    private ArrayList<String> parameters;
    private String id;
    private String returnType;
    private SymbolTable variables; //For symbols that have  their own scope
    /**
     * @param id (required) name of the variable inside the program.
     * @param returnType (optional) type of variable that return something
     * @param variables (optional) variables inside the scope of the actual symbol
     */
    public Symbol(String id,  SymbolTable variables,String returnType) {
        this.id = id;
        this.returnType = returnType;
        this.variables = variables;
    }

    /**
     * @param id
     * @param parameters
     * @param returnType
     */
    public Symbol(String id, ArrayList<String> parameters, String returnType) {
        this.id = id;
        this.parameters = parameters;
        this.returnType = returnType;
    }

    /**
     * @param id
     * @param isArray
     * @param arraySize
     * @param returnType
     */
    public Symbol(String id, Boolean isArray, Integer arraySize, String returnType) {
        this.id = id;
        this.isArray = isArray;
        this.arraySize = arraySize;
        this.returnType = returnType;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    /**
     * @return
     */
    public Boolean getStruct() {
        return isStruct;
    }

    /**
     * @param struct
     */
    public void setStruct(Boolean struct) {
        isStruct = struct;
    }

    /**
     * @return
     */
    public Boolean getMethod() {
        return isMethod;
    }

    /**
     * @param method
     */
    public void setMethod(Boolean method) {
        isMethod = method;
    }

    /**
     * @return
     */
    public Boolean getVar() {
        return isVar;
    }

    /**
     * @param var
     */
    public void setVar(Boolean var) {
        isVar = var;
    }

    /**
     * @return
     */
    public Boolean getArray() {
        return isArray;
    }

    /**
     * @param array
     */
    public void setArray(Boolean array) {
        isArray = array;
    }

    /**
     * @return
     */
    public Integer getArraySize() {
        return arraySize;
    }

    /**
     * @param arraySize
     */
    public void setArraySize(Integer arraySize) {
        this.arraySize = arraySize;
    }

    /**
     * @return
     */
    public ArrayList<String> getParameters() {
        return parameters;
    }

    /**
     * @param parameters
     */
    public void setParameters(ArrayList<String> parameters) {
        this.parameters = parameters;
    }

    /**
     * @return
     */
    public String getReturnType() {
        return returnType;
    }

    /**
     * @param returnType
     */
    public void setReturnType(String returnType) {
        this.returnType = returnType;
    }

    /**
     * @return
     */
    public SymbolTable getVariables() {
        return variables;
    }

    /**
     * @param variables
     */
    public void setVariables(SymbolTable variables) {
        this.variables = variables;
    }


}