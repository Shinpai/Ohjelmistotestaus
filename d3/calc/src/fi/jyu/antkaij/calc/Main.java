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

public class Main {

    public static void main(String[] args) {
        if (args.length == 0) {
            System.err.println("No arguments given.");
            System.exit(1);
        }
        StringBuilder sb = new StringBuilder(args[0]);
        for (int i = 1; i < args.length; i++) {
            sb.append(' ');
            sb.append(args[i]);
        }
        int res = Calc.compute(sb.toString());
        System.out.println(res);
    }
}
