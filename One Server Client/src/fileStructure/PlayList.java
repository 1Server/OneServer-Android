package fileStructure;

import java.util.ArrayList;
import java.util.List;

import org.teleal.cling.support.model.Res;
import org.teleal.cling.support.model.item.PlaylistItem;

/**
 * This class is used to represent the PlayList item defined in the upnp contentdirectory service 1 specification
 * @author kuszewskij
 *
 */
public class PlayList extends MediaObject{

	/**
	 * Basic constructor for this class. Only adds the base elements to the item.
	 * @param classType The upnp class type.
	 * @param Id The unique identifier of this object
	 * @param parentId The unique identifier of this objects parent.
	 * @param creator The creator of this PlayList.
	 * @param title The title of this PlayList.
	 * @param resources The resources used to generate this PlayList.
	 */
	public PlayList(String classType, String Id, String parentId,
			String creator, String title, List<Res> resources) {
		super(classType, Id, parentId, creator, title, resources,MediaObject.mediaType.playlist);
	}

	/**
	 * Detailed constructor. Adds all the possible meta data values to this object.
	 * @param classType The upnp class type.
	 * @param Id The unique identifier of this object
	 * @param parentId The unique identifier of this objects parent.
	 * @param creator The creator of this PlayList.
	 * @param title The title of this PlayList.
	 * @param resources The resources used to generate this PlayList.
	 * @param artists The artists of the media objects contained in this playlist.
	 * @param genres The genres of all the media objects contained within this playlist.
	 * @param longDescription The long description of this playlist.
	 * @param storageMedium The storageMedium of this playlist.
	 * @param description The description of this playlist.
	 * @param date The date this playlist was created.
	 * @param languages The languages of the media objects contained within this playlist.
	 */
	@SuppressWarnings("serial")
	public PlayList(String classType, String Id, String parentId,
			String creator, String title, List<Res> resources,
			ArrayList<String> artists, ArrayList<String> genres, final String longDescription, final String storageMedium,
			final String description, final String date, ArrayList<String> languages) {
		super(classType, Id, parentId, creator, title, resources,MediaObject.mediaType.playlist);

		super.getMetaData().put(MetaDataTypes.PLAYLIST_LONG_DESCRIPTION, new ArrayList<String>(){{add(longDescription);}});
		super.getMetaData().put(MetaDataTypes.PLAYLIST_STORAGE_MEDIUM, new ArrayList<String>(){{add(storageMedium);}});
		super.getMetaData().put(MetaDataTypes.PLAYLIST_DESCRIPTION, new ArrayList<String>(){{add(description);}});
		super.getMetaData().put(MetaDataTypes.PLAYLIST_DATE, new ArrayList<String>(){{add(date);}});

		super.getMetaData().put(MetaDataTypes.PLAYLIST_ARTIST, artists);
		super.getMetaData().put(MetaDataTypes.PLAYLIST_GENRE, genres);
		super.getMetaData().put(MetaDataTypes.PLAYLIST_LANGUAGE, languages);
	}

	public PlayList(PlaylistItem p) {
		super(p.getClazz().getValue(), p.getId(), p.getParentID(), p.getCreator(), p.getTitle(), p.getResources(), MediaObject.mediaType.playlist);

		//set artists
		if(p.getArtists() != null){
			ArrayList<String> artists = new ArrayList<String>();
			for(int i = 0; i < p.getArtists().length; i++){
				artists.add(p.getArtists()[i].getName());
			}
			setArtists(artists);
		}
		
		//set genres
		if(p.getGenres() != null){
			ArrayList<String> genres = new ArrayList<String>();
			for(int i = 0; i < p.getGenres().length; i++){
				genres.add(p.getGenres()[i]);
			}
			setGenres(genres);
		}
		
		//set the long description
		if(p.getLongDescription() != null){
			setLongDescription(p.getLongDescription());
		}
		
		//set the storage medium of the playlist
		if(p.getStorageMedium().name()!=null){
			setStorageMedium(p.getStorageMedium().name());
		}
		
		//Set the description of the playlist
		if(p.getDescription() != null){
			setDescription(p.getDescription());
		}
		
		//set the date of the playlist
		if(p.getDate() != null){
			setDate(p.getDate());
		}
		
		//set the languages used in the playlist.
		if(p.getLanguage() != null){
			ArrayList<String> languages = new ArrayList<String>();
			languages.add(p.getLanguage());
			setLanguage(languages);
		}
	}

	/**
	 * Returns the artists of this playlist.
	 * @return The artists of this playlist.
	 */
	public String getArtsts(){
		return this.getValueOfMetaDataType(MetaDataTypes.PLAYLIST_ARTIST);
	}

	/**
	 * Sets the artists property of this playlist.
	 * @param artists The producers property of this playlist.
	 */
	public void setArtists(ArrayList<String> artists){
		this.getMetaData().put(MetaDataTypes.PLAYLIST_ARTIST, artists);
	}

	/**
	 * Returns the meta data containing the description of the playlist 
	 * @return A String containing the description of the playlist
	 */
	public String getGenres(){
		return this.getValueOfMetaDataType(MetaDataTypes.PLAYLIST_GENRE);
	}

	/**
	 * Sets the Genres of the playlist.
	 * @param genres The genres of the playlist.
	 */
	public void setGenres(ArrayList<String> genres){
		this.getMetaData().put(MetaDataTypes.PLAYLIST_GENRE, genres);
	}

	/**
	 * Returns the meta data containing the language of the playlist 
	 * @return A String containing the language of the playlist
	 */
	public String getLongDescription(){
		return this.getValueOfMetaDataType(MetaDataTypes.PLAYLIST_LONG_DESCRIPTION);
	}

	/**
	 * Sets the description of the playlist.
	 * @param longDescription The description of the playlist.
	 */
	@SuppressWarnings("serial")
	public void setLongDescription(final String longDescription){
		this.getMetaData().put(MetaDataTypes.PLAYLIST_LONG_DESCRIPTION, new ArrayList<String>(){{add(longDescription);}});
	}

	/**
	 * Returns the storage medium of this playlist.
	 * @return The storage medium of this playlist.
	 */
	public String getStorageMedium(){
		return this.getValueOfMetaDataType(MetaDataTypes.PLAYLIST_STORAGE_MEDIUM);
	}

	/**
	 * Sets the storage medium of this playlist.
	 * @param storageMedium The storage medium of this playlist belongs.
	 */
	@SuppressWarnings("serial")
	public void setStorageMedium(final String storageMedium){
		this.getMetaData().put(MetaDataTypes.PLAYLIST_STORAGE_MEDIUM, new ArrayList<String>(){{add(storageMedium);}});
	}

	/**
	 * Returns the description of this playlist.
	 * @return The description of this playlist.
	 */
	public String getDescription(){
		return this.getValueOfMetaDataType(MetaDataTypes.PLAYLIST_DESCRIPTION);
	}

	/**
	 * Sets the description of this playlist.
	 * @param description The description of this playlist.
	 */
	@SuppressWarnings("serial")
	public void setDescription(final String description){
		this.getMetaData().put(MetaDataTypes.PLAYLIST_DESCRIPTION, new ArrayList<String>(){{add(description);}});
	}

	/**
	 * Returns the date of this playlist.
	 * @return The date of this playlist.
	 */
	public String getDate(){
		return this.getValueOfMetaDataType(MetaDataTypes.PLAYLIST_DATE);
	}

	/**
	 * Sets the date of this playlist.
	 * @param date The date of this playlist belongs.
	 */
	@SuppressWarnings("serial")
	public void setDate(final String date){
		this.getMetaData().put(MetaDataTypes.PLAYLIST_DATE, new ArrayList<String>(){{add(date);}});
	}

	/**
	 * Returns the meta data containing the language of the audio file 
	 * @return A String containing the language of the audio file
	 */
	public String getLanguage(){
		return this.getValueOfMetaDataType(MetaDataTypes.PLAYLIST_LANGUAGE);
	}

	/**
	 * Sets the languages of the Audio Item.
	 * @param languages The language of the Audio Item.
	 */
	public void setLanguage(ArrayList<String> languages){
		this.getMetaData().put(MetaDataTypes.PLAYLIST_LANGUAGE, languages);
	}
}
