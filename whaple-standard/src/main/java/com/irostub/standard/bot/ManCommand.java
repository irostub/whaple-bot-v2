package com.irostub.standard.bot;



public abstract class ManCommand extends BotCommand implements IManCommand {

    private final String extendedDescription;

    public ManCommand(String commandIdentifier, String description, String extendedDescription) {
        super(commandIdentifier, description);
        this.extendedDescription = extendedDescription;
    }

    @Override
    public String getExtendedDescription() {
        return extendedDescription;
    }
}

