package fileStructure;

import java.util.List;

/**
 * This class will act as a data structure for holding mediaObjects.
 * @author kuszewskij
 *
 */
public class Directory {
	
	/**
	 * This will hold all of the media objects within the directory 
	 */
	private List<MediaObject> media;
	
	/**
	 * This is the name of the directory;
	 */
	private String name;
	
	/**
	 * This is the name of the directory's parent.
	 */
	private String parent;
	
	/**
	 * This will be true if the directory has no parent, false otherwise.
	 */
	private boolean isRoot = true;
	
	/**
	 * Constructor for this class. 
	 * @param name The name of the directory
	 * @param parentDirectory The name of this directories parent.
	 * @param media The media contained within this directory.
	 */
	public Directory(String name, String parentDirectory, List<MediaObject> media){
		this.name = name;
		this.parent = parentDirectory;
		if(parent != null){
			isRoot = false;
		}
		this.media =media;
	}

	/**
	 * 
	 * @return returns the MediaObjects contained within this directory.
	 */
	public List<MediaObject> getMedia() {
		return media;
	}
	
	/**
	 * 
	 * @return Returns the name of this directory.
	 */
	public String getName(){
		return name;
	}
	
	/**
	 * 
	 * @return Returns the name of this directories parent. If it has no parent then it will return null.
	 */
	public String getParentDirectoryName(){
		return parent;
	}

	/**
	 * 
	 * @return Returns true if this directory has no parent, false if it does.
	 */
	public boolean isRoot() {
		return isRoot;
	}

	/**
	 * Sets whether or not this directory is a root directory.
	 * @param isRoot
	 */
	public void setRoot(boolean isRoot) {
		this.isRoot = isRoot;
	}

}
