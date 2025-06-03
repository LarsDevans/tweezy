package nl.avans.parser;

import nl.avans.declaration.Declaration;
import nl.avans.tokenizer.Token;

// ANCHOR - Strategy pattern
public interface DeclarationParserStrategy {

    public abstract Declaration parse(Token[] tokens, ParsingContext ctx);

}
