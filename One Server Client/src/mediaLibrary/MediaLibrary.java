package mediaLibrary;
import java.util.TreeMap;

import android.graphics.Bitmap;

import network.ContentDirectoryCallback;
import network.NetworkManager;

import fileStructure.Directory;
import fileStructure.MediaObject;

/**
 * This class will handle the clients access to the media library. For now just add methods needed get the list view file structure working.
 * @author kuszewskij
 *
 *@version 2 Added stubs for methods needed to navigate through a file structure.
 */
public class MediaLibrary implements ContentDirectoryCallback{

	/**
	 * The one instance of the media library
	 */
	private static MediaLibrary mediaLib;

	/**
	 * This will be the search types that are supported for searching through the server.
	 * @author kuszewskij
	 *
	 */
	public enum searchTypes{
		//TODO: add search types.
	};

	//TODO: For now this will just hold a set of mock directories to test with. Eventually each directory will be retrieved from the media library
	private TreeMap<String, Directory> tempData = new TreeMap<String, Directory>(); 

	/**
	 * Private Constructor.
	 */
	private MediaLibrary(){
		mediaLib = this;
	}

	/**
	 * Singleton constructor for the media Library. 
	 * @return
	 */
	public static MediaLibrary getMediaLibrary(){
		if(mediaLib == null){
			mediaLib = new MediaLibrary();
		}

		return mediaLib;
	}

	/**
	 * Calling this method will return the requested directory.
	 * @param name The name of the directory to be returned
	 * @return The directory with the given name.
	 */
	public void getDirectory(String name, ContentDirectoryCallback c){
		//TODO: Caching will be something like this?
		if(tempData.get(name)==null){
			try {
				NetworkManager.getNetworkManager().getContentDirectoryService().getMediaObjectFromServer(name, c);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}else{
			c.directoryRecieved(tempData.get(name));
		}
	}

	/**
	 * checks to see if a directory is cached or not.
	 * TODO: Implement. Also, should this be done here or in the Directory class.
	 * @return
	 */
	public boolean isDirectoryCached(Directory d){
		return false;
	}

	/**
	 * checks to see if a mediaObject is cached or not.
	 * TODO: Implement
	 * @return
	 */
	public boolean isMediaObjectCached(MediaObject mo){
		return false;
	}

	/**
	 * This method will be responsible for creating and returning a directory of media objects fitting the search criteria given on the media server.
	 * @param type The type of search
	 * @param query The query information.
	 * @return A directory containing all of the media objects fitting the search criteria.
	 */
	public Directory search(searchTypes type, String query){
		
		
		return null;
	}

	@Override
	public void mediaObjectRecieved(MediaObject object) {
		// TODO Auto-generated method stub

	}

	@Override
	public void directoryRecieved(Directory d) {
		// TODO Auto-generated method stub

	}
}
