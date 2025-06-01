package gitlet;

import java.io.File;

import static gitlet.Utils.*;
import static gitlet.Repository.*;

public class Refs {
	
	/** The current working directory. */
    public static final File CWD = new File(System.getProperty("user.dir"));
    /** The .gitlet directory. */
    public static final File GITLET_DIR = join(CWD, ".gitlet");
    // store commits and blobs.
    public static final File OBJECT_DIR = join(GITLET_DIR, "objects");
    public static final File COMMIT_DIR = join(OBJECT_DIR, "commits");
    public static final File BLOBS_DIR = join(OBJECT_DIR, "blobs");

    // store references.
    public static final File REFS_DIR = join(GITLET_DIR, "refs");
    public static final File HEAD_DIR = join(REFS_DIR, "heads");

    // the current .gitlet's head pointer.
    public static final File HEAD_POINT = join(HEAD_DIR, "HEAD");

    // the stage directory.
    public static final File ADD_STAGE_DIR = join(GITLET_DIR, "addstage");
    public static final File REMOVE_STAGE_DIR = join(GITLET_DIR, "removestage");

    // saveBranch method saves the files of the head of the given branch.
    public static void saveBranch(String branchName, String hashName) {
        File branchHeadToSave = join(HEAD_DIR, branchName);
        writeContents(branchHeadToSave, hashName);
    }

    // saveHEAD method writes the hash value of the current branch head.
    public static void saveHEAD(String branchName, String commitHashName) {
        writeContents(HEAD_POINT, branchName + ":" + commitHashName);
    }

    // Return the branch name of the current head.
    public static String getHeadBranchName() {
        String helper = readContentsAsString(HEAD_POINT);
        String helperSplit[] = helper.split(":");
        String result = helperSplit[0];
        return result;
    }
}
