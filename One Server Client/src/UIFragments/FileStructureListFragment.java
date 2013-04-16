package UIFragments;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.teleal.cling.model.meta.RemoteDevice;
import org.teleal.cling.model.meta.RemoteDeviceIdentity;

import network.ConnectionService.ConnectionObserver;
import network.ContentDirectoryCallback;
import network.NetworkManager;

import mediaLibrary.MediaLibrary;
import edu.msoe.oneserver.client.MainScreenActivityListView;
import edu.msoe.oneserver.client.MainScreenActivityListView.States;
import edu.msoe.oneserver.client.R;
import fileStructure.Directory;
import fileStructure.Folder;
import fileStructure.MediaObject;
import adapters.DirectoryAdapter;
import android.app.ActionBar;
import android.app.Activity;
import android.app.ListFragment;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

/**
 * This class  is used to handle displaying a file structure in a list style view. 
 * @author kuszewskij
 *
 */
public class FileStructureListFragment extends ListFragment implements ContentDirectoryCallback, ConnectionObserver{

	/**
	 * The adapter for showing media objects in the view.
	 */
	public DirectoryAdapter dAdapter;

	/**
	 * This is the directory that is currently being shown in the list view.
	 */
	private Directory currentDirectory;

	/**
	 * This is the parent activity that implements the OnMediaObjectSelected interface.
	 */
	private OnMediaObjectSelectedListener mCallback;

	/**
	 * The progress dialog that is used to 
	 */
	private ProgressDialog pd;

	/**
	 * If this is set to true then the page will update with a given directory.
	 */
	private boolean waitingForDirectory = false;

	/**
	 * This indicates if a new device has been connected ad needs to be synced with the rest of the app.
	 */
	public static boolean newDeviceConnected = true;

	/**
	 * This is the AsynchTask used to give the UI thread media objects from the network thread. It takes in a Media object as an argument.
	 */
	private AsyncTask<Object, Object, MediaObject> at = new AsyncTask<Object,Object,MediaObject>() {

		@Override
		protected MediaObject doInBackground(Object ...mo) {
			return (MediaObject)mo[0];//just hand over the media object
		}

		@Override
		protected void onPostExecute(MediaObject object) {
			if(object.getMediaType().equals(MediaObject.mediaType.StorageFolder)){//check to see if its a folder. only folders should ever be returned since this only displays the contents of a folder, so if its not, do nothing.
				Directory d = new Directory(object.getId(), object.getParentId(), ((Folder)object).getMediaObjects());
				if(object.getId().equals("0")){
					//its a root
					d.setRoot(true);
				}
				directoryRecieved(d);//finally give the Media object to the UI 
			}
		}

	};

	@Override
	public void onCreate(Bundle bundle){
		super.onCreate(bundle);
		setListAdapter(new DirectoryAdapter(this.getActivity(),new ArrayList<MediaObject>()));
	}

	@Override
	public void onStart(){
		super.onStart();
		this.setEmptyText(this.getActivity().getText(R.string.not_connected));
		NetworkManager.getNetworkManager().getConnectionService().subscribe(this);
	}

	@Override
	public void onResume(){
		super.onResume();
		//update the tab on the action bar to display the list view tab as the current tab.
		ActionBar ab = getActivity().getActionBar();
		((MainScreenActivityListView) getActivity()).setActionBarState(States.ListView);
		ab.show();
		if(MainScreenActivityListView.getTransitionDirectory()!=null && !newDeviceConnected){//check to see if there was previously a directory being shown.
			setFileStructure(MainScreenActivityListView.getTransitionDirectory());//if there is then show that directory.
		}else{
			if(NetworkManager.getNetworkManager().getConnectionService().isConnected()){
				waitingForDirectory = true;
				//when this page is first created, start on the root directory. Root is defined as id 0 in upnp.
				MediaLibrary.getMediaLibrary().getDirectory("0",this);
				newDeviceConnected = false;
			}
		}
	}

	@SuppressWarnings("deprecation")
	@Override
	public void onListItemClick(ListView l, View v, int position, long id) {
		MediaObject item = (MediaObject) getListAdapter().getItem(position);
		if(item.getType() == MediaObject.mediaType.StorageFolder){
			waitingForDirectory = true;
			MediaLibrary.getMediaLibrary().getDirectory(((Folder)item).getId(),this);//grab new file structure from media library
			pd = new ProgressDialog(this.getActivity());
			pd.setMessage("Loading");
			pd.setButton("Cancel", new DialogInterface.OnClickListener(){

				@Override
				public void onClick(DialogInterface dialog, int which) {
					waitingForDirectory = false;
					pd.cancel();
				}

			});
			pd.show();
		}else{//its a media object that the user wants to display info for.
			mCallback.onMediaObjectSelected(item);
		}

	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		// This makes sure that the container activity has implemented
		// the callback interface. If not, it throws an exception
		try {
			mCallback = (OnMediaObjectSelectedListener) activity;
		} catch (ClassCastException e) {
			throw new ClassCastException(activity.toString()
					+ " must implement OnMediaObjectSelectedListener");
		}
	}

	@Override
	public void onPause(){
		super.onPause();
		MainScreenActivityListView.setTransitionDirectory(currentDirectory);//give the current directory being shown to the main activity so that once this page is shown again
	}

	/**
	 * This will be used to set the the files that are currently showing in the list view.
	 * @param list
	 */
	public void setFileStructure(Directory d){
		dAdapter = new DirectoryAdapter(getActivity(),d.getMedia());
		setListAdapter(dAdapter);
		currentDirectory = d;
		MainScreenActivityListView.setTransitionDirectory(currentDirectory);
		dAdapter.notifyDataSetInvalidated();
	}

	/**
	 * 
	 * @return Returns the current file structure that is being shown.
	 */
	public Directory  getFileStructure(){
		return currentDirectory;
	}


	@Override
	public void mediaObjectRecieved(MediaObject object) {
		at = new AsyncTask<Object,Object,MediaObject>(){

			@Override
			protected MediaObject doInBackground(Object ...mo) {
				return (MediaObject)mo[0];//pass the media object sent as a parameter.
			}

			@Override
			protected void onPostExecute(MediaObject object) {
				System.out.println("Object is: "+object);
				if(object.getMediaType().equals(MediaObject.mediaType.StorageFolder)){//check to see if its a folder. only folders should ever be returned since this only displays the contents of a folder, so if its not, do nothing.
					Directory d = new Directory(object.getId(), object.getParentId(), ((Folder)object).getMediaObjects());
					if(object.getId().equals("0")){
						//its a root
						d.setRoot(true);
					}
					directoryRecieved(d);
				}
			}

		};
		at.execute(object);
	}

	@Override
	public void directoryRecieved(Directory d) {
		if(waitingForDirectory){
			List<MediaObject> newList = new ArrayList<MediaObject>();
			newList.addAll(d.getMedia());
			if(!d.isRoot()){ //if its not a root then ad a folder that will take the user up one level back to the parent folder if it is pressed.
				Folder folder = new Folder( d.getParentDirectoryName(), d.getParentDirectoryName(), null, "Up One Level", null);
				folder.useUpOneIcon = true;
				folder.directory = currentDirectory.getName();
				newList.add(0,folder);
			}

			if(currentDirectory != null){
				currentDirectory = new Directory(d.getName(),currentDirectory.getName(), newList);
			}else{
				currentDirectory = new Directory(d.getName(),null, newList);
			}
			setFileStructure(currentDirectory);
			if(pd !=null){
				pd.dismiss();
				pd = null;
				waitingForDirectory = false;
			}
		}
	}

	@Override
	public void dataUpdated(Map<RemoteDeviceIdentity, RemoteDevice> devices) {
		//Not needed for this class
	}

	@Override
	public void newDeviceConnected(RemoteDevice rd) {
		newDeviceConnected = true;
		currentDirectory = null;
	}

	@Override
	public void disconnected() {
		if(dAdapter != null){
			at = new AsyncTask<Object,Object,MediaObject>(){

				@Override
				protected MediaObject doInBackground(Object ...mo) {
					return null;//pass the media object sent as a parameter. will be null
				}

				@Override
				protected void onPostExecute(MediaObject object) {
					//now just clear the adapters to display empty text
					dAdapter.clear();
					dAdapter.notifyDataSetInvalidated();
				}

			};
			at.execute();
		}

		currentDirectory = null;
		MainScreenActivityListView.setTransitionDirectory(currentDirectory);
	}
}
