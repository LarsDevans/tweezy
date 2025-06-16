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

public abstract class FMS42 {

    private static ParsingContext parsingContext = new ParsingContext();

    /*
     * Allow this method to crash the program, because any exception throws are
     * a result of invalid .fsm-scripting syntax.
     *
     * We are not writing a fail-proof interpreter, we are writing a FSM for a
     * school project that will never go into production.
     */
    public static List<Declaration> interpret(String script) throws IOException {
        String sanitizedScript = sanitizeScript(script);

        // Positive lookbehind that split the chain of statements into singular statements.
        String[] statements = sanitizedScript.split("(?<=;)");

        // Every declaration extracted from the statements.
        List<Declaration> declarations = new ArrayList<>();

        for (int i = 0; i < statements.length; i++) {
            String scriptLine = statements[i];
            Token[] tokens = extractTokensFromStatement(scriptLine);

            // Token index zero is always the declaration identifier.
            String identifier = tokens[0].literal();
            Declaration declaration = DeclarationFactory.create(
                identifier,
                tokens,
                parsingContext
            );

            declarations.add(declaration);
        }

        // First, all declarations need their references and rulesets before
        // confirming or denying the validity of it.
        for (Declaration declaration : declarations) {
            declaration.attachReferences(parsingContext);
            declaration.attachRuleset();
        }

        for (Declaration declaration : declarations) {
            declaration.getRuleset().validate();
        }

        return declarations;
    }

    private static Token[] extractTokensFromStatement(String statement) {
        Tokenizer tokenizer = TokenizerFactory.create(statement);
        return tokenizer.tokenize();
    }

    private static String sanitizeScript(String script) {
        // Replaces all comment character (#) with a blank string.
        String withoutComments = script.replaceAll("#.*|\\n", "");

        // Replaces all whitespace gaps bigger than one with a single whitespace character.
        String normalizedWhitespace = withoutComments.replaceAll(
            "\\s{2,}",
            " "
        );

        return normalizedWhitespace.trim();
    }
}
