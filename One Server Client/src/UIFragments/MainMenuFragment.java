package UIFragments;

import edu.msoe.oneserver.client.MainScreenActivityListView;
import edu.msoe.oneserver.client.R;
import edu.msoe.oneserver.client.MainScreenActivityListView.States;
import android.app.ActionBar;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * This class is used to represent the main page that is first shown when the app is launched.
 * @author kuszewskij
 *
 */
public class MainMenuFragment extends Fragment{

	@Override
	public void onCreate(Bundle bundle){
		super.onCreate(bundle);
	}
	
	@Override
	public void onResume(){
		super.onResume();
		ActionBar ab = getActivity().getActionBar();
		((MainScreenActivityListView) getActivity()).setActionBarState(States.MainMenu);
		ab.hide();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
		//For now just have a layout with a text field that says MediaObjectInfoFrag
		return inflater.inflate(R.layout.activity_main_menu_screen, container, false);
	}

}
