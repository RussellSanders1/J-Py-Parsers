import pyparser
import unittest
from os.path import exists
from os import remove
from unittest.mock import patch
import sys

class TestParser(unittest.TestCase):

    def test_file(self):
        pyparser.parsefile('files/test.py')

        # this file should exist
        self.assertTrue(exists('files/test.py.ast'))

        # compare output
        f = open('files/test.py.ast')
        lines = f.read()
        f.close()
        self.assertEqual(lines,
        "Module([\n    Expr(Call(Name('print', Load()), [\n        BinOp(Constant('Hello World ', None), Add(), Constant('test', None)),\n      ], [])),\n  ], [])\n"
        )

        # cleanup
        remove('files/test.py.ast')

    def test_dir(self):
        pyparser.parseDir('files')
        # these files should exist
        self.assertTrue(exists('files/more/test3.py.ast'))
        self.assertTrue(exists('files/astpp.py.ast'))
        self.assertTrue(exists('files/test2.py.ast'))
        self.assertTrue(exists('files/test.py.ast'))

        # this file should not exist
        self.assertFalse(exists('files/more/badfile.txt.ast'))

        # cleanup
        remove('files/test.py.ast')
        remove('files/test2.py.ast')
        remove('files/more/test3.py.ast')
        remove('files/astpp.py.ast')