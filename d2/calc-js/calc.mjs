//     Written by Antti-Juhani Kaijanaho in 2018

//     Licensed under the Apache License, Version 2.0 (the "License");
//     you may not use this file except in compliance with the License.
//     You may obtain a copy of the License at

//         http://www.apache.org/licenses/LICENSE-2.0

//     Unless required by applicable law or agreed to in writing,
//     software distributed under the License is distributed on an "AS
//     IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either
//     express or implied.  See the License for the specific language
//     governing permissions and limitations under the License.


import {evaluate} from "./eval.mjs";

function eval_clicked(ev) {
    let disp = document.getElementById("display");
    disp.textContent = evaluate(disp.textContent);
}

function key_clicked(ev) {
    let disp = document.getElementById("display");
    let tgt = ev.target;
    disp.textContent += tgt.textContent;
}

function bs_clicked(ev) {
    let disp = document.getElementById("display");
    disp.textContent = disp.textContent.slice(0, -1);
}

function clear_clicked(ev) {
    let disp = document.getElementById("display");
    disp.textContent = "";
}

document.addEventListener("DOMContentLoaded", (ev) => {
    let calc_ul = document.getElementById("calc");
    for (let child of calc_ul.getElementsByClassName("key")) {
        child.addEventListener("click", key_clicked);
    }
    document.getElementById("bs").addEventListener("click", bs_clicked);
    document.getElementById("clear").addEventListener("click", clear_clicked);
    document.getElementById("eval").addEventListener("click", eval_clicked);
});
