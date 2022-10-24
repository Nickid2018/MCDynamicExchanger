package io.github.nickid2018.mcde.injector.ui;

import io.github.nickid2018.mcde.data.DataCollector;
import io.github.nickid2018.mcde.data.DataEntry;
import io.github.nickid2018.mcde.remapper.ASMRemapper;
import io.github.nickid2018.mcde.util.I18N;

import javax.swing.*;
import javax.swing.event.TreeExpansionEvent;
import javax.swing.event.TreeExpansionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.*;
import java.util.List;

public class DataValueFrame {

    private final JFrame frame;

    private final JTextField indexField;
    private final JTree tree;
    private DefaultTreeModel model;

    private DataEntry<?> value;
    private final String name;

    private final ASMRemapper remapper;

    public DataValueFrame(String name, ASMRemapper remapper) {
        this.remapper = remapper;
        this.name = name;
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ignored) {
        }

        value = DataCollector.getData(name).get(0);

        frame = new JFrame();

        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());

        indexField = new JTextField();
        indexField.setText("0");
        indexField.addActionListener(this::actionField);
        panel.add(indexField, BorderLayout.NORTH);

        JScrollPane scrollPane = new JScrollPane();
        panel.add(scrollPane, BorderLayout.CENTER);

        tree = new JTree();
        tree.setCellRenderer(new DataTreeCellRenderer());
        tree.addTreeExpansionListener(new TreeExpansionListener() {
            @Override
            public void treeExpanded(TreeExpansionEvent event) {
                expandNode(event);
                tree.invalidate();
            }
            @Override
            public void treeCollapsed(TreeExpansionEvent event) {
            }
        });

        JPopupMenu popupMenu = new JPopupMenu();
        JMenuItem menuItem = new JMenuItem(I18N.getTranslation("injector.dataframe.refresh"));
        menuItem.setAccelerator(KeyStroke.getKeyStroke("F5"));
        menuItem.addActionListener(e -> updateTreeData());
        popupMenu.add(menuItem);

        tree.setComponentPopupMenu(popupMenu);
        scrollPane.setViewportView(tree);
        updateTree(null);

        frame.setContentPane(panel);
        frame.setSize(400, 300);
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setVisible(true);
    }

    public boolean isShowing() {
        return frame.isVisible();
    }

    public void focus() {
        frame.requestFocus();
    }

    private void actionField(ActionEvent e) {
        String text = indexField.getText();
        if (text.isEmpty())
            UIManager.getLookAndFeel().provideErrorFeedback(frame);
        else {
            try {
                int index = Integer.parseInt(text);
                if (index < 0)
                    UIManager.getLookAndFeel().provideErrorFeedback(frame);
                else {
                    List<DataEntry<?>> list = DataCollector.getData(name);
                    if (index >= list.size())
                        UIManager.getLookAndFeel().provideErrorFeedback(frame);
                    else {
                        Class<?> type = value.getValue().getClass();
                        value = list.get(index);
                        updateTree(type);
                    }
                }
            } catch (NumberFormatException ex) {
                UIManager.getLookAndFeel().provideErrorFeedback(frame);
            }
        }
    }

    private void updateTree(Class<?> prevClass) {
        if (prevClass == null || !prevClass.equals(value.getValue().getClass())) {
            DataValueEntry entry = new DataValueEntry("value", value.getValue(), remapper);
            model = new DefaultTreeModel(new DefaultMutableTreeNode(entry));
            Map<String, DataValueEntry> subs = entry.getSubObjects();
            for (Map.Entry<String, DataValueEntry> sub : subs.entrySet()) {
                DataValueEntry value = sub.getValue();
                DefaultMutableTreeNode node = new DefaultMutableTreeNode(value);
                model.insertNodeInto(node, (DefaultMutableTreeNode) model.getRoot(), model.getChildCount(model.getRoot()));
                Map<String, DataValueEntry> subSubs = value.getSubObjects();
                for (Map.Entry<String, DataValueEntry> subSub : subSubs.entrySet()) {
                    DataValueEntry value1 = subSub.getValue();
                    DefaultMutableTreeNode node1 = new DefaultMutableTreeNode(value1);
                    model.insertNodeInto(node1, node, model.getChildCount(node));
                }
                if (!subSubs.isEmpty())
                    tree.collapsePath(new TreePath(node.getPath()));
            }
            tree.setModel(model);
        } else
            updateTreeData();
        tree.invalidate();
    }

    private void updateTreeData() {
        Queue<DefaultMutableTreeNode> queue = new LinkedList<>();
        queue.add((DefaultMutableTreeNode) model.getRoot());
        ((DataValueEntry) ((DefaultMutableTreeNode) model.getRoot()).getUserObject()).setValue(value.getValue());
        while (!queue.isEmpty()) {
            DefaultMutableTreeNode node = queue.poll();
            DataValueEntry entry = (DataValueEntry) node.getUserObject();
            Map<String, DataValueEntry> subs = entry.getSubObjects();
            Set<DefaultMutableTreeNode> toRemove = new HashSet<>();
            for (int i = 0; i < model.getChildCount(node); i++) {
                DefaultMutableTreeNode nodeNext = (DefaultMutableTreeNode) model.getChild(node, i);
                String name = ((DataValueEntry) nodeNext.getUserObject()).getName();
                if (!subs.containsKey(name))
                    toRemove.add(nodeNext);
                else {
                    nodeNext.setUserObject(subs.get(name));
                    subs.remove(name);
                    if (!nodeNext.isLeaf() && tree.isExpanded(new TreePath(nodeNext.getPath())))
                        queue.add(nodeNext);
                }
            }
            for (DefaultMutableTreeNode nodeNext : toRemove)
                model.removeNodeFromParent(nodeNext);
            for (Map.Entry<String, DataValueEntry> sub : subs.entrySet()) {
                DataValueEntry value = sub.getValue();
                DefaultMutableTreeNode node1 = new DefaultMutableTreeNode(value);
                model.insertNodeInto(node1, node, model.getChildCount(node));
                Map<String, DataValueEntry> subSubs = value.getSubObjects();
                for (Map.Entry<String, DataValueEntry> subSub : subSubs.entrySet()) {
                    DataValueEntry value1 = subSub.getValue();
                    DefaultMutableTreeNode node2 = new DefaultMutableTreeNode(value1);
                    model.insertNodeInto(node2, node1, model.getChildCount(node1));
                }
                if (!subSubs.isEmpty())
                    tree.collapsePath(new TreePath(node1.getPath()));
            }
        }
    }

    private void expandNode(TreeExpansionEvent event) {
        DefaultMutableTreeNode node = (DefaultMutableTreeNode) event.getPath().getLastPathComponent();
        for (int i = 0; i < node.getChildCount(); i++) {
            DefaultMutableTreeNode child = (DefaultMutableTreeNode) node.getChildAt(i);
            if (child.getChildCount() > 0)
                continue;
            DataValueEntry entry = (DataValueEntry) child.getUserObject();
            Map<String, DataValueEntry> subs = entry.getSubObjects();
            for (Map.Entry<String, DataValueEntry> sub : subs.entrySet()) {
                DataValueEntry value = sub.getValue();
                DefaultMutableTreeNode node1 = new DefaultMutableTreeNode(value);
                model.insertNodeInto(node1, child, model.getChildCount(child));
            }
        }
    }
}
