# JParser

## Purpose
The purpose of this module is to walk
given directories to find java source code
files, and parse them, generating an AST
for every file and placing it in a file
with the same name, but with the AST
extension added on the end.  The AST
files are placed in the same directory
as the file they are generated from.

## Usage
Use with your favorite maven-compatible IDE (recommended)
or run on the command line by installing maven and using
```
mvn clean package
```
to generate a jar of the program, then using
```
mvn exec:java -Dexec.args="arguments"
```
to run the program, where "arguments" is
the list of directories you would like to parse

## Test
Basic unit tests demonstrate parsing of a single file,
as well as a multi-level directory with a file
that should not be parsed