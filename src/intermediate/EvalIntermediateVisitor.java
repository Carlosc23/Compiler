package intermediate;

import grammar.DecafBaseVisitor;
import grammar.DecafParser;
import semantic.Symbol;
import semantic.SymbolTable;

import java.util.ArrayList;
import java.util.Stack;

import static intermediate.Operator.*;

public class EvalIntermediateVisitor extends DecafBaseVisitor<String> {
    /* Attributes*/
    private Integer scope_counter; // id to have control in where scope the compilation goes
    private Stack<Intercode> listInterCode = new Stack<>();
    private Intercode globalCode;
    private String methodReturnType;
    private int counter;

    /**
     * Constructor that initialize variables
     */
    public EvalIntermediateVisitor() {
        globalCode = new Intercode();
        methodReturnType = "";
        counter = 0;
    }

    //Declaration Scope

    /**
     * visitProgram is the first method  that executes so first push a globaltable to a stack that saves
     * tables.
     *
     * @param ctx
     * @return
     */
    @Override
    public String visitProgram(DecafParser.ProgramContext ctx) {
        System.out.println("visitProgramInter");
        String result = visitChildren(ctx);
        return "";
    }


    /**
     * visitMethodDeclaration is the the part of the syntax tree where the methods are declared.
     * In normal conditions (without parameters) it only has 5 parameters
     *
     * @param ctx
     * @return
     */
    @Override
    public String visitMethodDeclaration(DecafParser.MethodDeclarationContext ctx) {
        System.out.println("visitMethodDeclarationInter");
        Quadruple function;
        ArrayList<String> parameters = new ArrayList<>(); // List of parameters of a method
        String signature = ""; // String to represent the signature of a method
        String id = ctx.getChild(1).getText();
        // Case 1 : Main method
        if (id.equals("main")) { // If the id of the method is main (which represents "main") a flag is raised
            System.out.println("Oh wow im in the main method");
        }
        String varType = ctx.getChild(0).getText();
        methodReturnType = varType;
        listInterCode.push(globalCode);
        function = new Quadruple(id, LAB_FUNCSTART);
        listInterCode.peek().addQuadruple(function);
        String result = visitChildren(ctx);
        function = new Quadruple(id, LAB_FUNCEND);
        listInterCode.peek().addQuadruple(function);
        System.out.println(listInterCode.peek().listQuad.toString());
        return result;
    }

    @Override
    public String visitParameterDeclaration(DecafParser.ParameterDeclarationContext ctx) {
        System.out.println("visitParameterDeclaration");
        return "";
    }

    /**
     * visitInt_literal is the part of the syntax tree where a number of type Integer
     * is represented. Usually used in declaredMethodCall or assignation.
     *
     * @param ctx
     * @return
     */
    @Override
    public String visitInt_literal(DecafParser.Int_literalContext ctx) {
        System.out.println("__visitInt_literal, " + ctx.getText());
        return ctx.getText();
    }

    @Override
    public String visitChar_literal(DecafParser.Char_literalContext ctx) {
        System.out.println("__visitCharLiteral, " + ctx.getText());
        return "char";
    }

    @Override
    public String visitBool_literal(DecafParser.Bool_literalContext ctx) {
        System.out.println("__visitBool_literal, " + ctx.getText());
        return "boolean";
    }

    /**
     * visitAssignation is the part of the syntax tree where a value is assigned to a variable
     * previously declared. If not, it returns an error.
     *
     * @param ctx
     * @return
     */
    @Override
    public String visitAssignation(DecafParser.AssignationContext ctx) {
        System.out.println("visitAssignationInter");
        System.out.println(String.valueOf(ctx.getChildCount()));
        System.out.println("******************************************************");
        // Visit location, it refers to the specific variable where the value will be stored.
        System.out.println("Porter1");
        String location = visit(ctx.getChild(0));
        System.out.println("Porter2");
        System.out.println(location);
        System.out.println("******************************************************");
        String expressionscan = visit(ctx.getChild(2));
        System.out.println("******************************************************");
        System.out.println("ExpressionScan -->"+expressionscan);
        System.out.println("******************************************************");
        Quadruple function = new Quadruple(expressionscan,null,location,LAB_ASSIGN);
        listInterCode.peek().addQuadruple(function);
        return "";
    }

    @Override
    public String visitArrayVariable(DecafParser.ArrayVariableContext ctx) {
        System.out.println("visitArrayVariableInter");
        String id = ctx.getChild(0).getText();
        String number = visit(ctx.getChild(2));
        System.out.println("El numero es: "+number);
        return id+"["+number+"]";
    }

    @Override
    public String visitParameterType(DecafParser.ParameterTypeContext ctx) {
        System.out.println("visitParameterType");
        return "Error";
    }

    /**
     * visitVariable is the method where the method visitLocation searches if the name
     * of the variable used to store a value/expression exists in scope. If not exists
     * returns error.
     *
     * @param ctx
     * @return
     */
    public String visitVariable(DecafParser.VariableContext ctx) {
        System.out.println("******************************************************");
        System.out.println("visitVariable");
        String id = ctx.getChild(0).getText();
        System.out.println(id);
        return id;
    }

    @Override
    public String visitLocation(DecafParser.LocationContext ctx) {
        System.out.println("visitLocation");
        return visitChildren(ctx);

    }

    /**
     * Method that visit nodes in the tree. It doesnt permit repeated values.
     *
     * @param ctx
     * @return
     */
    @Override
    public String visitVarDeclaration(DecafParser.VarDeclarationContext ctx) {
        System.out.println("visitVarDeclarationInter");
        String id = ctx.getChild(1).getText();
        return "";
    }

    /**
     * @param ctx
     * @return
     */
    @Override
    public String visitBasic(DecafParser.BasicContext ctx) {
        System.out.println("visitBasic");
        String result = visitChildren(ctx); // Children could be call of methods
        return result;
    }

    @Override
    public String visitBasicExpression(DecafParser.BasicExpressionContext ctx) {
        System.out.println("visitBasicExpression");
        System.out.println(ctx.getChild(0));
        System.out.println("Todotodo");
        if (ctx.getChildCount() >= 2) {
            System.out.println("Tengo 2 ");
            String b = ctx.getChild(0).getText();
            System.out.println();
            String a = visit(ctx.getChild(1));
            System.out.println("--" + a);
            System.out.println("proye");
            String id = ctx.getChild(1).getText();
        }
        return visitChildren(ctx);
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
    public String visitReturnBlock(DecafParser.ReturnBlockContext ctx) {
        System.out.println("visitReturnBlock");
        return "Error";
    }

    /**
     * visitDeclaredMethod represents the part of the syntax tree where a method
     * is called for something with parameters or without them.
     *
     * @param ctx
     * @return
     */
    @Override
    public String visitDeclaredMethodCall(DecafParser.DeclaredMethodCallContext ctx) {
        System.out.println("visitDeclaredMethodCall");
        return "Error";

    }

    /**
     * visitIfBlock represents the part of the syntax tree where it is declared an
     * if statement. Also it generates a new Scope and a new father and maybe
     * childrens scopes.
     *
     * @param ctx
     * @return
     */
    @Override
    public String visitIfBlock(DecafParser.IfBlockContext ctx) {
        System.out.println("visitIfBlock");
        return "";
    }


    /**
     * @param ctx
     * @return
     */
    @Override
    public String visitWhileBlock(DecafParser.WhileBlockContext ctx) {
        System.out.println("visitWhileBlock");
        return "";
    }

    @Override
    public String visitOrExpression(DecafParser.OrExpressionContext ctx) {
        System.out.println("visitOrExpressionInter");
        System.out.println(ctx.getChildCount());
        // If it the type exp || exp
        if (ctx.getChildCount() == 3) {
            //orExpression OR andExpression
            String orExpression = visit(ctx.getChild(0));
            String or = visit(ctx.getChild(1));
            String andExpression = visit(ctx.getChild(2));
            //print
            System.out.println("**orExpressionType : " + orExpression);
            System.out.println("**or : " + or);
            System.out.println("**andExpressionType : " + andExpression);
            if (orExpression.equals(andExpression)) {
                return "boolean";
            }
        } else {
            //andExpression
            String andExpression = visit(ctx.getChild(0));
            System.out.println("**andExpressionType : " + andExpression);
            return andExpression;
        }
        return "";
    }

    @Override
    public String visitAndExpression(DecafParser.AndExpressionContext ctx) {
        System.out.println("visitAndExpression");
        System.out.println(String.valueOf(ctx.getChildCount()));
        String t = "t";
        if(ctx.getChildCount() == 3){
            //andExpression AND equalsExpression
            String andExpression = visit(ctx.getChild(0));
            System.out.println("--------------------------------------");
            String and = ctx.getChild(1).getText();
            System.out.println("--------------------------------------");
            String equalsExpression = visit(ctx.getChild(2));
            //print
            System.out.println("**andExpressionType : "+andExpression);
            System.out.println("**and : "+and);
            System.out.println("**equalsExpressionType : "+equalsExpression);
            t+=counter;
            if (and.equals("&&")){
                Quadruple function = new Quadruple(andExpression,equalsExpression,t,LAB_AND);
                listInterCode.peek().addQuadruple(function);
            }
            counter+=1;
            return t;
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
            }
        }
        else {
            //relationExpression
            String relationExpression = visit(ctx.getChild(0));
            System.out.println("**relationExpressionType : "+relationExpression);
            return relationExpression;
        }
        return "";
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
            }
        }
        else {
            //addSubsExpression
            String addSubsExpression = visit(ctx.getChild(0));
            System.out.println("**addSubsExpression : "+addSubsExpression);
            return addSubsExpression;
        }
        return "";
    }

    //AddSubs and MulDiv operations

    @Override
    public String visitAddSubsExpression(DecafParser.AddSubsExpressionContext ctx){
        System.out.println("visitAddSubsExpression");
        System.out.println(String.valueOf(ctx.getChildCount()));
        String t = "t";
        if(ctx.getChildCount() == 3){
            System.out.println("The reason");
            System.out.println(ctx.getChild(0).getText());
            System.out.println(ctx.getChild(1).getText());
            System.out.println(ctx.getChild(2).getText());
            //addSubsExpression as_op mulDivExpression
            String addSubsExpression = visit(ctx.getChild(0));
            String as_op = ctx.getChild(1).getText();
            String mulDivExpression = visit(ctx.getChild(2));
            //print
            System.out.println("##############################################################");
            System.out.println("**addSubsExpressionType : "+addSubsExpression);
            System.out.println("**as_op : "+as_op);
            System.out.println("**mulDivExpressionType : "+mulDivExpression);
            t+=counter;
            if (as_op.equals("+")){
                Quadruple function = new Quadruple(addSubsExpression,mulDivExpression,t,LAB_ADD);
                listInterCode.peek().addQuadruple(function);
            }
            else {
                Quadruple function = new Quadruple(addSubsExpression,mulDivExpression,t,LAB_SUB);
                listInterCode.peek().addQuadruple(function);
            }

            counter+=1;
            System.out.println("##############################################################");
            //Return Error if types are different, and both most be int
            if((addSubsExpression.equals(mulDivExpression)) && (addSubsExpression.equals("int"))){
                return addSubsExpression;
            }
        } else {
            //MulDivExpression
            String mulDivExpression = visitChildren(ctx);
            System.out.println("**mulDivExpressionType : "+mulDivExpression);
            return mulDivExpression;
        }
        return t;

    }

    @Override
    public String visitMulDivExpression(DecafParser.MulDivExpressionContext ctx){
        System.out.println("visitMulDivExpression");
        System.out.println(String.valueOf(ctx.getChildCount()));
        String t = "t";
        if(ctx.getChildCount() == 3){
            //mulDivExpression md_op prExpression
            String mulDivExpression = visit(ctx.getChild(0));
            String md_op = ctx.getChild(1).getText();
            String prExpression = visit(ctx.getChild(2));
            //print
            System.out.println("**mulDivExpressionType : "+mulDivExpression);
            System.out.println("**md_op : " + md_op);
            System.out.println("**prExpressionType : "+prExpression);
            t+=counter;
            if (md_op.equals("*")){
                Quadruple function = new Quadruple(mulDivExpression,prExpression,t,LAB_MULT);
                listInterCode.peek().addQuadruple(function);
            }
            else{
                Quadruple function = new Quadruple(mulDivExpression,prExpression,t,LAB_DIV);
                listInterCode.peek().addQuadruple(function);
            }

            counter+=1;

        } else {
            //prExpression
            String prExpression = visitChildren(ctx);
            System.out.println("**prExpressionType : "+prExpression);
            return prExpression;
        }
        return t;
    }

    @Override
    public String visitStructDeclaration(DecafParser.StructDeclarationContext ctx){
        System.out.println("visitStructDeclaration");
        return "";
    }

}