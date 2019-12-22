package gitlet;

import ucb.junit.textui;
import org.junit.Test;

import java.io.File;
import java.io.IOException;

import static org.junit.Assert.*;

/**
 * The suite of all JUnit tests for the gitlet package.
 *
 * @author Mingjie Chen
 */
public class UnitTest {

    /**
     * Run the JUnit tests in the loa package. Add xxxTest.class entries to
     * the arguments of runClasses to run other JUnit tests.
     */
    public static void main(String[] ignored) {
        textui.runClasses(UnitTest.class);
    }

    /**
     * A dummy test to avoid complaint.
     */
    @Test
    public void placeholderTest() {
    }

    @Test
    public void testInit() throws IOException, ClassNotFoundException {
        Main.main("init");
        deleteDir(new File(".gitlet"));
    }

    @Test
    public void testAdd() throws IOException, ClassNotFoundException {
        Main.main("init");
        Main.main("add", "testing/src/wug.txt");
        deleteDir(new File(".gitlet"));
    }

    @Test
    public void testCommit() throws IOException, ClassNotFoundException {
        Main.main("init");
        Main.main("add", "testing/src/wug.txt");
        Main.main("add", "testing/src/notwug.txt");
        Main.main("commit", "commit wug");
        deleteDir(new File(".gitlet"));
    }

    @Test
    public void testCheckout1() throws IOException, ClassNotFoundException {
        Main.main("init");
        Main.main("add", "testing/src/wug.txt");
        Main.main("commit", "commit wug");
        Main.main("add", "testing/src/notwug.txt");
        Main.main("commit", "commit wug");
        Main.main("branch", "newBranch");
        Main.main("checkout", "newBranch");
        deleteDir(new File(".gitlet"));
    }

    @Test
    public void testCheckout2() throws IOException, ClassNotFoundException {
        Main.main("init");
        Main.main("add", "testing/src/wug.txt");
        Main.main("commit", "commit wug");
        Main.main("checkout", "--", "wug.txt");
        deleteDir(new File(".gitlet"));
    }

    @Test
    public void testCheckout3() throws IOException, ClassNotFoundException {
        Main.main("init");
        Main.main("add", "testing/src/wug.txt");
        Main.main("commit", "version 1 of wug");
        Main.main("add", "testing/src/wug.txt");
        Main.main("commit", "version 2 of wug");
        Main.main("log");
        deleteDir(new File(".gitlet"));
    }

    @Test
    public void tsetRm() throws IOException, ClassNotFoundException {
        Main.main("init");
        Main.main("add", "wug.txt");
        Main.main("commit", "commit wug");
        Main.main("rm", "wug.txt");
        deleteDir(new File(".gitlet"));
    }

    @Test
    public void testLog() throws IOException, ClassNotFoundException {
        Main.main("init");
        Main.main("add", "testing/src/wug.txt");
        Main.main("commit", "added wug");
        Main.main("log");
        deleteDir(new File(".gitlet"));
    }

    @Test
    public void testGLog() throws IOException, ClassNotFoundException {
        Main.main("init");
        Main.main("add", "testing/src/wug.txt");
        Main.main("commit", "commit wug");
        Main.main("global-log");
    }

    @Test
    public void testFind() throws IOException, ClassNotFoundException {
        Main.main("init");
        Main.main("add", "testing/src/wug.txt");
        Main.main("commit", "commit wug");
        Main.main("add", "testing/src/notwug.txt");
        Main.main("commit", "commit wug");
        Main.main("find", "commit wug");
        Main.main("global-log");
        deleteDir(new File(".gitlet"));
    }

    @Test
    public void testStatus() throws IOException, ClassNotFoundException {
        Main.main("init");
        Main.main("add", "testing/src/wug.txt");
        Main.main("status");
        Main.main("add", "testing/src/notwug.txt");
        Main.main("status");
        Main.main("commit", "commit wug");
        Main.main("status");
        deleteDir(new File(".gitlet"));
    }

    @Test
    public void testBranch() throws IOException, ClassNotFoundException {
        Main.main("init");
        Main.main("add", "testing/src/wug.txt");
        Main.main("commit", "commit wug");
        Main.main("add", "testing/src/notwug.txt");
        Main.main("commit", "commit wug");
        Main.main("branch", "newBranch");
        Main.main("checkout", "newBranch");
        deleteDir(new File(".gitlet"));
    }

    @Test
    public void testRmBranch() throws IOException, ClassNotFoundException {
        Main.main("init");
        Main.main("add", "testing/src/wug.txt");
        Main.main("commit", "commit wug");
        Main.main("add", "testing/src/notwug.txt");
        Main.main("commit", "commit wug");
        Main.main("branch", "newBranch");
        Main.main("checkout", "newBranch");
        Main.main("rm-branch", "master");
        deleteDir(new File(".gitlet"));
    }

    @Test
    public void testReset() throws IOException, ClassNotFoundException {

    }

    @Test
    public void testMerge() throws IOException, ClassNotFoundException {

    }

    @Test
    public void test14() throws IOException, ClassNotFoundException {
        Main.main("init");
        Main.main("add", "f.txt");
        Main.main("add", "g.txt");
        Main.main("rm", "f.txt");
        Main.main("status");
        deleteDir(new File(".gitlet"));

    }

    @Test
    public void test15() throws IOException, ClassNotFoundException {
        Main.main("init");
        Main.main("add", "f.txt");
        Main.main("add", "g.txt");
        Main.main("commit", "Two files");
        Main.main("rm", "f.txt");
        Main.main("add", "f.txt");
        Main.main("status");
        deleteDir(new File(".gitlet"));
    }

    @Test
    public void test23() throws IOException, ClassNotFoundException {
        Main.main("init");
        Main.main("add", "f.txt");
        Main.main("add", "g.txt");
        Main.main("commit", "Two files");
        Main.main("add", "h.txt");
        Main.main("commit", "Add h");
        Main.main("log");
        Main.main("global-log");
        deleteDir(new File(".gitlet"));
    }

    @Test
    public void test29() throws IOException, ClassNotFoundException {
        Main.main("init");
        Main.main("add", "wug.txt");
        Main.main("commit", "version 1");
        Main.main("add", "wug.txt");
        Main.main("commit", "version 2");
        Main.main("log");
        Main.main("checkout", "123456", "--", "wug.txt");
        Main.main("checkout", "foobar");
        Main.main("checkout", "master");
        deleteDir(new File(".gitlet"));
    }

    @Test
    public void test30() throws IOException, ClassNotFoundException {
        Main.main("init");
        Main.main("branch", "other");
        Main.main("add", "g.txt");
        Main.main("add", "f.txt");
        Main.main("commit", "Main two files");
        Main.main("checkout", "other");
        Main.main("add", "f.txt");
        Main.main("commit", "Alternative file");
        deleteDir(new File(".gitlet"));
    }

    @Test
    public void test33() throws IOException, ClassNotFoundException {
        Main.main("init");
        Main.main("add", "g.txt");
        Main.main("add", "f.txt");
        Main.main("commit", "Two files");
        Main.main("branch", "other");
        Main.main("add", "h.txt");
        Main.main("rm", "g.txt");
        Main.main("commit", "Add h.txt and remove g.txt");
        Main.main("checkout", "other");
        Main.main("rm", "f.txt");
        Main.main("add", "k.txt");
        Main.main("commit", "Add k.txt and remove f.txt");
        Main.main("checkout", "master");
        Main.main("merge", "other");
        Main.main("log");
        Main.main("status");
        deleteDir(new File(".gitlet"));
    }

    /**
    @Test
    public void test34() throws IOException, ClassNotFoundException {
        Main.main("init");
        Utils.writeContents(new File("f.txt"),
    Utils.readContents(new File("testing/src/wug.txt")));
        Utils.writeContents(new File("g.txt"),
    Utils.readContents(new File("testing/src/notwug.txt")));
        Main.main("add","g.txt");
        Main.main("add","f.txt");
        Main.main("commit", "Two files");
        Main.main("branch", "other");
        Utils.writeContents(new File("h.txt"),
    Utils.readContents(new File("testing/src/wug2.txt")));
        Main.main("add","h.txt");
        Main.main("rm","g.txt");
        Main.main("commit", "Add h.txt and remove g.txt");
        Main.main("checkout","other");
        Utils.writeContents(new File("f.txt"),
    Utils.readContents(new File("testing/src/notwug.txt")));
        Main.main("add","f.txt");
        Utils.writeContents(new File("k.txt"),
    Utils.readContents(new File("testing/src/wug3.txt")));
        Main.main("add","k.txt");
        Main.main("commit", "Add k.txt and remove f.txt");
        Main.main("checkout","master");
        Main.main("log");
        Main.main("merge","other");
        Main.main("log");
        Main.main("status");
        assertEquals(Utils.readContentsAsString(new File("f.txt")),
                Utils.readContentsAsString
    (new File("testing/src/conflict1.txt")));
        deleteDir(new File(".gitlet"));
    }
    */

/**
//    public void test36() throws IOException, ClassNotFoundException {
//        Main.main("init");
//        Utils.writeContents(new File("f.txt"),
 Utils.readContents(new File("testing/src/wug.txt")));
//        Utils.writeContents(new File("g.txt"),
 Utils.readContents(new File("testing/src/notwug.txt")));
//        Main.main("add","g.txt");
//        Main.main("add","f.txt");
//        Main.main("commit", "Two files");
//        Main.main("branch", "other");
//        Utils.writeContents(new File("h.txt"),
 Utils.readContents(new File("testing/src/wug2.txt")));
//        Main.main("add","h.txt");
//        Main.main("rm","g.txt");
//        Main.main("commit", "Add h.txt and remove g.txt");
//        Main.main("checkout","other");
//        Main.main("merge","other");
//        Main.main("rm","f.txt");
//        Utils.writeContents(new File("k.txt"),
 Utils.readContents(new File("testing/src/wug3.txt")));
//        Main.main("add","k.txt");
//        Main.main("commit", "Add k.txt and remove f.txt");
//        Main.main("checkout","master");
//        Main.main("merge","foobar");
//        Utils.writeContents(new File("k.txt"),
 Utils.readContents(new File("testing/src/wug.txt")));
//        Main.main("merge","other");
//        new File("k.txt").delete();
//        Utils.writeContents(new File("k.txt"),
 Utils.readContents(new File("testing/src/wug.txt")));
//        Main.main("add","k.txt");
//        Main.main("merge","other");
//        Main.main("rm","k.txt");
//        Utils.writeContents(new File("f.txt"),
 Utils.readContents(new File("testing/src/notwug.txt")));
//        Main.main("add","f.txt");
//        Main.main("log");
//        Main.main("log");
//        Main.main("status");
//        deleteDir(new File(".gitlet"));
//    }
//    @Test
//    public void test36() throws IOException, ClassNotFoundException {
//        Main.main("init");
//        Main.main("branch", "B1");
//        Main.main("branch", "B2");
//        Main.main("checkout","B1");
//        Utils.writeContents(new File("h.txt"),
 Utils.readContents(new File("testing/src/wug.txt")));
//        Main.main("add","h.txt");
//        Main.main("commit", "Add h.txt");
//        Main.main("checkout","B2");
//        Utils.writeContents(new File("f.txt"),
 Utils.readContents(new File("testing/src/wug.txt")));
//        Main.main("add","f.txt");
//        Main.main("commit", "Add f.txt");
//        Main.main("branch", "C1");
//        Utils.writeContents(new File("g.txt"),
 Utils.readContents(new File("testing/src/notwug.txt")));
//        Main.main("add","g.txt");
//        Main.main("rm","f.txt");
//        Main.main("commit", "Add g.txt and remove f.txt");
//        Main.main("checkout","B1");
//        Main.main("merge","C1");
//        Main.main("merge","B2");
//        deleteDir(new File(".gitlet"));
//    }
*/

    public static boolean deleteDir(File dir) {
        if (dir.isDirectory()) {
            String[] children = dir.list();
            for (int i = 0; i < children.length; i++) {
                boolean success = deleteDir
                        (new File(dir, children[i]));
                return success;
            }
        }
        return dir.delete();
    }

}


