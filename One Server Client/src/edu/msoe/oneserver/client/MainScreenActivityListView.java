package edu.msoe.oneserver.client;


import helpers.Search;
import mediaLibrary.MediaLibrary;
import network.NetworkManager;
import fileStructure.Directory;
import fileStructure.MediaObject;
import UIFragments.ConnectionPageFragment;
import UIFragments.FileStructureGridFragment;
import UIFragments.FileStructureListFragment;
import UIFragments.MainMenuFragment;
import UIFragments.MediaObjectInfoFragment;
import UIFragments.OnMediaObjectSelectedListener;
import android.app.ActionBar;
import android.app.ActionBar.Tab;
import android.app.ActionBar.TabListener;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.View;
import android.widget.Toast;
import android.widget.SearchView.OnQueryTextListener;

public class MainScreenActivityListView extends FragmentActivity implements OnMediaObjectSelectedListener, TabListener{

	/**
	 * This is the list view fragment that shows the file structure
	 */
	private FileStructureListFragment fsfrag;

	/**
	 * This is the info fragment that will show the selected media objects information.
	 */
	private MediaObjectInfoFragment mofrag;

	/**
	 * This is the fragment that will hold the grid view fragment.
	 */
	private FileStructureGridFragment fsgfrag;

	/**
	 * This will indicate whether or not the view mode is in list view or grid view.
	 */
	private boolean isListView = true;

	/**
	 * This is the current state of the activity.
	 */
	private States currentState;

	/**
	 * This will hold the last directory viewed by a view before it is transitioned. This is the directory that will be restored when a view is switched.
	 */
	private static Directory transitionDirectory;

	/**
	 * This string will hold the current value of the search box.
	 */
	private String searchQuery = null;

	private OnQueryTextListener searchQueryListener = null;

	private Activity mainActivity = this;

	/**
	 * This represents the possible states the activity can be in.
	 * @author kuszewskij
	 *
	 */
	public enum States{
		MainMenu,GridView,ListView,InfoFragment, connectionFragment
	}


	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main_screen_activity_list_view);

		this.currentState = States.MainMenu;//the app will start at the main menu.

		searchQueryListener = new OnQueryTextListener() {

			@Override
			public boolean onQueryTextChange(String newText) {
				if(TextUtils.isEmpty(newText)){
					getActionBar().setSubtitle("List");
					searchQuery = null;
				} else {
					getActionBar().setSubtitle("List - Searching for: " + newText);
					searchQuery = newText;
				}
				return false;
			}

			@Override
			public boolean onQueryTextSubmit(String query) {
				String temp = Search.search(query);
				MediaLibrary ml = MediaLibrary.getMediaLibrary();
				//TODO Finish Implementing
				//Directory d = ml.search(temp);
				
				if(isListView){
					//getFSFrag().setFileStructure(d);
				}else{
					//getFsgfrag().setFileStructure(d);
				}
				
				Toast.makeText(mainActivity, "Searching for: " + temp + "...", Toast.LENGTH_LONG).show();
				return false;
			}

		};

		//set up the tabs to handle transitioning between the different views
		ActionBar ab = getActionBar();
		ab.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
		ab.setDisplayShowTitleEnabled(false);

		Tab tab = ab.newTab();
		tab.setText(R.string.menu_listview);
		tab.setTabListener(this);
		ab.addTab(tab);
		ab.addTab(ab.newTab().setText(R.string.menu_gridview).setTabListener(this));
		ab.hide();

		//Check to see if this is the layout that only shows one fragment
		if(findViewById(R.id.fragment_container) != null){
			MainMenuFragment mainScreenFragment = new MainMenuFragment();
			FragmentTransaction transaction = getFragmentManager().beginTransaction();
			transaction.replace(R.id.fragment_container, mainScreenFragment);
			transaction.commit();
		}

		//start up the network manager
		try {
			NetworkManager.init((WifiManager) this.getSystemService(Context.WIFI_SERVICE));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Calling this method will swap out the fragment that is currently showing with the one that is passed in.
	 * @param frag The fragment to show
	 */
	private void swapFragments(Fragment frag){

		//hide or show the action bar
		if(frag instanceof FileStructureListFragment | frag instanceof FileStructureGridFragment){
			getActionBar().show();
		}else{
			getActionBar().hide();
		}

		//Set the current state.
		if(frag instanceof FileStructureGridFragment){
			this.currentState = States.GridView;
		}else if(frag instanceof FileStructureListFragment){
			this.currentState = States.ListView;
		}else if(frag instanceof MainMenuFragment){
			this.currentState = States.MainMenu;
		}else if(frag instanceof MediaObjectInfoFragment){
			this.currentState = States.InfoFragment;
		}else if(frag instanceof ConnectionPageFragment){
			this.currentState = States.connectionFragment;
		}

		//Check to see if this is the layout that only shows one fragment
		if(findViewById(R.id.fragment_container) != null){

			FragmentTransaction transaction = getFragmentManager().beginTransaction();
			transaction.replace(R.id.fragment_container, frag);
			transaction.addToBackStack(null);
			transaction.commit();
		}
	}

	/**
	 * This method allows for the setting of the action bar state from the fragments.
	 * @param state The state the fragment is putting the action bar into.
	 */
	public void setActionBarState(States state){
		this.currentState = state;
	}

	/**
	 * This method handles telling the mediaObjectInfoFragment that the play button was pressed. Unfortunately the button in the layout 
	 * cannot communicate directly with the fragment and must go through the activity first.
	 */
	public void playMedia(View view){
		getMOFrag().playMedia();
	}
	
	public void refresh(View view){
		NetworkManager.getNetworkManager().getConnectionService().refresh();
	}

	/**
	 * This is called when a button is clicked. it will handle the state transitions appropriatly
	 * @param view
	 */
	public void buttonClicked(View view){
		if(view.getId() == R.id.main_screen_browse_button){
			//if were not connected then no point in going to the browse page yet.
			if(NetworkManager.getNetworkManager().getConnectionService().isConnected()){
				if(isListView){
					swapFragments(getFSFrag());
				}else{
					swapFragments(getFsgfrag());
				}
			}else{
				Toast.makeText(getApplicationContext(), "Not connected to a server.", Toast.LENGTH_SHORT).show();
			}
		}

		if(view.getId() == R.id.main_screen_connect_button){
			swapFragments(new ConnectionPageFragment());
		}
		
		if(view.getId() == R.id.main_screen_synch_button){
			 Intent i = new Intent(getApplicationContext(), SyncActivity.class);
			 startActivity(i);
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.activity_main_screen_activity_list_view, menu);
		//		SearchView searchView = (SearchView)menu.findItem(R.id.menu_search).getActionView();
		//		searchView.setOnQueryTextListener(searchQueryListener);
		return true;
	}


	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK && 
				getFragmentManager().getBackStackEntryCount()>0) {
			getFragmentManager().popBackStack();
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

	/**
	 * This method is called when the list view of the file structure has a media object selected that needs to be viewed.
	 * This method handles the transition of handing off the media object to the info fragment.
	 */
	public void onMediaObjectSelected(MediaObject mo) {
		swapFragments(getMOFrag());
		//now give the moinfofrag the media object.
		getMOFrag().setMediaObjectToDisplay(mo);
	}

	/**
	 * Getter for the fragment that shows a list view of the file structure. Uses lazy instantiation.
	 * @return The fragment for the list view of the file structure.
	 */
	private FileStructureListFragment getFSFrag(){
		if(fsfrag == null){
			fsfrag = new FileStructureListFragment();
		}

		return fsfrag;
	}

	/**
	 * Getter for the fragment that shows a info panel of the currently selected media object. Uses lazy instantiation.
	 * @return The fragment showing the info for the media object.
	 */
	private MediaObjectInfoFragment getMOFrag(){
		if(mofrag == null){
			mofrag = new MediaObjectInfoFragment();
		}

		return mofrag;
	}

	/**
	 * Getter for the grid view fragment. Uses lazy instantiation
	 * @return
	 */
	private FileStructureGridFragment getFsgfrag() {
		if(fsgfrag == null){
			fsgfrag = new FileStructureGridFragment(); 
		}
		return fsgfrag;
	}

	@Override
	public void onTabReselected(Tab tab, FragmentTransaction ft) {
		// TODO Auto-generated method stub
	}

	@Override
	public void onTabSelected(Tab tab, FragmentTransaction ft) {
		// TODO Auto-generated method stub
		if(currentState == States.ListView){
			ft.replace(R.id.fragment_container, getFsgfrag());
			currentState = States.GridView;
			isListView = false;
		}else if(currentState == States.GridView){
			ft.replace(R.id.fragment_container, getFSFrag());
			currentState = States.ListView;
			isListView = true;
		}
	}



	@Override
	public void onTabUnselected(Tab tab, FragmentTransaction ft) {
		//do nothing
	}

	public static Directory getTransitionDirectory() {
		return transitionDirectory;
	}

	public static void setTransitionDirectory(Directory transitionDirectory) {
		MainScreenActivityListView.transitionDirectory = transitionDirectory;
	}

	@Override
	public void onContainerSelected(MediaObject mo) {
		// TODO Auto-generated method stub
		
	}

}
