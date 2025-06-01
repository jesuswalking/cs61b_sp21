package gitlet;

import java.io.File;
import java.io.Serializable;

import static gitlet.Refs.*;
import static gitlet.Repository.*;
import static gitlet.Utils.*;

public class Blob implements Serializable {
	private String content;
	private File filePath;  // Blob 文件本身所在的路径
	private String hashName;

	public Blob(String content, String hashName) {
		this.content = content;
		this.hashName = hashName;
		this.filePath = join(BLOBS_DIR, hashName);
	}

	public File getFilePath() {
		return this.filePath;
	}

	// 将blob对象保存进BLOB_FOLDER文件,内容就是blob文件的content
	public void saveBlob() {
		if (!this.filePath.exists()) writeContents(BLOBS_DIR, this.content);
	}

	// Return the content of Blob, return null if there is no content.
	public static String getBlobContentFromName(String blobName) {
		String returnContent = null;
		File blobFile = join(BLOBS_DIR, blobName);
		if (blobFile.isFile() && blobFile.exists()) returnContent = readContentsAsString(blobFile);
		return returnContent;
	}

	// Replicate blob.content into the certain file.
	public static void overWriteFileWithBlob(File file, String content) {
		writeContents(file, content);
	}
}
