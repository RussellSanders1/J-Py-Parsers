# PyParser

## Purpose
The purpose of this module is to walk given directories to find python source code files, and parse them, generating an AST for every file and placing it in a file with the same name, but with the AST extension added on the end.  The AST files are placed in the same directory as the file they are generated from.

## Usage
Open your terminal with python3 installed and call the command

```bash
python pyparser.py paths
```

where paths is a list of either absolute paths or relative paths to the current directory.

## Dependencies
None! This code works using the ast module in python and requires no other modules to be installed.

## Test
Use the command
```
python -m unittest testparse
```
to run tests to confirm files and directories are correctly accessed.
