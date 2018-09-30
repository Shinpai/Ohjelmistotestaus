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

import unittest
import sys
from selenium import webdriver
from selenium.webdriver.common.by import By
from selenium.webdriver.support.ui import WebDriverWait
from selenium.webdriver.support import expected_conditions as EC

class SeleniumTests(unittest.TestCase):

    def setUp(self):
        self.driver = webdriver.Chrome(sys.argv[1])
        self.driver.get("http://localhost:8000/")
        WebDriverWait(self.driver, 10) \
            .until(EC.element_to_be_clickable((By.ID,'eval')))

    def tearDown(self):
        self.driver.quit()

    def test_one_plus_one(self):
        key1 = self.driver.find_element(By.ID, "key1");
        add  = self.driver.find_element(By.ID, "add");
        ev   = self.driver.find_element(By.ID, "eval");
        disp = self.driver.find_element(By.ID, "display");
        self.assertEqual(disp.text, "")
        key1.click();
        add.click();
        key1.click();
        self.assertEqual(disp.text, "1+1")
        ev.click();
        self.assertEqual(disp.text, "2")

if __name__ == '__main__':
    unittest.main(argv = [sys.argv[0]] + sys.argv[2:])
