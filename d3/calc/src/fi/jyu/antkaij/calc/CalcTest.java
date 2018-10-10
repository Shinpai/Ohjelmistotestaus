// A sample simple calculator
// Copyright Â© 2017 Antti-Juhani Kaijanaho

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

import org.junit.*;
import static org.junit.Assert.*;

/**
 * @author Antkaij, muokannut Harri Juutilainen
 * @version Oct 7, 2018
 *
 */
public class CalcTest {
    
    /**
     * Osituksen luokka 1. yhteen- ja vähennylaskut
     */
    @Test
    public void testBasic() {
        assertEquals(2, Calc.compute("1+1"));
        assertEquals(2, Calc.compute("5-3"));
    }
    
    /**
     * Osituksen 2. luokka, tapaus 1 (kertolasku)
     */
    @Test
    public void testMultiply() {
        assertEquals(9, Calc.compute("3*3"));
    }
    
    /**
     * Osituksen 2. luokka, tapaus 2 (jakolasku)
     * Ei toimi
     * Calc.java / rivi 65: pitäisi olla [ a = a / b ]
     *                      nyt on [ a = a - b ]
     */
    @Test
    public void testDivide() {
        assertEquals(7, Calc.compute("21/3"));
        assertEquals(1, Calc.compute("2/2"));
    }

    /**
     * Osituksen 3. luokka, (sulut)
     */
    @Test
    public void testParens() {
        assertEquals(20, Calc.compute("(2+3)*4"));
        assertEquals(8, Calc.compute("(1+1)*4"));
    }
    
    /**
     * Osituksen 4. luokka, (nollalla jako)
     * Ei toimi, koska jako ei toimi
     */
    @Test
    public void testDivideByZero() {
        assertEquals("Infinity", Calc.compute("5/0"));
        assertEquals("Infinity", Calc.compute("14/(3-3)"));
    }
    
    /**
     * Osituksen 5. luokka, (väärä syöte)
     */
    @Test(expected=ParseException.class)
    public void testError0() {
        Calc.compute("1!");
    }

    /**
     * Osituksen 5. luokka, (väärä syöte)
     * Syöte loppuu + (tai - , toimii samalla tavalla)
     * input Array menee yli indeksin, eikä siihen ole tarkistusta
     */
    @Test(expected=ArrayIndexOutOfBoundsException.class)
    public void testError1() {
        Calc.compute("1+");
    }
    
    /**
     * Osituksen 5. luokka, (väärä syöte)
     * Sulku väärässä kohta 
     */
    @Test(expected=ParseException.class)
    public void testError2() {
        Calc.compute("(1 - 1))- 1");
    }
    /**
     * Osituksen 5. luokka, (väärä syöte)
     * Ei lopettavaa sulkua
     */
    @Test(expected=ParseException.class)
    public void testError3() {
        Calc.compute("(1 - 1");
    }
}
