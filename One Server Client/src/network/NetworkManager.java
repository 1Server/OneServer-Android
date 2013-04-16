package network;

import org.teleal.cling.UpnpService;
import org.teleal.cling.UpnpServiceImpl;
import org.teleal.cling.android.AndroidUpnpServiceConfiguration;

import android.net.wifi.WifiManager;


/**
 * This class is the head of all the network features for the client. It maintains all of the individual components used in network communication through the cling library.
 * @author kuszewskij
 *
 */
public class NetworkManager {

	/**
	 * The connectionService in charge of handling detecting media servers and connecting to them.
	 */
	private ConnectionService connectionService;
	
	/**
	 * the service that will allow for navigating through the stored media on a connected server.
	 */
	private ContentDirectoryService contentDirectoryService;
	
	/**
	 * The only instance of this class.
	 */
	private static NetworkManager networkManagerInstance;
	
	/**
	 * The access point into the cling api
	 */
	private UpnpService upnpService;
	
	/**
	 * the wifi manager used by this android app
	 */
	private static WifiManager wfm;
	
	/**
	 * Private constructor as part of singleton
	 * @throws IllegalStateException Throws an exception if init has not yet been called.
	 */
	private NetworkManager(){
		//check to see if the wiwfi manager isnt null before passing it to the upnpService. 
		if(wfm == null){
			throw new IllegalStateException("The wifimanager was null. Init must first be called");
		}
		
		//set up all the instance variables
		contentDirectoryService = new ContentDirectoryService();
		upnpService = new UpnpServiceImpl(new AndroidUpnpServiceConfiguration(wfm));
		connectionService = new ConnectionService(); 
		connectionService.subscribe(contentDirectoryService);
		upnpService.getRegistry().addListener(connectionService);//add a listener for new devices 
		upnpService.getControlPoint().search();//when the network manager is first created then search for all devices
	}
	
	/**
	 * Returns the singleton instance of the network manager. Prior to calling this method the init method must be called at least once.
	 * 
	 * @throws IllegalStateException if init has not yet been called.
	 * @return The singleton instance of the network manager.
	 */
	public static synchronized NetworkManager getNetworkManager(){
		if(networkManagerInstance == null){
			networkManagerInstance = new NetworkManager();
		}
		
		return networkManagerInstance;
	}
	
	/**
	 * Calling this method will set the WIFI manager to be used by the network manager.
	 * @param wfm The Wifi Manager
	 * @throws IllegalArgumentException if the wifi manager passed in was null. 
	 */
	public static void init(WifiManager wfm) throws IllegalArgumentException{
		if(wfm == null){
			throw new IllegalArgumentException("Wifi Manager can't be null");
		}
		NetworkManager.wfm = wfm;
	}
	
	/**
	 * Returns the entry point into the cling stack used by the client.
	 * @return
	 */
	public UpnpService getUpnpService(){
		return upnpService;
	}
	
	/**
	 * 
	 * @return Returns the connection service used to identify and connect to media servers
	 */
	public ConnectionService getConnectionService(){
		return connectionService;
	}
	
	/**
	 * 
	 * @return Returns the service that allows for navigating through the media stored on a connected media server.
	 */
	public ContentDirectoryService getContentDirectoryService(){
		return contentDirectoryService;
	}
}
