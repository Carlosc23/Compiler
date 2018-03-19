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
        //symbolTablePerScopeArray = new ArrayList<>();
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
            System.out.println("Espiritu");
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
            errors.append("***Error 7.***\n-->Decaf.ValueReturnException\n ");
            errors.append("--MethodDeclaration ");
            errors.append(id);
            errors.append(" in line ");
            errors.append(ctx.getStart().getLine());
            errors.append(" return void but has a return block with other type\n");
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
        System.out.println("Porter1");
        String location = visit(ctx.getChild(0));
        System.out.println("Porter2");
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
    @Override
    public String visitArrayVariable(DecafParser.ArrayVariableContext ctx){
        System.out.println("visitArrayVariable");
        String id = ctx.getChild(0).getText();
        String number = visit(ctx.getChild(2));
        if(number.equals("int")){
            if((symbolTablePerScope.peek().verify(id, 0) != 0) && (number.equals("int"))){
                Integer scope_number= symbolTablePerScope.peek().verify(id, 0);
                if(symbolTablePerScope.peek().isArray(id, scope_number)){
                    String arrayType = symbolTablePerScope.peek().getType(id, scope_number);
                    arrayType = arrayType.substring(0, arrayType.length()-2);
                    return arrayType;
                }
            }
        }
        errors.append("--Array ");
        errors.append(id);
        errors.append(" in line ");
        errors.append(ctx.getStart().getLine());
        errors.append("  not found\n");
        return "Error";
    }
    @Override
    public String visitParameterType(DecafParser.ParameterTypeContext ctx){
        System.out.println("visitParameterType");
        if(ctx.getText().equals("int")){
            return "int";
        }
        else if(ctx.getText().equals("char")){
            return "char";
        }
        else if(ctx.getText().equals("boolean")){
            return "boolean";
        }
        return "Error";
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
        // TODO view cases of dot
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

                String type = visit(ctx.getChild(3));
                /*if(!type.equals("int")){
                    errors.append("***Error 9.***\n-->Decaf.ExpressionArrayException\n ");
                    errors.append("---> on variable: "+id+",");
                    errors.append(" in line: ");
                    errors.append(ctx.getStart().getLine());
                    System.out.println(errors);
                    return "Error";
                }*/
                Integer arraySize = Integer.parseInt(ctx.getChild(3).getText());
                System.out.println(arraySize);
                if(arraySize<=0){
                    errors.append("***Error 4.***\n-->Decaf.ArraySizeException\n ");
                    errors.append("---> on variable: "+id+",");
                    errors.append(" in line: ");
                    errors.append(ctx.getStart().getLine());
                    System.out.println(errors);
                    return "Error";
                }
                varType = varType + "[]";
                symbolTablePerScope.peek().insert(id, new Symbol(id, true, arraySize, varType));
            }
            // Condition for only a simple declaration ( varType ID DOTCOMMA)
            else{

                symbolTablePerScope.peek().insert(id, new Symbol(id, false, 0, varType));
            }
            System.out.println("******************************************************");
            return visitChildren(ctx);
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
        String result = visitChildren(ctx); // Children could be call of methods
        System.out.println(result);
        System.out.println("Soy todo lo que soy");
        System.out.println("******************************************************");
        return result;
    }
    @Override
    public String visitBasicExpression(DecafParser.BasicExpressionContext ctx){
        System.out.println("visitBasicExpression");
        System.out.println(ctx.getChild(0));
        System.out.println("Todotodo");
        if (ctx.getChildCount()>=2){
            System.out.println("Tengo 2 ");
            String b = ctx.getChild(0).getText();
            System.out.println();
            String a = visit(ctx.getChild(1));
            System.out.println("--"+ a);
            System.out.println("proye");
            String id = ctx.getChild(1).getText();
            if(b.equals("!") && !a.equals("boolean") ){
                errors.append("***Error 13.***\n-->Decaf.cond_opsTypeException\n ");
                errors.append(" in line: ");
                errors.append(ctx.getStart().getLine()+"\n");
                errors.append("Variable: "+ id +" has to be boolean\n");
                System.out.println(errors);
                return "Error";
            }
        }
        return  visitChildren(ctx);
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
       errors.append("***Error 8.***\n-->Decaf.ReturnBlockException\n ");
       errors.append("in line "+ctx.getStart().getLine()+"\n");
       errors.append("the type of the method is : "+methodReturnType+"\n");
       errors.append(" and the return type is ");
       errors.append(currentReturnType+"\n they are not the same type");
        System.out.println(errors);
        return "Error";
    }

    /** visitDeclaredMethod represents the part of the syntax tree where a method
     * is called for something with parameters or without them.
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

    /**
     * visitIfBlock represents the part of the syntax tree where it is declared an
     * if statement. Also it generates a new Scope and a new father and maybe
     * childrens scopes.
     * @param ctx
     * @return
     */
    @Override
    public String visitIfBlock(DecafParser.IfBlockContext ctx){
        System.out.println("visitIfBlock");
        scope_counter += 1;
        System.out.println("--Scope control : " + String.valueOf(scope_counter));
        System.out.println(symbolTablePerScope.peek().getScope_id());
        System.out.println(symbolTablePerScope.peek().getChildren().toString());
        //Represent a new scope with his parent
        SymbolTable ifscope = new SymbolTable(scope_counter, symbolTablePerScope.peek());
        // Add actual scope this scope to represent a children
        symbolTablePerScope.peek().getChildren().add(ifscope);
        // Add the ifscope to the stack
        symbolTablePerScope.push(ifscope);
        //If (orExpression)
        String bool = visit(ctx.getChild(2));
        System.out.println(bool);
        System.out.println("gg");
        symbolTablePerScope.pop();
        // if expression in if is not type boolean then it is an error
        if(!bool.equals("boolean")){
            errors.append("***Error 10.***\n-->Decaf.IfBlockException\n ");
            errors.append("in line "+ctx.getStart().getLine());
            errors.append(" the parameter is not a boolean type\n");
            System.out.println(errors);
            return "Error";
        }
        System.out.println("******************************************************\n");
        return "";
    }


    /**
     * @param ctx
     * @return
     */
    @Override
    public String visitWhileBlock(DecafParser.WhileBlockContext ctx){
        System.out.println("visitWhileBlock");
        scope_counter += 1;
        System.out.println("--Scope control : " + String.valueOf(scope_counter));
        System.out.println(symbolTablePerScope.peek().getScope_id());
        System.out.println(symbolTablePerScope.peek().getChildren().toString());
        //Represent a new scope with his parent
        SymbolTable whilescope = new SymbolTable(scope_counter, symbolTablePerScope.peek());
        // Add actual scope this scope to represent a children
        symbolTablePerScope.peek().getChildren().add(whilescope);
        // Add the whilescope to the stack
        symbolTablePerScope.push(whilescope);
        //while (orExpression)
        String bool = visit(ctx.getChild(2));
        System.out.println(bool);
        System.out.println("gg");
        symbolTablePerScope.pop();
        // if expression in while is not type boolean then it is an error
        if(!bool.equals("boolean")){
            errors.append("***Error 10.***\n-->Decaf.WhileBlockException\n ");
            errors.append("in line "+ctx.getStart().getLine());
            errors.append(" the parameter is not a boolean type\n");
            System.out.println(errors);
            return "Error";
        }
        System.out.println("******************************************************\n");
        return "";
    }
    @Override
    public String visitOrExpression(DecafParser.OrExpressionContext ctx){
        System.out.println("visitOrExpression");
        System.out.println(String.valueOf(ctx.getChildCount()));
        // If it the type exp || exp
        if(ctx.getChildCount() == 3){
            //orExpression OR andExpression
            String orExpression = visit(ctx.getChild(0));
            String or = visit(ctx.getChild(1));
            String andExpression = visit(ctx.getChild(2));
            //print
            System.out.println("**orExpressionType : "+orExpression);
            System.out.println("**or : "+or);
            System.out.println("**andExpressionType : "+andExpression);
            if(orExpression.equals(andExpression)){
                return "boolean";
            } else {
                errors.append("--OrExpression in line ");
                errors.append(ctx.getStart().getLine());
                errors.append(" the types are different\n");
                return "Error";
            }
        }
        else {
            //andExpression
            String andExpression = visit(ctx.getChild(0));
            System.out.println("**andExpressionType : "+andExpression);
            return andExpression;
        }
    }
    @Override
    public String visitAndExpression(DecafParser.AndExpressionContext ctx){
        System.out.println("visitAndExpression");
        System.out.println(String.valueOf(ctx.getChildCount()));
        if(ctx.getChildCount() == 3){
            //andExpression AND equalsExpression
            String andExpression = visit(ctx.getChild(0));
            String and = visit(ctx.getChild(1));
            String equalsExpression = visit(ctx.getChild(2));
            //print
            System.out.println("**andExpressionType : "+andExpression);
            System.out.println("**and : "+and);
            System.out.println("**equalsExpressionType : "+equalsExpression);
            if(!andExpression.equals(equalsExpression)){
                errors.append("--AndExpression in line ");
                errors.append(ctx.getStart().getLine());
                errors.append(" the types are different\n");
                return andExpression;
            } else {
                return "Error";
            }
        } else {
            //equalsExpression
            String equalsExpression = visitChildren(ctx);
            System.out.println("**equalsExpressionType : "+equalsExpression);
            return equalsExpression;
        }
    }

    @Override
    public String visitEqualsExpression(DecafParser.EqualsExpressionContext ctx){
        System.out.println("visitEqualsExpression");
        System.out.println(String.valueOf(ctx.getChildCount()));
        if(ctx.getChildCount() == 3){
            //equalsExpression eq_op relationExpression
            String equalsExpression = visit(ctx.getChild(0));
            String eq_op = visit(ctx.getChild(1));
            String relationExpression = visit(ctx.getChild(2));
            //print
            System.out.println("**equalsExpressionType : "+equalsExpression);
            System.out.println("**eq_op : "+eq_op);
            System.out.println("**relationExpressionType : "+relationExpression);
            //both most be boolean
            if(equalsExpression.equals(relationExpression)){
                return "boolean";
            } else {
                errors.append("***Error 12.***\n-->Decaf.eq_opsTypeException\n ");
                errors.append("in line "+ctx.getStart().getLine());
                errors.append(" the types of the operands are different\n");
                System.out.println(errors);
                return "Error";
            }
        } else {
            //relationExpression
            String relationExpression = visit(ctx.getChild(0));
            System.out.println("**relationExpressionType : "+relationExpression);
            return relationExpression;
        }
    }

    @Override
    public String visitRelationExpression(DecafParser.RelationExpressionContext ctx){
        System.out.println("visitRelationExpression");
        System.out.println(String.valueOf(ctx.getChildCount()));
        if(ctx.getChildCount() == 3){
            //relationExpression rel_op addSubsExpression
            String relationExpression = visit(ctx.getChild(0));
            String rel_op = visit(ctx.getChild(1));
            String addSubsExpression = visit(ctx.getChild(2));
            //print
            System.out.println("**relationExpresionType : "+relationExpression);
            System.out.println("**rel_op : "+rel_op);
            System.out.println("**addSubsExpression : "+addSubsExpression);
            if((relationExpression.equals(addSubsExpression)) && (relationExpression.equals("int"))){
                return "boolean";
            } else {
                errors.append("***Error 11.***\n-->Decaf.rel_opTypeException\n ");
                errors.append("in line "+ctx.getStart().getLine());
                errors.append(" the types are not int\n");
                System.out.println(errors);
                return "Error";
            }
        } else {
            //addSubsExpression
            String addSubsExpression = visit(ctx.getChild(0));
            System.out.println("**addSubsExpression : "+addSubsExpression);
            return addSubsExpression;
        }
    }

    //AddSubs and MulDiv operations

    @Override
    public String visitAddSubsExpression(DecafParser.AddSubsExpressionContext ctx){
        System.out.println("visitAddSubsExpression");
        System.out.println(String.valueOf(ctx.getChildCount()));
        if(ctx.getChildCount() == 3){
            //addSubsExpression as_op mulDivExpression
            String addSubsExpression = visit(ctx.getChild(0));
            String as_op = ctx.getChild(1).getText();
            String mulDivExpression = visit(ctx.getChild(2));
            //print
            System.out.println("**addSubsExpressionType : "+addSubsExpression);
            System.out.println("**as_op : "+as_op);
            System.out.println("**mulDivExpressionType : "+mulDivExpression);
            //Return Error if types are different, and both most be int
            if((addSubsExpression.equals(mulDivExpression)) && (addSubsExpression.equals("int"))){
                return addSubsExpression;
            }
            else {
                errors.append("***Error 11.***\n-->Decaf.arith_opTypeException\n ");
                errors.append("in line "+ctx.getStart().getLine());
                errors.append(" the types are not int\n");
                System.out.println(errors);
                return "Error";
            }
        } else {
            //MulDivExpression
            String mulDivExpression = visitChildren(ctx);
            System.out.println("**mulDivExpressionType : "+mulDivExpression);
            return mulDivExpression;
        }

    }

    @Override
    public String visitMulDivExpression(DecafParser.MulDivExpressionContext ctx){
        System.out.println("visitMulDivExpression");
        System.out.println(String.valueOf(ctx.getChildCount()));
        if(ctx.getChildCount() == 3){
            //mulDivExpression md_op prExpression
            String mulDivExpression = visit(ctx.getChild(0));
            String md_op = ctx.getChild(1).getText();
            String prExpression = visit(ctx.getChild(2));
            //print
            System.out.println("**mulDivExpressionType : "+mulDivExpression);
            System.out.println("**md_op : " + md_op);
            System.out.println("**prExpressionType : "+prExpression);
            //Return Error if types are different, and both most be int
            if((mulDivExpression.equals(prExpression)) && (mulDivExpression.equals("int"))){
                return mulDivExpression;
            } else {
                errors.append("***Error 11.***\n-->Decaf.arith_opTypeException\n ");
                errors.append("in line "+ctx.getStart().getLine());
                errors.append(" the types are not int\n");
                System.out.println(errors);
                return "Error";
            }
        } else {
            //prExpression
            String prExpression = visitChildren(ctx);
            System.out.println("**prExpressionType : "+prExpression);
            return prExpression;
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
            System.out.println(symbolTablePerScope.peek().getScope_id()+""+symbolTablePerScope.peek());
            //children
            symbolTablePerScope.peek().getChildren().add(symbTable);
            //new current symbTable
            symbolTablePerScope.push(symbTable);
            String result = visitChildren(ctx);
            symbolTablePerScope.pop();
            //add struct[]
            symbolTablePerScope.peek().insert(id+"[]", new Symbol(id+"[]", symbTable, id+"[]"));
            System.out.println(symbolTablePerScope.peek().getScope_id()+""+symbolTablePerScope.peek());
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

}