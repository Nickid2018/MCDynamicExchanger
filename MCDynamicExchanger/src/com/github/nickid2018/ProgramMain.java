package com.github.nickid2018;

import com.github.nickid2018.util.*;
import com.github.nickid2018.compare.*;
import com.github.nickid2018.mcremap.*;
import com.github.nickid2018.argparser.*;
import com.github.nickid2018.decompile.*;

public class ProgramMain {

	public static ISystemLogger logger;

	public static void main(String[] args) throws Exception {
		AddClassPath.addClassPathInDirs("dynamicexchanger/libs");
		CommandModel model = getRemapperModel().subCommand("compare", getComparatorModel()).subCommand("decompile",
				getDecompilerModel());
		try {
			CommandResult result = model.parse(args);
			if (result.containsSwitch("compare"))
				CompareProgram.compareSimple(result.getSwitch("old_version").toString(),
						result.getSwitch("new_version").toString());
			else if (result.containsSwitch("decompile"))
				DecompileProgram.decompileSimple(result.getSwitch("source_file").toString(),
						result.getStringOrDefault("--output", "decompiled.jar"), result.containsSwitch("-D"));
			else
				RemapProgram.doSimpleRemap(result);
		} catch (CommandParseException e) {
			(logger = new DefaultConsoleLogger()).error("Command is illegal!", e);
		}
	}

	private static CommandModel getComparatorModel() throws CommandParseException {
		CommandModel model = new CommandModel();
		model.switches.add(new StringSwitch("old_version"));
		model.switches.add(new StringSwitch("new_version"));
		return model;
	}

	private static CommandModel getDecompilerModel() throws CommandParseException {
		CommandModel model = new CommandModel();
		UnorderSwitchTable table = new UnorderSwitchTable();
		model.switches.add(table);
		table.addSwitch("--output", new StringArgumentSwitch("--output"));// Output File
		table.addLiteral(new LiteralSwitch("-D", true));// Detail Output
		model.switches.add(new StringSwitch("source_file"));
		return model;
	}

	private static CommandModel getRemapperModel() throws CommandParseException {
		CommandModel model = new CommandModel();
		UnorderSwitchTable table = new UnorderSwitchTable();
		model.switches.add(table);
		table.addLiteral(new LiteralSwitch("-Nh", true));// No hacks
		table.addLiteral(new LiteralSwitch("-D", true));// Detail Output
		table.addLiteral(new LiteralSwitch("-Dy", true));// Dry Run
		table.addSwitch("--output", new StringArgumentSwitch("--output"));// Output File
		table.addSwitch("--tmpdir", new StringArgumentSwitch("--tmpdir"));// Temporary Directory
		table.addSwitch("--outmap", new StringArgumentSwitch("--outmap"));// Output Formatted Output Map File
		table.addSwitch("--outrev", new StringArgumentSwitch("--outrev"));// Output Formatted Reverse Output Map File
																			// (For vanilla version mapping)
		model.switches.add(new StringSwitch("mapping_url"));// URL of mapping
		model.switches.add(new StringSwitch("mc_file"));// Minecraft Main JAR
		return model;
	}
}
