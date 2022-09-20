package io.github.nickid2018.mcde.injector;

import io.github.nickid2018.mcde.injector.commands.InjectCommander;
import io.github.nickid2018.mcde.util.I18N;

import javax.swing.*;
import javax.swing.text.*;
import java.awt.*;
import java.io.PrintWriter;
import java.io.StringWriter;

public class InjectorFrame {
    private JTextPane outputPane;
    private JTextField inputField;
    private final JFrame frame;

    private final InjectCommander commander = new InjectCommander();

    private static final MutableAttributeSet INFO = new SimpleAttributeSet();
    private static final MutableAttributeSet ERROR = new SimpleAttributeSet();

    static {
        StyleConstants.setForeground(INFO, Color.GREEN.darker());
        StyleConstants.setForeground(ERROR, Color.RED.darker());
    }

    public InjectorFrame() {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ignored) {
        }
        frame = new JFrame(I18N.getTranslation("injector.frame.title"));
        createUIComponents();
    }

    private void createUIComponents() {
        JPanel contentPane = new JPanel();
        outputPane = new JTextPane();
        inputField = new JTextField();

        inputField.addActionListener(e -> {
            String text = inputField.getText();
            inputField.setText("");
            commander.doCommand(text, this);
        });

        outputPane.setEditable(false);

        contentPane.setLayout(new BorderLayout());
        JScrollPane scrollPane = new JScrollPane(outputPane);
        contentPane.add(scrollPane, BorderLayout.CENTER);
        contentPane.add(inputField, BorderLayout.SOUTH);

        inputField.setFont(new Font("微软雅黑", Font.PLAIN, 12));
        outputPane.setFont(new Font("微软雅黑", Font.PLAIN, 12));

        frame.setContentPane(contentPane);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    }

    public void show() {
        frame.setSize(800, 600);
        frame.setLocationByPlatform(true);
        frame.setVisible(true);
    }

    public void newLine() {
        try {
            outputPane.getDocument().insertString(outputPane.getDocument().getLength(), "\n", null);
        } catch (BadLocationException ignored) {
        }
    }

    public void info(String text) {
        try {
            outputPane.getDocument().insertString(outputPane.getDocument().getLength(), text, INFO);
        } catch (BadLocationException ignored) {
        }
        newLine();
    }

    public void error(String text, Throwable e) {
        if (text != null)
            try {
                outputPane.getDocument().insertString(outputPane.getDocument().getLength(), text, ERROR);
            } catch (BadLocationException ignored) {
            }
        if (e != null) {
            if (text != null)
                newLine();
            StringWriter writer = new StringWriter();
            e.printStackTrace(new PrintWriter(writer));
            try {
                outputPane.getDocument().insertString(outputPane.getDocument().getLength(), writer.toString(), ERROR);
            } catch (BadLocationException ignored) {
            }
        }
        newLine();
    }
}
