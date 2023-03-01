package com.irostub.whaple.bot;

import org.apache.commons.lang3.StringUtils;

public interface IManCommand {
    String getExtendedDescription();
    default String toMan(){
        StringBuilder sb = new StringBuilder(toString());
        sb.append(System.lineSeparator())
                .append("-----------------")
                .append(System.lineSeparator());
        String swap = StringUtils.replace(getExtendedDescription(), BotCommand.DESCRIPTION_SWAP_IDENTIFIER, BotCommand.COMMAND_INIT_CHARACTER);
        if (swap != null) sb.append(swap);
        return sb.toString();
    }
}
