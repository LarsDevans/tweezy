package nl.avans.interpreter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import nl.avans.declaration.Declaration;
import nl.avans.parser.DeclarationFactory;
import nl.avans.parser.ParsingContext;
import nl.avans.tokenizer.Token;
import nl.avans.tokenizer.Tokenizer;
import nl.avans.tokenizer.TokenizerFactory;
import nl.avans.visitor.ConsoleDeclarationVisitor;
import nl.avans.visitor.IDrawStrategy;
import nl.avans.visitor.plantuml.PlantUmlDeclarationVisitor;

public abstract class FMS42 {

    private static ParsingContext parsingContext = new ParsingContext();

    /*
     * Allow this method to crash the program, because any exception throws are
     * a result of invalid .fsm-scripting syntax.
     *
     * We are not writing a fail-proof interpreter, we are writing a FSM for a
     * school project that will never go into production.
     */
    public static void interpret(String script) throws IOException {
        String sanitizedScript = sanitizeScript(script);
        String[] statements = sanitizedScript.split("(?<=;)");

        List<Declaration> declarations = new ArrayList<>();

        for (int i = 0; i < statements.length; i++) {
            String scriptLine = statements[i];
            Token[] tokens = extractTokensFromStatement(scriptLine);

            String identifier = tokens[0].literal();
            Declaration declaration = DeclarationFactory.create(
                identifier,
                tokens,
                parsingContext
            );

            declarations.add(declaration);
        }

        for (Declaration declaration : declarations) {
            declaration.attachReferences(parsingContext);
            declaration.attachRuleset();
        }

        for (Declaration declaration : declarations) {
            if (declaration.getRuleset() != null) {
                declaration.getRuleset().validate();
            }
        }

        // IDrawStrategy drawer = new PlantUmlDeclarationVisitor();
        IDrawStrategy drawer = new ConsoleDeclarationVisitor();
        drawer.Draw(declarations);
    }

    private static Token[] extractTokensFromStatement(String statement) {
        Tokenizer tokenizer = TokenizerFactory.create(statement);
        return tokenizer.tokenize();
    }

    private static String sanitizeScript(String script) {
        String withoutComments = script.replaceAll("#.*|\\n", "");
        String normalizedWhitespace = withoutComments.replaceAll(
            "\\s{2,}",
            " "
        );
        return normalizedWhitespace.trim();
    }

}
