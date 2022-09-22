package io.github.nickid2018.mcde.asmdl.decompile;

import java.util.Stack;

public class DecompileContext {

    private final TextBlockElement root;
    private final Stack<TextBlockElement> elementStack = new Stack<>();

    public DecompileContext(TextBlockElement root) {
        this.root = root;
        elementStack.push(root);
    }

    public TextBlockElement getRoot() {
        return root;
    }

    public void pushBlock(TextBlockElement element) {
        elementStack.peek().addElement(element);
        elementStack.push(element);
    }

    public void addElement(TextElement element) {
        elementStack.peek().addElement(element);
    }

    public void popBlock() {
        elementStack.pop();
    }
}
