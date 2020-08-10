package com.github.nickid2018.mcremap;

public class RemapperMain {

	private static String dest;

	public static void main(String[] args) throws Exception {
		// Command Format:
		// remap.jar [--output:<destination>] <mapping_file_url>
		// <source_jar>
		OfficalFormat f = new OfficalFormat();
		f.processInitMap(args[args.length - 2]);
		System.out.println("Generated class mapping");
		FileRemapper remapper = new FileRemapper();
		dest = "remapped.jar";
		if (args.length > 2) {
			for (String arg : args) {
				if (arg.startsWith("--output")) {
					dest = arg.split(":", 2)[1];
				}
			}
		}
		remapper.remapAll(args[args.length - 1], f, dest);
	}

}
