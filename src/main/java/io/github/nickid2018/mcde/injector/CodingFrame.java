package io.github.nickid2018.mcde.injector;

import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea;
import org.fife.ui.rtextarea.RTextScrollPane;

import javax.swing.*;
import java.awt.*;

public class CodingFrame {

    private final JFrame frame;

    private final RSyntaxTextArea textArea;

    public CodingFrame(String title, String syntaxHighlighting, boolean editable) {
        frame = new JFrame(title);
        JPanel contentPane = new JPanel();

        textArea = new RSyntaxTextArea();
        textArea.setSyntaxEditingStyle(syntaxHighlighting);
        textArea.setCodeFoldingEnabled(true);
        textArea.setEditable(editable);

        RTextScrollPane scroll = new RTextScrollPane(textArea);
        contentPane.setLayout(new BorderLayout());
        contentPane.add(scroll);
        frame.setContentPane(contentPane);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    }

    public void setCode(String code) {
        textArea.setText(code);
    }

    public String getCode() {
        return textArea.getText();
    }

    public void show() {
        frame.setSize(800, 600);
        frame.setLocationByPlatform(true);
        frame.setVisible(true);
    }
}
