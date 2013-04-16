package UIFragments;
import java.net.MalformedURLException;
import java.net.URL;

import org.teleal.cling.support.model.Protocol;

import edu.msoe.oneserver.client.R;
import fileStructure.Image;

import UIViews.GestureImageView;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

/**
 * This class will be used to view images and resize them with gestures.
 * @author kuszewskij
 *
 */
public class ImageViewerFragment extends Fragment{

	/**
	 * The image that is being displayed
	 */
	private Image image;

	/**
	 * This is the view that will draw the image media object
	 */
	private GestureImageView view;

	/**
	 * Sets the image to display and passes it to its view.
	 * @param i
	 */
	public void setImage(Image i){
		image = i;
		if(view != null){//if the view has already been loaded then give it the image to draw.
			try {
				if(image.getResources().get(0).getProtocolInfo().getProtocol().equals(Protocol.HTTP_GET)){//were only supporting http-get
					view.loadImage(new URL(image.getResources().get(0).getValue()));//load the image
				}else{
					throw new Exception("The protocol for streaming this media is not supprted.");
				}
			} catch (MalformedURLException e) {
				Toast.makeText(getActivity(), "Invalid image URL", Toast.LENGTH_LONG).show();
				getFragmentManager().popBackStack();	
			} catch (Exception e) {
				Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_LONG).show();
				getFragmentManager().popBackStack();
			}	
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
		return inflater.inflate(R.layout.gesture_image_view_layout, container, false);
	}

	@Override
	public void onStart(){
		super.onStart();
		view = (GestureImageView) this.getActivity().findViewById(R.id.gestureImageView);//grab the view
		if(image != null){//if an image has already been set then give it to the view.
			setImage(image);
		}
	}

}
