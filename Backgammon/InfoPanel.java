/*
 * ShaggyRogers group

 * Written by:
 * 	-Rafal Koziel 17472032
 * 	-Jordan Ahere 17482772
 * 	-Oisin McPhillips 17409914
 */
import javax.swing.*;
import javax.swing.text.*;
import java.awt.*;

public class InfoPanel extends JPanel {

    private static final long serialVersionUID = 1L;
    private static final int TEXT_AREA_HEIGHT = 40;
    private static final int CHARACTER_WIDTH = 47;
    private static final int FONT_SIZE = 12;

    private final JTextArea textArea;

    InfoPanel() {
        textArea  = new JTextArea(TEXT_AREA_HEIGHT, CHARACTER_WIDTH);
        JScrollPane scrollPane = new JScrollPane(textArea);
        DefaultCaret caret = (DefaultCaret)textArea.getCaret();
        textArea.setEditable(false);
        textArea.setFont(new Font("monospaced", Font.PLAIN, FONT_SIZE));
        textArea.setLineWrap(true);
        textArea.setWrapStyleWord(true);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);
        setLayout(new BorderLayout());
        add(scrollPane, BorderLayout.CENTER);
    }

    public void addText(String text) {
        textArea.setText(textArea.getText()+"\n"+text);
    }

}
