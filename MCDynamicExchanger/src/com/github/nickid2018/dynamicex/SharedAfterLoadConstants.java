package com.github.nickid2018.dynamicex;

import net.minecraft.client.*;
import org.apache.logging.log4j.*;

/**
 * The class storages some resources that can't be loaded before Minecraft
 * loads.
 * 
 * @author Nickid2018
 */
public class SharedAfterLoadConstants {

	public static final Logger logger;
	public static String version;
	public static boolean is16;

	static {
		logger = LogManager.getLogger("Dynamic Exchanger");
		version = Minecraft.getInstance().getLaunchedVersion();
		try {
			Class.forName(ClassNameTransformer.getClassName("com.mojang.blaze3d.vertex.PoseStack"));
			is16 = true;
		} catch (ClassNotFoundException e) {
			is16 = false;
		}
	}
}
