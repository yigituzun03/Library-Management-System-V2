package com.library.command;

import java.util.Stack;

public class CommandHistory {

    private Stack<ICommand> history = new Stack<>();

    public void push(ICommand command) {
        command.execute();
        history.push(command);
    }

    public boolean undo() {
        if (!history.isEmpty()) {
            ICommand command = history.pop();
            command.undo();
            return true;
        } else {
            System.out.println("  [!!] Nothing to undo.");
            return false;
        }
    }
}