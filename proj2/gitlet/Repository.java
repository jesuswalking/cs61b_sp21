package gitlet;

import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

import static gitlet.Commit.*;
import static gitlet.Refs.*;
import static gitlet.Utils.*;
import static gitlet.Blob.*;
import static java.lang.System.exit;

// TODO: any imports you need here

/** Represents a gitlet repository.
 *  TODO: It's a good idea to give a description here of what else this Class
 *  does at a high level.
 *
 *  @author TODO
 */
public class Repository {
    /**
     * TODO: add instance variables here.
     *
     * List all instance variables of the Repository class here with a useful
     * comment above them describing what that variable represents and how that
     * variable is used. We've provided two examples for you.
     */
    /* TODO: fill in the rest of this class. */
    public static void checkEmptyArg(String args[]) {
        if (args.length == 0) {
            System.out.println("Please enter a command.");
            exit(0);
        }
    }

    public static void setUpPersistance() {
        GITLET_DIR.mkdir();
        OBJECT_DIR.mkdir();
        COMMIT_DIR.mkdir();
        BLOBS_DIR.mkdir();
        REFS_DIR.mkdir();
        HEAD_DIR.mkdir();
        ADD_STAGE_DIR.mkdir();
        REMOVE_STAGE_DIR.mkdir();
    }

    public static String dateToTimeStamp(Date timeStamp) {
        DateFormat dateFormat = new SimpleDateFormat("EEE MMM d HH:mm:ss yyyy Z", Locale.US);
        return dateFormat.format(timeStamp);
    }

    // Return the headCommit.
    public static Commit getHeadCommit() {
        String headContent = readContentsAsString(HEAD_POINT);
        String headHashName = headContent.split(":")[1];

        File headCommit = join(COMMIT_DIR, headHashName);
        Commit commit = readObject(headCommit, Commit.class);
        return commit;
    }

    //--------------------------------function methods below--------------------------------//
    // init methods
    public static void initPersistance() throws GitletException {
        if (GITLET_DIR.exists()) {
            throw new GitletException("A Gitlet version-control system already exists in the current directory.");
            //exit(0);
        }
        setUpPersistance();

        Date initialTimeStamp = new Date(0);
        Commit initialCommit = new Commit("initial commmit", initialTimeStamp, "", null, null);
        initialCommit.saveCommit();

        String commitHashName = initialCommit.getHashName();
        String brachName = "master";
        saveBranch(brachName, commitHashName);
        saveHEAD(brachName, commitHashName);
    }

    // add methods
    public static void addStage(String addFileName) throws GitletException {
        // if user doesnt input or incorrectly input a filename.
        if (addFileName == null || addFileName.isEmpty()) {
            throw new GitletException("Please enter a file name.");
            //exit(0);
        }

        File fileAdded = join(CWD, addFileName);

        // if the file doesnt exist.
        if (!fileAdded.exists()) {
            throw new GitletException("File does not exist.");
            //exit(0);
        }

        // get the hashmap of the headcommit.
        Commit headCommit = getHeadCommit();
        HashMap<String, String> headCommitBlopMap = headCommit.getBlobMap();

        // content of the file that is about to add.
        String fileContent = readContentsAsString(fileAdded);

        // senario 3.
        if (!headCommitBlopMap.containsKey(addFileName)) {
            //String blobHashName = sha1(fileContent);
            //Blob blobAdded = new Blob(fileContent, blobHashName);
            //blobAdded.saveBlob();

            File blobPoint = join(ADD_STAGE_DIR, addFileName);
            writeContents(blobPoint, fileContent);
        }

        // senario 4 and 5.
        if (headCommitBlopMap.containsKey(addFileName)) {
            String commitFileAddedInHash = headCommitBlopMap.get(addFileName);
            String commitContent = getBlobContentFromName(commitFileAddedInHash);

            if (!commitContent.equals(fileContent)) {
                File blobPoint = join(ADD_STAGE_DIR, addFileName);
                writeContents(blobPoint, fileContent);
            }

            if (commitContent.equals(fileContent)) {
                List<String> fileAddedd = plainFilenamesIn(ADD_STAGE_DIR);
                List<String> fileRemoved = plainFilenamesIn(REMOVE_STAGE_DIR);

                if (fileAddedd.contains(addFileName)) {
                    join(ADD_STAGE_DIR, addFileName).delete();
                }

                if (fileRemoved.contains(addFileName)) {
                    join(REMOVE_STAGE_DIR, addFileName).delete();
                }
            }
        }
    }

    // commit method.
    // get everything in the stages. Then if no files or no msg.
    // init the new commit. Then do stuff in adding stage. then removing stage.
    // then save commit.
    public static void commitFile(String msg) throws GitletException {
        List<String> addStageFiles = plainFilenamesIn(ADD_STAGE_DIR);
        List<String> removeStageFiles = plainFilenamesIn(REMOVE_STAGE_DIR);

        if (addStageFiles.isEmpty() && removeStageFiles.isEmpty()) {
            throw new GitletException("No changes added to the commit.");
        }

        // null means there is no object at all, but isEmpty() means there IS a object, but the content inside is null.
        if (msg == null || msg.isEmpty()) { 
            throw new GitletException("Please enter a commit message.");
        }

        Commit oldCommit = getHeadCommit();
        Commit newCommit = new Commit(oldCommit);
        newCommit.setDirectParent(oldCommit.getHashName());
        newCommit.setDateStamp(new Date(System.currentTimeMillis()));
        newCommit.setMessage(msg);

        for (String stageFileName : addStageFiles) {
            String hashName = readContentsAsString(join(ADD_STAGE_DIR, stageFileName));
            newCommit.addBlob(stageFileName, hashName);
            join(ADD_STAGE_DIR, stageFileName).delete();
        }

        HashMap<String, String> blobMap = newCommit.getBlobMap();

        for (String stageFileName : removeStageFiles) {
            if (blobMap.containsKey(stageFileName)) {
                newCommit.removeBlob(stageFileName);
            }
            join(REMOVE_STAGE_DIR, stageFileName).delete();
        }

        newCommit.saveCommit();

        saveHEAD(getHeadBranchName(), newCommit.getHashName());
        saveBranch(getHeadBranchName(), newCommit.getHashName());
    }
}
