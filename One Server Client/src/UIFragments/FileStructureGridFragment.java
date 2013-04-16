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
import edu.msoe.oneserver.client.R;
import edu.msoe.oneserver.client.MainScreenActivityListView.States;
import fileStructure.Directory;
import fileStructure.Folder;
import fileStructure.MediaObject;
import adapters.DirectoryGridAdapter;
import android.app.ActionBar;
import android.app.Activity;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;

public class FileStructureGridFragment extends Fragment implements ContentDirectoryCallback, ConnectionObserver{

	public DirectoryGridAdapter dgAdapter;

	private Directory currentDirectory;

	private OnMediaObjectSelectedListener mCallback;

	private GridView gridView;

	/**
	 * The progress dialog that is used to 
	 */
	private ProgressDialog pd;

	/**
	 * If this is set to true then the page will update with a given directory.
	 */
	private boolean waitingForDirectory = false;


	/**
	 * This is the 
	 */
	private AsyncTask<Object, Object, MediaObject> at = new AsyncTask<Object,Object,MediaObject>(){

		@Override
		protected MediaObject doInBackground(Object ...mo) {
			return (MediaObject)mo[0];
		}

		@Override
		protected void onPostExecute(MediaObject object) {
			if(object.getMediaType().equals(MediaObject.mediaType.StorageFolder)){
				Directory d = new Directory(object.getId(), object.getParentId(), ((Folder)object).getMediaObjects());
				if(object.getId().equals("0")){
					//its a root
					d.setRoot(true);
				}
				directoryRecieved(d);
			}
		}

	};

	@Override
	public void onCreate(Bundle bundle){
		super.onCreate(bundle);
	}

	@Override
	public void onStart(){
		super.onStart();
		gridView.setEmptyView(this.getActivity().findViewById(R.id.emptyGridText));
		NetworkManager.getNetworkManager().getConnectionService().subscribe(this);
	}

	@Override
	public void onResume(){
		super.onResume();
		ActionBar ab = getActivity().getActionBar();
		((MainScreenActivityListView) getActivity()).setActionBarState(States.GridView);
		ab.show();
		if(MainScreenActivityListView.getTransitionDirectory()!=null && !FileStructureListFragment.newDeviceConnected){
			setFileStructure(MainScreenActivityListView.getTransitionDirectory());
		}else{
			if(NetworkManager.getNetworkManager().getConnectionService().isConnected()){
				waitingForDirectory = true;
				//when this page is first created, start on the root directory. Root is defined as id 0 in upnp.
				MediaLibrary.getMediaLibrary().getDirectory("0",this);
				FileStructureListFragment.newDeviceConnected = false;
			}
		}
	}


	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
		//For now just have a layout with a text field that says MediaObjectInfoFrag
		gridView = (GridView) inflater.inflate(R.layout.activity_main_screen_activity_grid_view, container, false);
		gridView.setOnItemClickListener(new OnItemClickListener() {

			@SuppressWarnings("deprecation")
			public void onItemClick(AdapterView<?> gridView, View view, int position,
					long id) {
				MediaObject item = (MediaObject)gridView.getAdapter().getItem(position);
				if(item.getType() == MediaObject.mediaType.StorageFolder){
					waitingForDirectory = true;
					MediaLibrary.getMediaLibrary().getDirectory(((Folder)item).getId(),FileStructureGridFragment.this);//grab new file structure from media library
					pd = new ProgressDialog(getActivity());
					pd.setMessage("Loading");
					pd.setButton("Cancel", new DialogInterface.OnClickListener(){

						@Override
						public void onClick(DialogInterface dialog, int which) {
							waitingForDirectory = false;
							pd.cancel();
						}

					});
					pd.show();
				}else {
					mCallback.onMediaObjectSelected(item);
				}

			}
		});
		return gridView;
	}

	@Override
	public void onAttach(Activity activity){
		super.onAttach(activity);

		try{
			mCallback = (OnMediaObjectSelectedListener) activity;
		}catch (ClassCastException e) {
			throw new ClassCastException(activity.toString() + "must implement OnMediaObjectSelectedListener");
		}

	}

	@Override
	public void onPause(){
		super.onPause();
		MainScreenActivityListView.setTransitionDirectory(currentDirectory);

	}

	public void setFileStructure(Directory d) {
		dgAdapter = new DirectoryGridAdapter(getActivity(), d.getMedia());
		gridView.setAdapter(dgAdapter);
		currentDirectory = d;
		dgAdapter.notifyDataSetInvalidated();
	}

	public Directory  getFileStructure(){
		return currentDirectory;
	}

	@Override
	public void mediaObjectRecieved(MediaObject object) {
		at = new AsyncTask<Object,Object,MediaObject>(){

			@Override
			protected MediaObject doInBackground(Object ...mo) {
				return (MediaObject)mo[0];
			}

			@Override
			protected void onPostExecute(MediaObject object) {
				if(object.getMediaType().equals(MediaObject.mediaType.StorageFolder)){
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
			if(!d.isRoot()){ 
				Folder folder = new Folder(currentDirectory.getName(), null, null, "Up One Level", null);
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
		FileStructureListFragment.newDeviceConnected = true;
		currentDirectory = null;
	}

	@Override
	public void disconnected() {
		if(dgAdapter != null){
			at = new AsyncTask<Object,Object,MediaObject>(){

				@Override
				protected MediaObject doInBackground(Object ...mo) {
					return null;//pass the media object sent as a parameter. will be null
				}

				@Override
				protected void onPostExecute(MediaObject object) {
					//now just clear the adapters to display empty text
					dgAdapter.clear();
					dgAdapter.notifyDataSetInvalidated();
				}

			};
			at.execute();
		}
		currentDirectory = null;
		MainScreenActivityListView.setTransitionDirectory(currentDirectory);
	}
}
