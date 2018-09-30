from selenium import webdriver
from selenium.webdriver.chrome.options import Options
import unittest
import sys
import inspect

# Määritellään driver
driver = webdriver.Chrome(sys.argv[1])
driver.get('http://localhost:8000/calc-js')

# Käytettävät elementit muuttujiin
result = driver.find_element_by_id('display')
b_eval = driver.find_element_by_id('eval')
b_1 = driver.find_element_by_id('key1')
b_2 = driver.find_element_by_id('key2')
add = driver.find_element_by_id('add')
mul = driver.find_element_by_id('mul')
div = driver.find_element_by_id('div')
sub = driver.find_element_by_id('sub')
keyOP = driver.find_element_by_id('keyOP')
keyCP = driver.find_element_by_id('keyCP')
clear = driver.find_element_by_id('clear')


def testi_plus():
    # 1 + 1 = 2
    vaste = '2'
    b_1.click(), add.click(), b_1.click(), b_eval.click()
    tulos = result.text
    if tulos == vaste:
        print('%s == %s, %s -- OK' % (tulos, vaste, inspect.stack()[0][3]))


def testi_minus():
    # 2 - 1 = 1
    vaste = '1'
    b_2.click(), sub.click(), b_1.click(), b_eval.click()
    tulos = result.text
    if tulos == vaste:
        print('%s == %s, %s -- OK' % (tulos, vaste, inspect.stack()[0][3]))


def testi_kerto():
    # 2 * 1 = 2
    vaste = '2'
    b_2.click(), mul.click(), b_1.click(), b_eval.click()
    tulos = result.text
    if tulos == vaste:
        print('%s == %s, %s -- OK' % (tulos, vaste, inspect.stack()[0][3]))


def testi_jako():
    # 2 / 2 = 1
    vaste = '1'
    b_2.click(), div.click(), b_2.click(), b_eval.click()
    tulos = result.text
    if tulos == vaste:
        print('%s == %s, %s -- OK' % (tulos, vaste, inspect.stack()[0][3]))


def testi_sulut():
    # (2 / 2) - 1 = 0
    vaste = '0'
    keyOP.click(), b_2.click(), div.click(), b_2.click()
    keyCP.click(), sub.click(), b_1.click(), b_eval.click()
    tulos = result.text
    if tulos == vaste:
        print('%s == %s, %s -- OK' % (tulos, vaste, inspect.stack()[0][3]))


def main():
    print("-- Testit --")
    testi_plus(), clear.click()
    testi_minus(), clear.click()
    testi_kerto(), clear.click()
    testi_jako(), clear.click()
    testi_sulut(), clear.click()
    driver.close()

if __name__ == "__main__":
    main()
