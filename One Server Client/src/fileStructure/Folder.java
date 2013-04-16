package fileStructure;

import java.util.ArrayList;
import java.util.List;

import org.teleal.cling.support.model.DIDLContent;
import org.teleal.cling.support.model.Res;
import org.teleal.cling.support.model.container.Container;
import org.teleal.cling.support.model.item.Item;

/**
 * This class represents a folder object which will lead to a directory which may contain more media objects.
 * @author kuszewskij
 *
 */
public class Folder extends MediaObject {

	/**
	 * This is the number of files in the folder.
	 */
	public int fileCount = 0;

	/**
	 * The string name of the directory this folder links to.
	 */
	public String directory;

	/**
	 * True if this folder should use the up one icon instead of the folder icon.
	 */
	public boolean useUpOneIcon = false;

	/**
	 * This is the list of items that are contained within this storage folder.
	 */
	private ArrayList<MediaObject> mediaObjects;

	/**
	 * basic constructor for this class. Only adds the meta data for the base media object class.
	 * @param Id The unique identifier of this object
	 * @param parentId The unique identifier of this objects parent.
	 * @param creator The creator of this PlayList.
	 * @param title The title of this PlayList.
	 * @param resources The resources used to generate this PlayList.
	 */
	public Folder(String Id, String parentId, String creator,
			String title, List<Res> resources) {
		super("object.container.folder", Id, parentId, creator, title, resources,MediaObject.mediaType.StorageFolder);
		setMediaObjects(new ArrayList<MediaObject>());
	}

	/**
	 * Detailed constructor for this class. Adds the meta data types for the folder class.
	 * @param classType The upnp class type.
	 * @param Id The unique identifier of this object
	 * @param parentId The unique identifier of this objects parent.
	 * @param creator The creator of this PlayList.
	 * @param title The title of this PlayList.
	 * @param resources The resources used to generate this PlayList.
	 * @param storageUsed The storage used by this storage folder.
	 */
	@SuppressWarnings("serial")
	public Folder(String Id, String parentId, String creator,
			String title, List<Res> resources, final String storageUsed) {
		super("object.container.folder", Id, parentId, creator, title, resources,MediaObject.mediaType.StorageFolder);
		setMediaObjects(new ArrayList<MediaObject>());
		super.getMetaData().put(MetaDataTypes.FOLDERITEM_STORAGE_USED, new ArrayList<String>(){{add(storageUsed);}});
	}

	/**
	 * Created a Folder from a cling StorageFolder. This folder will contain all the objects contained within it as well
	 * @param sf
	 */
	public Folder(Container sf,DIDLContent children){
		super(sf.getClazz().getValue(), sf.getId(), sf.getParentID(), sf.getCreator(), sf.getTitle(), sf.getResources(),MediaObject.mediaType.StorageFolder);
		setMediaObjects(new ArrayList<MediaObject>());
		
		for(Item i: children.getItems()){
			mediaObjects.add(MediaObject.createMediaObject(i));
		}
		
		for(Container c: children.getContainers()){
			Folder f = new Folder(c.getId(),c.getParentID(),c.getCreator(),c.getTitle(),c.getResources());
			mediaObjects.add(f);
		}

	}

	/**
	 * Returns the storage used of this storage folder.
	 * @return The storage used of this storage folder.
	 */
	public String getStorageUsed(){
		return this.getValueOfMetaDataType(MetaDataTypes.FOLDERITEM_STORAGE_USED);
	}

	/**
	 * Sets the storage used property of this storage folder.
	 * @param storageUsed The storage used property of this storage folder.
	 */
	@SuppressWarnings("serial")
	public void setStorageUsed(final String storageUsed){
		this.getMetaData().put(MetaDataTypes.FOLDERITEM_STORAGE_USED, new ArrayList<String>(){{add(storageUsed);}});
	}

	public ArrayList<MediaObject> getMediaObjects() {
		return mediaObjects;
	}

	public void setMediaObjects(ArrayList<MediaObject> mediaObjects) {
		this.mediaObjects = mediaObjects;
	}
}
