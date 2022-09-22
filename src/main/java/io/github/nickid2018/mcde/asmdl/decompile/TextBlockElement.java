package io.github.nickid2018.mcde.asmdl.decompile;

import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;

public class TextBlockElement extends TextElement {

    private final List<TextElement> elements = new ArrayList<>();

    public TextBlockElement(String text) {
        super(text);
    }

    public void addElement(TextElement element) {
        elements.add(element);
    }

    @Override
    public void append(StringWriter writer, int indent) {
        if (elements.size() > 0) {
            writer.append(INDENT.repeat(indent)).append(text).append(" {\n");
            for (TextElement element : elements)
                element.append(writer, indent + 1);
            writer.append(INDENT.repeat(indent)).append("}\n");
        } else
            super.append(writer, indent);
    }
}
