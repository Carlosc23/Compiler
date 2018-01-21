package GUI;

import Grammars.DecafLexer;
import Grammars.DecafParser;
import javafx.application.Application;
import javafx.embed.swing.SwingNode;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.antlr.v4.gui.TreeViewer;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.TerminalNodeImpl;

import javax.swing.*;
import java.io.File;
import java.io.IOException;
import java.util.*;

public class Main extends Application {
    private static Stage primaryStage;
    @FXML
    private AnchorPane pane;
    @FXML
    private TextArea area;
    @FXML
    private AnchorPane panelInput;
    @FXML
    private CodeEditor editor;
    private String arch = " ";
    private static ArrayList<Ruler> reglas= new ArrayList<>();
    @Override
    public void start(Stage primaryStage) throws Exception{
       // Parent root = FXMLLoader.load(getClass().getResource("sample.fxml"));
        Main.primaryStage = primaryStage;
        primaryStage.setTitle("Compiler");
        primaryStage.setResizable(false);
        sal();

    }
    private void sal() throws IOException {

        Parent root = FXMLLoader.load(getClass().getResource("sample.fxml"));
        primaryStage.setScene(new Scene(root, 850, 775));
        primaryStage.show();


    }
    public void nuevoView() {
        this.editor = new CodeEditor(arch);
        panelInput.getChildren().add(editor);
    }
    public void showMainView() {
        if(!arch.equals(" ")){
            SwingNode swingNode = new SwingNode();
            createAndSetSwingContent(swingNode);
            //String arbol2=this.arbol;
            pane.getChildren().removeAll();
            pane.getChildren().add(swingNode); // Adding swing
        }
        else{
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            //alert.initStyle(StageStyle.UTILITY);
            alert.setTitle("Information");
            alert.setHeaderText("Warning");
            alert.setContentText("Please insert code and then compile, to see the Syntax Tree");

            alert.showAndWait();
        }


    }
    private void createAndSetSwingContent(SwingNode swingNode) {
        SwingUtilities.invokeLater(new Runnable() {

            @Override
            public void run() {
                JPanel panel = new JPanel();

                try {
                   // System.out.println("happy");
                   // System.out.println(arch);
                    CharStream stream = new ANTLRInputStream(arch);
                    //System.out.print("Hola a todos");
                    DecafLexer lexer = new DecafLexer(stream);
                    // Token token = lexer.nextToken();
                    CommonTokenStream tokens = new CommonTokenStream(lexer);

                    //Analisis lexico
            /*while(token.getType()!=(DecafLexer.EOF)){
                System.out.print("Tipo["+token.getType()+"],Token["+
                token.getText()+"]");
                token = lexer.nextToken();
            }*/

                    // anasl

                    DecafParser parser = new DecafParser(tokens);
                    ParseTree tree = parser.program();
                    TreeViewer viewer = new TreeViewer(
                            Arrays.asList(parser.getRuleNames()), tree
                    );
                    //System.out.print(Arrays.asList(parser.getRuleNames()));
                   // System.out.println(tree.toStringTree(parser));
                   /* for(int i =0;i<tree.getChildCount();i++){

                        System.out.println(" "+tree.getChild(i));
                        //***********
                        ParseTree tree2 = tree.getChild(i);
                        //System.out.println(tree2.toStringTree(parser));
                        //System.out.println(tree2.getChildCount());
                        if(tree2.getChildCount()!=0){
                            for(int j = 0; j< tree2.getChildCount(); j++){
                                System.out.println("  "+tree2.getChild(j));
                                ParseTree tree3 = tree2.getChild(j);
                                if(tree3.getChildCount()!=0){
                                    for(int k = 0; k< tree3.getChildCount(); k++){
                                        System.out.println("  "+tree3.getChild(k));
                                    }
                                }
                            }
                        }
                    }*/
                    //this.arbol = (String)tree.toStringTree(parser);
                    System.out.println("Ok");
                    viewer.setScale(1.5);
                    //viewer.open();
                    panel.removeAll();
                    panel.add(viewer);
                    swingNode.setContent(panel);
                } catch (RecognitionException ignored) {
                }

            }
        });
    }
    @FXML
    private void compile(){
        //String filename = "C:\\Users\\Carlos Calderón\\IdeaProjects\\Compiler\\src\\Grammars\\Test.txt";
        this.arch = editor.getCodeAndSnapshot();
        if(arch.matches("[\\\\r\\\\n]+\\\\s")){
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            //alert.initStyle(StageStyle.UTILITY);
            alert.setTitle("Information");
            alert.setHeaderText("Warning");
            alert.setContentText("Please insert code before compile");

            alert.showAndWait();
        }
        else try {

            CharStream stream = new ANTLRInputStream(this.editor.getCodeAndSnapshot());
            DecafLexer lexer = new DecafLexer(stream);
            lexer.removeErrorListeners();
            lexer.addErrorListener(ThrowingErrorListener.INSTANCE);
            //Token token = lexer.nextToken();
            CommonTokenStream tokens = new CommonTokenStream(lexer);
            //Analisis lexico
            /*while(token.getType()!=(DecafLexer.EOF)){
                System.out.print("Tipo["+token.getType()+"],Token["+
                token.getText()+"]");
                token = lexer.nextToken();
            }*/

            DecafParser parser = new DecafParser(tokens);
            parser.removeErrorListeners();
            parser.addErrorListener(ThrowingErrorListener.INSTANCE);
            ParseTree tree = parser.program();
            TreeViewer viewer = new TreeViewer(
                    Arrays.asList(parser.getRuleNames()), tree
            );
            //System.out.print(tree.toStringTree(parser));
           // String arbol = tree.toStringTree(parser);
            nuevo(tree, parser, "program");
            //System.out.println("Sali de la recursion");

            Collections.reverse(reglas);
            String t = "";
            for (Ruler r : reglas) {
               // System.out.println(r.toString());
               // System.out.println("****************");
                t += r.toString() + "\n" + "****************\n";
            }
            area.setText(t);
            arch = this.editor.getCodeAndSnapshot();
            System.out.print("Ok");
            viewer.setScale(1.5);
            //viewer.open();
        } catch (Exception e) {
            //System.out.print(e);
        }


    }
    private static void nuevo(ParseTree tree,DecafParser parser,String p){
        Ruler r2 = new Ruler();
        for (int i=0;i<tree.getChildCount();i++){

            if(tree.getChild(i) instanceof RuleContext){
                ParseTree tree2 = tree.getChild(i);
                //System.out.println("---");
                RuleContext r = (RuleContext) tree.getChild(i);
                //nodes.add(parser.getRuleNames()[r.getRuleIndex()]);
               // System.out.println(p+" "+parser.getRuleNames()[r.getRuleIndex()]);
                r2.setName(p);
                r2.addRuler(parser.getRuleNames()[r.getRuleIndex()]);
                //reglas.add(r2);
                nuevo(tree2,parser,parser.getRuleNames()[r.getRuleIndex()]);
                // espacio+=" ";
                //espacio=parser.getRuleNames()[r.getRuleIndex()];
                //modulos.add(espacio);
            }
            else if (tree.getChild(i) instanceof TerminalNodeImpl){
                Token representedToken = ((TerminalNodeImpl) tree.getChild(i)).getSymbol();
               // System.out.println(representedToken.getType());
                //System.out.println(representedToken.getText());
                r2.setName(p);
                r2.addRuler(representedToken.getText());
            }

        }
        reglas.add(r2);
    }
    @FXML
    private void attach(){

        FileChooser fileChooser = new FileChooser();
        fileChooser.setInitialDirectory(new File("C:\\Users\\Carlos Calderón\\IdeaProjects\\Compiler\\src\\Grammars"));
        File selectedFile = fileChooser.showOpenDialog(null);
        if (selectedFile != null) {
            //System.out.print("File selected: " + selectedFile.getName());
            Scanner scan_file;

            try {
                scan_file = new Scanner(selectedFile);
                while(scan_file.hasNext()){
                    arch += scan_file.nextLine()+"\n";
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(null, "Archivo invalido");
            }
            //txta.setText(arch);
            this.editor.setCode(arch);

        }
        else {
            System.out.print("File selection cancelled.");
           // txta.setText("Hola mjn");
        }

    }
    public static void main(String[] args) {
        launch(args);
    }
}