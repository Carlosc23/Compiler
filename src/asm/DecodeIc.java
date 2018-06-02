package asm;

import gui.Constant;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static gui.Constant.code1;

public class DecodeIc {
    public String generalCode = "";
    public DecodeIc() {
    }
    public String decode(String codeIc){
        System.out.println("------------------------");
        String newCode = ".globl main\n.text\n";
        codeIc = newCode+codeIc;
        Scanner scanner = new Scanner(codeIc);
        newCode = "";
        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            newCode+=line+"\n";
            //System.out.println(line);
            // process the line
        }
        System.out.println("------------------------");
        scanner.close();
        return newCode;
    }
    public String decodeMain(String codeIc){
        //String result = codeIc.substring(codeIc.indexOf("FUNC BEGIN main") , codeIc.indexOf("FUNC END "));
        String pattern1 = "FUNC BEGIN main";
        String pattern2 = "FUNC END ";
        String text = codeIc;
        System.out.println(text);
        Pattern p = Pattern.compile(Pattern.quote(pattern1) + "(?s)(.*?)" + Pattern.quote(pattern2));
        Matcher m = p.matcher(text);
        text = "main:";
        while (m.find()) {
            //System.out.println("al");
           // System.out.println(m.group(1));
            text+=m.group(1);
        }
        generalCode=text;
        return text;
    }
    public String addData(int max,int max2){


        System.out.println("Entramos a max: "+max);
        int space = (max*4)+4;
        String data = " .data\n" +
                "L:\n" +
                "  .space "+space+"\n"+
                "G:\n" +
                "   .space "+space+"\n";
        generalCode+=data;
        Scanner scanner = new Scanner(generalCode);
        String newCode = "";
        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            if (line.contains("[0")) {
                newCode+=line.replace("[0]","($t0)")+"\n";
            }
            else if (line.contains("[1")) {
                newCode+=line.replace("[1]","($t0)")+"\n";
            }
            else if (line.contains("[2")) {
                newCode+=line.replace("[2]","($t0)")+"\n";
            }
            else if (line.contains("[3")) {
                newCode+=line.replace("[3]","($t0)")+"\n";
            }
            else if (line.contains("[4")) {
                newCode+=line.replace("[4]","($t0)")+"\n";
            }
            else if (line.contains("[5")) {
                newCode+=line.replace("[5]","($t0)")+"\n";
            }
            else if (line.contains("[6")) {
                newCode+=line.replace("[6]","($t0)")+"\n";
            }
            else if (line.contains("[7")) {
                newCode+=line.replace("[7]","($t0)")+"\n";
            }
            else     if (line.contains("[8")) {
                newCode+=line.replace("[8]","($t0)")+"\n";
            }
            else  if (line.contains("[9")) {
                newCode+=line.replace("[9]","($t0)")+"\n";
            }

            else{
                newCode+=line+"\n";
            }
           /* newCode+=line.replace("[2]","[$t0]")+"\n";
            newCode+=line.replace("[3]","[$t0]")+"\n";
            newCode+=line.replace("[4]","[$t0]")+"\n";
            newCode+=line.replace("[5]","[$t0]")+"\n";
            newCode+=line.replace("[6]","[$t0]")+"\n";
            newCode+=line.replace("[7]","[$t0]")+"\n";
            newCode+=line.replace("[8]","[$t0]")+"\n";
            newCode+=line.replace("[9]","[$t0]")+"\n";*/
            //System.out.println(line);
            // process the line
        }
        System.out.println("------------------------");
        scanner.close();
        generalCode = newCode;
        return generalCode;
    }
    public int searchMaxIndexLocal(){
        //String result = codeIc.substring(codeIc.indexOf("FUNC BEGIN main") , codeIc.indexOf("FUNC END "));
        List<Integer> list = new ArrayList<Integer>();
        String pattern1 = "L[";
        String pattern2 = "]";
        String text = generalCode;
        System.out.println(generalCode);
        Pattern p = Pattern.compile(Pattern.quote(pattern1) + "(?s)(.*?)" + Pattern.quote(pattern2));
        Matcher m = p.matcher(text);
        text = "main:";
        while (m.find()) {
            System.out.println("all");
            System.out.println(m.group(1));
            int foo = Integer.parseInt(m.group(1));
            //generalCode = generalCode.replace(""+foo,"$t0");
            list.add(foo);
            //text+=m.group(1);
        }
        System.out.println("El mayor es: "+ Collections.max(list));
       // generalCode=text;
        return Collections.max(list);
    }
    public int searchMaxIndexGlobal(){
        //String result = codeIc.substring(codeIc.indexOf("FUNC BEGIN main") , codeIc.indexOf("FUNC END "));
        List<Integer> list = new ArrayList<Integer>();
        String pattern1 = "G[";
        String pattern2 = "]";
        String text = generalCode;
        System.out.println(generalCode);
        Pattern p = Pattern.compile(Pattern.quote(pattern1) + "(?s)(.*?)" + Pattern.quote(pattern2));
        Matcher m = p.matcher(text);
        text = "main:";
        while (m.find()) {
            System.out.println("all");
            System.out.println(m.group(1));
            int foo = Integer.parseInt(m.group(1));
            list.add(foo);
            //text+=m.group(1);
        }
        System.out.println("El mayor es: "+ Collections.max(list));
        // generalCode=text;
        return Collections.max(list);
    }
    public String searchMov(){
        System.out.println("------------------------");
        String newCode = ".globl main\n.text\n";
        generalCode = newCode+generalCode;
        Scanner scanner = new Scanner(generalCode);
        newCode = "";
        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            String inm=line.substring(line.length() - 1);
            if(inm.matches("-?\\d+(\\.\\d+)?")){
               line= "addi $s0,$zero,"+line.charAt(line.length()-1)+"\n"+line;
                line = line.replace("MOV","sw");

            }
            else{
                line = line.replace("MOV","move");
                line = line.replace("move","sw");
            }
            newCode+=line+"\n";
            //System.out.println(line);
            // process the line
        }
        System.out.println("------------------------");
        scanner.close();
        generalCode=newCode;
        return newCode;
    }
    public String caseWhile(String code){
        generalCode = Constant.code1 + Constant.code2 + Constant.code3;
        return generalCode;
    }
    public String caseSuma(String code){
        generalCode = Constant.code4;
        return generalCode;
    }
}
