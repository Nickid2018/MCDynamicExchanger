package io.github.nickid2018.mcde.injector.ui;

import javax.swing.*;
import javax.swing.tree.DefaultTreeCellRenderer;
import java.awt.*;

public class DataTreeCellRenderer extends DefaultTreeCellRenderer {

    public DataTreeCellRenderer() {
        setLeafIcon(null);
        setOpenIcon(null);
        setClosedIcon(null);
        setSize(10000, getHeight());
    }

    @Override
    public Component getTreeCellRendererComponent(JTree tree, Object value,
                                                  boolean selected, boolean expanded,
                                                  boolean leaf, int row, boolean hasFocus) {
        String valueStr = value.toString();
        String[] split = valueStr.replace("<", "&lt;").replace(">", "&gt;").split(":", 3);
        String text;
        if (split.length == 3)
            text = "<html><span style=\"font-family: 微软雅黑\"><span style=\"font-weight: bold\">%s</span>: <span style=\"color: gray; font-style: italic\">(%s)</span> %s</span></html>".formatted(split[0], split[1], split[2]);
        else if (split.length == 2)
            text = "<html><span style=\"font-family: 微软雅黑\"><span style=\"font-weight: bold\">%s</span>: %s</span></html>".formatted(split[0], split[1]);
        else
            text = "<html><span style=\"font-family: 微软雅黑\">%s</span></html>".formatted(valueStr);
        return super.getTreeCellRendererComponent(tree, text, false, expanded, leaf, row, hasFocus);
    }
}
