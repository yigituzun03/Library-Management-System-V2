package com.library.command;

public interface ICommand {
    void execute(); // İşlemi yap
    void undo();    // İşlemi geri al
}