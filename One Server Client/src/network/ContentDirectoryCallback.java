package network;

import fileStructure.Directory;
import fileStructure.MediaObject;

/**
 * This interface is used by classes that wish to request a media object over the network. Since communication in the cling library is done asynchronisly 
 * the UI elements will need a way to get items over the network without blocking the thread. 
 * @author kuszewskij
 *
 */
public interface ContentDirectoryCallback {

	/**
	 * This method will be called by the network manager when the requested media object has been received. 
	 * @param object The media object that was retrieved from the media server.
	 */
	public void mediaObjectRecieved(MediaObject object);
	
	/**
	 * This method will be called by the network manager when a requested directory is recieved.
	 * @param d The recieved directory.
	 */
	public void directoryRecieved(Directory d);
}
