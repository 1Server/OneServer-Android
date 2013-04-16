package adapters;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import edu.msoe.oneserver.client.R;
import fileStructure.Folder;
import fileStructure.MediaObject;

public class DirectoryGridAdapter extends ArrayAdapter<MediaObject> {

	private final Context context;
	private List<MediaObject> values;
	
	public DirectoryGridAdapter(Context context, List<MediaObject> list) {
		super(context, R.layout.gridlayout, list);
		this.context = context;
		this.values = list;
	}
	
	public void setValues(List<MediaObject> list) {
		this.values = list;
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		LayoutInflater inflator = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View gridView = inflator.inflate(R.layout.gridlayout, parent, false);
		TextView textView = (TextView) gridView.findViewById(R.id.gridlabel);
		ImageView imageView = (ImageView) gridView.findViewById(R.id.gridicon);
		textView.setText(values.get(position).getTitle());
		int image;
		
		switch(values.get(position).getType()){
		case videoItem:
			image = R.drawable.video_icon;
			break;
		case audioItem:
		case musicTrack:
			image = R.drawable.music_icon;
			break;
		case imageItem:
		case Photo:
			image = R.drawable.photo_icon;
			break;
		case textItem:
			image = R.drawable.text_icon;
			break;
		case playlist:
			image = R.drawable.playlist_icon;
			break;
		case StorageFolder:
			if(((Folder)(values.get(position))).useUpOneIcon){
				image = R.drawable.up_folder_icon;
			}else{
				image = R.drawable.folder_icon;
			}
			break;
		default:
			image = R.drawable.default_icon;
			break;
		}
		
		imageView.setImageResource(image);
		return gridView;
	}
}
