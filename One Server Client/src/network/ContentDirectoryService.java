package network;

import java.util.ArrayList;
import java.util.Map;

import network.ConnectionService.ConnectionObserver;

import org.teleal.cling.controlpoint.SubscriptionCallback;
import org.teleal.cling.model.action.ActionInvocation;
import org.teleal.cling.model.gena.CancelReason;
import org.teleal.cling.model.gena.GENASubscription;
import org.teleal.cling.model.message.UpnpResponse;
import org.teleal.cling.model.meta.RemoteDevice;
import org.teleal.cling.model.meta.RemoteDeviceIdentity;
import org.teleal.cling.model.meta.RemoteService;
import org.teleal.cling.model.state.StateVariableValue;
import org.teleal.cling.model.types.ServiceId;
import org.teleal.cling.model.types.UDAServiceId;
import org.teleal.cling.support.contentdirectory.callback.Browse;
import org.teleal.cling.support.model.BrowseFlag;
import org.teleal.cling.support.model.DIDLContent;
import org.teleal.cling.support.model.container.Container;
import org.teleal.cling.support.model.item.Item;
import android.util.Log;
import fileStructure.Folder;
import fileStructure.MediaObject;

/**
 * This class is used to access and use the functionality of the content directory service on dlna/upnp compatible media servers.
 * @author kuszewskij
 *
 */
public class ContentDirectoryService implements ConnectionObserver{

	/**
	 * This is the remote service on the server that is used to browser through the servers content directory
	 */
	private RemoteService browseService;

	/**
	 * This is the callback that is used to subscribe to the content directory service on the connected media server.
	 */
	private SubscriptionCallback callback;

	/**
	 * Used to indicate how many requests have been made and to identify each request.
	 */
	private static int request = 0;

	/**
	 * This ArrayList is used to hold all of the object segments that are created until they can be combined into a single media object 
	 */
	private ArrayList<ObjectSegment> objectSegments = new ArrayList<ObjectSegment>();

	/**
	 * The timeout in seconds that is used when waiting for a response from connected devices
	 */
	private int timeout = 600;

	/**
	 * This method will update the connected device to be browsed through.
	 * @param connectedDevice This is the Remote device that will be connected to and used for network comunication.
	 */
	public void setConnectedDevice(RemoteDevice connectedDevice){
		ServiceId serviceId = new UDAServiceId("ContentDirectory");
		browseService = connectedDevice.findService(serviceId);
		callback = new SubscriptionCallback(browseService, timeout) { // Timeout in seconds

			@Override
			public void established(@SuppressWarnings("rawtypes") GENASubscription sub) {
				//TODO: For now just print to the console that a connection was established to a device.
				Log.v("ContentDirectoryService", "Established: " + sub.getSubscriptionId());
			}

			@SuppressWarnings({ "rawtypes", "unchecked" })
			@Override
			public void eventReceived(GENASubscription sub) {
				//TODO: So far this method has not been needed for the current functionality. For now just print out that an event was received until it is needed, most likely for syncing. 
				//TODO: Update here for changes on server.
				Map<String, StateVariableValue> values = sub.getCurrentValues();
				StateVariableValue status = values.get("Status");//"Status is the key defined in the cling doc for the event status.
				if(status != null){
					Log.v("ContentDirectoryService", "Status is: " + status.toString());
				}else{
					Log.v("ContentDirectoryService", "Status was null.");
				}
			}

			@SuppressWarnings("rawtypes")
			@Override
			public void eventsMissed(GENASubscription sub, int numberOfMissedEvents) {
				Log.v("ContentDirectoryService", "Missed events: " + numberOfMissedEvents);
			}

			@SuppressWarnings("rawtypes")
			@Override
			protected void ended(GENASubscription arg0, CancelReason arg1,
					UpnpResponse arg2) {

			}

			@SuppressWarnings("rawtypes")
			@Override
			protected void failed(GENASubscription arg0, UpnpResponse arg1,
					Exception arg2, String arg3) {
			}
		};

		NetworkManager.getNetworkManager().getUpnpService().getControlPoint().execute(callback);
	}

	/**
	 * This is a helper method that will take an objects metadata and its children and create a media object that represents it.
	 * @param metadata This is the metadata of the object returned when requesting the METADATA of an object.
	 * @param children This is the metadata returned of the object when requesting DIRECT_CHILDREN of an object. 
	 * @return Returns the media object 
	 */
	private MediaObject createMediaObject(DIDLContent metadata, DIDLContent children){
		MediaObject mo = null;
		
		//a folder is being made
		if(metadata.getContainers().size()==1){
			Container c = metadata.getFirstContainer();
			mo = new Folder(c, children);
		}

		//a non folder media object is being made
		if(metadata.getItems().size() == 1){
			Item i = metadata.getItems().get(0);
			mo = MediaObject.createMediaObject(i);
		}

		return mo;
	}

	/**
	 * This method will retrieve the requested media object on the connected media server. The media object that was retrieved will be returned through
	 * the passed in ContentDirectoryCallback's callback method.
	 * @param objectID The object id of the media object to retrieve. Use 0 for the root directory.
	 * @param cdc The ContentDirectoryCallback that will receive the the requested media object.
	 * @throws Exception Throws if a device is not currently connected.
	 */
	public void getMediaObjectFromServer(String objectID, final ContentDirectoryCallback cdc) throws Exception{
		if(callback == null || browseService == null){
			throw new Exception("The content directory service does not have a connected device.");
		}
		browse(objectID, cdc,request,BrowseFlag.DIRECT_CHILDREN);
		browse(objectID, cdc,request,BrowseFlag.METADATA);
		request++;
	}
	
	private void search(String objectID, final ContentDirectoryCallback cdc, final int requestNum, final BrowseFlag mode) throws Exception{
		
		//Browse action = new Browse()
	}

	/**
	 * Helper method used by this class to create and execute a browse action on the connected media server. Usually this method will need to be called twice to fully assemble an object, once with each mode type.
	 * @param objectID This is the unique identifier for the object to be retrieved from the server. 0 is default for the root node.
	 * @param cdc This is the Content directory service that will be given the media object once it is assembled.
	 * @param requestNum This is the request number of the request. 
	 * @param mode Indicates whether the metadata or direct children should be returned.
	 */
	private void browse(String objectID, final ContentDirectoryCallback cdc, final int requestNum, final BrowseFlag mode){
		Browse action = new Browse(browseService, objectID,mode){

			@SuppressWarnings("rawtypes")
			@Override
			public void received(ActionInvocation arg0, DIDLContent didl) {
				segmentRecieved(new ObjectSegment(cdc, mode, didl, requestNum));
			}

			@Override
			public void updateStatus(Status arg0) {
				//Unused
			}

			/**
			 * This method is called by the browse action whenever an error occurs.
			 */
			@SuppressWarnings("rawtypes")
			@Override
			public void failure(ActionInvocation arg0, UpnpResponse arg1,
					String arg2) {
				//The cling frame work automatically throws an exception and catches it. For now this method is not needed.
			}

		};
		//execute the browse action
		NetworkManager.getNetworkManager().getUpnpService().getControlPoint().execute(action);
	}

	/**
	 * This method is called once an ObjectSegment is received from a browse action. It will check to see if
	 * both parts of the request have been obtained and will then create a media object and pass the data off to the 
	 * ContentDirectoryCallback.
	 * @param os The ObjectSegment that was just received
	 */
	private synchronized void segmentRecieved(ObjectSegment os){
		boolean matched = false;//indicates if the segment has been matched with its other half.

		//go through each segment and see if the other half has been received yet.
		for(ObjectSegment os2: objectSegments){
			if(os2.requestNumber == os.requestNumber){
				DIDLContent meta = os.didl;
				DIDLContent child = os2.didl;
				if(os.type.equals(BrowseFlag.DIRECT_CHILDREN)){
					meta = os2.didl;
					child = os.didl;
				}
				MediaObject mo = createMediaObject(meta, child);

				//give object to call back
				os.cdc.mediaObjectRecieved(mo);

				//cleanup
				objectSegments.remove(os2);//remove the stored half of the returned object
				matched = true;//it has been matched
				break;//there will only be one other half so no need to keep searching.
			}
		}

		if(!matched){//if its not matched yet then keep it until it is.
			objectSegments.add(os);
		}
	}

	@Override
	public void dataUpdated(Map<RemoteDeviceIdentity, RemoteDevice> devices) {
		// TODO Auto-generated method stub

	}

	@Override
	public void newDeviceConnected(RemoteDevice rd) {
		// TODO Auto-generated method stub

	}

	@Override
	public void disconnected() {
		if(callback != null){
			callback.end();
			callback = null;
		}
		if(browseService != null){
			browseService = null;
		}
	}

	/**
	 * This class is used to maintain the different parts of a requested media object. Since the meta data and the objects children will need to be returned separately 
	 * there needs to be some way to hold onto the different parts of the object so that once both the meta data and the children are returned the entire object can be returned
	 * at once without needing to block any threads.
	 * @author kuszewskij
	 *
	 */
	private class ObjectSegment{

		/**
		 * This is the original Callback that requested the object
		 */
		private ContentDirectoryCallback cdc;

		/**
		 * This indicates if the segment is meta data or children. can be either METADATA or DIRECT_CHILDREN
		 */
		private BrowseFlag type;

		/**
		 * This is the data of the segment, either the meta data or the children.
		 */
		private DIDLContent didl;

		/**
		 * The request number identifying which object is being created.
		 */
		private int requestNumber;

		/**
		 * Simple constructor for this Object segment
		 * @param cdc The original Callback that requested the object
		 * @param type METADATA or DIRECT_CHILDREN
		 * @param didl The data of the segment, either the meta data or the children.
		 */
		private ObjectSegment(ContentDirectoryCallback cdc, BrowseFlag type, DIDLContent didl, int requestNumber){
			this.cdc = cdc;
			this.type = type;
			this.didl = didl;
			this.requestNumber = requestNumber;
		}
	}


}
