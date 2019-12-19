package jparser;

import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.printer.YamlPrinter;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import com.github.javaparser.*;
import static java.nio.file.FileVisitResult.*;
import static org.apache.commons.io.FilenameUtils.*;

public class JParser {
    public static class ParseVisitor extends SimpleFileVisitor<Path> {

        /**
         * This is the function called on files during the directory
         * walk.  If the file is a java source file, it is parsed,
         * and its AST representation is dumped in YAML format into
         * a file of the same name in the same directory, the only
         * difference being that the generated file has an added .ast extension.
         *
         * @param file the file currently being visited
         * @param attr the attributes of the current file
         * @return a FileVisitResult indicating to continue the walk
         * @throws IOException if errors occur in either parsing or file output
         */
        @Override
        public FileVisitResult visitFile(Path file, BasicFileAttributes attr) throws IOException {
            if(getExtension(String.valueOf(file)).equals("java")){
                // Parse the file
                CompilationUnit parse = StaticJavaParser.parse(file);

                //create file.ast for output
                BufferedWriter writer = new BufferedWriter(new FileWriter(file+".ast"));
                //dump YAML format of parse into file.ast
                YamlPrinter printer = new YamlPrinter(false);
                writer.write(printer.output(parse));
                writer.close();
            }
            return CONTINUE;
        }
    }

    /**
     * Check at least one path is provided,
     * and walk all paths provided using the
     * ParseFiles class to perform the parse
     * and generate the output
     *
     * @param args paths to directories to be parsed
     * @throws IOException if errors occur in file walk
     */
    public static void main(String[] args) throws IOException {
        ParseVisitor pv = new ParseVisitor();

        //check args is not empty
        if (args.length == 0){
            System.out.println("Error: Provide at least one path");
            return;
        }

        //use the parsevisitor to walk every directory
        for (String arg : args) {
            Path p = Paths.get(arg);
            Files.walkFileTree(p,pv);
        }
    }

}
