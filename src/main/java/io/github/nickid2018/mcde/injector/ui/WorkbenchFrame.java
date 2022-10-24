package io.github.nickid2018.mcde.injector.ui;

import io.github.nickid2018.mcde.asmdl.ASMDLParser;
import io.github.nickid2018.mcde.asmdl.ASMDLSyntaxException;
import io.github.nickid2018.mcde.asmdl.decompile.ClassDecompileVisitor;
import io.github.nickid2018.mcde.injector.ClassDataRepository;
import io.github.nickid2018.mcde.injector.ClassWriterHacked;
import io.github.nickid2018.mcde.injector.MCProgramInjector;
import io.github.nickid2018.mcde.injector.ui.completion.CompletionData;
import io.github.nickid2018.mcde.remapper.ClassRemapperFix;
import io.github.nickid2018.mcde.util.ClassUtils;
import io.github.nickid2018.mcde.util.I18N;
import org.fife.ui.autocomplete.AutoCompletion;
import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea;
import org.fife.ui.rtextarea.RTextScrollPane;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

public class WorkbenchFrame {

    private final JFrame frame;
    private final JTabbedPane tabbedPane;
    private final JList<String> list;

    private final List<String> fileList = new ArrayList<>();
    private final Map<String, RSyntaxTextArea> textAreas = new HashMap<>();
    private final Map<String, String> codes = new HashMap<>();

    private FindDialog findDialog;

    public WorkbenchFrame(String title, Consumer<Map<String, byte[]>> onApply) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ignored) {
        }

        frame = new JFrame(title);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JSplitPane splitPane = new JSplitPane();
        frame.setContentPane(splitPane);

        tabbedPane = new JTabbedPane();
        tabbedPane.setTabLayoutPolicy(JTabbedPane.SCROLL_TAB_LAYOUT);
        tabbedPane.setFont(new Font("微软雅黑", Font.PLAIN, 12));
        splitPane.setRightComponent(tabbedPane);

        list = new JList<>();
        list.setFont(new Font("微软雅黑", Font.PLAIN, 12));
        list.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);

        splitPane.setLeftComponent(list);
        splitPane.setDividerLocation(250);

        JPopupMenu listMenu = new JPopupMenu();
        JMenuItem addFile = new JMenuItem(I18N.getTranslation("injector.workbench.add_file"));
        addFile.addActionListener(e -> {
            String className = normalizedName(JOptionPane.showInputDialog(
                    frame, I18N.getTranslation("injector.workbench.class")));
            if (className == null)
                return;
            String data = getCodeFromName(className);
            if (data == null)
                return;
            addCode(className, data);
        });
        listMenu.add(addFile);
        JMenuItem process = new JMenuItem(I18N.getTranslation("injector.workbench.process"));
        process.addActionListener(e -> {
            syncCodes();
            try {
                Map<String, byte[]> map = new HashMap<>();
                for (String className : list.getSelectedValuesList()) {
                    ASMDLParser parser = new ASMDLParser(codes.get(className),
                            () -> new ClassWriterHacked(ClassWriter.COMPUTE_FRAMES, MCProgramInjector.format.getToSourceMapper()));
                    byte[] data = parser.toClass();
                    ClassReader classReader = new ClassReader(data);
                    ClassWriter writer = new ClassWriter(0);
                    classReader.accept(new ClassRemapperFix(writer, MCProgramInjector.format.getToSourceMapper()), 0);
                    data = writer.toByteArray();
                    map.put(MCProgramInjector.format.getToSourceMapper().map(className), data);
                }
                onApply.accept(map);
            } catch (ASMDLSyntaxException ex) {
                InjectorFrame.INSTANCE.error(I18N.getTranslation("injector.workbench.syntaxerror", ex.getMessage()), null);
            }
        });
        listMenu.add(process);

        list.setComponentPopupMenu(listMenu);

        frame.setSize(800, 600);
        frame.setLocationRelativeTo(null);
    }

    public void show() {
        frame.setVisible(true);
    }

    public String normalizedName(String className) {
        if (className == null)
            return null;
        String remappedName = ClassUtils.toBinaryName(MCProgramInjector.format.getToSourceMapper().map(className));
        return ClassUtils.toBinaryName(MCProgramInjector.format.getToNamedMapper().map(remappedName));
    }

    public String getCodeFromName(String name) {
        String remappedName = ClassUtils.toBinaryName(MCProgramInjector.format.getToSourceMapper().map(name));
        byte[] bytes = ClassDataRepository.getInstance().classData.get(remappedName);
        if (bytes == null) {
            JOptionPane.showMessageDialog(frame, I18N.getTranslation(
                    "injector.workbench.class.notfound", name, remappedName));
            return null;
        }
        ClassReader remapReader = new ClassReader(bytes);
        ClassWriter remapWriter = new ClassWriter(0);
        ClassRemapperFix remapper = new ClassRemapperFix(remapWriter, MCProgramInjector.format.getToNamedMapper());
        remapReader.accept(remapper, 0);

        ClassReader reader = new ClassReader(remapWriter.toByteArray());
        ClassDecompileVisitor visitor = new ClassDecompileVisitor();
        reader.accept(visitor, 0);

        return visitor.decompiledString();
    }

    public void addCode(String name, String code) {
        codes.put(name, code);
        RSyntaxTextArea textArea = new RSyntaxTextArea();
        textArea.setSyntaxEditingStyle("text/asmdl");
        textArea.setCodeFoldingEnabled(true);
        textArea.setText(code);
        JMenuItem find = new JMenuItem(I18N.getTranslation("injector.find_dialog.name"));
        find.setAccelerator(KeyStroke.getKeyStroke("control F"));
        find.addActionListener(e -> showFindDialog(textArea));
        textArea.getPopupMenu().add(find);
        textArea.registerKeyboardAction(e -> showFindDialog(textArea),
                KeyStroke.getKeyStroke("control F"), JComponent.WHEN_FOCUSED);
        AutoCompletion ac = new AutoCompletion(CompletionData.getProvider());
        ac.install(textArea);
        textAreas.put(name, textArea);
        fileList.add(name);
        list.setListData(fileList.toArray(String[]::new));
        list.setSelectedValue(name, true);
        tabbedPane.addTab(name, new RTextScrollPane(textArea));
    }

    public void syncCodes() {
        for (String name : fileList)
            codes.put(name, textAreas.get(name).getText());
    }

    private void showFindDialog(RSyntaxTextArea textArea) {
        if (findDialog != null) {
            if (findDialog.getTextArea().equals(textArea) && findDialog.isVisible()) {
                findDialog.requestFocus();
                return;
            } else {
                findDialog.setVisible(false);
                findDialog.dispose();
            }
        }
        findDialog = new FindDialog(textArea);
        findDialog.setVisible(true);
    }
}
