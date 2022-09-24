package io.github.nickid2018.mcde.injector;

import io.github.nickid2018.mcde.util.ConsumerE;
import org.fife.ui.rsyntaxtextarea.AbstractTokenMakerFactory;
import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea;
import org.fife.ui.rsyntaxtextarea.TokenMakerFactory;
import org.fife.ui.rtextarea.RTextScrollPane;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.util.function.Consumer;

public class CodingFrame {

    private final JFrame frame;

    private final RSyntaxTextArea textArea;

    public CodingFrame(String title, String syntaxHighlighting, boolean editable) {
        this(title, syntaxHighlighting, editable, f -> {});
    }

    public CodingFrame(String title, String syntaxHighlighting, boolean editable, Consumer<CodingFrame> runnable) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ignored) {
        }
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
        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent windowEvent) {
                runnable.accept(CodingFrame.this);
            }
        });
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
