package network.clingaddons.search;

import java.util.logging.Logger;

import org.teleal.cling.controlpoint.ActionCallback;
import org.teleal.cling.model.action.ActionException;
import org.teleal.cling.model.action.ActionInvocation;
import org.teleal.cling.model.message.UpnpResponse;
import org.teleal.cling.model.meta.Service;
import org.teleal.cling.model.types.ErrorCode;
import org.teleal.cling.model.types.UnsignedIntegerFourBytes;
import org.teleal.cling.support.contentdirectory.DIDLParser;
import org.teleal.cling.support.model.DIDLContent;
import org.teleal.cling.support.model.SortCriterion;

public abstract class Search extends ActionCallback {
	
	public static final String CAPS_WILDCARD = "*";
	
	public enum Status {
		NO_CONTENT("No Content"),
		LOADING("Loading..."),
		OK("OK");
		
		private String defaultMessage;
		
		Status(String defaultMessage) {
			this.defaultMessage = defaultMessage;
		}
		
		public String getDefaultMessage() {
			return defaultMessage;
		}
	}
	
	private static Logger log = Logger.getLogger(Search.class.getName());
	
	public Search(Service service, String containerId, String searchParams){
		this(service, containerId, searchParams, CAPS_WILDCARD, 0, null);
	}
	
	public Search(Service service, String objectId, String searchParams,
							String filter, long firstResult, Long maxResults, SortCriterion... orderBy){
		
		super(new ActionInvocation(service.getAction("Search")));
		
		log.fine("Creating Search action for search parameters: " + searchParams);
		
		getActionInvocation().setInput("ContainerID", objectId);
		getActionInvocation().setInput("SearchCriteria", searchParams);
		getActionInvocation().setInput("Filter", filter);
		getActionInvocation().setInput("StartingIndex", new UnsignedIntegerFourBytes(firstResult));
		getActionInvocation().setInput("RequestedCount", 
				new UnsignedIntegerFourBytes(maxResults == null ? getDefaultMaxResults() : maxResults));
		getActionInvocation().setInput("SortCriteria", SortCriterion.toString(orderBy));
	}
	
	@Override
	public void run() {
		updateStatus(Status.LOADING);
	}

	@Override
	public void failure(ActionInvocation arg0, UpnpResponse arg1, String arg2) {
		// TODO Auto-generated method stub

	}

	@Override
	public void success(ActionInvocation arg0) {
		log.fine("Successful search action, reading output argument values");
		
		SearchResult result = new SearchResult(
				arg0.getOutput("Result").getValue().toString(),
				(UnsignedIntegerFourBytes) arg0.getOutput("NumberReturned").getValue(),
				(UnsignedIntegerFourBytes) arg0.getOutput("TotalMatches").getValue(),
				(UnsignedIntegerFourBytes) arg0.getOutput("UpdateID").getValue()
				);
		
		boolean proceed  = receivedRaw(arg0, result);
		
		if(proceed && result.getCountLong() > 0 && result.getResult().length() > 0) {
			
			try {
				DIDLParser didlParser = new DIDLParser();
				DIDLContent didl = didlParser.parse(result.getResult());
				received(arg0, didl);
				updateStatus(Status.OK);
			}catch(Exception e){
				arg0.setFailure(new ActionException(ErrorCode.ACTION_FAILED, "Can't parse DIDL XML response: " + e, e));
				failure(arg0, null);
			}
			
		}else{
			received(arg0, new DIDLContent());
			updateStatus(Status.NO_CONTENT);
		}

	}
	
	public long getDefaultMaxResults() {
		return 999;
	}
	
	public boolean receivedRaw(ActionInvocation actionInvocation, SearchResult searchResult){
		return true;
	}
	
	public abstract void received(ActionInvocation actionInvocation, DIDLContent didl);
	public abstract void updateStatus(Status status);

}
