package network;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.InputStreamEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.teleal.cling.model.action.ActionInvocation;
import org.teleal.cling.model.message.UpnpResponse;
import org.teleal.cling.model.types.UDAServiceId;
import org.teleal.cling.support.model.DIDLContent;

import fileStructure.MediaObject;

import android.media.MediaMetadataRetriever;
import android.os.AsyncTask;

public class SyncService {

	private List<SyncServiceListener> observers; 

	private enum State{
		waiting, uploading
	}

	public interface SyncServiceListener{
		public void progress(String msg, int total, int remaining);
	}

	public void uploadFiles(List<File> files, String containerId){
		AsyncTask<Object,Object,Object> task = new AsyncTask<Object, Object, Object>(){

			@Override
			protected Object doInBackground(Object... arg0) {
				List<File> files = (List<File>) arg0[0];
				String containerId = (String) arg0[1];

				int totalFiles = files.size();
				int progress = 0;

				for(File f: files){

					SyncService.this.notifyAll("Uploading "+f.getName(),progress,totalFiles);
					MediaObject mo = MediaObject.createMediaObject(f);

					if(mo!=null){
						//create the object
						CreateObjectAction coa = new CreateObjectAction(NetworkManager.getNetworkManager().getConnectionService().getConnectedDevice().findService(new UDAServiceId("CreateObject")),
								containerId, mo){

							@Override
							public void received(DIDLContent didl) {
								//HTTP Post
								//TODO:
							}

							@Override
							public void failure(ActionInvocation arg0,
									UpnpResponse arg1, String arg2) {
								// TODO Auto-generated method stub

							}

						};
						NetworkManager.getNetworkManager().getUpnpService().getControlPoint().execute(coa);


					}else{
						//TODO: report error.
					}

					progress++;
				}

				return null;
			}

		};
		task.execute();
	}

	
	private static void httpPostObject(String url, File f) throws ClientProtocolException, IOException{
		HttpClient httpc = new DefaultHttpClient();
		HttpPost httpp = new HttpPost(url);

		//add params
		InputStreamEntity reqEntity = new InputStreamEntity(new FileInputStream(f), -1);
		reqEntity.setContentType("binary/octet-stream");
		reqEntity.setChunked(true); 
		httpp.setEntity(reqEntity);

		httpc.execute(httpp);
	}

	public void stop(){

	}

	private void notifyAll(String msg, int total, int remaining){
		for(SyncServiceListener s: observers){
			s.progress(msg, total, remaining);
		}
	}

	public void subsribe(SyncServiceListener s){
		observers.add(s);
	}
}
