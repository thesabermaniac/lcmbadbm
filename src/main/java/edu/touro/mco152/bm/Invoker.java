package edu.touro.mco152.bm;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Invoker {

    private final List<Command> commandList = new ArrayList<Command>();

    public void addCommand(Command command){
        commandList.add(command);
    }

    public void executeCommands() throws IOException {
        for(Command c : commandList){
            c.execute();
        }
        commandList.clear();
    }
}
