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

    public static void printCommitLog(Commit commit) {
        System.out.println("===");
        System.out.println("commit " + commit.getHashName());
        System.out.println("Date: " + dateToTimeStamp(commit.getTimeStamp()));
        System.out.println(commit.getMessage());
        System.out.print("\n");
    }

    // Return the parent commit.
    public static Commit getCommit(String directParent) {
        List<String> commitFiles = plainFilenamesIn(COMMIT_DIR);
        if (!commitFiles.contains(directParent)) {
            return null;
        }

        File commitFile = join(COMMIT_DIR, directParent);
        Commit commit = readObject(commitFile, Commit.class);
        return commit;
    }

    // Return the commit with the given commitId.
    public static Commit getCommitFromId(String commitId) {
        List<String> commitList = plainFilenamesIn(COMMIT_DIR);
        if (!commitList.contains(commitId)) {
            return null;
        }

        File commitFile = join(COMMIT_DIR, commitId);
        Commit commit = readObject(commitFile, Commit.class);
        return commit;
    }

    public static boolean unTrackedFilesExists(Commit commit) {
        List<String> CWDList = plainFilenamesIn(CWD);
        Set<String> trackedFiles = commit.getBlobMap().keySet();

        for (String fileName : CWDList) {
            if (!trackedFiles.contains(fileName)) return true;
        }
        return false;
    }

    public static void checkOutBranch(String branchName) {
        Commit headCommit = getHeadCommit();

        if (branchName.equals(getHeadBranchName())) {
            System.out.println("No need to checkout the current branch.");
            exit(0);
        }

        // get the branchheadCommit
        Commit branchHeadCommit = getBranchHeadCommit(branchName);
        HashMap<String, String> branchHeadCommitBlobMap = branchHeadCommit.getBlobMap();
        Set<String> fileNameSet = branchHeadCommitBlobMap.keySet();

        List<String> CWDfiles = plainFilenamesIn(CWD);

        if (unTrackedFilesExists(headCommit)) {
            System.out.println("");
            exit(0);
        }

        // qing kong CWD zhong dui ying de wenjian.
        for (String workFile : CWDfiles) {
            restrictedDelete(join(CWD, workFile));
        }

        // chong xin zai CWD li xie ru wen jian.
        for (String fileName : fileNameSet) {
            String hashName = branchHeadCommitBlobMap.get(fileName);
            String content = getBlobContentFromName(hashName);
            File newFileCWD = join(CWD, fileName);
            writeContents(newFileCWD, content);
        }

        // saveHEAD dao dang qian de branch.
        saveHEAD(branchName, branchHeadCommit.getHashName());
    }

    // Return the branchheadCommit.
    public static Commit getBranchHeadCommit(String branchName) {
        List<String> branchList = plainFilenamesIn(HEAD_DIR);

        if (!branchList.contains(branchName)) {
            System.out.println("No such branch exists.");
            exit(0);
        }

        File branchFile = join(HEAD_DIR, branchName);
        String commitHashId = readContentsAsString(branchFile);

        File commitFile = join(COMMIT_DIR, commitHashId);
        Commit commit = readObject(commitFile, Commit.class);
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

    public static void rmFile(String fileName) throws GitletException {
        if (fileName == null || fileName.isEmpty()) {
            throw new GitletException("Please enter a file name.");
        }

        Commit headCommit = getHeadCommit();
        HashMap<String, String> blobMap = headCommit.getBlobMap();
        List<String> addStageFiles = plainFilenamesIn(ADD_STAGE_DIR);

        if (!blobMap.containsKey(fileName)) {
            if (!addStageFiles.contains(fileName)) {
                throw new GitletException("No reason to remove the file.");
            }
        }

        File addStageFile = join(ADD_STAGE_DIR, fileName);
        if (addStageFile.exists()) {
            addStageFile.delete();
        }

        // if the file is tracked, move it into removeDIR, then delete the file in the working DIR.
        if (blobMap.containsKey(fileName)) {
            File removeFile = join(REMOVE_STAGE_DIR, fileName);
            writeContents(removeFile, "");

            File removeFileCWD = join(CWD, fileName);
            restrictedDelete(removeFileCWD);
        }
    }

    // log method.
    // use commit.directParent instance variable.
    public static void printLog(String args[]) throws GitletException {
        if (args.length != 1) throw new GitletException("Wrong operands.");

        Commit headCommit = getHeadCommit();
        Commit commit = headCommit;

        while (!commit.getDirectParent().equals("")) {
            printCommitLog(commit);
            commit = getCommit(commit.getDirectParent());
        }

        printCommitLog(commit);
    }   

    // global-log method.
    public static void printLogGlobal(String args[]) throws GitletException {
        if (args.length != 1) throw new GitletException("Wrong operands");

        List<String> commitFiles = plainFilenamesIn(COMMIT_DIR);
        for (String commitFile : commitFiles) {
            Commit commit = getCommit(commitFile);
            printCommitLog(commit);
        }   
    }

    //checkout method.
    public static void checkOut(String[] args) {
        String fileName;
        // gitlet.Main checkout [branchName].
        if (args.length == 2) {
            checkOutBranch(args[1]);
        } else if (args.length == 4) {
        // gitlet.Main checkout [commitid] -- [filename].
            if (!args[2].equals("--")) {
                System.out.println("Incorrect operands.");
                exit(0);
            }

            fileName = args[3];
            String commitId = args[1];
            Commit commit = getHeadCommit();

            if (getCommitFromId(commitId) == null) {
                System.out.println("No commit with that Id exists.");
                exit(0);
            } else {
                commit = getCommitFromId(commitId);
            }

            if (!commit.getBlobMap().containsKey(fileName)) {
                System.out.println("File dose not exist in that commit.");
                exit(0);
            } 

            String blobName = commit.getBlobMap().get(fileName);
            String targetBlobContent = getBlobContentFromName(blobName);

            File fileInCWD = join(CWD, fileName);
            overWriteFileWithBlob(fileInCWD, targetBlobContent);
        } else if (args.length == 3) {
            // checkout -- [file name].
            Commit commit = getHeadCommit();
            fileName = args[2];

            if (!commit.getBlobMap().containsKey(fileName)) {
                System.out.println("File does not exist in that commit.");
                exit(0);
            }

            String blobName = commit.getBlobMap().get(fileName);
            String targetBlobContent = getBlobContentFromName(blobName);

            File fileInCWD = join(CWD, fileName);
            writeContents(fileInCWD, targetBlobContent);
        }    
    }
}
