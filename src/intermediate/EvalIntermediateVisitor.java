package intermediate;

import grammar.DecafBaseVisitor;
import grammar.DecafParser;
import semantic.Symbol;
import semantic.SymbolTable;

import java.util.*;
import java.util.regex.Pattern;

import static intermediate.Operator.*;

public class EvalIntermediateVisitor extends DecafBaseVisitor<String> {
    /* Attributes*/
    private Integer scope_counter; // id to have control in where scope the compilation goes
    private Stack<Intercode> listInterCode = new Stack<>();
    private ArrayList<Intercode> listInterCode2 = new ArrayList<>();
    private Intercode globalCode;
    private String methodReturnType;
    private int counter,counterLab;
    private int counterG, counterL;
    private static String code = "";

   /* public String decode(){
        System.out.println("El tamano del stack es: "+listInterCode.size());
        LinkedList<Intercode> listInterCode2 = new LinkedList<Intercode>(listInterCode);
        System.out.println("size"+listInterCode2.size());
        for (int i =0;i<=listInterCode2.size()+1;i++){
            System.out.println(i);
            for (Quadruple q : listInterCode2.get(i).listQuad){
                if (q.getOp().equals("ASSIGN")){
                    System.out.println("MOV "+q.getRes()+","+q.getDir1());
                }
                else if (q.getOp().equals("FUNC BEGIN")){
                    System.out.println("FUNC BEGIN "+q.getRes());
                }
                else if (q.getOp().equals("FUNC END")){
                    System.out.println("FUNC END ");
                }
            }
        }

        System.out.println("flute"+listInterCode2.get(0).listQuad.toString());
        return "";
    }*/
   public String decode(ArrayList<Quadruple> Quadruples){
       System.out.println("..............................");
       for (Quadruple q: Quadruples){
           System.out.println(q.getOp());

           if (q.getOp().equals("ASSIGN")){
               System.out.println("MOV "+q.getRes()+","+q.getDir1());
               code+="MOV "+q.getRes()+","+q.getDir1()+"\n";
           }
           else if (q.getOp().equals("FUNC BEGIN")){
               System.out.println("FUNC BEGIN "+q.getRes());
               code +="FUNC BEGIN "+q.getRes()+"\n";
           }
           else if (q.getOp().equals("FUNC END")){
               System.out.println("FUNC END ");
               code +="FUNC END \n";
           }
           else if(q.getOp().equals("ADD") ||q.getOp().equals("SUB") ||q.getOp().equals("DIV") ||q.getOp().equals("MULT") ){
               System.out.println(q.getOp()+" "+q.getRes()+","+q.getDir1()+", "+q.getDir2());
               code+= q.getOp()+" "+q.getRes()+","+q.getDir1()+", "+q.getDir2()+"\n";
           }
           else if(q.getOp().equals("GREATER")){
               code+= "CMP "+q.getDir1()+", "+q.getDir2()+"\n";
               code+= "JG "+disarmif(Quadruples,q)+"\n";
           }
           else if(q.getOp().equals("LESS")){
               code+= "CMP "+q.getDir1()+", "+q.getDir2()+"\n";
               code+= "JNLE "+disarmif(Quadruples,q)+"\n";
           }
           else if(q.getOp().equals(LAB_EQUALS)){
               code+= "CMP "+q.getDir1()+", "+q.getDir2()+"\n";
               code+= "JE "+disarmif(Quadruples,q)+"\n";
               System.out.println("CMP "+q.getDir1()+", "+q.getDir2()+"\n");
               System.out.println("JE "+disarmif(Quadruples,q)+"\n");
           }

           else if(q.getOp().equals("GOTO") && q.getDir1().equals("if")){
               code+= q.getRes()+":\n";
           }
           else if(q.getOp().equals("GOTO") && q.getDir1().equals("while")){
               code+= q.getRes()+":\n";
           }
           else if(q.getOp().equals("END WHILE") && q.getDir1().equals("while")){
               code+= "loop: "+q.getRes()+"\n";
           }
           else if(q.getOp().equals("PARAM")){
               code+= q.getOp()+" "+q.getRes()+"\n";
           }
           else if(q.getOp().equals("CALL")){
               code+= q.getOp()+" "+q.getDir1()+"\n";
               if(q.getRes()!=null){
                   code+= "STORE "+q.getDir1()+"in "+q.getRes()+"\n";
               }
           }
           else if (q.getOp().equals("POP")){
                code+=q.getOp()+" "+q.getRes()+"\n";
           }

       }
       System.out.println("..............................");
       return "";
   }
   public String disarmif(ArrayList<Quadruple> Quadruples,Quadruple q){
       int index = Quadruples.indexOf(q);
       for (int i = index; i<=Quadruples.size();i++){
           if (Quadruples.get(i).getDir1().equals("if")){
               return Quadruples.get(i).getOp()+" "+Quadruples.get(i).getRes();
           }
           else if (Quadruples.get(i).getDir1().equals("while")){
               return Quadruples.get(i).getOp()+" "+Quadruples.get(i).getRes();
           }


       }
       return "";
   }
   public String decode(){
       return code;
   }
   public void imprimir(){
       System.out.println("///////////////////////////////////");
       code = "";
       String fin = "";
       for (Intercode i: listInterCode2){
           for (Quadruple q: i.listQuad){
               System.out.println(q);
               if (q.getOp().equals("ASSIGN")){
                   //System.out.println("MOV "+q.getRes()+","+q.getDir1());
                   code+="MOV "+q.getRes()+","+q.getDir1()+"\n";
               }
               else if (q.getOp().equals("FUNC BEGIN")){
                   //System.out.println("FUNC BEGIN "+q.getRes());
                   code +="FUNC BEGIN "+q.getRes()+"\n";
               }
               else if (q.getOp().equals("FUNC END")){
                   //System.out.println("FUNC END ");
                   code +="FUNC END \n";
               }
               else if(q.getOp().equals("ADD") ||q.getOp().equals("SUB") ||q.getOp().equals("DIV") ||q.getOp().equals("MULT") ){
                   //System.out.println(q.getOp()+" "+q.getRes()+","+q.getDir1()+", "+q.getDir2());
                   code+= q.getOp()+" "+q.getRes()+","+q.getDir1()+", "+q.getDir2()+"\n";
               }
               else if(q.getOp().equals("GREATER")){
                   code+= "CMP "+q.getDir1()+", "+q.getDir2()+"\n";
                   code+= "JG "+disarmif(i.listQuad,q)+"\n";
               }
               else if(q.getOp().equals("LESS")){
                   code+= "CMP "+q.getDir1()+", "+q.getDir2()+"\n";
                   code+= "JNLE "+disarmif(i.listQuad,q)+"\n";
               }
               else if(q.getOp().equals(LAB_EQUALS)){
                   code+= "CMP "+q.getDir1()+", "+q.getDir2()+"\n";
                   code+= "JE "+disarmif(i.listQuad,q)+"\n";
                   //System.out.println("CMP "+q.getDir1()+", "+q.getDir2()+"\n");
                   //System.out.println("JE "+disarmif(i.listQuad,q)+"\n");
               }

               else if(q.getOp().equals("GOTO") && q.getDir1().equals("if")){
                   code+= q.getRes()+":\n";
               }
               else if(q.getOp().equals("END IF1")){
                   code+= "GOTO "+ "END IF"+"\n";
               }
               else if(q.getOp().equals("END IF")){
                   code+= q.getOp()+"\n";
               }
               else if(q.getOp().equals("GOTO") && q.getDir1().equals("while")){
                   code+= q.getRes()+":\n";
               }
               else if(q.getOp().equals("END WHILE") && q.getDir1().equals("while")){
                   code+= "loop: "+q.getRes()+"\n";
               }
               else if(q.getOp().equals("PARAM")){
                   code+= q.getOp()+" "+q.getRes()+"\n";
               }
               else if(q.getOp().equals("CALL")){
                   code+= q.getOp()+" "+q.getDir1()+"\n";
                   if(q.getRes()!=null){
                       code+= "STORE "+q.getDir1()+"in "+q.getRes()+"\n";
                   }
               }
               else if (q.getOp().equals("POP")){
                   code+=q.getOp()+" "+q.getRes()+"\n";
               }
               else if (q.getOp().equals("FUNC END")){
                    fin = "FUNC END \n";

               }

           }
           code+=fin;

       }
   }
    /**
     * Constructor that initialize variables
     */
    public EvalIntermediateVisitor() {
        globalCode = new Intercode();
        methodReturnType = "";
        counter = 1;
        counterLab=0;
        counterG = 0 ;
        counterL = 0;
        listInterCode.push(globalCode);
        listInterCode2.add(globalCode);
        //listInterCode2 = new Queue<>();
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
        System.out.println(listInterCode.peek().listQuad.toString());
        decode(listInterCode.peek().listQuad);
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
        String t = "t";
        // Case 1 : Main method
        if (id.equals("main")) { // If the id of the method is main (which represents "main") a flag is raised
            System.out.println("Oh wow im in the main method");
        }
        String varType = ctx.getChild(0).getText();
        methodReturnType = varType;

        Intercode scopeCode = new Intercode();
        listInterCode.push(scopeCode);
        listInterCode2.add(scopeCode);
        function = new Quadruple(id, LAB_FUNCSTART);

        listInterCode.peek().addQuadruple(function);
        if(ctx.getChildCount() > 5){
            System.out.println("Tengo mas de 5");
            Integer i = 0;
            while(i<ctx.getChildCount()-5){
                //If the parameter is not a comma
                if(!ctx.getChild(3+i).getText().equals(",")){
                    System.out.println("Parameter " + i + " " + ctx.getChild(3+i).getText());
                    //t+=counter;
                    String aa = ctx.getChild(3+i).getChild(1).getText();
                    //addTempVar(aa);
                    //function = new Quadruple(aa,null,"L["+counterL+"]",LAB_ASSIGN);
                    //listInterCode.peek().addQuadruple(function);
                    //counterL+=1;

                    //Simple Parameter
                    if(ctx.getChild(3+i).getChildCount() == 2){
                        System.out.println(ctx.getChild(3+i).getChild(1).getText());
                    }
                    // Condition if a parameter is an array
                    // TODO think in this case
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
        //counterL--;
        String result = visitChildren(ctx);
        System.out.println(listInterCode.peek().listQuad.toString());
        System.out.println("*****************************");
        decode(listInterCode.peek().listQuad);
        System.out.println("*****************************");
        function = new Quadruple(id, LAB_FUNCEND);
        listInterCode.peek().addQuadruple(function);
        listInterCode.pop();



        counterL = 0;
        counter = 0;
        //TODO descomentar esto

        return result;
    }

    @Override
    public String visitParameterDeclaration(DecafParser.ParameterDeclarationContext ctx) {
        System.out.println("visitParameterDeclaration");
        String location = ctx.getChild(1).getText();
        addTempVar(location);
        String newLoc = recorrer(location);
        Quadruple function = new Quadruple(location,null,newLoc,LAB_POP);
        listInterCode.peek().addQuadruple(function);
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
        System.out.println("Mi dream: "+location);
        String newLoc = recorrer(location);

        Quadruple function = new Quadruple(expressionscan,null,newLoc,LAB_ASSIGN);
        listInterCode.peek().addQuadruple(function);
        return "";
    }
    String recorrer (String location){
        System.out.println(location);
        Stack<Intercode> tempStack = new Stack<>();
        System.out.println(listInterCode.size());
        tempStack.addAll(listInterCode);
        System.out.println(tempStack.size());
        Map<String,String> map = tempStack.peek().listRegisters;
        System.out.println(location);
        String newLoc = "";
        while (tempStack.size()!=0){
            System.out.println("you and me");
            System.out.println(tempStack.size());
            Iterator it = map.keySet().iterator();
            while(it.hasNext()) {

                String key = (String) it.next();
                System.out.println("Clave: " + key + " -> Valor: " + map.get(key));
                if (key.equals(location)){
                    System.out.println("Clave: " + key + " -> Valor: " + map.get(key));
                    newLoc = map.get(key);
                    System.out.println("Retorne");
                    return newLoc;
                }

            }
            tempStack.pop();
            System.out.println(tempStack.size());
            if(tempStack.size()!=0){
                map = tempStack.peek().listRegisters;
                System.out.println(map.toString());
            }
            System.out.println("-----------");
        }

        return location;
    }
    String recorrer (String location,boolean G){
        Stack<Intercode> tempStack = new Stack<>();
        System.out.println(listInterCode.size());
        tempStack.addAll(listInterCode);
        while(tempStack.size()>1){
            tempStack.pop();
        }
        System.out.println(tempStack.size());
        Map<String,String> map = tempStack.peek().listRegisters;
        System.out.println(location);
        String newLoc = "";
        while (newLoc.equals("")){
            System.out.println("you and me");
            Iterator it = map.keySet().iterator();
            while(it.hasNext()) {

                String key = (String) it.next();
                System.out.println("Clave: " + key + " -> Valor: " + map.get(key));
                if (key.equals(location)){
                    System.out.println("Clave: " + key + " -> Valor: " + map.get(key));
                    newLoc = map.get(key);
                    return newLoc;
                }

            }
            //tempStack.pop();
            map = tempStack.peek().listRegisters;
            System.out.println(map.toString());
            return location;
        }

        return newLoc;
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
        if (listInterCode.size()==1){
            System.out.println("Estoy en el global"+id+"G["+counterG+"]");
            listInterCode.peek().listRegisters.put(id,"G["+counterG+"]");
           // Quadruple function = new Quadruple(id,null,"G["+counterG+"]",LAB_ASSIGN );
            //listInterCode.peek().addQuadruple(function);
            counterG+=1;
        }
        else{
            System.out.println("Estoy en el local"+id+"L["+counterL+"]");
            listInterCode.peek().listRegisters.put(id,"L["+counterL+"]");
            //Quadruple function = new Quadruple(id,null,"L["+counterL+"]",LAB_ASSIGN );
            //listInterCode.peek().addQuadruple(function);
            counterL+=1;
        }
        return "";
    }
    void addTempVar(String var){
        // TODO verify nested scopes (identify with a boolean or an id the global scope)
        if (listInterCode.size()==1){
            System.out.println("Estoy en el global"+var+"G["+counterG+"]");
            if (!listInterCode.peek().listRegisters.containsKey(var)){
                listInterCode.peek().listRegisters.put(var,"G["+counterG+"]");
                Quadruple function = new Quadruple(var,null,"G["+counterG+"]",LAB_ASSIGN );
                listInterCode.peek().addQuadruple(function);
                counterG+=1;
            }

        }
        else{
            Stack<Intercode> tempStack = new Stack<>();
            System.out.println(listInterCode.size());
            tempStack.addAll(listInterCode);
            while (tempStack.size() >1){
                if (!tempStack.peek().listRegisters.containsKey(var)){
                    System.out.println("Estoy en el local"+var+"L["+counterL+"]");
                    listInterCode.peek().listRegisters.put(var,"L["+counterL+"]");
                    System.out.println("duele");
                    String var2 = recorrer(var,true);
                    Quadruple function = new Quadruple(var2,null,"L["+counterL+"]",LAB_ASSIGN );
                    listInterCode.peek().addQuadruple(function);
                    counterL+=1;
                    break;
                }
                tempStack.pop();
            }

        }
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
        String currentReturnType = visit(ctx.getChild(1));
        System.out.println("Current"+currentReturnType);
        String l = "l";
        l+=counterLab;
        currentReturnType = recorrer (currentReturnType);
        Quadruple function = new Quadruple(null,null,currentReturnType,LAB_RETURN );
        listInterCode.peek().addQuadruple(function);
        return currentReturnType;
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
        String id = ctx.getChild(0).getText();
        String t = "t";
        Pattern pattern = Pattern.compile("[\\d+]");
        String signature="";
        Quadruple function;
        //It will verify if the children are more than 3, because the minimum children
        // are ID ( ), in case it the call has more than 3 parameters it will enter in this condition
        String metodo = ctx.getText();
        //t+=counter;
        if(ctx.getChildCount() > 3){
            Integer i = 0;
            while(i<ctx.getChildCount()-3){
                if(!ctx.getChild(2+i).getText().equals(",")){
                    t+=counter;
                    System.out.println("Parameter " + i + " " + ctx.getChild(2+i).getText());
                    signature = visit(ctx.getChild(2+i).getChild(0));
                    System.out.println("Firma"+signature);
                    metodo = metodo.replace(ctx.getChild(2+i).getText(),t);
                    //String temp = recorrer(signature);
                    function = new Quadruple(signature,null,t,LAB_PARAM );
                    listInterCode.peek().addQuadruple(function);
                    System.out.println("counter: "+counter+ctx.getChild(2+i).getText());
                    System.out.println(t);
                    t = pattern.matcher(t).replaceAll("");
                    System.out.println(t);
                    counter++;
                }
                i++;
            }
        }
        System.out.println("abuela"+t);
        System.out.println(counter);
        //t = t.replace("[^t]","");
        counter--;
        t = pattern.matcher(t).replaceAll("");
        System.out.println("abuela2"+t);
        t+=counter;
        function = new Quadruple(metodo,null,t,LAB_CALL);
        listInterCode.peek().addQuadruple(function);
        counter+=1;
        return t;

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
        System.out.println(counterLab);
        String l = "l";
        counterLab+=1;
        l+=counterLab;

        //System.out.println("/////////////////////////////");
        String exp = visit(ctx.getChild(2));
        //System.out.println("/////////////////////////////");

        Quadruple function = new Quadruple("if",exp,l,LAB_GOTO);
        listInterCode.peek().addQuadruple(function);
        Intercode scopeCode = new Intercode();
        listInterCode.push(scopeCode);
        listInterCode2.add(scopeCode);

        System.out.println("/////////////////////////////");
        String returnn = visit(ctx.getChild(4));
        System.out.println("/////////////////////////////");
        System.out.println("/-----------------------/");

        System.out.println(listInterCode.peek().listQuad.toString());
        decode(listInterCode.peek().listQuad);
        listInterCode.pop();
        //function = new Quadruple("if",null,null,LAB_FIF+"1");
        //listInterCode.peek().addQuadruple(function);
        System.out.println("counterLab: "+counterLab);

        //scopeCode = new Intercode();
        //listInterCode.push(scopeCode);

        String elseblock = visit(ctx.getChild(5));
        //listInterCode.pop();
        System.out.println("/-----------------------/");
        System.out.println("-----------------------");
        System.out.println(exp);
        System.out.println(returnn);
        System.out.println("-----------------------");
        function = new Quadruple("if",null,null,LAB_FIF);
        listInterCode.peek().addQuadruple(function);
        return "";
    }

    /**
     * {@inheritDoc}
     * <p>
     * <p>The default implementation returns the result of calling
     * {@link #visitChildren} on {@code ctx}.</p>
     *
     * @param ctx
     */
    @Override
    public String visitElseBlock(DecafParser.ElseBlockContext ctx) {
        System.out.println("visitElseBlockInter");

        //Intercode scopeCode = new Intercode();
        //listInterCode.push(scopeCode);
        return visitChildren(ctx);
    }

    /**
     * @param ctx
     * @return
     */
    @Override
    public String visitWhileBlock(DecafParser.WhileBlockContext ctx) {
        System.out.println("visitWhileBlock");
        String l = "l";
        counterLab+=1;
        l+=counterLab;
        String exp = visit(ctx.getChild(2));
        Quadruple function = new Quadruple("while",exp,l,LAB_GOTO);
        listInterCode.peek().addQuadruple(function);
        Intercode scopeCode = new Intercode();
        listInterCode.push(scopeCode);
        listInterCode2.add(scopeCode);
        String block = visit(ctx.getChild(4));
        System.out.println(listInterCode.peek().listQuad.toString());
        decode(listInterCode.peek().listQuad);
        listInterCode.pop();
        function = new Quadruple("while",null,l,LAB_ENDWHILE);
        listInterCode.peek().addQuadruple(function);
        return "";
    }

    /**
     * {@inheritDoc}
     * <p>
     * <p>The default implementation returns the result of calling
     * {@link #visitChildren} on {@code ctx}.</p>
     *
     * @param ctx
     */
    @Override
    public String visitExpressionInP(DecafParser.ExpressionInPContext ctx) {
        return visit(ctx.getChild(1));
    }

    /**
     * {@inheritDoc}
     * <p>
     * <p>The default implementation returns the result of calling
     * {@link #visitChildren} on {@code ctx}.</p>
     *
     * @param ctx
     */
    @Override
    public String visitBasic(DecafParser.BasicContext ctx) {
        return visitChildren(ctx);
    }

    @Override
    public String visitPrExpression(DecafParser.PrExpressionContext ctx) {
        System.out.println("visitPrExpressionInter");
        String result = visitChildren(ctx);
        System.out.println("Darle la vuelta al mundo "+result);
        return result;
    }
    @Override
    public String visitOrExpression(DecafParser.OrExpressionContext ctx) {
        System.out.println("visitOrExpressionInter");
        System.out.println(ctx.getChildCount());
        String t = "t";
        // If it the type exp || exp
        if (ctx.getChildCount() == 3) {
            //orExpression OR andExpression
            String orExpression = visit(ctx.getChild(0));
            String or = ctx.getChild(1).getText();
            String andExpression = visit(ctx.getChild(2));
            //print
            System.out.println("**orExpressionType : " + orExpression);
            System.out.println("**or : " + or);
            System.out.println("**andExpressionType : " + andExpression);
            addTempVar(orExpression);
            addTempVar(andExpression);
            t+=counter;
            if (or.equals("||")){
                String newLoc1 = recorrer(orExpression);
                String newLoc2 = recorrer(andExpression);
                Quadruple function = new Quadruple(newLoc1,newLoc2,t,LAB_OR);
                listInterCode.peek().addQuadruple(function);
            }
            counter+=1;
            return t;
        } else {
            //andExpression
            String andExpression = visit(ctx.getChild(0));
            System.out.println("--andExpressionType : " + andExpression);
            return andExpression;
        }
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
            addTempVar(andExpression);
            addTempVar(equalsExpression);
            t+=counter;
            if (and.equals("&&")){
                String newLoc1 = recorrer(andExpression);
                String newLoc2 = recorrer(equalsExpression);
                Quadruple function = new Quadruple(newLoc1,newLoc2,t,LAB_AND);
                listInterCode.peek().addQuadruple(function);
            }
            counter+=1;
            return t;
        } else {
            //equalsExpression
            String equalsExpression = visitChildren(ctx);
            System.out.println("--equalsExpressionType : "+equalsExpression);
            return equalsExpression;
        }
    }


    @Override
    public String visitEqualsExpression(DecafParser.EqualsExpressionContext ctx){
        System.out.println("visitEqualsExpression");
        System.out.println(String.valueOf(ctx.getChildCount()));
        String t = "t";
        if(ctx.getChildCount() == 3){
            //equalsExpression eq_op relationExpression
            String equalsExpression = visit(ctx.getChild(0));
            String eq_op = ctx.getChild(1).getText();
            String relationExpression = visit(ctx.getChild(2));
            //print
            System.out.println("**equalsExpressionType : "+equalsExpression);
            System.out.println("**eq_op : "+eq_op);
            System.out.println("**relationExpressionType : "+relationExpression);
            addTempVar(equalsExpression);
            addTempVar(relationExpression);
            t+=counter;
            String newLoc1 = recorrer(equalsExpression);
            String newLoc2 = recorrer(relationExpression);
            if (eq_op.equals("==")){
                Quadruple function = new Quadruple(newLoc1,newLoc2,t,LAB_EQUALS);
                listInterCode.peek().addQuadruple(function);
            }
            counter+=1;
            return t;
        }
        else {
            //relationExpression
            String relationExpression = visit(ctx.getChild(0));
            System.out.println("--relationExpressionType : "+relationExpression);
            return relationExpression;
        }
    }

    @Override
    public String visitRelationExpression(DecafParser.RelationExpressionContext ctx){
        System.out.println("visitRelationExpression");
        System.out.println(String.valueOf(ctx.getChildCount()));
        String t = "t";
        if(ctx.getChildCount() == 3){
            //relationExpression rel_op addSubsExpression
            String relationExpression = visit(ctx.getChild(0));
            String rel_op = ctx.getChild(1).getText();
            String addSubsExpression = visit(ctx.getChild(2));
            //print
            System.out.println("**relationExpresionType : "+relationExpression);
            System.out.println("**rel_op : "+rel_op);
            System.out.println("**addSubsExpression : "+addSubsExpression);
            addTempVar(relationExpression);
            addTempVar(addSubsExpression);
            t+=counter;
            String newLoc1 = recorrer(relationExpression);
            String newLoc2 = recorrer(addSubsExpression);
            if (rel_op.equals(">")){
                Quadruple function = new Quadruple(newLoc1,newLoc2,t,LAB_GREATER);
                listInterCode.peek().addQuadruple(function);
            }
            else if (rel_op.equals("<")){
                Quadruple function = new Quadruple(newLoc1,newLoc2,t,LAB_LESS);
                listInterCode.peek().addQuadruple(function);
            }
            if (rel_op.equals(">=")){
                Quadruple function = new Quadruple(newLoc1,newLoc2,t,LAB_GREATERTEQUAL);
                listInterCode.peek().addQuadruple(function);
            }
            if (rel_op.equals("<=")){
                Quadruple function = new Quadruple(newLoc1,newLoc2,t,LAB_LESSEQUAL);
                listInterCode.peek().addQuadruple(function);
            }
            counter+=1;
            return t;

        }
        else {
            //addSubsExpression
            String addSubsExpression = visit(ctx.getChild(0));
            System.out.println("--addSubsExpression : "+addSubsExpression);
            return addSubsExpression;
        }
    }

    /**
     * {@inheritDoc}
     * <p>
     * <p>The default implementation returns the result of calling
     * {@link #visitChildren} on {@code ctx}.</p>
     *
     * @param ctx
     */
    @Override
    public String visitNExpression(DecafParser.NExpressionContext ctx) {
        String result = visit(ctx.getChild(0));
        System.out.println("El resultado es: "+result);

        return result;
    }

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
            addTempVar(addSubsExpression);
            addTempVar(mulDivExpression);
            t+=counter;
            if (as_op.equals("+")){
                String newLoc1 = recorrer(addSubsExpression);
                String newLoc2 = recorrer(mulDivExpression);
                Quadruple function = new Quadruple(newLoc1,newLoc2,t,LAB_ADD);
                listInterCode.peek().addQuadruple(function);
            }
            else {
                String newLoc1 = recorrer(addSubsExpression);
                String newLoc2 = recorrer(mulDivExpression);
                Quadruple function = new Quadruple(newLoc1,newLoc2,t,LAB_SUB);
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
            System.out.println("--mulDivExpressionType : "+mulDivExpression);
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
            System.out.println("**//**");
            String prExpression = visit(ctx.getChild(2));
            System.out.println("**//**");
            //print
            System.out.println("**mulDivExpressionType : "+mulDivExpression);
            System.out.println("**md_op : " + md_op);
            System.out.println("**prExpressionType : "+prExpression);
            addTempVar(mulDivExpression);
            addTempVar(prExpression);
            t+=counter;
            if (md_op.equals("*")){
                String newLoc1 = recorrer(mulDivExpression);
                String newLoc2 = recorrer(prExpression);
                Quadruple function = new Quadruple(newLoc1,newLoc2,t,LAB_MULT);
                listInterCode.peek().addQuadruple(function);
            }
            else{
                String newLoc1 = recorrer(mulDivExpression);
                String newLoc2 = recorrer(prExpression);
                Quadruple function = new Quadruple(newLoc1,newLoc2,t,LAB_DIV);
                listInterCode.peek().addQuadruple(function);
            }

            counter+=1;

        } else {
            //prExpression
            String prExpression = visitChildren(ctx);
            System.out.println("--prExpressionType : "+prExpression);
            return prExpression;
        }
        return t;
    }
    @Override
    public String visitStructDeclaration(DecafParser.StructDeclarationContext ctx){
        System.out.println("visitStructDeclaration");
        String id = ctx.getChild(1).getText();
        Quadruple function = new Quadruple(id, LAB_STRUCT);
        listInterCode.peek().addQuadruple(function);
        String result = visitChildren(ctx);
        return "";
    }


}