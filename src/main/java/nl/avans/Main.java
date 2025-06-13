package nl.avans;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;
import java.util.Map;

import nl.avans.declaration.Declaration;
import nl.avans.interpreter.FMS42;
import nl.avans.visitor.IDrawStrategy;
import nl.avans.visitor.ConsoleDeclarationVisitor;
import nl.avans.visitor.plantuml.PlantUmlDeclarationVisitor;

public class Main {
    private static Map<String, IDrawStrategy> displayMethods = Map.of (
        "console", new ConsoleDeclarationVisitor(),
        "plantuml", new PlantUmlDeclarationVisitor()
    );

    public static void main(String[] args) throws IOException {
        String fileLocation = args[0];

        StringBuilder script = new StringBuilder();
        try {
            BufferedReader br = new BufferedReader(new FileReader(fileLocation));
            String scriptLine = null;
            while ((scriptLine = br.readLine()) != null) {
                script.append(scriptLine + "\n");
            }
            br.close();
        } catch (Exception e) {
            System.out.println("You probably given the interpreter a non-existing file");
        }

        List<Declaration> declarations = FMS42.interpret(script.toString());

        IDrawStrategy displayMethod = displayMethods.get(args[1]);
        displayMethod.Draw(declarations); // Blabla
    }

}
