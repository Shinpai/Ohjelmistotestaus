// A sample simple programming language interpreter
// Copyright © 2017 Antti-Juhani Kaijanaho

//     Licensed under the Apache License, Version 2.0 (the "License");
//     you may not use this file except in compliance with the License.
//     You may obtain a copy of the License at

//         http://www.apache.org/licenses/LICENSE-2.0

//     Unless required by applicable law or agreed to in writing,
//     software distributed under the License is distributed on an "AS
//     IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either
//     express or implied.  See the License for the specific language
//     governing permissions and limitations under the License.

package fi.jyu.antkaij.whilelang;

import java.io.IOException;
import java.io.Writer;
import java.util.HashMap;
import static java.lang.Character.digit;
import static java.lang.Character.isDigit;
import static java.lang.Character.isWhitespace;
import static java.lang.Character.isUnicodeIdentifierStart;
import static java.lang.Character.isUnicodeIdentifierPart;

public class Interpreter {

    /** Executes the given statement. */
    public static void exec(String s, Writer out) throws IOException {
        Interpreter c = new Interpreter(s, out);
        c.exec(true);
        if (!c.endOfInput()) c.unexpected("end of input");
        out.flush();
    }

    /** Executes or skips the remaining statements in the input, up to
     * an unbalanced curly brace. */
    private void exec(boolean run) throws IOException {
        while (!endOfInput() && peekChar() != '}') {
            String name = getId();
            // NOTE: This requires Java 7 or later
            switch (name) {
            case "while":
                execWhile(run);
                break;
            case "print":
                execPrint(run);
                break;
            default:
                // assignment
                getChar('=');
                int val = eval();
                getChar(';');
                if (run) {
                    vars.put(name, val);
                }
            }
        }
    }

    /** Executes a while statement (the keyword has already been read). */
    private void execWhile(boolean run) throws IOException {
        int start = inx;
        boolean runLoop;
        do {
            inx = start;
            getChar('(');
            runLoop = eval() != 0 && run;
            getChar(')');
            getChar('{');
            exec(runLoop);
            getChar('}');
        } while (runLoop);
    }

    /** Executes a print statement (the keyword has already been read). */
    private void execPrint(boolean run) throws IOException {
        int val = eval();
        getChar(';');
        if (run) out.write(""+val+"\n");
    }

    /** Computes the integer value of the remaining arithmetical or
     * Boolean expression, up to an (unbalanced) parenthesis or
     * semicolon. */
    private int eval() {
        int a = evalArith();
        int rv = a;
        while (!endOfInput() && peekChar() != ')' && peekChar() != ';') {
            int b;
            switch (peekChar()) {
            case '=':
                getChar();
                b = evalArith();
                rv = a == b ? 1 : 0;
                a = b;
                break;
            case '<':
                getChar();
                b = evalArith();
                rv = a < b ? 1 : 0;
                a = b;
                break;
            default:
                unexpected("'<', '=', ';', ')', or the end of input");
            }
        }
        return rv;
    }


    /** Computes the integer value of the remaining arithmetical
     * expression, up to an (unbalanced) parenthesis, comparison
     * operator, or semicolon. */
    private int evalArith() {
        int a = evalTerm();
        while (!endOfInput() &&
               peekChar() != ')' &&
               peekChar() != ';' &&
               peekChar() != '=' &&
               peekChar() != '<') {
            switch (peekChar()) {
            case '+':
                getChar();
                a = a + evalTerm();
                break;
            case '-':
                getChar();
                a = a - evalTerm();
                break;
            default:
                unexpected("'+', '-', ';', ')', or the end of input");
            }
        }
        return a;
    }

    /** Evaluates the integer value of the remaining arithmetical
        expression, up to (but not including) the next + or - sign,
        (unbalanced) parenthesis, or semicolon. */
    private int evalTerm() {
        int a = evalFactor();
        while (!endOfInput() &&
               peekChar() != ')' &&
               peekChar() != ';' &&
               peekChar() != '+' &&
               peekChar() != '-' &&
               peekChar() != '=' &&
               peekChar() != '<') {
            char op = getChar();
            int b = evalFactor();
            switch (op) {
            case '*':
                a = a * b;
                break;
            case '/':
                a = a / b;
                break;
            default:
                unexpected("'*', '/', or the end of input");
            }
        }
        return a;
    }

    /** Evaluates the integer value of a single integer constant, of a
        variable name or of an arithmetical expression in
        parentheses. */
    private int evalFactor() {
        if (!endOfInput()) {
            if (peekChar() == '(') {
                getChar();
                int rv = eval();
                if (endOfInput() || getChar() != ')') {
                    unexpected("')'");
                }
                return rv;
            }
            if (isDigit(peekChar())) {
                return getInt();
            }
            if (isUnicodeIdentifierStart(peekChar())) {
                String name = getId();
                Integer val = vars.get(name);
                return val != null ? val : 0;
            }
        }
        unexpected("'(', an identifier, or a digit");
        throw new Error("internal error"); // should be unreachable
    }

    /** The full input string. */
    private final char[] input;
    /** Index to input indicating the next character not yet read. */
    private int inx = 0;
    /** Output writer to which print statements write to. */
    private final Writer out;
    /** The current values of any variables. */
    private final HashMap<String,Integer> vars = new HashMap<String,Integer>();

    /** Initializes the interpreter object.

        @param s the input string
        @param out the output writer
    */
    private Interpreter(String s, Writer out) {
        input = s.toCharArray();
        this.out = out;
    }

    /** Determines whether the end of input has been reached. */
    private boolean endOfInput() {
        skipWhitespace();
        return inx >= input.length;
    }

    /** Returns (but does not mark read) the next input character. */
    private char peekChar() {
        skipWhitespace();
        return input[inx];
    }

    /** Returns (and marks read) the next input character. */
    private char getChar() {
        skipWhitespace();
        return input[inx++];
    }

    /** Verifies that the next input character matches the argument
     * and marks it read. */
    private char getChar(char c) {
        skipWhitespace();
        if (inx >= input.length || input[inx] != c) unexpected("'" + c + "'");
        return input[inx++];
    }

    /** Returns the integer constant that the next input characters comprise,
        and marks those characters read.
        If  the next input character does not start an integer constant,
        the return value is arbitrary.
    */
    private int getInt() {
        skipWhitespace();
        int rv = 0;
        do {
            rv = 10 * rv + digit(input[inx], 10);
            inx++;
        } while (inx < input.length &&
                 isDigit(input[inx]));
        return rv;
    }

    /** Returns the identifier name that next input characters comprise,
        and marks those characters read. */
    private String getId() {
        skipWhitespace();
        if (inx >= input.length || !isUnicodeIdentifierStart(input[inx])) {
            unexpected("an identifier");
        }
        StringBuilder sb = new StringBuilder();
        while (inx < input.length && isUnicodeIdentifierPart(input[inx])) {
            sb.append(input[inx]);
            inx++;
        }
        return sb.toString();
    }

    /** If the next input characters are whitespace, marks them read. */
    private void skipWhitespace() {
        while (inx < input.length && isWhitespace(input[inx])) inx++;
    }

    /** Throws a ParseException indicating that the next input character
        is not what was expected

        @param what a string describing the expected next character
    */
    private void unexpected(String what) {
        if (endOfInput()) {
            throw new ParseException("premature end of input, expected " +
                                     what);
        } else {
            throw new ParseException("got " + peekChar() + ", expected " +
                                     what);
        }
    }
}
