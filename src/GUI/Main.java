package GUI;

import Grammars.DecafLexer;
import Grammars.DecafParser;
import javafx.application.Application;
import javafx.embed.swing.SwingNode;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextArea;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.antlr.v4.gui.TreeViewer;
import org.antlr.v4.runtime.ANTLRFileStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.tree.ParseTree;

import javax.swing.*;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Scanner;

public class Main extends Application {
    private static Stage primaryStage;
    private VBox mainLayout;
    @FXML
    private AnchorPane pane;
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
    public void showMainView() throws IOException {
        /*Parent root = FXMLLoader.load(getClass().getResource("sample.fxml"));
        final SwingNode swingNode = new SwingNode();
        createAndSetSwingContent(swingNode);
        pane.getChildren().add(swingNode); // Adding swing node
        Scene scene2 = new Scene(pane);
        primaryStage.setScene(new Scene(pane, 100, 50));
        primaryStage.show();*/
        Parent root = FXMLLoader.load(getClass().getResource("sample.fxml"));
        final SwingNode swingNode = new SwingNode();
        createAndSetSwingContent(swingNode);
        pane.getChildren().add(swingNode); // Adding swing
        //primaryStage.setScene(new Scene(pane, 850, 775));
        //primaryStage.show();
        System.out.print("Hola que tal");

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
                    System.out.print(tree.toStringTree(parser));
                    System.out.print("Ok");
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
            System.out.print("Ok");
            // viewer.setScale(1.5);
            viewer.open();
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
            txta.setText(arch);
        }
        else {
            System.out.print("File selection cancelled.");
            txta.setText("Hola mjn");
        }

    }
    public static void main(String[] args) {
        launch(args);
    }
}