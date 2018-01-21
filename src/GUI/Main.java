package GUI;

import Grammars.DecafLexer;
import Grammars.DecafParser;
import javafx.application.Application;
import javafx.embed.swing.SwingNode;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.TextFieldTreeCell;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBoxBuilder;
import javafx.scene.layout.VBox;
import javafx.scene.layout.VBoxBuilder;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Callback;
import javafx.util.converter.DefaultStringConverter;
import org.antlr.v4.gui.TreeViewer;
import org.antlr.v4.runtime.ANTLRFileStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.tree.ParseTree;

import javax.swing.*;
import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Main extends Application {
    private static Stage primaryStage;
    public  String arbol;
    @FXML
    private AnchorPane pane;
    @FXML
    private AnchorPane panelInput;
    @FXML
    private AnchorPane pane2;
    private CodeEditor editor;
    @Override
    public void start(Stage primaryStage) throws Exception{
       // Parent root = FXMLLoader.load(getClass().getResource("sample.fxml"));
        this.primaryStage = primaryStage;
        primaryStage.setTitle("Compiler");
        sal();

    }
    public void sal() throws IOException {

        Parent root = FXMLLoader.load(getClass().getResource("sample.fxml"));
        primaryStage.setScene(new Scene(root, 850, 775));
        primaryStage.show();


    }
    public void nuevoView() throws IOException {
        this.editor = new CodeEditor("");
        //panelInput.setLayoutX(300);
        //panelInput.setLayoutY(300);
        //panelInput.setPadding(editor,new Insets(0, 0, 0, 8));
        panelInput.getChildren().add(editor);
    }
    public void showMainView() throws IOException {
        /*Parent root = FXMLLoader.load(getClass().getResource("sample.fxml"));
        final SwingNode swingNode = new SwingNode();
        createAndSetSwingContent(swingNode);
        pane.getChildren().add(swingNode); // Adding swing node
        Scene scene2 = new Scene(pane);
        primaryStage.setScene(new Scene(pane, 100, 50));
        primaryStage.show();*/
       // Parent root = FXMLLoader.load(getClass().getResource("sample.fxml"));
        final SwingNode swingNode = new SwingNode();
        createAndSetSwingContent(swingNode);

        //-------------------------------------------------------------------------------------------------------------
        /*Pattern p = Pattern.compile( "\\((.*)" );
        System.out.println("Data Science");
        System.out.println(this.arbol);
        System.out.println("Red hot chile peppers");
        Matcher m = p.matcher( this.arbol);
        ArrayList<String> parseo = new ArrayList<String>();
        if ( m.find() ) {
            String s = m.group(1); // " that is awesome"
            System.out.println("fuck");
            System.out.println(s.split(" ")[0]);
            parseo.add(s.split(" ")[0]);
        }
        System.out.println("fuck universal");*/
        boolean a= true;
        String arbol2=this.arbol;
        //TODO hacer el TreeView

       /* TreeItem<String> rootItem = new TreeItem<String> ("Tree");
        rootItem.setExpanded(true);
        ArrayList<ArrayList<String>> arr2 = new ArrayList<ArrayList<String>>();
        ArrayList<String> arr = new ArrayList<String>();
        arr.add("class");
        arr.add("Hola");
        arr.add("{");
        HashMap<String,String> listaProductos = new HashMap<String,String>();
        listaProductos.put("program","class");
        listaProductos.put("program","Hola");
        listaProductos.put("program","{");
        Iterator iterador = listaProductos.entrySet().iterator();
        Map.Entry producto;
        while (iterador.hasNext()) {
            System.out.println("acero");
            producto = (Map.Entry) iterador.next();
            TreeItem<String> item = new TreeItem<String> ((String) producto.getKey());
            TreeItem<String> item2 = new TreeItem<String> ((String) producto.getValue());
            System.out.println(producto.getKey() + " - " + producto.getValue());
            item.getChildren().add(item2);
            rootItem.getChildren().add(item);

        }
        TreeView<String> tree = new TreeView<String> (rootItem);
        tree.setEditable(true);
        tree.setCellFactory(new Callback<TreeView<String>,TreeCell<String>>(){
            @Override
            public TreeCell<String> call(TreeView<String> p) {
                return new RenameMenuTreeCell();
            }
        });
        //-------------------------------------------------------------------------------------------------------------

        pane2.getChildren().add(tree);*/
        //primaryStage.setScene(new Scene(pane, 850, 775));
        //primaryStage.show();
        pane.getChildren().add(swingNode); // Adding swing
        System.out.print("Hola que tal");

    }
    private static class RenameMenuTreeCell extends TextFieldTreeCell<String> {
        private ContextMenu menu = new ContextMenu();

        public RenameMenuTreeCell() {
            super(new DefaultStringConverter());

            MenuItem renameItem = new MenuItem("Rename");
            menu.getItems().add(renameItem);
            renameItem.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent arg0) {
                    startEdit();
                }
            });
        }

        @Override
        public void updateItem(String item, boolean empty) {
            super.updateItem(item, empty);

            if (!isEditing()) {
                setContextMenu(menu);
            }
        }
    }
    private void createAndSetSwingContent(final SwingNode swingNode) {
        SwingUtilities.invokeLater(new Runnable() {

            @Override
            public void run() {
                JPanel panel = new JPanel();
                String filename = "C:\\Users\\Carlos Calderón\\IdeaProjects\\Compiler\\src\\Grammars\\Test.txt";

                try{

                    ANTLRFileStream input = new ANTLRFileStream(filename);
                    System.out.print("Hola a todos");
                    DecafLexer lexer = new DecafLexer(input);
                    Token token = lexer.nextToken();
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
                            Arrays.asList(parser.getRuleNames()),tree
                    );
                    //System.out.print(Arrays.asList(parser.getRuleNames()));
                    System.out.println(tree.toStringTree(parser));
                    for(int i =0;i<tree.getChildCount();i++){

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
                    }
                    //this.arbol = (String)tree.toStringTree(parser);
                    System.out.println("Ok");
                     viewer.setScale(1.5);
                    //viewer.open();
                    panel.add(viewer);
                    swingNode.setContent(panel);
                }
                catch (Exception e){
                    System.out.print(e);
                }

            }
        });
    }
    @FXML
    private TextArea txta;
    @FXML
    private void compile(){
        String filename = "C:\\Users\\Carlos Calderón\\IdeaProjects\\Compiler\\src\\Grammars\\Test.txt";

        try{

            ANTLRFileStream input = new ANTLRFileStream(filename);
            System.out.print("Hola a todos");
            DecafLexer lexer = new DecafLexer(input);
            Token token = lexer.nextToken();
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
                    Arrays.asList(parser.getRuleNames()),tree
            );
            System.out.print(tree.toStringTree(parser));
            this.arbol = tree.toStringTree(parser);
            System.out.print("Ok");
             viewer.setScale(1.5);
            //viewer.open();
        }
        catch (Exception e){
            System.out.print(e);
        }

    }
    @FXML
    private void attach(){

        FileChooser fileChooser = new FileChooser();
        fileChooser.setInitialDirectory(new File("C:\\Users\\Carlos Calderón\\IdeaProjects\\Compiler\\src\\Grammars"));
        File selectedFile = fileChooser.showOpenDialog(null);
        if (selectedFile != null) {
            System.out.print("File selected: " + selectedFile.getName());
            Scanner scan_file;
            String arch = "";
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