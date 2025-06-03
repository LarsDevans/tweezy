package nl.avans;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import nl.avans.interpreter.FMS42;

public class Main {

    public static void main(String[] args) throws IOException {
        StringBuilder script = new StringBuilder();
        try {
            BufferedReader br = new BufferedReader(new FileReader(args[0]));
            String scriptLine = null;
            while ((scriptLine = br.readLine()) != null) {
                script.append(scriptLine + "\n");
            }
            br.close();
        } catch (Exception e) {
            System.out.println("You probably given the interpreter a non-existing file");
        }

        FMS42.interpret(script.toString());
    }

}
