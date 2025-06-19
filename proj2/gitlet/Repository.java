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
            System.out.println("There is an untracked file in the way; delete it, or add and commit it first.");
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

    public static void printStatusPerField(String field, Collection<String> files,
                                           String branchName) {
        System.out.println("=== " + field + " ===");
        if (field.equals("Branches")) {
            for (var file : files) {
                // 如果是head文件
                if (file.equals(branchName)) {
                    System.out.println("*" + file);
                } else {
                    System.out.println(file);
                }
            }
        } else {
            for (var file : files) {
                System.out.println(file);
            }
        }

        System.out.print("\n");
    }

    public static void printStatusWithStatus(String field, Collection<String> modifiedFiles,
                                             Collection<String> deletedFiles) {
        System.out.println("=== " + field + " ===");

        for (var file : modifiedFiles) {
            System.out.println(file + " " + "(modified)");
        }
        for (var file : deletedFiles) {
            System.out.println(file + " " + "(deleted)");
        }

        System.out.print("\n");
    }

    //--------------------------------function methods below--------------------------------//
    // init methods
    public static void initPersistance() {
        if (GITLET_DIR.exists()) {
            System.out.println("A Gitlet version-control "
                    + "system already exists in the current directory.");
            exit(0);
        }
        setUpPersistance();

        Date initialTimeStamp = new Date(0);
        Commit initialCommit = new Commit("initial commit", initialTimeStamp, "", null, null);
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
            String blobHashName = sha1(fileContent);
            Blob blobAdded = new Blob(fileContent, blobHashName);
            blobAdded.saveBlob();

            File blobPoint = join(ADD_STAGE_DIR, addFileName);
            writeContents(blobPoint, blobHashName);
        }

        // senario 4 and 5.
        if (headCommitBlopMap.containsKey(addFileName)) {
            String commitFileAddedInHash = headCommitBlopMap.get(addFileName);
            String commitContent = getBlobContentFromName(commitFileAddedInHash);

            if (!commitContent.equals(fileContent)) {
                String blobHashName = sha1(fileContent);
                Blob blobAdded = new Blob(fileContent, blobHashName);
                blobAdded.saveBlob();

                File blobPoint = join(ADD_STAGE_DIR, addFileName);
                writeContents(blobPoint, blobHashName);
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
        if (args.length == 2) {
            //  git checkout branchName
            checkOutBranch(args[1]);
        } else if (args.length == 4) {
            //  git checkout [commit id] -- [file name]
            if (!args[2].equals("--")) {
                message("Incorrect operands.");
                exit(0);
            }
            /* 获取到Blob对象 */
            fileName = args[3];
            String commitId = args[1];
            Commit commit = getHeadCommit();

            /* 是否可以进行对objects文件夹的重构，实现hashMap结构
                使得时间效率上不是线性, 而不是依靠链表查找？ */
            if (getCommitFromId(commitId) == null) {
                System.out.println("No commit with that id exists.");
                exit(0);
            } else {
                commit = getCommitFromId(commitId);
            }

            if (!commit.getBlobMap().containsKey(fileName)) {
                System.out.println("File does not exist in that commit.");
                exit(0);
            }
            String blobName = commit.getBlobMap().get(fileName);
            String targetBlobContent = getBlobContentFromName(blobName);

            /* 将Blob对象中的内容覆盖working directory中的内容 */
            File fileInWorkDir = join(CWD, fileName);
            overWriteFileWithBlob(fileInWorkDir, targetBlobContent);

        } else if (args.length == 3) {
            //  git checkout -- [file name]
            /* 获取到Blob对象中的内容 */
            fileName = args[2];
            Commit headCommit = getHeadCommit();
            if (!headCommit.getBlobMap().containsKey(fileName)) {
                System.out.println("File does not exist in that commit.");
                exit(0);
            }
            String blobName = headCommit.getBlobMap().get(fileName);
            String targetBlobContent = getBlobContentFromName(blobName);

            /* 将Blob对象中的内容覆盖working directory中的内容 */
            File fileInWorkDir = join(CWD, fileName);
            overWriteFileWithBlob(fileInWorkDir, targetBlobContent);

        }
    }

    // find methods.
    public static void findCommit(String commitMsg) {
        boolean isfind = false;
        List<String> commitList = plainFilenamesIn(COMMIT_DIR);
        for (String commitId : commitList) {
            Commit commit = getCommitFromId(commitId);
            if (commit.getMessage().equals(commitMsg)) {
                System.out.println(commitId);
                isfind = true;
            }
        }

        if (!isfind) {
            System.out.println("Found no commit with that message.");
        }
    }

    // status methods. finished branch, addstage, removestage.
    public static void showStatus() {
        File gitletFile = join(CWD, ".gitlet");
        if (!gitletFile.exists()) {
            message("Not in an initialized Gitlet directory.");
            exit(0);
        }
        /* 获取当前分支名 */
        Commit headCommit = getHeadCommit();
        String branchName = getHeadBranchName();

        List<String> filesInHead = plainFilenamesIn(HEAD_DIR);
        List<String> filesInAdd = plainFilenamesIn(ADD_STAGE_DIR);
        List<String> filesInRm = plainFilenamesIn(REMOVE_STAGE_DIR);
        HashMap<String, String> blobMap = headCommit.getBlobMap();
        Set<String> trackFileSet = blobMap.keySet();  // commit中跟踪着的文件名
        LinkedList<String> modifiedFilesList = new LinkedList<>();
        LinkedList<String> deletedFilesList = new LinkedList<>();
        LinkedList<String> untrackFilesList = new LinkedList<>();

        printStatusPerField("Branches", filesInHead, branchName);
        printStatusPerField("Staged Files", filesInAdd, branchName);
        printStatusPerField("Removed Files", filesInRm, branchName);

        /* 开始进行：Modifications Not Staged For Commit */
        /* 暂存已经添加，但内容与工作目录中的内容不同 */
        for (String fileAdd : filesInAdd) {
            /* 如果文件在暂存区存在，但是在工作区不存在，则直接加入modifiedFilesList */
            if (!join(CWD, fileAdd).exists()) {
                deletedFilesList.add(fileAdd);
                continue;
            }
            String workFileContent = readContentsAsString(join(CWD, fileAdd));
            String addStageBlobName = readContentsAsString(join(ADD_STAGE_DIR, fileAdd));
            String addStageFileContent = readContentsAsString(join(BLOBS_DIR, addStageBlobName));
            if (!workFileContent.equals(addStageFileContent)) {
                // 当工作区和addStage中文件内容不一致，则进入modifiedFilesList
                modifiedFilesList.add(fileAdd);
            }
        }

        /* 在当前commit中跟踪，在工作目录中更改，但未暂存 */
        for (String trackFile : trackFileSet) {
            if (trackFile.isEmpty() || trackFile == null) {
                continue;
            }
            File workFile = join(CWD, trackFile);
            File fileInRmStage = join(REMOVE_STAGE_DIR, trackFile);
            if (!workFile.exists()) {      // 当工作区文件直接不存在的情况
                if (!fileInRmStage.exists()) {
                    deletedFilesList.add(trackFile);       // 在rmStage中无此文件，同时工作区也没有这个文件
                }
                continue;
            }
            if (!filesInAdd.contains(trackFile)) { // 当addStage中没有此文件
                String workFileContent = readContentsAsString(workFile);
                String blobFileContent = readContentsAsString(join(BLOBS_DIR,
                        blobMap.get(trackFile)));
                if (!workFileContent.equals(blobFileContent)) {
                    // 当正在track的文件被修改，但addStage中无此文件，则进入modifiedFilesList
                    modifiedFilesList.add(trackFile);
                }
            }
        }
        printStatusWithStatus("Modifications Not Staged For Commit",
                modifiedFilesList, deletedFilesList);
        /* 开始进行：Untracked Files */
        List<String> workFiles = plainFilenamesIn(CWD);
        for (String workFile : workFiles) {
            if (!filesInAdd.contains(workFile)
                    && !filesInRm.contains(workFile)
                    && !trackFileSet.contains(workFile)) {
                untrackFilesList.add(workFile);
                continue;
            }
            if (filesInRm.contains(workFile)) {
                untrackFilesList.add(workFile);
            }
        }
        printStatusPerField("Untracked Files", untrackFilesList, branchName);
    }

    // branch method.
    public static void createBranch(String branchName) {
        List<String> branchList = plainFilenamesIn(HEAD_DIR);
        for (String branch : branchList) {
            if (branch.equals(branchName)) {
                System.out.println("A branch with that name already exists.");
                exit(0);
            }
        }

        Commit headCommit = getHeadCommit();
        String commitId = headCommit.getHashName();
        saveBranch(branchName, commitId);
    }

    // rm-branch method.
    public static void removeBranch(String branchName) {
        File branchFile = join(HEAD_DIR, branchName);
        if (!branchFile.exists()) {
            System.out.println("A branch with that name does not exist.");
            exit(0);
        }

        Commit headCommit = getHeadCommit();
        if (getHeadBranchName().equals(branchName)) {
            System.out.println("Cannot remove the current branch.");
            exit(0);
        }

        File branchDelete = join(HEAD_DIR, branchName);
        branchDelete.delete();
    }   
}
