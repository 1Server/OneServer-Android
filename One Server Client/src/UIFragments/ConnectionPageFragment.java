package UIFragments;

import java.util.ArrayList;
import java.util.Map;

import org.teleal.cling.model.meta.RemoteDevice;
import org.teleal.cling.model.meta.RemoteDeviceIdentity;

import network.ConnectionService.ConnectionObserver;
import network.NetworkManager;
import edu.msoe.oneserver.client.R;
import adapters.ConnectionPageAdapter;
import android.app.Fragment;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;

/**
 * This is the page that will display the discovered devices and allow for one of them to be connected to.
 * @author kuszewskij
 *
 */
public class ConnectionPageFragment extends Fragment implements ConnectionObserver{

	//The adapter this view uses to populate the list with.
	private ConnectionPageAdapter adapter;


	//will be used to update the list view from the network thread when a new connection is made.
	private Handler handler;
	
	private static class cpfHandler extends Handler{
		@SuppressWarnings("unchecked")
		@Override
		public void handleMessage(Message msg) {//when a message is received then invalidate and refresh the data set.
			ConnectionPageAdapter adapter = (ConnectionPageAdapter) ((Object[])msg.obj)[1];
			adapter.clear();
			adapter.addAll(((Map<RemoteDeviceIdentity, RemoteDevice>)((Object[])msg.obj)[0]).values());
			adapter.notifyDataSetInvalidated();
		}
	}
	
	@Override
	public void onCreate(Bundle bundle){
		super.onCreate(bundle);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
		return inflater.inflate(R.layout.connection_page_layout, container, false);
	}

	@Override
	public void onStart(){
		super.onStart();
		
		//TODO: if not working then just change this back to inline instantiation.
		handler = new cpfHandler();
		
		adapter = new ConnectionPageAdapter(getActivity(),new ArrayList<RemoteDevice>(NetworkManager.getNetworkManager().getConnectionService().getDevices()));
		final ListView listView = (ListView) this.getActivity().findViewById(R.id.connection_list_view);
		listView.setEmptyView(this.getActivity().findViewById(R.id.emptyText));
		listView.setOnItemClickListener(new OnItemClickListener(){

			int lastPosition = -1;

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position,
					long id) {

				Button b = (Button)view.findViewById(R.id.button);
				b.setOnClickListener(new OnClickListener(){

					@Override
					public void onClick(View v) {
						if(((Button)v).getText().equals("Connect")){
							if(lastPosition >=0){
								NetworkManager.getNetworkManager().getConnectionService().setConnectedDevice(adapter.getItem(lastPosition).getIdentity());
							}
						}else if(((Button)v).getText().equals("Disconnect")){
							if(lastPosition >=0){
								NetworkManager.getNetworkManager().getConnectionService().disconnect();
							}
						}
					}

				});


				if(NetworkManager.getNetworkManager().getConnectionService().getConnectedDevice()!=null && NetworkManager.getNetworkManager().getConnectionService().getConnectedDevice().equals(adapter.getItem(position))){
					b.setText("Disconnect");
				}else{
					b.setText("Connect");
				}

				if(lastPosition == position){
					if(b.getVisibility() == View.VISIBLE){
						b.setVisibility(View.GONE);
						view.setBackgroundColor(Color.TRANSPARENT);
					}else{
						b.setVisibility(View.VISIBLE);
						view.setBackgroundColor(Color.argb(255, 135, 206, 250));
					}
				}else{
					View lastView = parent.getChildAt(lastPosition);
					if(lastView != null){
						((Button)lastView.findViewById(R.id.button)).setVisibility(View.GONE);
						lastView.setBackgroundColor(Color.TRANSPARENT);
					}

					b.setVisibility(View.VISIBLE);
					view.setBackgroundColor(Color.argb(255, 135, 206, 250));
				}
				lastPosition = position;
			}

		});
		listView.setAdapter(adapter);
		listView.invalidate();
		NetworkManager.getNetworkManager().getConnectionService().subscribe(this);
	}

	@Override//called by the connection manager when a new device has been discovered or removed.
	public void dataUpdated(Map<RemoteDeviceIdentity, RemoteDevice> devices) {
		Message msg = new Message();
		Object[] arg = new Object[2];
		arg[0] = devices;
		arg[1] = adapter;
		msg.obj = arg;
		handler.sendMessage(msg);
	}

	@Override
	public void newDeviceConnected(RemoteDevice rd) {
		Message msg = new Message();
		Object[] arg = new Object[2];
		arg[0] = NetworkManager.getNetworkManager().getConnectionService().getDevicesMap();
		arg[1] = adapter;
		msg.obj = arg;
//		msg.obj = NetworkManager.getNetworkManager().getConnectionService().getDevicesMap();
		handler.sendMessage(msg);
	}

	@Override
	public void disconnected() {
		Message msg = new Message();
		Object[] arg = new Object[2];
		arg[0] = NetworkManager.getNetworkManager().getConnectionService().getDevicesMap();
		arg[1] = adapter;
		msg.obj = arg;
//		msg.obj = NetworkManager.getNetworkManager().getConnectionService().getDevicesMap();
		handler.sendMessage(msg);
	}
	
	public void refresh(){
		System.out.println("Refreshing");
	}
}
