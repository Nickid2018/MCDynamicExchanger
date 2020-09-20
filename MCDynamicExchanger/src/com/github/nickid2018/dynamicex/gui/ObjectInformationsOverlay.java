package com.github.nickid2018.dynamicex.gui;

import net.minecraft.client.*;
import net.minecraft.client.gui.*;

public class ObjectInformationsOverlay extends GuiComponent {
	
	public static final int DEFAULT_COLOR = 0xE0E0E0;

	private Minecraft minecraft;
	private Font font;

	public ObjectInformationsOverlay(Minecraft mc) {
		minecraft = mc;
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
		RenderInterface.renderString(mayPoseStack, font, "OHHHHH", 10, 10, DEFAULT_COLOR);
	}
}
