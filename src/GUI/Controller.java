package GUI;

import Grammars.DecafLexer;
import Grammars.DecafParser;
import javafx.fxml.FXML;
import javafx.stage.FileChooser;
import org.antlr.v4.gui.TreeViewer;
import org.antlr.v4.runtime.ANTLRFileStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.tree.ParseTree;

import java.io.File;
import java.util.Arrays;

public class Controller {
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
        }
        else {
            System.out.print("File selection cancelled.");
        }

    }
    @FXML
    private void setText(){
        //TODO
        //Set del texto adjunto
    }
}
