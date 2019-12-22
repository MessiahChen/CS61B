package gitlet;


import java.io.IOException;

/**
 * Driver class for Gitlet, the tiny stupid version-control system.
 *
 * @author Mingjie Chen
 */
public class Main {

    /**
     * Usage: java gitlet.Main ARGS, where ARGS contains
     * <COMMAND> <OPERAND> ....
     */
    public static void main(String... args)
            throws IOException, ClassNotFoundException {
        Gitlet gitlet = Gitlet.deserialize(SER_DIR);
        if (gitlet == null) {
            gitlet = new Gitlet();
        }
        if (args.length == 0) {
            System.out.println("Please enter a command.");
        } else {
            switch (args[0]) {
            case "init":
                gitlet.init();
                break;
            case "add":
                gitlet.add(args[1]);
                break;
            case "commit":
                if (args.length != 2 || args[1].equals("")) {
                    System.out.println("Please enter a commit message.");
                } else {
                    gitlet.commit(args[1]);
                }
                break;
            case "rm":
                gitlet.remove(args[1]);
                break;
            case "log":
                gitlet.log();
                break;
            case "global-log":
                gitlet.globalLog();
                break;
            case "find":
                gitlet.find(args[1]);
                break;
            case "status":
                gitlet.status();
                break;
            case "checkout":
                gitlet.checkout(args);
                break;
            case "branch":
                gitlet.branch(args);
                break;
            case "rm-branch":
                gitlet.rmBranch(args[1]);
                break;
            case "reset":
                gitlet.reset(args[1]);
                break;
            case "merge":
                gitlet.merge(args[1]);
                break;
            default:
                System.out.println("No command with that name exists.");
                break;
            }
        }
        Gitlet.serialize(gitlet, SER_DIR);
    }

    /**
     * Serialization directory.
     */
    private static final String SER_DIR = ".gitlet/gitlet.ser";

}
