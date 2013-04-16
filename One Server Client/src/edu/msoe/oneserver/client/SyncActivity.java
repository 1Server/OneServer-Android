package edu.msoe.oneserver.client;

import fileStructure.MediaObject;

import UIFragments.FileStructureListFragment;
import UIFragments.LocalFileViewerFragment;
import UIFragments.LocalFileViewerFragment.localFileViewerListener;
import UIFragments.OnMediaObjectSelectedListener;
import UIFragments.ServerDirListFragment;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.app.ListFragment;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class SyncActivity extends FragmentActivity implements OnMediaObjectSelectedListener{

	/**
	 * The fragment that is currently being shown in this activity.
	 */
	private LocalFileViewerFragment selectFilesFragment;
	
	private MediaObject destination;


	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.sync_page);
		showFragmentInMainArea(getSelectFilesFragment());
//		showFragmentInSecondaryArea(new ListFragment());
	}

	private void updateUploadFileTextView(){
		String text = "";
		for(MediaObject mo: selectFilesFragment.getSelectedMedia()){
			text+= mo.getTitle()+"; ";
		}
		((TextView)this.findViewById(R.id.files_input_field)).setText(text);
	}


	public void buttonClicked(View view){
		if(view.getId() == R.id.select_files_button){
			Toast.makeText(getApplicationContext(), "Selecting Files", Toast.LENGTH_SHORT).show();
			showFragmentInMainArea(getSelectFilesFragment());
		}
		
		if(view.getId() == R.id.select_destination_button){
			Toast.makeText(getApplicationContext(), "Selecting destination", Toast.LENGTH_SHORT).show();
			showFragmentInMainArea(new ServerDirListFragment());
		}

	}


	private Fragment getSelectFilesFragment() {
		if(selectFilesFragment == null){
			Toast.makeText(getApplicationContext(), "selectFileFragmentIsNull", Toast.LENGTH_SHORT).show();
			selectFilesFragment = new LocalFileViewerFragment();
			selectFilesFragment.subscribe(new localFileViewerListener(){

				@Override
				public void MediaObjectSelected(MediaObject mo) {
					// TODO Auto-generated method stub
					updateUploadFileTextView();
				}

				@Override
				public void MediaObjectDeselected(MediaObject mo) {
					// TODO Auto-generated method stub
					updateUploadFileTextView();
				}

				@Override
				public void DirectorySelected(MediaObject mo) {
					// TODO Auto-generated method stub
				}
				
			});
		}else{

			Toast.makeText(getApplicationContext(), "selectFileFragmentIs not Null", Toast.LENGTH_SHORT).show();
		}
		return selectFilesFragment;
	}

	/**
	 * Calling this will go through the list and upload the selected files to the media server.
	 */
	private void startUpload(String containerId){
		//create new syncservice
		//pass args and upload
	}

	/**
	 * This is a helper class to switch a fragment to the main area.
	 * @param f
	 */
	private void showFragmentInMainArea(Fragment frag){
		//Will need to add the fragments dynamically. Contain one linear layout and just swap in there with getChilfFragmentManager? 
		//hide or show the action bar
		FragmentTransaction transaction = getFragmentManager().beginTransaction();
		transaction.replace(R.id.sync_page_fragment, frag);
		transaction.commit();
	}

	/**
	 * This is a helper class to switch a fragment to the main area.
	 * @param f
	 */
	private void showFragmentInSecondaryArea(Fragment frag){
		//Will need to add the fragments dynamically. Contain one linear layout and just swap in there with getChilfFragmentManager? 
		//hide or show the action bar
		FragmentTransaction transaction = getFragmentManager().beginTransaction();
		transaction.replace(R.id.sync_page_fragment2, frag);
		transaction.commit();
	}
	
	@Override
	public void onMediaObjectSelected(MediaObject mo) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onContainerSelected(MediaObject mo) {
		// TODO Auto-generated method stub
		destination = mo;
		((EditText)findViewById(R.id.destination_input_field)).setText(mo.getTitle());
		
	}
}
