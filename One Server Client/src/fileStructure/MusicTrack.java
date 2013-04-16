package fileStructure;

import java.util.ArrayList;
import java.util.List;

import org.teleal.cling.support.model.Res;

/**
 * This class is used to hold the metadata for music track item. This object is an extension of the upnp audioItem class.
 * @author kuszewskij
 *
 */
public class MusicTrack extends Audio{

	/**
	 * Simple constructor for this class. Only adds the minimum object attributes.
	 * @param classType
	 * @param Id
	 * @param parentId
	 * @param creator
	 * @param title
	 * @param resources 
	 */
	public MusicTrack(String classType, String Id, String parentId,
			String creator, String title, List<Res> resources) {
		super(classType, Id, parentId, creator, title, resources);
	}
	
	/**
	 * Detailed constructor for this class. Accepts all the possible values for this type of data.
	 * @param classType The class type of the object
	 * @param Id The unique identifier of this object
	 * @param parentId The unique identifier of this objects parent.
	 * @param creator The creator of this music track
	 * @param title The title of this music track.
	 * @param resources The resources used in this music track.
	 * @param description The description of this music track.
	 * @param genres The genre of this music track.
	 * @param language The language of this music track.
	 * @param longDescription The long description of this music track
	 * @param publishers The publishers of this music track. can have multiple values.
	 * @param relation The relation of this music track.
	 * @param rights The rights of this music track. Can have multiple values.
	 * @param artists The artists of this music track.
	 * @param albums The albums this music track belongs to.
	 * @param originalTrackNumber The original track number of this music track.
	 * @param playlists The play lists this music track belongs to.
	 * @param storageMedium The storage medium of this music track.
	 * @param contributors The contributors of this music track.
	 * @param date The date this music track was created.
	 */
	@SuppressWarnings("serial")
	public MusicTrack(final String classType,String Id, String parentId, final String creator, String title, List<Res> resources
			,final String description, ArrayList<String> genres, final String language, final String longDescription,ArrayList<String> publishers
			,final String relation, ArrayList<String> rights,
			ArrayList<String> artists, ArrayList<String> albums, final String originalTrackNumber, ArrayList<String> playlists,
			final String storageMedium, ArrayList<String> contributors, final String date){
		super(classType, Id,  parentId,  creator,  title, resources, description,  genres,  language,  longDescription, publishers, relation,  rights);
		this.type = MediaObject.mediaType.musicTrack;
		
		super.getMetaData().put(MetaDataTypes.MUSICTRACK_ORIGINAL_TRACK_NUMBER, new ArrayList<String>(){{add(originalTrackNumber);}});
		super.getMetaData().put(MetaDataTypes.MUSICTRACK_STORAGE_MEDIUM, new ArrayList<String>(){{add(storageMedium);}});
		super.getMetaData().put(MetaDataTypes.MUSICTRACK_DATE, new ArrayList<String>(){{add(date);}});

		super.getMetaData().put(MetaDataTypes.MUSICTRACK_ARTIST, artists);
		super.getMetaData().put(MetaDataTypes.MUSICTRACK_ALBUM, albums);
		super.getMetaData().put(MetaDataTypes.MUSICTRACK_PLAYLIST, playlists);
		super.getMetaData().put(MetaDataTypes.MUSICTRACK_CONTRIBUTOR, contributors);
	}
	
	/**
	 * This constructor takes in a MusicTrack object created in the cling library and takes the metadata values we want from it to create a MusicTrack.
	 * @param mt
	 */
	public MusicTrack(org.teleal.cling.support.model.item.MusicTrack mt){
		super(mt.getClazz().getValue(), mt.getId(), mt.getParentID(), mt.getCreator(), mt.getTitle(), mt.getResources());
		this.type = MediaObject.mediaType.musicTrack;
		if(mt.getDescription() != null){
			setDescription(mt.getDescription());
		}
		if(mt.getGenres() != null){
			ArrayList<String> al = new ArrayList<String>();
			for(int c = 0; c < mt.getGenres().length; c++){
				al.add(mt.getGenres()[c]);
			}
			setGenres(al);
		}
		if(mt.getLanguage() != null){
			setLanguage(mt.getLanguage());
		}
		if(mt.getLongDescription() != null){
			setLongDescription(mt.getLongDescription());
		}
		if(mt.getPublishers() != null){
			ArrayList<String> al = new ArrayList<String>();
			for(int c = 0; c < mt.getPublishers().length; c++){
				al.add(mt.getPublishers()[c].getName());
			}
			setPublishers(al);
		}
		if(mt.getRelations() != null){
			ArrayList<String> al = new ArrayList<String>();
			for(int c = 0; c < mt.getRelations().length; c++){
				al.add(mt.getRelations()[c].getPath());
			}
			setRelation(al);
		}
		if(mt.getRights() != null){
			ArrayList<String> al = new ArrayList<String>();
			for(int c = 0; c < mt.getRights().length; c++){
				al.add(mt.getRights()[c]);
			}
			setRights(al);
		}
		if(mt.getArtists() != null){
			ArrayList<String> al = new ArrayList<String>();
			for(int c = 0; c < mt.getArtists().length; c++){
				al.add(mt.getArtists()[c].getName());
			}
			setArtists(al);
		}
		if(mt.getAlbum() != null){
			ArrayList<String> al = new ArrayList<String>();
			al.add(mt.getAlbum());
			setAlbum(al);
		}
		if(mt.getOriginalTrackNumber() != null){
			setMusicTrackNumber(""+mt.getOriginalTrackNumber());
		}
		if(mt.getPlaylists() != null){
			ArrayList<String> al = new ArrayList<String>();
			for(int c = 0; c < mt.getPlaylists().length; c++){
				al.add(mt.getPlaylists()[c]);
			}
			setPlaylist(al);
		}
		if(mt.getStorageMedium() != null){
			setStorageMedium(mt.getStorageMedium().toString());
		}
		if(mt.getContributors() != null){
			ArrayList<String> al = new ArrayList<String>();
			for(int c = 0; c < mt.getContributors().length; c++){
				al.add(mt.getContributors()[c].getName());
			}
			setContributor(al);
		}
		if(mt.getDate() != null){
			setDate(mt.getDate());
		}
	}
	
	/**
	 * Returns the music track number of this music track.
	 * @return The music track number of this music track.
	 */
	public String getMusicTrackNumber(){
		return this.getValueOfMetaDataType(MetaDataTypes.MUSICTRACK_ORIGINAL_TRACK_NUMBER);
	}
	
	/**
	 * Sets the music track number of this video.
	 * @param musicTrackNumber The long description of this video.
	 */
	@SuppressWarnings("serial")
	public void setMusicTrackNumber(final String musicTrackNumber){
		this.getMetaData().put(MetaDataTypes.MUSICTRACK_ORIGINAL_TRACK_NUMBER, new ArrayList<String>(){{add(musicTrackNumber);}});
	}
	
	/**
	 * Returns the storage medium of this music track.
	 * @return The storage medium of this music track.
	 */
	public String getStorageMedium(){
		return this.getValueOfMetaDataType(MetaDataTypes.MUSICTRACK_STORAGE_MEDIUM);
	}
	
	/**
	 * Sets the storage medium property of this video.
	 * @param storageMedium The storage medium of this video.
	 */
	@SuppressWarnings("serial")
	public void setStorageMedium(final String storageMedium){
		this.getMetaData().put(MetaDataTypes.MUSICTRACK_STORAGE_MEDIUM, new ArrayList<String>(){{add(storageMedium);}});
	}
	
	/**
	 * Returns the date of this music track.
	 * @return The date property of this music track.
	 */
	public String getDate(){
		return this.getValueOfMetaDataType(MetaDataTypes.MUSICTRACK_DATE);
	}
	
	/**
	 * Sets the date property of this music track.
	 * @param date The date property of this music track.
	 */
	@SuppressWarnings("serial")
	public void setDate(final String date){
		this.getMetaData().put(MetaDataTypes.MUSICTRACK_DATE, new ArrayList<String>(){{add(date);}});
	}
	
	/**
	 * Returns the artists of this music track.
	 * @return The artists of this music track.
	 */
	public String getArtsts(){
		return this.getValueOfMetaDataType(MetaDataTypes.MUSICTRACK_ARTIST);
	}
	
	/**
	 * Sets the artists property of this music track.
	 * @param artists The producers property of this music track.
	 */
	public void setArtists(ArrayList<String> artists){
		this.getMetaData().put(MetaDataTypes.MUSICTRACK_ARTIST, artists);
	}
	
	/**
	 * Returns the album of this music track.
	 * @return The album of this music track.
	 */
	public String getAlbum(){
		return this.getValueOfMetaDataType(MetaDataTypes.MUSICTRACK_ALBUM);
	}
	
	/**
	 * Sets the album property of this music track.
	 * @param albums The albums property of this music track.
	 */
	public void setAlbum(ArrayList<String> albums){
		this.getMetaData().put(MetaDataTypes.MUSICTRACK_ALBUM, albums);
	}
	
	/**
	 * Returns the play lists of this music track.
	 * @return The play lists of this music track.
	 */
	public String getPlaylist(){
		return this.getValueOfMetaDataType(MetaDataTypes.MUSICTRACK_PLAYLIST);
	}
	
	/**
	 * Sets the play list property of this music track.
	 * @param playlists The playlist property of this music track.
	 */
	public void setPlaylist(ArrayList<String> playlist){
		this.getMetaData().put(MetaDataTypes.MUSICTRACK_PLAYLIST, playlist);
	}

	/**
	 * Returns the contributors of this music track.
	 * @return The contributors of this music track.
	 */
	public String getContributor(){
		return this.getValueOfMetaDataType(MetaDataTypes.MUSICTRACK_CONTRIBUTOR);
	}
	
	/**
	 * Sets the contributor property of this music track.
	 * @param contributors The contributor property of this music track.
	 */
	public void setContributor(ArrayList<String> contributors){
		this.getMetaData().put(MetaDataTypes.MUSICTRACK_CONTRIBUTOR, contributors);
	}
}
