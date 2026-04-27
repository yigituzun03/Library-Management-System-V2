package com.library.command;

import java.util.Stack;

public class CommandHistory {
    // Komutları LIFO (Son giren ilk çıkar) mantığıyla tutacak yığın
    private Stack<ICommand> history = new Stack<>();

    public void push(ICommand command) {
        command.execute(); // Komutu çalıştır
        history.push(command); // Başarılı olursa geçmişe ekle
    }

    public boolean undo() {
        if (!history.isEmpty()) {
            ICommand command = history.pop(); // En son komutu yığından al
            command.undo(); // Geri alma metodunu tetikle
            return true;
        } else {
            System.out.println("Hata: Geri alınacak bir işlem bulunamadı.");
            return false;
        }
    }
}