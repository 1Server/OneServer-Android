package fileStructure;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.teleal.cling.support.model.Res;
import org.teleal.cling.support.model.item.Item;
import org.teleal.cling.support.model.item.VideoItem;

import android.media.MediaMetadataRetriever;
import android.util.Log;
import android.webkit.MimeTypeMap;

/**
 * This class will represent a media object such as a video, picture, or image. It will contain all of its meta data, and the location of the actual media.
 * @author kuszewskij
 *
 */
public abstract class MediaObject {

	/**
	 * this is the class type of the mediaobject as defined by the upnp spec.
	 */
	private String classType;

	/**
	 * This is the Creator of the media
	 */
	private String creator;

	/**
	 * This string is used to uniquely identify this piece of media on the media server.
	 */
	private String Id;

	/**
	 * This is the unique Id of the media objects parent.
	 */
	private String parentId;

	/**
	 * This is the title of the media object
	 */
	private String title;

	/**
	 * This is a collection of resources that is used to get the media eg. stream audio or video
	 */
	private List<Res> resources;

	/**
	 * This is the collection of meta data that the media object has. 
	 */
	protected HashMap<String,ArrayList<String>> metaData;

	/**
	 * This is the type of media this media object represents.
	 */
	protected mediaType type;

	/**
	 * True if the media object is cached, false is it is located on the media server.
	 */
	private boolean isCached = false;

	/**
	 * True if this media object is only available to users with a password.
	 */
	private boolean isProtected = false;
	
	/**
	 * Indicates whether this media object is selected or not.
	 */
	public boolean isSelected = false;


	/**
	 * The different types of media this media object can represent.
	 * @author kuszewskij
	 *
	 */
	public enum mediaType{
		videoItem,musicTrack,playlist,textItem,Photo,StorageFolder,audioItem,imageItem
	};

	/**
	 * Constructor for creating a media Object based on the types of metaData defined in the upnp specification.
	 * @param classType
	 * @param Id
	 * @param parentId
	 * @param creator
	 * @param title
	 * @param resources
	 */
	@SuppressWarnings("serial")
	public MediaObject(final String classType,String Id, String parentId, final String creator, String title, List<Res> resources,mediaType type){
		this.classType = classType;
		this.Id = Id;
		this.parentId = parentId;
		this.creator = creator;
		this.title = title;
		this.resources = resources;
		this.metaData = new HashMap<String, ArrayList<String>>();
		this.type = type;
		metaData.put(MetaDataTypes.CREATOR, new ArrayList<String>(){{add(creator);}});
		metaData.put(MetaDataTypes.MEDIA_TYPE, new ArrayList<String>(){{add(classType);}});
	}


	private static mediaType getMediaTypeFromExtension(String path){
		mediaType typeRet = null;

		String type = null;
		String extension = MimeTypeMap.getFileExtensionFromUrl(path);

		//get the type
		if (extension != null) {
			MimeTypeMap mime = MimeTypeMap.getSingleton();
			type = mime.getMimeTypeFromExtension(extension);//try getting the mimie type from the MimeTypeMAp.
			if(type == null){//if it doesnt work then try using MediaMetadataRetriever
				try{
					MediaMetadataRetriever mmdr = new MediaMetadataRetriever();
					mmdr.setDataSource(path);
					type = mmdr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_MIMETYPE);
				}catch(RuntimeException e){
					throw new IllegalArgumentException("Couldnt create object from given file");
				}
			}
			if(type == null){
				throw new IllegalArgumentException("Couldnt create object from given file");
			}
			int index = type.indexOf('/');
			if(index >= 0){
				type = type.substring(0,type.indexOf('/'));
			}
		}

		//Photo
		if(type.equals("image")){
			typeRet = mediaType.Photo;
		}//Audio
		else if(type.equals("audio")){
			typeRet = mediaType.musicTrack;
		}//Video
		else if(type.equals("video")){
			typeRet = mediaType.videoItem;
		}
		else if(type.equals("text")){
			typeRet = mediaType.textItem;
		}else{
			throw new IllegalArgumentException("Couldnt create object from given file");
		}

		return typeRet;
	}

	/**
	 * Will create a new Media object from a given file
	 * @param file The file to create the object from
	 * @return A media Object that represents the file.
	 */
	public static MediaObject createMediaObject(File file){
		MediaObject mo = null;

		//if null then we cant make a media object out of it.
		if(file == null  ){
			return null;
		}

		//check if its a directory
		if(file.isDirectory()){
			Log.v("Media Object", "Creating folder from file: "+file.getName());
			mo = new Folder(file.getAbsolutePath(), null, null, file.getName(), null);
		}else{

			//otherwise its media

			//get the type of media it is.
			mediaType mt = null;
			try{
				mt = MediaObject.getMediaTypeFromExtension(file.getPath());
			}catch(IllegalArgumentException e){
				Log.e("Media Object", e.getLocalizedMessage());
				return null;
			}

			if(mt != null){

				MediaMetadataRetriever mmdr;
				switch(mt){

				case musicTrack:
				case audioItem:
					mmdr = new MediaMetadataRetriever();
					mmdr.setDataSource(file.getPath());
					mo = new MusicTrack("object.item.audioItem.musicTrack", file.getAbsolutePath(), "","", mmdr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_TITLE), null);
					

					ArrayList<String> albums = new ArrayList<String>();
					albums.add(mmdr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ALBUM));
					((MusicTrack)mo).setAlbum(albums);

					ArrayList<String> artists = new ArrayList<String>();
					artists.add(mmdr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ARTIST));
					((MusicTrack)mo).setArtists(artists);

					((MusicTrack)mo).setDate(mmdr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DATE));

					ArrayList<String> genres = new ArrayList<String>();
					genres.add(mmdr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_GENRE));
					((MusicTrack)mo).setGenres(genres);
					break;
				case videoItem:
					mmdr = new MediaMetadataRetriever();
					mmdr.setDataSource(file.getPath());
					mo = new Video("object.item.audioItem.videoItem", file.getAbsolutePath(), "","", mmdr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_TITLE), null);
					break;

				case Photo:
					mo = new Photo("object.item.audioItem.photo", file.getAbsolutePath(), "","", file.getName(), null);
					break;
				case textItem:
					mo = new TextItem("object.item.audioItem.testItem", file.getAbsolutePath(), "","", file.getName(), null);
					break;
				}
			}


		}
		return mo;
	}

	/**
	 * Creates a media object from an Item.
	 * @param i The item.
	 * @return The media object.
	 */
	public static MediaObject createMediaObject(Item i){
		MediaObject mo = null;
		if(i.getClazz().getValue().equals("object.item.audioItem.musicTrack")){
			mo = new MusicTrack((org.teleal.cling.support.model.item.MusicTrack) i);
		}else

			//creating photo
			if(i.getClazz().getValue().equals("object.item.imageItem.photo")){
				mo = new Photo((org.teleal.cling.support.model.item.Photo) i);
			}else

				//creating video
				if(i.getClazz().getValue().equals("object.item.videoItem") || i.getClazz().getValue().equals("object.item.videoItem.movie")){
					mo = new Video((VideoItem) i);
				}else

					//creating text item
					if(i.getClazz().getValue().equals("object.item.textItem")){
						mo = new TextItem((org.teleal.cling.support.model.item.TextItem) i);
					}else

						//creating playlistItem
						if(i.getClazz().getValue().equals("object.item.playlistItem")){
							mo = new PlayList((org.teleal.cling.support.model.item.PlaylistItem) i);
						}else{
							Log.e("Media Object", i.getClazz().getValue()+" is not yet supported.");
						}
		return mo;
	}

	/**
	 * Helper method used to retrieve a string representation of a metadata value.
	 * @param type
	 * @return
	 */
	protected String getValueOfMetaDataType(String type){
		String retString = null;
		if(metaData.get(type).size() >0){
			for(int i = 0; i < metaData.get(type).size(); i++){
				if(i != 0){
					retString += ", ";
				}
				retString += metaData.get(type).get(i);
			}
		}else{
			retString = "Unknown";
		}
		return retString;
	}

	/**
	 * Helper method for getting a string value of all the metadata values for an arbitrary ArrayList
	 * @param s The ArrayList of meta data values in string form
	 * @return The string representation of all the metadata values separated by commas.
	 */
	public static String getValueOfMetaDataType(ArrayList<String> s){
		String retString = null;
		if(s.size() >0){
			for(int i = 0; i < s.size(); i++){
				if(i != 0){
					retString += ", ";
				}
				retString += s.get(i);
			}
		}else{
			retString = "Unknown";
		}
		return retString;
	}

	/**
	 * Returns the name of this media object
	 * @return
	 */
	public mediaType getMediaType(){
		return getType();
	}



	/**
	 * Returns true if this media object is protected.
	 * @return
	 */
	public boolean isProtected() {
		return isProtected;
	}

	/**
	 * Sets whether or not the media object is protected
	 * @param isProtected
	 */
	public void setProtected(boolean isProtected) {
		this.isProtected = isProtected;
		//TODO: Synch with server?
	}

	/**
	 * 
	 * @return true if it is cached, false otherwise. 
	 */
	public boolean isCached() {
		return isCached;
	}

	/**
	 * Sets whether or not this media object is cached on the server.
	 * @param isCached
	 */
	public void setCached(boolean isCached) {
		this.isCached = isCached;
	}

	/**
	 * Returns a map containing the meta data for the media object as strings. 
	 * @return
	 */
	public HashMap<String, ArrayList<String>> getMetaData() {
		return metaData;
	}

	public void setMetadata(HashMap<String, ArrayList<String>> map){
		this.metaData = map;
	}

	public mediaType getType() {
		return type;
	}

	public String getId() {
		return Id;
	}

	public String getParentId() {
		return parentId;
	}

	public String getTitle() {
		return title;
	}

	public List<Res> getResources() {
		return resources;
	}

	public String getClassType() {
		return classType;
	}

	public String getCreator() {
		return creator;
	}
}
