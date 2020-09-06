package com.github.nickid2018.mcremap.argparser;

import java.util.*;

public class CommandModel {

	public final Vector<CommandSwitch> switches;

	public CommandModel() {
		this(new Vector<>());
	}

	public CommandModel(Vector<CommandSwitch> switches) {
		this.switches = switches;
	}

	public CommandResult parse(String[] args) throws CommandParseException {
		CommandResult result = new CommandResult();
		int nowPosition = 0;
		boolean allowReadNextToken = false;
		int requiresArgs = 0;
		int nowIndex = 0;
		boolean meetLast = false;
		CommandSwitch now = switches.elementAt(0);
		CommandSwitch writingIn = null;
		for (String token : args) {
			if (meetLast && requiresArgs == 0)
				throw new CommandParseException("The command is illegal!");
			if (requiresArgs > 0) {
				requiresArgs--;
				writingIn.nextArgument(nowIndex++, token);
			} else {
				boolean needReadNext = true;
				while (needReadNext) {
					if (now.isThisSwitch(token)) {
						result.putSwitch(now.getSwitchName(token), writingIn = now.getSwitchInstance(token));
						requiresArgs = writingIn.requireArguments();
						nowIndex = 0;
						if (!now.isRepeatable())
							allowReadNextToken = true;
						needReadNext = false;
					} else {
						if (now.isOptional() || now.isRepeatable()) {
							allowReadNextToken = true;
							needReadNext = true;
						} else
							throw new CommandParseException("Unknown token: " + token);
					}
					if (allowReadNextToken) {
						if (nowPosition == switches.size() - 1) {
							if (needReadNext)
								throw new CommandParseException("The command is illegal!");
							meetLast = true;
						} else
							now = switches.elementAt(++nowPosition);
						allowReadNextToken = false;
					}
				}
			}
		}
		if (!meetLast)
			throw new CommandParseException("The command is illegal!");
		return result;
	}
}
