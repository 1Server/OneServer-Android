package adapters;

import java.util.List;

import network.NetworkManager;

import org.teleal.cling.model.meta.RemoteDevice;

import edu.msoe.oneserver.client.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class ConnectionPageAdapter extends ArrayAdapter<RemoteDevice>{

	private List<RemoteDevice> list;

	private final Context context;

	public ConnectionPageAdapter(Context context, List<RemoteDevice> objects) {
		super(context, R.layout.rowlayout, objects);
		this.list = objects;
		this.context = context;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View rowView = inflater.inflate(R.layout.connection_rowlayout, parent, false);
		TextView textView = (TextView) rowView.findViewById(R.id.label);
		ImageView imageView = (ImageView) rowView.findViewById(R.id.icon);

		textView.setText(list.get(position).getDetails().getFriendlyName());

		int image;
		//assume it is not connected.
		image = R.drawable.disconnected_icon;
		//set the icon to indicate if the device is connected or not.
		if(NetworkManager.getNetworkManager().getConnectionService().isConnected()){
			if(list.get(position).getIdentity().equals(NetworkManager.getNetworkManager().getConnectionService().getConnectedDevice().getIdentity())){
				//this is the connected device
				image = R.drawable.connected_icon;
			}
		}

		imageView.setImageResource(image);
		return rowView;
	}
}
