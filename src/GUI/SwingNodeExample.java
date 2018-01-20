package GUI;

import Grammars.DecafLexer;
import Grammars.DecafParser;
import org.antlr.runtime.ANTLRInputStream;
import org.antlr.v4.gui.TreeViewer;
import org.antlr.v4.runtime.ANTLRFileStream;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.TokenStream;
import org.antlr.v4.runtime.tree.ParseTree;

import javax.swing.*;
import java.io.IOException;
import java.util.Arrays;

public class SwingNodeExample {
    public static void main(String[] args) throws IOException {
        //prepare token stream
        String filename = "C:\\Users\\Carlos Calderón\\IdeaProjects\\Compiler\\src\\Grammars\\Test.txt";
        ANTLRFileStream input = new ANTLRFileStream(filename);
        DecafLexer lexer  = new DecafLexer(input);
        TokenStream tokenStream = new CommonTokenStream(lexer);
        DecafParser parser = new DecafParser(tokenStream);
        ParseTree tree = parser.program();

        //show AST in console
        System.out.println(tree.toStringTree(parser));

        //show AST in GUI
        JFrame frame = new JFrame("Antlr AST");
        JPanel panel = new JPanel();
        TreeViewer viewr = new TreeViewer(Arrays.asList(
                parser.getRuleNames()),tree);
        viewr.setScale(1.5);//scale a little
        panel.add(viewr);
        frame.add(panel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(200,200);
        frame.setVisible(true);
    }
}