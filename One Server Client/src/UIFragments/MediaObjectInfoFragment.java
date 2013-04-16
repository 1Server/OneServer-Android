package UIFragments;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map.Entry;

import org.teleal.cling.support.model.Protocol;

import edu.msoe.oneserver.client.MainScreenActivityListView;
import edu.msoe.oneserver.client.MainScreenActivityListView.States;
import edu.msoe.oneserver.client.Mediaplayer;
import edu.msoe.oneserver.client.R;
import fileStructure.Image;
import fileStructure.MediaObject;
import fileStructure.MetaDataTypes;
import android.app.ActionBar;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Html;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

/**
 * This class is used to display information about a media object.
 * @author kuszewskij
 *
 */
public class MediaObjectInfoFragment extends Fragment{

	/**
	 * This is the media object that is currently being displayed.
	 */
	MediaObject currentlyDisplayed;

	@Override
	public void onCreate(Bundle bundle){
		super.onCreate(bundle);
	}

	@Override
	public void onResume(){
		super.onResume();
		ActionBar ab = getActivity().getActionBar();
		((MainScreenActivityListView) getActivity()).setActionBarState(States.InfoFragment);
		ab.hide();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
		//For now just have a layout with a text field that says MediaObjectInfoFrag
		return inflater.inflate(R.layout.media_object_info_layout, container, false);
	}

	@Override
	public void onActivityCreated(Bundle bundle){
		super.onActivityCreated(bundle);
		((TextView)getView().findViewById(R.id.specific_info)).setMovementMethod(new ScrollingMovementMethod());
		if(currentlyDisplayed != null){
			setMediaObjectToDisplay(currentlyDisplayed);
		}
	}

	/**
	 * Calling this method will set the media object that is being displayed by this info fragment.
	 * @param mo
	 */
	public void setMediaObjectToDisplay(MediaObject mo){
		currentlyDisplayed = mo;
		if(getView()!=null){

			//Set the general info
			String generalText = "";
			if(mo.getTitle()!=null){
				generalText +="<big><b>Name:</b></big> "+mo.getTitle()+"<br>";
			}else{
				generalText +="<big><b>Name:</b></big> Unknown\n";
			}

			if(mo.getClassType()!=null){
				generalText +="<big><b>Type:</b></big> "+mo.getClassType()+"<br>";
			}else{
				generalText +="<big><b>Type:</b></big> Unknown<br>";
			}

			if(mo.isCached()){
				generalText +="<big><b>Cached</b></big><br>";
			}else{
				generalText +="<big><b>Not cached</b></big><br>";
			}

			if(mo.isProtected()){
				generalText +="<big><b>Protected</b></big><br>";
			}else{
				generalText +="<big><b>Not Protected</b></big><br>";
			}

			((TextView)getView().findViewById(R.id.general_info)).setText(Html.fromHtml(generalText));

			//set detailed info
			Iterator<Entry<String, ArrayList<String>>> iterator = mo.getMetaData().entrySet().iterator();
			String specificInfo = "";
			while(iterator.hasNext()){
				Entry<String, ArrayList<String>> pairs = iterator.next(); 
				ArrayList<String> values = (ArrayList<String>)pairs.getValue();
				if(values != null){
					specificInfo += "<big><b>"+MetaDataTypes.getFriendlyStringValue(pairs.getKey().toString())+": </b></big>";
					if(values.size() == 0){
						specificInfo += "Unknown";
					}
					for(int i = 0;  i < values.size(); i++){
						if(i>0){
							specificInfo += ", ";
						}
						if(values.get(i) != null && !values.get(i).equals("") && !values.get(i).equals(" ")){
							specificInfo += values.get(i);
						}else{
							specificInfo += "Unknown";
						}
					}
					specificInfo += "<br>";
				}
			}

			((TextView)getView().findViewById(R.id.specific_info)).setText(Html.fromHtml(specificInfo));

			//now set the select buttons text
			if(mo.getMediaType() == MediaObject.mediaType.videoItem | mo.getMediaType() == MediaObject.mediaType.musicTrack){
				((Button)getView().findViewById(R.id.selectButtonMediaInfo)).setText("Play");
			}else if(mo.getMediaType() == MediaObject.mediaType.imageItem){
				((Button)getView().findViewById(R.id.selectButtonMediaInfo)).setText("View");
			}

			//load icon
			ImageView im = (ImageView)getView().findViewById(R.id.preview_icon);
			switch(mo.getType()){
			case audioItem:
			case musicTrack:
				im.setImageResource(R.drawable.music_icon);
				break;
			case videoItem:
				im.setImageResource(R.drawable.video_icon);
				break;
			case Photo:
				//load icon up asynchronisly
				this.getActivity().findViewById(R.id.preview_icon).setVisibility(View.GONE);
				this.getActivity().findViewById(R.id.media_object_info_loadingbar).setVisibility(View.VISIBLE);
				try {
					View v = getActivity().findViewById(R.id.media_object_info_loadingbar);
					new LoadURLToImage().execute(new URL(mo.getResources().get(0).getValue()),im,v);
				} catch (MalformedURLException e) {
					//use default image icon
					im.setImageResource(R.drawable.photo_icon);
				}
				break;
			}
		}
	}

	/**
	 * This method will play the current media object 
	 */
	public void playMedia(){
		//If its an imageItem or photo then pass it to an image viewer.
		if(currentlyDisplayed.getMediaType() == MediaObject.mediaType.Photo || currentlyDisplayed.getMediaType() == MediaObject.mediaType.imageItem){
			if(currentlyDisplayed.getResources().get(0).getProtocolInfo().getProtocol().equals(Protocol.HTTP_GET)){
				ImageViewerFragment ivf = new ImageViewerFragment();
				ivf.setImage((Image) currentlyDisplayed);
				FragmentTransaction transaction = getFragmentManager().beginTransaction();
				transaction.replace(R.id.fragment_container,ivf);
				transaction.addToBackStack(null);
				transaction.commit();
			}else{
				Toast.makeText(this.getActivity(), "Unable to retrieve from server: Only HTTP-Get protocol is supported.", Toast.LENGTH_LONG).show();
			}
			
		//If its a music track or videoItem then give it to the MediaPlayer to play.
		}else if(currentlyDisplayed.getMediaType() == MediaObject.mediaType.videoItem || currentlyDisplayed.getMediaType() == MediaObject.mediaType.musicTrack){
			if(currentlyDisplayed.getResources().get(0).getProtocolInfo().getProtocol().equals(Protocol.HTTP_GET)){
				Intent intent = new Intent(this.getActivity().getApplicationContext(),Mediaplayer.class);
				intent.putExtra(Mediaplayer.URI_KEY, currentlyDisplayed.getResources().get(0).getValue());
				this.getActivity().startActivity(intent);
			}else{
				Toast.makeText(this.getActivity(), "Unable to retrieve from server: Only HTTP-Get protocol is supported.", Toast.LENGTH_LONG).show();
			}
			
		//This type of file does not have playback supported yet.
		}else{
			Toast.makeText(this.getActivity(), "Unable to play media: Playback for this file type is not yet supported.", Toast.LENGTH_LONG).show();
		}
	}
	
	/**
	 * Helper class that will load an image from a url and set it as the image that is being viewed.
	 * @author kuszewskij
	 *
	 */
	private class LoadURLToImage extends AsyncTask<Object, Void, Bitmap> {

		//the GestureImageView that started the task
		ImageView iv;
		View v;

		@Override
		protected Bitmap doInBackground(Object... url) {

			Bitmap bmp = null;
			iv = (ImageView)url[1];
			v = (View)url[2];

			try {
				bmp = BitmapFactory.decodeStream(((URL)url[0]).openConnection().getInputStream());
			} catch (Exception e) {
			}
			return bmp;
		}

		@Override
		protected void onPostExecute(Bitmap b){
			iv.setImageBitmap(b);
			iv.setVisibility(View.VISIBLE);
			v.setVisibility(View.GONE);
		}
	}
}
