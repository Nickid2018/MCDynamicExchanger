package com.github.nickid2018.dynamicex;

import org.apache.log4j.*;
import java.lang.instrument.*;

public class DEProgramInterface {

	public static Instrumentation instrumention;
	public static final Logger logger;

	public static void premain(String preargs, Instrumentation inst) {
		instrumention = inst;
		inst.addTransformer(new DETransformer());
	}

	static {
		PropertyConfigurator.configure(DEProgramInterface.class
				.getResourceAsStream("/com/github/nickid2018/dynamicex/resources/defaultLogFormat.properties"));
		logger = Logger.getLogger("DynamicExchanger");
	}
}
