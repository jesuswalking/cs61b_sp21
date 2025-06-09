package gitlet;

// TODO: any imports you need here
import java.io.File;
import java.io.Serializable;
import java.util.Date; // TODO: You'll likely use this in this class
import java.util.HashMap;

import static gitlet.Refs.*;
import static gitlet.Utils.*;
import static gitlet.Repository.*;

/** Represents a gitlet commit object.
 *  TODO: It's a good idea to give a description here of what else this Class
 *  does at a high level.
 *
 *  @author TODO
 */
public class Commit implements Serializable{
    /**
     * TODO: add instance variables here.
     *
     * List all instance variables of the Commit class here with a useful
     * comment above them describing what that variable represents and how that
     * variable is used. We've provided one example for `message`.
     */

    /** The message of this Commit. */
    private String message;
    private Date timeStamp;
    private String directParent;
    private String otherParent;
    private HashMap<String, String> blobMap = new HashMap<>();

    /* TODO: fill in the rest of this class. */

    public Commit(String message, Date timeStamp, String directParent, String blobFileName, String blobHashName) {
        this.message = message;
        this.timeStamp = timeStamp;
        this.directParent = directParent;
        if (blobFileName == null || blobFileName.isEmpty()) this.blobMap = new HashMap<>();
        else this.blobMap.put(blobFileName, blobHashName);
    }

    public Commit(Commit commit) {
        this.directParent = commit.directParent;
        this.blobMap = commit.blobMap;
    }

    public String getMessage() {
        return this.message;
    }

    public Date getTimeStamp() {
        return this.timeStamp;
    }
    
    public String getDirectParent() {
        return this.directParent;
    }

    public HashMap<String, String> getBlobMap() {
        return this.blobMap;
    }

    public void saveCommit() {
        String commitID = this.getHashName();

        File commitFile = join(COMMIT_DIR, commitID);
        writeObject(commitFile, this);
    }

    public String getHashName() {
        return sha1(this.message, dateToTimeStamp(this.timeStamp), this.directParent); // sha1 only takes one reference type.
    }

    public void setMessage(String msg) {
        this.message = msg;
    }

    public void setDirectParent(String hashName) {
        this.directParent = hashName;
    }

    public void setDateStamp(Date date) {
        this.timeStamp = date;
    }

    public void addBlob(String fileName, String hashName) {
        HashMap<String, String> blopMap = this.blobMap;
        blopMap.put(fileName, hashName);
    }

    public void removeBlob(String fileName) {
        HashMap<String, String> blopMap = this.blobMap;
        blopMap.remove(fileName);
    }
}
