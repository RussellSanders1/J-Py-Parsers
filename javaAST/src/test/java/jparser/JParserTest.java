package jparser;

import org.junit.After;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;


public class JParserTest {

    @Test
    public void parseFileTest() throws IOException {
        JParser.ParseVisitor pv = new JParser.ParseVisitor();
        Path p =  Paths.get("src/test/resources/files/JParser.java");
        //parse single file
        Files.walkFileTree(p,pv);
        File f = new File("src/test/resources/files/JParser.java.ast");
        assertTrue(f.exists());
    }

    @Test
    public void parseDirectoryTest() throws IOException {
        JParser.ParseVisitor pv = new JParser.ParseVisitor();
        Path p = Paths.get("src/test/resources/files");
        //parse directory
        Files.walkFileTree(p,pv);
        File f1 = new File("src/test/resources/files/more/JParser.java.ast");
        File f2 = new File("src/test/resources/files/JParser.java.ast");
        File f3 = new File("src/test/resources/files/more/badfile.txt.ast");
        assertTrue(f1.exists());
        assertTrue(f2.exists());
        assertFalse(f3.exists());
    }

    @After
    public void cleanup() throws IOException {
        //Delete generated .ast files
        Path p = Paths.get("src/test/resources/files/JParser.java.ast");
        Files.deleteIfExists(p);
        p = Paths.get("src/test/resources/files/more/JParser.java.ast");
        Files.deleteIfExists(p);
        p = Paths.get("src/test/resources/files/JParser.java.ast");
        Files.deleteIfExists(p);
        p = Paths.get("src/test/resources/files/more/badfile.txt.ast");
        Files.deleteIfExists(p);
    }
}
