package semantic;

import grammar.DecafBaseVisitor;
import grammar.DecafParser;

import java.util.ArrayList;
import java.util.Stack;

/*
@author: Carlos Calderon
@version: 1.1
@since: 1.0
*/
public class EvalVisitor extends DecafBaseVisitor<String> {
    /* Attributes*/
    private Integer scope_counter; // id to have control in where scope the compilation goes
    private Stack<SymbolTable> symbolTablePerScope; // table
    private ArrayList<SymbolTable> symbolTablePerScopeArray;
    private SymbolTable globalTable; //global scope
    private String locationDotLocation;
    private StringBuffer errors; //Variable to display in the GUI the semantic errors
    private String methodReturnType;
    private boolean visitReturnBlock;
    private boolean visitMain; // Variable to know in other methods beside methodDeclaration if main is present.

    public StringBuffer getErrors() {
        return errors;
    }


    /**
     * Constructor that initialize variables
     *
     */
    public EvalVisitor(){
        scope_counter = 0;
        symbolTablePerScope = new Stack<>();
        symbolTablePerScopeArray = new ArrayList<>();
        scope_counter += 1;
        globalTable = new SymbolTable(scope_counter, null);
        errors = new StringBuffer();
        methodReturnType = "";
        visitReturnBlock = false;
        visitMain = false;
    }

    //Declaration Scope

    /**
     *  visitProgram is the first method  that executes so first push a globaltable to a stack that saves
     *  tables.
     * @param ctx
     * @return
     */
    @Override
    public String visitProgram(DecafParser.ProgramContext ctx){
        System.out.println("visitProgram");
        locationDotLocation = "";
        symbolTablePerScope.push(globalTable);
        System.out.println("--Scope control : " + String.valueOf(scope_counter));
        System.out.println("******************************************************");
        String result = visitChildren(ctx);
        System.out.println("nuestros"+symbolTablePerScope.peek().getscopes().toString());

        if((symbolTablePerScope.peek().verify("main", 0) == 1) || visitMain ){
            symbolTablePerScope.pop();
            System.out.println(symbolTablePerScope.size());
            //< System.out.println("Symbol Table "+SymbolTable);
            return result;
        }
        else {
            errors.append("***Error 3.***\n-->Decaf.MissingMainMethod\n");
            errors.append("---->There is no main method declared in the class\n");
            System.out.println(errors);
            return "Error";
        }

    }



    /**
     * visitMethodDeclaration is the the part of the syntax tree where the methods are declared.
     * In normal conditions (without parameters) it only has 5 parameters
     * @param ctx
     * @return
     */
    @Override
    public String visitMethodDeclaration(DecafParser.MethodDeclarationContext ctx){
        System.out.println("visitMethodDeclaration");
        ArrayList<String> parameters = new ArrayList<>(); // List of parameters of a method
        String signature = ""; // String to represent the signature of a method
        String id = ctx.getChild(1).getText();
        if(id.equals("main")){ // If the id of the method is main (which represents "main") a flag is raised
            System.out.println("Oh wow im in the main method");
            visitMain = true; // Flag to know if this methodDeclaration is the main method
        }

        String varType = ctx.getChild(0).getText();
        methodReturnType = varType;
        /* This condition verifies if a method has parameters, the number is 5 because in normal
        * conditions it will be the number of children that always appear.
        */
        if(ctx.getChildCount() > 5){
            System.out.println("Tengo mas de 5");
            Integer i = 0;
            while(i<ctx.getChildCount()-5){
                //If the parameter is not a comma
                if(!ctx.getChild(3+i).getText().equals(",")){
                    System.out.println("Parameter " + i + " " + ctx.getChild(3+i).getText());
                    //Simple Parameter
                    if(ctx.getChild(3+i).getChildCount() == 2){
                        //System.out.println(ctx.getChild(3+i).getChild(0).getText());
                        parameters.add(ctx.getChild(3+i).getChild(0).getText());
                        signature += ctx.getChild(3+i).getChild(0).getText();
                    }
                    // Condition if a parameter is an array
                    else {
                        String temp_parameter = ctx.getChild(3+i).getChild(0).getText();

                        temp_parameter += ctx.getChild(3+i).getChild(2).getText();
                        temp_parameter += ctx.getChild(3+i).getChild(3).getText();
                        temp_parameter += ctx.getChild(3+i).getChild(4).getText();
                        parameters.add(temp_parameter);
                        signature += temp_parameter;

                    }
                }
                i++;
            }
        }
        // If the method has no signature (no parameters)
        if(signature.equals("")){
            System.out.println("Method : "+ id + ", doesnt have Signature ");
        }
        else {
            System.out.println("Method : "+ id + ", Signature: " + signature);
            if (id.equals("main")){
                errors.append("***Error 3.***\n-->Decaf.MissingMainMethod\n");
                errors.append("---->There is a main method in the class with signature.\n");
                System.out.println(errors);
            }
        }
        id = id + signature;
        scope_counter += 1; // A new scope is traveled
        // If the method is new theres no problem
        if(symbolTablePerScope.peek().verify(id, 0) == 0){
            System.out.println("Heres the id: "+id);
            symbolTablePerScope.peek().insert(id, new Symbol(id, parameters, varType));
            //Make a reference to the father of the scope of the current method
            SymbolTable symbTable = new SymbolTable(scope_counter, symbolTablePerScope.peek());
            //Make a reference to the children of the actual scope
            symbolTablePerScope.peek().getChildren().add(symbTable);
            //Add to the stack the scope of the new method
            symbolTablePerScope.push(symbTable);
        }
        else {
            errors.append("***Error 1.***\n-->Decaf.MethodRepeatedInScope\n ");
            errors.append("---> on variable: "+id+",");
            errors.append(" in line: ");
            errors.append(ctx.getStart().getLine());
            System.out.println(errors);
            methodReturnType = "";
            symbolTablePerScope.pop();
            return "Error";
        }

        System.out.println("--Scope control : "+scope_counter);
        System.out.println("******************************************************");
        String result = visitChildren(ctx);
        System.out.println("libros"+methodReturnType);
        System.out.println(visitReturnBlock);
        if(visitReturnBlock && methodReturnType.equals("void")){
            System.out.println("Somos void");
            errors.append("--MethodDeclaration ");
            errors.append(id);
            errors.append(" in line ");
            errors.append(ctx.getStart().getLine());
            errors.append(" return void but has a return block with other type");
            System.out.println(errors);
            result = "Error";
        }

        else if(!methodReturnType.equals("void") && !visitReturnBlock){
            errors.append("--MethodDeclaration ");
            errors.append(id);
            errors.append(" in line ");
            errors.append(ctx.getStart().getLine());
            errors.append(" return block missing");
            System.out.println(errors);
            result = "Error";
        }
        methodReturnType = "";
        visitReturnBlock = false;
        symbolTablePerScope.pop();
        System.out.println("******************************************************");
        return result;
    }

    @Override
    public String visitParameterDeclaration(DecafParser.ParameterDeclarationContext ctx){
        System.out.println("visitParameterDeclaration");
        String varType = ctx.getChild(0).getText();
        String id = ctx.getChild(1).getText();
        //parameterType ID LCORCH RCORCH
        if((symbolTablePerScope.peek().verify(id, 0) == 0) || (symbolTablePerScope.peek().verify(id, 0) != 1)){
           // Condition if it is an array
            if(ctx.getChildCount() == 4){
                varType = varType + "[]";
                System.out.println("Parametro corchete");
                symbolTablePerScope.peek().insert(id, new Symbol(id, true, 0, varType));
            }
            //parameterType ID
            else {
                System.out.println("Parametro normal");
                symbolTablePerScope.peek().insert(id, new Symbol(id, false, 0, varType));
            }
            System.out.println("******************************************************");
            return "";
        }
        // If parameter is already defined
        else {
            errors.append("***Error 1.***\n-->Decaf.IdRepeatedInScope\n ");
            errors.append("---> on variable: "+id+",");
            errors.append(" in line: ");
            errors.append(ctx.getStart().getLine());
            System.out.println(errors);
            System.out.println("******************************************************");
            return "Error";
        }
    }

    /**
     * visitInt_literal is the part of the syntax tree where a number of type Integer
     * is represented. Usually used in declaredMethodCall or assignation.
     * @param ctx
     * @return
     */
    @Override
    public String visitInt_literal(DecafParser.Int_literalContext ctx){
        System.out.println("__visitInt_literal, " + ctx.getText());
        return "int";
    }

    @Override
    public String visitChar_literal(DecafParser.Char_literalContext ctx){
        System.out.println("__visitCharLiteral, " + ctx.getText());
        return "char";
    }
    @Override
    public String visitBool_literal(DecafParser.Bool_literalContext ctx){
        System.out.println("__visitBool_literal, " + ctx.getText());
        return "boolean";
    }
    /**
     * visitAssignation is the part of the syntax tree where a value is assigned to a variable
     * previously declared. If not, it returns an error.
     * @param ctx
     * @return
     */
    @Override
    public String visitAssignation(DecafParser.AssignationContext ctx){
        System.out.println("visitAssignation");
        System.out.println(String.valueOf(ctx.getChildCount()));
        //location EQ (expression | scan) DOTCOMMA
        System.out.println("******************************************************");
        // Visit location, it refers to the specific variable where the value will be stored.
        String location = visit(ctx.getChild(0));
        System.out.println("******************************************************");
        // It refers to the symbol "="
        String eq = ctx.getChild(1).getText();
        // It refers to the type of expresion that would be stored in the location
        // Basically that location and expression are the same type, is the purpose
        String expressionscan = visit(ctx.getChild(2));
        //print
        System.out.println("**locationType : "+location);
        System.out.println("**eq : "+eq);
        System.out.println("**(expression|scan)Type : "+expressionscan);
        //Return Error if types are different
        if(location.equals(expressionscan) || location.equals("Error")){
            return location;
        }
        else {
            errors.append("***Error 14.***\n-->Decaf.AssignationException\n ");
            errors.append("in line "+ctx.getStart().getLine());
            errors.append(" the types are different\n");
            System.out.println(errors);
            return "Error";
        }
    }

    /**
     * visitVariable is the method where the method visitLocation searches if the name
     * of the variable used to store a value/expression exists in scope. If not exists
     * returns error.
     * @param ctx
     * @return
     */
    public String visitVariable(DecafParser.VariableContext ctx){
        System.out.println("******************************************************");
        System.out.println("visitVariable");
        String id = ctx.getChild(0).getText();
        System.out.println(id);
        Integer scope_number_up = symbolTablePerScope.peek().verify(id, 0);
        // This condition verifies if the variable exists in current scope or in
        //scopte parent
        if(scope_number_up!= 0){
            System.out.println(String.valueOf(scope_number_up));
            System.out.println(symbolTablePerScope.peek().getType(id, scope_number_up));
            return symbolTablePerScope.peek().getType(id, scope_number_up);
        }
        // If the variable doesnt exists it will return Error 2
        errors.append("***Error 2.***\n-->Decaf.VariableNotFound\n ");
        errors.append("--Variable ");
        errors.append(id);
        errors.append(" in line ");
        errors.append(ctx.getStart().getLine());
        errors.append("  not found the variable\n");
        System.out.println(errors);
        return "Error";
    }
    @Override
    public String visitLocation(DecafParser.LocationContext ctx){
        System.out.println("visitLocation");
        System.out.println(locationDotLocation);
        return visitChildren(ctx);


    }

    /**
     *  Method that visit nodes in the tree. It doesnt permit repeated values.
     * @param ctx
     * @return
     */
    @Override
    public String visitVarDeclaration(DecafParser.VarDeclarationContext ctx){
        System.out.println("visitVarDeclaration");
        String varType = ctx.getChild(0).getText();
        String id = ctx.getChild(1).getText();
        System.out.println(varType+" "+id);
        // If variable is not in the scope
        if((symbolTablePerScope.peek().verify(id, 0) == 0) || (symbolTablePerScope.peek().verify(id, 0) != 1)){
            //varType (ID [ NUM ] ;)
            // If variable is a array
            if(ctx.getChildCount() == 6){
                Integer arraySize = Integer.parseInt(ctx.getChild(3).getText());
                System.out.println(arraySize);
                if(arraySize<=0){
                    errors.append("***Error 4.***\n-->Decaf.ArraySizeException\n ");
                    errors.append("---> on variable: "+id+",");
                    errors.append(" in line: ");
                    errors.append(ctx.getStart().getLine());
                    System.out.println(errors);
                }
                varType = varType + "[]";
                symbolTablePerScope.peek().insert(id, new Symbol(id, true, arraySize, varType));
            }
            // Condition for only a simple declaration ( varType ID DOTCOMMA)
            else{

                symbolTablePerScope.peek().insert(id, new Symbol(id, false, 0, varType));
            }
            System.out.println("******************************************************");
            String result = visitChildren(ctx);
            return result;
        }
        // If variable is already in scope, error
        else {
            System.out.println("Entro aqui si se repitio");
            errors.append("***Error 1.***\n-->Decaf.IdRepeatedInScope\n ");
            errors.append("---> on variable: "+id+",");
            errors.append(" in line: ");
            errors.append(ctx.getStart().getLine());
            System.out.println(errors);
            return "Error";
        }
    }


    //TODO comment in javadoc this methods
    /**
     *
     * @param ctx
     * @return
     */
    @Override
    public String visitBasic(DecafParser.BasicContext ctx){
        System.out.println("visitBasic");
        System.out.println(ctx.getText());
        String result = visitChildren(ctx);
        System.out.println(result);
        System.out.println("Soy todo lo que soy");
        System.out.println("******************************************************");
        return result;
    }


   /* @Override
    public String visitBlock(DecafParser.BlockContext ctx){
        System.out.println("VisitBlock");
        visit(ctx.getChild(1));
        return "";
    }*/
    /**
     * @param ctx
     * @return
     */
   @Override
    public String visitReturnBlock(DecafParser.ReturnBlockContext ctx){
        System.out.println("visitReturnBlock");
        System.out.println(methodReturnType);
        visitReturnBlock = true;
        //RETURN (nExpression) DOTCOMMA ;
        String currentReturnType = visit(ctx.getChild(1));
        System.out.println("currentReturnType"+currentReturnType);
        if(methodReturnType.equals(currentReturnType)){
            System.out.println("The return and the type of the method is the same");
            return "";
        }
        errors.append("--ReturnBlock Method type is ");
        errors.append(methodReturnType);
        errors.append(" and the return type is ");
        errors.append(currentReturnType);
        errors.append(" in line ");
        errors.append(ctx.getStart().getLine());
        System.out.println(errors);
        return "Error";
    }

    /**
     * @param ctx
     * @return
     */
    @Override
    public String visitDeclaredMethodCall(DecafParser.DeclaredMethodCallContext ctx){
        System.out.println("visitDeclaredMethodCall");
        String id = ctx.getChild(0).getText();
        String signature = "";
        //It will verify if the children are more than 3, because the minimum children
        // are ID ( ), in case it the call has more than 3 parameters it will enter in this condition
        if(ctx.getChildCount() > 3){
            Integer i = 0;
            while(i<ctx.getChildCount()-3){
                if(!ctx.getChild(2+i).getText().equals(",")){
                    System.out.println("Parameter " + i + " " + ctx.getChild(2+i).getText());
                    signature += visit(ctx.getChild(2+i).getChild(0));
                }
                i++;
            }
        }

        if(signature.equals("")){
            System.out.println("Method : "+ id + ", no Signature ");
        } else {
            System.out.println("Method : "+ id + ", Signature: " + signature);
        }
        id = id + signature; // This line is important because the name of the methods are
        // in the scope are in the  format idsignature
        // If the method doesnt exist in any scope it will not return a result
        if(symbolTablePerScope.peek().verify(id, 0) != 0){
            Integer scope_number_up = symbolTablePerScope.peek().verify(id, 0);
            System.out.println("visitDeclaredMethodCall : "+String.valueOf(scope_number_up));
            String result = symbolTablePerScope.peek().getType(id, scope_number_up);
            System.out.println(result);
            return result;
        }

        errors.append("***Error 5.***\n-->Decaf.BadMethodCall\n");
        errors.append(id);
        errors.append(" in line ");
        errors.append(ctx.getStart().getLine());
        errors.append("  not found the method\n");
        System.out.println(errors);
        return "Error";

    }




}