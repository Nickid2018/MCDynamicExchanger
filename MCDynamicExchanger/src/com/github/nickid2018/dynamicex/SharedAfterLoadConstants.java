package com.github.nickid2018.dynamicex;

import org.apache.logging.log4j.*;

/**
 * The class storages some resources that can't be loaded before Minecraft
 * loads.
 * 
 * @author Nickid2018
 */
public class SharedAfterLoadConstants {

	public static final Logger logger;

	static {
		logger = LogManager.getLogger("Dynamic Exchanger");
	}
}
