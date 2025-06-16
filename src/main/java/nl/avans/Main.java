package nl.avans;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import nl.avans.declaration.Declaration;
import nl.avans.drawer.ConsoleDrawer;
import nl.avans.drawer.IDrawStrategy;
import nl.avans.drawer.IVisitable;
import nl.avans.drawer.plantuml.PumlDrawer;
import nl.avans.interpreter.FMS42;

public class Main {

    private static final Map<String, IDrawStrategy> displayMethods = Map.of(
        "console", new ConsoleDrawer(),
        "puml", new PumlDrawer()
    );

    public static void main(String[] args) throws IOException {
        if (args.length < 2) {
            printUsage();
            return;
        }

        String script = readScriptFromFile(args[0]);
        if (script == null) {
            System.out.println("Oops! You have probably given the interpreter a non-existing file.");
            return;
        }

        List<IVisitable> visitables = getVisitablesFromScript(script);
        IDrawStrategy displayMethod = getDisplayMethod(args[1]);
        if (displayMethod == null) {
            System.out.println("Unknown display method: " + args[1]);
            printUsage();
            return;
        }
        displayMethod.Draw(visitables);
    }

    private static void printUsage() {
        System.out.println(
            "Please provide valid arguments in the form of:\n" +
            "\t./run.sh <script_file_location> <console|puml>"
        );
    }

    private static String readScriptFromFile(String filePath) {
        StringBuilder script = new StringBuilder();
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String scriptLine;
            while ((scriptLine = br.readLine()) != null) {
                script.append(scriptLine).append("\n");
            }
            return script.toString();
        } catch (Exception e) {
            return null;
        }
    }

    private static List<IVisitable> getVisitablesFromScript(String script) throws IOException {
        List<Declaration> declarations = FMS42.interpret(script);
        return declarations.stream()
            .filter(d -> d instanceof IVisitable)
            .map(d -> (IVisitable) d)
            .collect(Collectors.toList());
    }

    private static IDrawStrategy getDisplayMethod(String fsmResultType) {
        return displayMethods.get(fsmResultType);
    }
}