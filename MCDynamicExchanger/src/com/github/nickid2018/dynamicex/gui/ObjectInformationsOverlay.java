package com.github.nickid2018.dynamicex.gui;

import java.util.*;
import net.minecraft.client.*;
import net.minecraft.client.gui.*;
import com.github.nickid2018.dynamicex.objects.*;

public class ObjectInformationsOverlay extends GuiComponent {

	public static final int DEFAULT_COLOR = 0xE0E0E0;
	public static final int DEFAULT_BACKGROUND = 0x90505050;

	private Font font;

	public ObjectInformationsOverlay(Minecraft mc) {
		font = mc.font;
	}

	/**
	 * In 1.16, render system has been changed by PoseStack rendering; but under
	 * 1.16, the function didn't need any arguments. So we have to input a null
	 * value.
	 * 
	 * @param mayPoseStack
	 */
	public void renderGlobal(Object mayPoseStack) {
		ObjectInfosHolder.elements.forEach((name, element) -> renderElement(mayPoseStack, name, element));
	}

	long last = System.currentTimeMillis();

	public void renderElement(Object mayPoseStack, String name, ObjectElement element) {
		int x0 = element.x0;
		int y0 = element.y0;
		Map<String, String> formatted = element.getFormattedInformation();
		Set<Map.Entry<String, String>> entries = formatted.entrySet();
		if (entries.size() == 0)
			return;
		int maxHeadLength = 0, maxLength = 0;
		for (Map.Entry<String, String> entry : entries) {
			maxHeadLength = Math.max(maxHeadLength, font.width(entry.getKey()));
			maxLength = Math.max(maxLength, font.width(entry.getValue()));
		}
		maxHeadLength += 2;
		int maxAll = maxHeadLength + maxLength;
		int start2 = x0 + maxHeadLength;
		RenderInterface.renderFill(mayPoseStack, x0, y0, x0 + maxAll, y0 + 9 + 9 * entries.size(), DEFAULT_BACKGROUND);
		RenderInterface.renderString(mayPoseStack, font, name, x0 + (maxAll - font.width(name)) / 2, y0, 0xFF0000);
		int now = 1;
		for (Map.Entry<String, String> entry : entries) {
			RenderInterface.renderString(mayPoseStack, font, entry.getKey(), x0, y0 + 9 * now, DEFAULT_COLOR);
			RenderInterface.renderString(mayPoseStack, font, entry.getValue(), start2, y0 + 9 * now, 0x00FF00);
			now++;
		}
	}

	static {
		System.out.println("Information Overlay added.");
	}
}
