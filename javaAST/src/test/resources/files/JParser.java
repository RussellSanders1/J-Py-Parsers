import com.github.javaparser.StaticJavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.printer.YamlPrinter;
import org.apache.commons.io.FilenameUtils;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;

import static java.nio.file.FileVisitResult.CONTINUE;

public class JParser {

    public static class ParseFiles extends SimpleFileVisitor<Path> {

        /**
         * visitFile
         *
         * This is the function called on files during the directory
         * walk.  If the file is a java source file, it is parsed,
         * and its AST representation is dumped in YAML format into
         * a file of the same name in the same directory, the only
         * difference being that the file has an added .ast extension.
         *
         * @param file: the file currently being visited
         * @param attr: the attributes of the current file
         * @return a FileVisitResult indicating to continue the walk
         * @throws IOException if errors occur in either parsing or file output
         */
        @Override
        public FileVisitResult visitFile(Path file, BasicFileAttributes attr) throws IOException {
            if(FilenameUtils.getExtension(String.valueOf(file)).equals("java")){
                CompilationUnit parse = StaticJavaParser.parse(file);
                YamlPrinter printer = new YamlPrinter(false);
                BufferedWriter writer = new BufferedWriter(new FileWriter(file+".ast"));
                writer.write(printer.output(parse));
            }
            return CONTINUE;
        }

        /**
         * visitFileFailed
         *
         * Prints IOExceptions caught during the walk of a directory.
         * @param file: the file being currently visited
         * @param exc: the exception being reported
         * @return a FileVisitResult indicating to continue the walk
         */
        @Override
        public FileVisitResult visitFileFailed(Path file,
                                               IOException exc) {
            System.err.println(exc);
            return CONTINUE;
        }
    }


    /**
     * main
     *
     * Check at least one path is provided,
     * and walk all paths provided using the
     * ParseFiles class to perform the parse
     * and generate the output
     *
     * @param args: paths to directories to be parsed
     * @throws IOException if errors occur in file walk
     */
    public static void main(String[] args) throws IOException {
        ParseFiles pf = new ParseFiles();

        if (args.length == 0){
            System.out.println("Error: Provide at least one path");
            return;
        }

        for (String arg : args) {
            Path p = Paths.get(arg);
            Files.walkFileTree(p,pf);
        }
    }

}
