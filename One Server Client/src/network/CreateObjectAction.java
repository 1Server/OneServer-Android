package network;

import helpers.ItemToDIDLConverter;

import org.teleal.cling.controlpoint.ActionCallback;
import org.teleal.cling.model.action.ActionInvocation;
import org.teleal.cling.model.meta.Service;
import org.teleal.cling.support.contentdirectory.DIDLParser;
import org.teleal.cling.support.model.DIDLContent;

import android.util.Log;

import fileStructure.MediaObject;

public abstract class CreateObjectAction extends ActionCallback{

	/**
	 * This is the string value defined in the UPNP spec for the container id parameter in the CreateObject action
	 */
	private final String CONTAINERID_PARAM = "ContainerID"; 
	
	/**
	 * This is the string value defined in the UPNP spec for the elements parameter for the CreateObject action.
	 */
	private final String ELEMENTS_PARAM = "Elements";
	
	public CreateObjectAction(Service service, String containerID, MediaObject mo){
		super(new ActionInvocation(service.getAction("CreateObject")));
		getActionInvocation().setInput(CONTAINERID_PARAM, containerID);
		getActionInvocation().setInput(ELEMENTS_PARAM, ItemToDIDLConverter.getDIDLString(mo));
	}
	
	@Override
	public void success(ActionInvocation invocation) {
		try {
			//Not concerned with getting the result output, but this is how:
			DIDLContent didl = new DIDLParser().parse(invocation.getOutput("Result").getValue().toString());
			
//			String objectID = invocation.getOutput("ObjectID").getValue().toString();
			received(didl);
		} catch (Exception e) {
			Log.e("CreateObjectAction", e.getMessage());
		}
	}
	
	public abstract void received(DIDLContent d);
	
	
}
