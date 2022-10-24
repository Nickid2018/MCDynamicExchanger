package io.github.nickid2018.mcde.injector.ui;

import javax.swing.*;
import javax.swing.tree.DefaultTreeCellRenderer;
import java.awt.*;

public class DataTreeCellRenderer extends DefaultTreeCellRenderer {

    public DataTreeCellRenderer() {
        setLeafIcon(null);
        setOpenIcon(null);
        setClosedIcon(null);
        setBackgroundSelectionColor(Color.WHITE);
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
            text = "<html><span style=\"font-family: 微软雅黑\"><b>%s</b>: <i>(%s)</i> %s</span></html>".formatted(split[0], split[1], split[2]);
        else
            text = "<html><span style=\"font-family: 微软雅黑\">%s</span></html>".formatted(valueStr);
        return super.getTreeCellRendererComponent(tree, text, selected, expanded, leaf, row, hasFocus);
    }
}
