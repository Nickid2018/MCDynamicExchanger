package io.github.nickid2018.mcde.injector.ui;

import javax.swing.*;
import javax.swing.tree.TreeCellRenderer;
import java.awt.*;

public class DataTreeCellRenderer implements TreeCellRenderer {

    @Override
    public Component getTreeCellRendererComponent(JTree tree,
                                                  Object value,
                                                  boolean selected,
                                                  boolean expanded,
                                                  boolean leaf,
                                                  int row,
                                                  boolean hasFocus) {
//        JTextPane pane = new JTextPane();
//        pane.setEditable(false);
//        pane.setContentType("text/html");
//        String valueStr = value.toString();
//        String[] split = valueStr.split(":", 2);
//        if (split.length == 2)
//            pane.setText("<b>%s</b>: %s".formatted(split[0], split[1]));
//        else
//            pane.setText(valueStr);
//        return pane;
        JLabel label = new JLabel(value.toString());
        label.setFont(new Font("微软雅黑", Font.PLAIN, 12));
        return label;
    }
}
