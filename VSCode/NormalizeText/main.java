package LAB211.NormalizeText;

import java.io.*;

import LAB211.NormalizeText.Model.*;
import LAB211.NormalizeText.Controller.Reader;

public class main {
    public static void main(String[] args) {
        Reader read = new Reader();
        // Model mods = new Model();
        // String locate = "C:\\Users\\kleqing\\OneDrive - The Creator\\Documents\\Code\\LAB211\\NormalizeText\\test.txt";
        // mods.setInput(locate);
        // File file = new File(mods.getInput());
        read.run();
    }

    
}
