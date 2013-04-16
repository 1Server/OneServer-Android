package network;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.teleal.cling.model.meta.RemoteDevice;
import org.teleal.cling.model.meta.RemoteDeviceIdentity;
import org.teleal.cling.model.types.UDADeviceType;
import org.teleal.cling.registry.DefaultRegistryListener;
import org.teleal.cling.registry.Registry;

import android.util.Log;



/**
 * This class is used to handle conections to media servers.
 * 
 * @author kuszewskij
 *
 */
public class ConnectionService extends DefaultRegistryListener{

	//the type of device that the client can connect to. Type is MediaServer
	private UDADeviceType deviceType;

	//All the devices that are of type MediaServer will be held here.
	private Map<RemoteDeviceIdentity, RemoteDevice> devices;

	//A collection holding anything that want to be informed when a new device is added or removed.
	private ArrayList<ConnectionObserver> observers;

	//The device that is currently being used by this client
	private RemoteDevice connectedDevice;


	/**
	 * Interface for an object wishing to receive a notification of when a new device is added of type mediaServer
	 * @author kuszewskij
	 *
	 */
	public interface ConnectionObserver{
		public void dataUpdated(Map<RemoteDeviceIdentity, RemoteDevice> devices);
		public void newDeviceConnected(RemoteDevice rd);
		public void disconnected();
	}

	/**
	 * Constructor for this class. Initializes to only look for devices of type MediaServer
	 */
	public ConnectionService() {
		deviceType = new UDADeviceType("MediaServer");//only connect to devices that are of type MediaServer
		devices = new HashMap<RemoteDeviceIdentity,RemoteDevice>();
		observers = new ArrayList<ConnectionObserver>();
	}

	@Override
	public void remoteDeviceAdded(Registry registry, RemoteDevice device) {
		System.out.println("Found device");
		Log.v("Connection Service","Remote device has been found: "+device.getDetails().getFriendlyName());

		if(device.getType().equals(deviceType)){
			Log.v("Connection Service","Adding remote device: "+device.getDetails().getFriendlyName()+" with type "+ device.getType());

			devices.put(device.getIdentity(), device);

			if(connectedDevice == null){
				setConnectedDevice(device.getIdentity());
			}
			for(ConnectionObserver c: observers){
				c.dataUpdated(devices);
			}
		}
	}

	@Override
	public void remoteDeviceRemoved(Registry registry, RemoteDevice device) {
		devices.remove(device.getIdentity());
		if(connectedDevice!=null){
			if(device.getIdentity().equals(connectedDevice.getIdentity())){
				disconnect();
				connectedDevice = null;
			}
		}
		for(ConnectionObserver c: observers){
			c.dataUpdated(devices);
		}
	}

	/**
	 * Subscribe to the connection service to be notified when a new devices has been discovered.
	 * @param c
	 */
	public void subscribe(ConnectionObserver c){
		observers.add(c);
	}

	/**
	 * Sets the device that is currently used by the connection service.
	 * @param rdi
	 */
	public void setConnectedDevice(RemoteDeviceIdentity rdi){
		if(devices.containsKey(rdi)){
			connectedDevice = devices.get(rdi);

			//notify the ContentDirectoryService that a new device has been connected to. 
			NetworkManager.getNetworkManager().getContentDirectoryService().setConnectedDevice(connectedDevice);

			for(ConnectionObserver o: observers){
				o.dataUpdated(devices);
				if(connectedDevice != null){
					o.newDeviceConnected(connectedDevice);
				}
			}
		}
	}

	/**
	 * Disconnects the client from any servers being used.
	 */
	public void disconnect(){
		connectedDevice = null;
		for(ConnectionObserver o: observers){
			o.disconnected();
		}
	}

	/**
	 * Returns all of the media server devices that have been discovered by the connection service.
	 * @return
	 */
	public Collection<RemoteDevice> getDevices(){
		return devices.values();
	}

	public Map<RemoteDeviceIdentity, RemoteDevice> getDevicesMap(){
		return devices;
	}

	/**
	 * Returns true if this connection service is currently connected to a device. 
	 * @return
	 */
	public boolean isConnected(){
		if(connectedDevice == null){
			return false;
		}else{
			return true;
		}
	}

	/**
	 * Returns the connected device that is being used by this connection service.
	 * @return
	 */
	public RemoteDevice getConnectedDevice(){
		return connectedDevice;
	}

	public void refresh() {
		NetworkManager.getNetworkManager().getUpnpService().getControlPoint().search();
	}
}
