package UIFragments;

import java.io.File;
import java.util.ArrayList;
import java.util.List;


import edu.msoe.oneserver.client.R;
import fileStructure.Folder;
import fileStructure.MediaObject;

import adapters.ListFileAdapter;
import android.app.ListFragment;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.Toast;

public class LocalFileViewerFragment extends ListFragment{

	/**
	 * The adapter used to populate the file selection listView with files
	 */
	private ListFileAdapter lfa;

	private List<localFileViewerListener> observers = new ArrayList<localFileViewerListener>();

	private List<MediaObject> selectedMediaObjects;

	/**
	 * interface to communicate outside the fragment.
	 * @author kuszewskij
	 *
	 */
	public interface localFileViewerListener{
		public void MediaObjectSelected(MediaObject mo);
		public void MediaObjectDeselected(MediaObject mo);
		public void DirectorySelected(MediaObject mo);
	}

	public void subscribe(localFileViewerListener l){
		observers.add(l);
	}

	public void unSubscribe(localFileViewerListener l){
		observers.remove(l);
	}


	public List<MediaObject> getSelectedMedia(){
		if(selectedMediaObjects == null){
			selectedMediaObjects = new ArrayList<MediaObject>();
		}
		return selectedMediaObjects;
	}

	@Override
	public void onCreate(Bundle bundle){
		super.onCreate(bundle);
	}

	@Override
	public void onStart(){
		super.onStart();
		String state = Environment.getExternalStorageState();

		if (Environment.MEDIA_MOUNTED.equals(state) || Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {
			// We can read the media
			if(lfa == null){
				File file = Environment.getExternalStorageDirectory();
				lfa = new ListFileAdapter(this.getActivity(),	createDirectory(file));
				setListAdapter(lfa);
			}
		} else {
			// Something else is wrong. It may be one of many other states, but all we need
			//TODO:better error handling
			Toast.makeText(getActivity(), "The external storage cannot be read in the current state", Toast.LENGTH_SHORT).show();
		}

	}

	@Override
	public void onListItemClick(ListView l, View v, int position, long id) {
		MediaObject item = (MediaObject) getListAdapter().getItem(position);//the file path for each of these items will be contained in their id.
		Toast.makeText(getActivity(), "Selected "+item.getTitle(), Toast.LENGTH_SHORT);
		if(item.getType() == MediaObject.mediaType.StorageFolder){
			for(localFileViewerListener lfvl: observers){
				lfvl.DirectorySelected(item);
			}
			ArrayList<MediaObject> list = createDirectory(new File(item.getId()));
			for(MediaObject mo: list){
				Log.v("new Item", "Found item: "+mo.getTitle());
			}
			lfa = new ListFileAdapter(getActivity(),list);
			setListAdapter(lfa);
			lfa.notifyDataSetInvalidated();
		}else{
			CheckBox selectedBox = ((CheckBox)v.findViewById(R.id.file_list_checkbox));
			if(selectedBox.isChecked()){
				//remove it
				for(MediaObject mo: getSelectedMedia()){
					if(mo.getId().equals(item.getId())){
						mo.isSelected = false;
						getSelectedMedia().remove(mo);
						Toast.makeText(getActivity(), "file removed", Toast.LENGTH_SHORT).show();
						for(localFileViewerListener lfvl: observers){
							lfvl.MediaObjectDeselected(mo);
						}
						break;
					}
				}
			}else{
				//add it
				item.isSelected = true;
				getSelectedMedia().add(item);
				for(localFileViewerListener lfvl: observers){
					lfvl.MediaObjectSelected(item);
				}
				Toast.makeText(getActivity(), "file added "+item.getId(), Toast.LENGTH_SHORT).show();
			}
			selectedBox.setChecked(!selectedBox.isChecked());//toggle it
		}
	}

	private ArrayList<MediaObject> createDirectory(File file){
		ArrayList<MediaObject> retList = new ArrayList<MediaObject>();
		Folder up = new Folder(file.getParent(), file.getParent(), null, "Up One Level", null);
		up.useUpOneIcon = true;
		if(up.getId() != null){
			retList.add(up);
		}

		File[] files = file.listFiles();
		if(files != null){



			//iterate through and create media objects
			for(int i = 0; i < files.length; i++){
				Log.v("LocalFileViewerFragmenr", files[i].getName());
				MediaObject mo = MediaObject.createMediaObject(files[i]);
				if(mo != null){
					retList.add(mo);
					for(MediaObject selected: getSelectedMedia()){
						if(selected.getId().equals(mo.getId())){
							mo.isSelected = true;
						}
					}
				}
			}
		}
		return retList;
	}
}
