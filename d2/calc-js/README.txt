Start development in Ubuntu or other Linux:

  python3 -m venv venv

Each time in a new shell:

  . venv/bin/activate

You can run the development localhost server with

  python3 server.py

To work with the Selenium tests

  pip install selenium

(Also make sure you have chromedriver installed.)

You can then run the tests with

  python3 tests/selenium_tests.py PATH-TO-CHROMEDRIVER
