package GUI;


import javafx.scene.control.Alert;
import org.antlr.v4.runtime.BaseErrorListener;
import org.antlr.v4.runtime.RecognitionException;
import org.antlr.v4.runtime.Recognizer;
import org.antlr.v4.runtime.misc.ParseCancellationException;

import javax.swing.*;
import java.io.PrintWriter;
import java.io.StringWriter;
// Inspired in : https://stackoverflow.com/questions/18132078/handling-errors-in-antlr4
public class ThrowingErrorListener extends BaseErrorListener {

    public static final ThrowingErrorListener INSTANCE = new ThrowingErrorListener();

    @Override
    public void syntaxError(Recognizer<?, ?> recognizer, Object offendingSymbol, int line, int charPositionInLine, String msg, RecognitionException e)
            throws ParseCancellationException {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        //alert.initStyle(StageStyle.UTILITY);
        alert.setTitle("Error");
       alert.setHeaderText("Warning");
        alert.setContentText(String.valueOf(new ParseCancellationException("line " + line + ":" + charPositionInLine + " " + msg)));
       alert.showAndWait();
        //JOptionPane.showMessageDialog(null,String.valueOf(new ParseCancellationException("line " + line + ":" + charPositionInLine + " " + msg)));
        throw new ParseCancellationException("line " + line + ":" + charPositionInLine + " " + msg);
    }
    public static String getStackTrace(final Throwable throwable) {
        final StringWriter sw = new StringWriter();
        final PrintWriter pw = new PrintWriter(sw, true);
        throwable.printStackTrace(pw);
        return sw.getBuffer().toString();
    }
}