import ast
from os import walk
from os.path import isfile, join, exists
import tokenize
import sys

"""
A pretty-printing dump function for the ast module.  The code was copied from
the ast.dump function and modified slightly to pretty-print.

Alex Leone (acleone ~AT~ gmail.com), 2010-01-30

From http://alexleone.blogspot.co.uk/2010/01/python-ast-pretty-printer.html
"""
def dump(node, annotate_fields=True, include_attributes=False, indent='  '):
    """
    Return a formatted dump of the tree in *node*.  This is mainly useful for
    debugging purposes.  The returned string will show the names and the values
    for fields.  This makes the code impossible to evaluate, so if evaluation is
    wanted *annotate_fields* must be set to False.  Attributes such as line
    numbers and column offsets are not dumped by default.  If this is wanted,
    *include_attributes* can be set to True.
    """
    def _format(node, level=0):
        if isinstance(node, ast.AST):
            fields = [(a, _format(b, level)) for a, b in ast.iter_fields(node)]
            if include_attributes and node._attributes:
                fields.extend([(a, _format(getattr(node, a), level))
                               for a in node._attributes])
            return ''.join([
                node.__class__.__name__,
                '(',
                ', '.join(('%s=%s' % field for field in fields)
                           if annotate_fields else
                           (b for a, b in fields)),
                ')'])
        elif isinstance(node, list):
            lines = ['[']
            lines.extend((indent * (level + 2) + _format(x, level + 2) + ','
                         for x in node))
            if len(lines) > 1:
                lines.append(indent * (level + 1) + ']')
            else:
                lines[-1] += ']'
            return '\n'.join(lines)
        return repr(node)
    
    if not isinstance(node, ast.AST):
        raise TypeError('expected AST, got %r' % node.__class__.__name__)
    return _format(node)

def parseprint(code, filename="<string>", mode="exec", file=None, **kwargs):
    """Parse some code from a string and pretty-print it to a file"""
    node = ast.parse(code, mode=mode)
    # write the parse dump to the file
    file.write(dump(node,**kwargs))
    file.write('\n')


def parsefile(filename):
    """Open a file with the same name as filename + .ast, and dump the parse
    of the file into this new file in the same directory as filename"""
    with open((filename + '.ast'), "w") as output:
        with tokenize.open(filename) as f:
            fstr = f.read()
        parseprint(fstr, filename=filename,include_attributes=False, annotate_fields=False, file=output)
    output.close()

# recursively call parseDir on subdirectories,
# and parsefile on files 
def parseDir(directory):
    """Recursively parse a directory by calling parseDir on subdirectories
    and parsefile on files that end with .py"""
    if exists(directory):
        for path, dirs, files in walk(directory):
            for file in files:
                if file.endswith('.py'):
                    parsefile(join(path,file))
            for dir in dirs:
                parseDir(join(path,dir))
    else:
        print(directory +' does not exist')

#parse directory in argv if it exists
def main():
    """Run the program passing in an argument
    vector containing the directory to parse"""

    if len(sys.argv) == 1:
        print('Error: Provide at least one path')
        return

    for arg in sys.argv[1:]:
        parseDir(arg)
   
#entry
if __name__ == '__main__':
    main()