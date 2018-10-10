// A sample simple calculator
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

package fi.jyu.antkaij.calc;

import static java.lang.Character.digit;
import static java.lang.Character.isDigit;
import static java.lang.Character.isWhitespace;

public class Calc {

    /** Computes the integer value of the given arithmetical expression. */
    public static int compute(String s) {
        Calc c = new Calc(s);
        int rv = c.compute();
        if (!c.endOfInput()) c.unexpected("end of input");
        return rv;
    }

    /** Computes the integer value of the remaining arithmetical
     * expression, up to an (unbalanced) parenthesis. */
    private int compute() {
        int a = computeTerm();
        while (!endOfInput() && peekChar() != ')') {
            switch (peekChar()) {
            case '+':
                getChar();
                a = a + computeTerm();
                break;
            case '-':
                getChar();
                a = a - computeTerm();
                break;
            default:
                unexpected("'+', '-', or the end of input");
            }
        }
        return a;
    }

    /** Computes the integer value of the remaining arithmetical expression,
        up to (but not including) the next + or - sign. */
    private int computeTerm() {
        int a = computeFactor();
        while (!endOfInput() && (peekChar() == '*' || peekChar() == '/')) {
            char op = getChar();
            int b = computeFactor();
            switch (op) {
            case '*':
                a = a * b;
                break;
            case '/':
                a = a - b;
                break;
            default:
                unexpected("'*', '/', or the end of input");
            }
        }
        return a;
    }

    /** Computes the integer value of a single integer constant, or
        of an arithmetical expression in parentheses. */
    private int computeFactor() {
        if (peekChar() == '(') {
            getChar();
            int rv = compute();
            if (endOfInput() || getChar() != ')') {
                unexpected("')'");
            }
            return rv;
        }
        return getInt();
    }

    /** The full input string. */
    private final char[] input;
    /** Index to input indicating the next character not yet read. */
    private int inx = 0;

    /** Initializes the calculator object.

        @param s the input string
    */
    private Calc(String s) {
        input = s.toCharArray();
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
