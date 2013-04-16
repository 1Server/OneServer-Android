package adapters;

import java.util.List;

import edu.msoe.oneserver.client.R;
import fileStructure.Folder;
import fileStructure.MediaObject;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

public class ListFileAdapter extends ArrayAdapter<MediaObject>{

	private final Context context;
	private List<MediaObject> values;

	public ListFileAdapter(Context context,List<MediaObject> list) {
		super(context,R.layout.rowlayout,list);
		this.context = context;
		this.values = list;
	}
	
	public void setValues(List<MediaObject> list){
		this.values = list;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View rowView = inflater.inflate(R.layout.file_selector_row_layout, parent, false);
		TextView textView = (TextView) rowView.findViewById(R.id.file_name_label);
		ImageView imageView = (ImageView) rowView.findViewById(R.id.file_icon);
		CheckBox cb = (CheckBox) rowView.findViewById(R.id.file_list_checkbox);
		cb.setChecked(values.get(position).isSelected);
		textView.setText(values.get(position).getTitle());
		int image;
		switch(values.get(position).getType()){
		case videoItem: 
			image = R.drawable.video_icon;
			cb.setVisibility(View.VISIBLE);
			break;
		case audioItem:
		case musicTrack:
			image = R.drawable.music_icon;
			cb.setVisibility(View.VISIBLE);
			break;
		case imageItem:
		case Photo:
			image = R.drawable.photo_icon;
			cb.setVisibility(View.VISIBLE);
			break;
		case textItem:
			image = R.drawable.text_icon;
			cb.setVisibility(View.VISIBLE);
			break;
		case playlist:
			image = R.drawable.playlist_icon;
			cb.setVisibility(View.VISIBLE);
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
		return rowView;
	}
	
}
