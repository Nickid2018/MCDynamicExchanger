package com.github.nickid2018.mcremap;

import com.github.nickid2018.mcremap.argparser.*;

public class RemapperMain {

	public static ISystemLogger logger;

	private static String dest;

	public static void main(String[] args) throws Exception {
		logger = new DefaultConsoleLogger();
		CommandModel model = new CommandModel();
		UnorderSwitchTable table = new UnorderSwitchTable();
		model.switches.add(table);
		table.addSwitch("--output", new StringArgumentSwitch("--output"));
		table.addSwitch("--tmpdir", new StringArgumentSwitch("--tmpdir"));
		model.switches.add(new StringSwitch("mapping_url"));
		model.switches.add(new StringSwitch("mc_file"));
		try {
			CommandResult result = model.parse(args);
			OfficalFormat f = new OfficalFormat();
			f.processInitMap(result.getSwitch("mapping_url").toString());
			logger.info("Generated class mapping");
			FileRemapper remapper = new FileRemapper();
			dest = "remapped.jar";
			if (result.containsSwitch("--output"))
				dest = result.getSwitch("--output").toString();
			if (result.containsSwitch("--tmpdir"))
				FileRemapper.tmpLocation = result.getSwitch("--tmpdir").toString();
			remapper.remapAll(result.getSwitch("mc_file").toString(), f, dest);
		} catch (CommandParseException e) {
			RemapperMain.logger.error("Command is illegal!", e);
		} catch (Throwable e) {
			RemapperMain.logger.error("Unknown error has been thrown!", e);
		}
	}

}
