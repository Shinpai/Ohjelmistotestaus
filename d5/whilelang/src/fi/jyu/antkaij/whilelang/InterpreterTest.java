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
import java.io.StringWriter;
import org.junit.*;
import static org.junit.Assert.*;

public class InterpreterTest {

    private String run(String s) throws IOException {
        StringWriter w = new StringWriter();
        Interpreter.exec(s, w);
        return w.toString();
    }

    @Test
    public void testBasic() throws IOException {
        assertEquals("42\n", run("print 42;"));
    }

    @Test
    public void testAssign() throws IOException {
        assertEquals("42\n", run("x = 42; print x;"));
    }

    @Test
    public void testTrivialWhile() throws IOException {
        assertEquals("99\n", run("while (1 < 0) {" +
                                  "  print 42;" +
                                  "}" +
                                  "print 99;"));
    }

    @Test
    public void testWhile() throws IOException {
        assertEquals("120\n", run("rv = 1;" +
                                  "i = 1;" +
                                  "while (i < 6) {" +
                                  "  rv = rv * i;" +
                                  "  i = i + 1;" +
                                  "}" +
                                  "print rv;"));
    }

    @Test(expected = ParseException.class)
    public void testGarbageAtEnd() throws IOException {
        run("i = 1;}");
    }

    @Test
    public void testEq() throws IOException {
        assertEquals("1\n", run("print 1 = 1;"));
    }

    @Test
    public void testNEq() throws IOException {
        assertEquals("0\n", run("print 1 = 2;"));
    }

    @Test
    public void testDiv() throws IOException {
        assertEquals("3\n", run("print 6 / 2;"));
    }

    @Test
    public void testSub() throws IOException {
        assertEquals("4\n", run("print 6 - 2;"));
    }

    @Test
    public void testParen() throws IOException {
        assertEquals("20\n", run("print (2+3)*4;"));
    }

    // 235 /// 2. replaced return of integer sized value with (x == 0 ? 1 : 0) → SURVIVED
    @Test
    public void testJokutesti() throws IOException {
        run("print 0 - 0;");
    }
}
