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
        } else {
            errors.append("***Error 3.***\n-->Decaf.MissingMainMethod\n");
            errors.append("---->There is no main method declared in the class\n");
            //System.out.println("***Error 3.***\n-->Decaf.MissingMainMethod\n");
            System.out.println(errors);
            return "Error";
        }

    }



    /**
     *
     * @param ctx
     * @return
     */
    @Override
    public String visitMethodDeclaration(DecafParser.MethodDeclarationContext ctx){
        System.out.println("visitMethodDeclaration");
        ArrayList<String> parameters = new ArrayList<>();
        String signature = "";
        String id = ctx.getChild(1).getText();
        if(id.equals("main")){
            System.out.println("Oh wow im in the main method");
            visitMain = true;
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
        scope_counter += 1;
        if(symbolTablePerScope.peek().verify(id, 0) == 0){
            System.out.println("HEred the id: "+id);
            symbolTablePerScope.peek().insert(id, new Symbol(id, parameters, varType));
            //Make a reference to the father of the scope of the current method
            SymbolTable symbTable = new SymbolTable(scope_counter, symbolTablePerScope.peek());
            //Make a reference to the children of the actual scope
            symbolTablePerScope.peek().getChildren().add(symbTable);
            //Add to the stack the scope of the new method
            symbolTablePerScope.push(symbTable);
        }
        else {
            errors.append("--MethodDeclaration ");
            errors.append(id);
            errors.append(" in line ");
            errors.append(ctx.getStart().getLine());
            errors.append(" has been already defined\n");
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
    @Override
    public String visitStructDeclaration(DecafParser.StructDeclarationContext ctx){
        System.out.println("visitStructDeclaration");
        String id = ctx.getChild(1).getText();
        System.out.println("--Scope counter : "+scope_counter);
        if(symbolTablePerScope.peek().verify(id, 0) == 0){
            //scope counter plus;
            scope_counter += 1;
            //father
            SymbolTable symbTable = new SymbolTable(scope_counter, symbolTablePerScope.peek());
            symbolTablePerScope.peek().insert(id, new Symbol(id, symbTable, id));
            //children
            symbolTablePerScope.peek().getChildren().add(symbTable);
            //new current symbTable
            symbolTablePerScope.push(symbTable);
            String result = visitChildren(ctx);
            symbolTablePerScope.pop();
            //add struct[]
            symbolTablePerScope.peek().insert(id+"[]", new Symbol(id+"[]", symbTable, id+"[]"));
            return result;
        } else {
            errors.append("--Struct ");
            errors.append(id);
            errors.append(" in line ");
            errors.append(ctx.getStart().getLine());
            errors.append(" has been already defined\n");
            return "Error";
        }
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

    //basic

    /**
     * Ya
     * @param ctx
     * @return
     */
    @Override
    public String visitBasic(DecafParser.BasicContext ctx){
        System.out.println("visitBasic");
        System.out.println(ctx.getText());
        String result = visitChildren(ctx);
        System.out.println(result);
        return result;
    }
    @Override
    public String visitExpressionInP(DecafParser.ExpressionInPContext ctx){
        System.out.println("visitExpressionInP");
        System.out.println(ctx.getText());
        return visit(ctx.getChild(1));
    }






}