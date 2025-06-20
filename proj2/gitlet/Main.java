package gitlet;

import static gitlet.Repository.*;
import static gitlet.Utils.message;
import static java.lang.System.exit;

/** Driver class for Gitlet, a subset of the Git version-control system.
 *  @author TODO
 */
public class Main {

    /** Usage: java gitlet.Main ARGS, where ARGS contains
     *  <COMMAND> <OPERAND1> <OPERAND2> ... 
     */
    public static void main(String[] args) {
        // TODO: what if args is empty?
        checkEmptyArg(args);
        String firstArg = args[0];
        switch(firstArg) {
            case "init":
                // TODO: handle the `init` command
                initPersistance();
                break;
            case "add":
                // TODO: handle the `add [filename]` command
                String addFileName = args[1];
                addStage(addFileName);
                break;
            // TODO: FILL THE REST IN
            case "commit":
                String msg = args[1];
                commitFile(msg);
                break;
            case "rm":
                String fileName = args[1];
                rmFile(fileName);
                break;
            case "log":
                printLog(args);
                break;
            case "checkout":
                checkOut(args);
                break;
            case "find":
                String commitMsg = args[1];
                findCommit(commitMsg);
                break;
            case "status":
                showStatus();
                break;
            case "rm-branch":
                removeBranch(args[1]);
                break;
        }
    }
}
