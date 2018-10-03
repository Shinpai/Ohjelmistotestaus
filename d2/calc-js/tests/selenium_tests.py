#    Written by Antti-Juhani Kaijanaho in 2018

#    Licensed under the Apache License, Version 2.0 (the "License");
#    you may not use this file except in compliance with the License.
#    You may obtain a copy of the License at

#        http://www.apache.org/licenses/LICENSE-2.0

#    Unless required by applicable law or agreed to in writing,
#    software distributed under the License is distributed on an "AS
#    IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either
#    express or implied.  See the License for the specific language
#    governing permissions and limitations under the License.

#    Muutokset 01.10.2018 @haeejuut, Harri Juutilainen

import unittest
import sys
from selenium import webdriver
from selenium.webdriver.common.by import By
from selenium.webdriver.support.ui import WebDriverWait
from selenium.webdriver.support import expected_conditions as EC
from selenium.webdriver.chrome.options import Options


class SeleniumTests(unittest.TestCase):

    def setUp(self):
        # ajetaan ilman UI:ta
        options = Options()
        options.headless = True
        self.driver = webdriver.Chrome(sys.argv[1],
                                       options=options)
        # alustetaan ajuri
        self.driver.get("http://127.0.0.1:8000/")
        WebDriverWait(self.driver, 10) \
            .until(EC.element_to_be_clickable((By.ID, 'eval')))

        # käytettävät elementit täällä koska ne ei muutu eri testien välillä
        self.disp = self.driver.find_element_by_id('display')
        self.b_eval = self.driver.find_element_by_id('eval')
        self.b_1 = self.driver.find_element_by_id('key1')
        self.b_2 = self.driver.find_element_by_id('key2')
        self.add = self.driver.find_element_by_id('add')
        self.mul = self.driver.find_element_by_id('mul')
        self.div = self.driver.find_element_by_id('div')
        self.sub = self.driver.find_element_by_id('sub')
        self.keyOP = self.driver.find_element_by_id('keyOP')
        self.keyCP = self.driver.find_element_by_id('keyCP')
        self.clear = self.driver.find_element_by_id('clear')

    def tearDown(self):
        self.driver.quit()

    def test_plus(self):
        # Osituksen 1. luokka, tapaus 1 (yhteenlasku)
        # 1 + 1 = 2
        self.assertEqual(self.disp.text, "")
        self.b_1.click(), self.add.click(), self.b_1.click()
        self.assertEqual(self.disp.text, "1+1")
        self.b_eval.click()
        self.assertEqual(self.disp.text, "2")

    def test_minus(self):
        # Osituksen 1. luokka, tapaus 2 (vähennyslasku)
        # 2 - 1 = 1
        self.assertEqual(self.disp.text, "")
        self.b_2.click(), self.sub.click(), self.b_1.click()
        self.assertEqual(self.disp.text, '2-1')
        self.b_eval.click()
        self.assertEqual(self.disp.text, '1')

    def test_kerto(self):
        # Osituksen 2. luokka, tapaus 1 (kertolasku)
        # 2 * 1 = 2
        self.assertEqual(self.disp.text, "")
        self.b_2.click(), self.mul.click(), self.b_1.click()
        self.assertEqual(self.disp.text, "2×1")
        self.b_eval.click()
        self.assertEqual(self.disp.text, "2")

    def test_jako(self):
        # Osituksen 2. luokka, tapaus 2 (jakolasku)
        # 2 / 2 = 1
        self.b_2.click(), self.div.click(), self.b_2.click()
        self.assertEqual(self.disp.text, "2/2")
        self.b_eval.click()
        self.assertEqual(self.disp.text, "1")

    def test_sulut(self):
        # Osituksen 3. luokka, (sulut)
        # (2 / 2) - 1 = 0
        self.keyOP.click(), self.b_2.click(), self.div.click()
        self.b_2.click(), self.keyCP.click()
        self.assertEqual(self.disp.text, "(2/2)")
        self.sub.click(), self.b_1.click(), self.b_eval.click()
        self.assertEqual(self.disp.text, "0")

    def test_nollajako(self):
        # Osituksen 4. luokka, (nollalla jako)
        # 1 / (2 - 2)
        self.b_1.click(), self.div.click()
        self.keyOP.click(), self.b_2.click(), self.sub.click()
        self.b_2.click(), self.keyCP.click(), self.b_eval.click()
        self.assertEqual(self.disp.text, "Infinity")
    
    def test_syote(self):
        # Osituksen 5. luokka, (väärä syöte)
        # )(21-+(1
        self.keyCP.click(), self.keyOP.click(), self.b_2.click(),
        self.assertEqual(self.disp.text, ")(2")
        self.sub.click(), self.add.click(), self.keyOP.click(),
        self.assertEqual(self.disp.text, ")(2-+(")
        # Tässä testissä tärkein huomio ettei eval-näppäimen
        # painaminen tee mitään jos syöte on väärä
        self.b_1.click(), self.b_eval.click()
        self.assertEqual(self.disp.text, ")(2-+(1")

if __name__ == '__main__':
    unittest.main(argv=[sys.argv[0]] + sys.argv[2:])
