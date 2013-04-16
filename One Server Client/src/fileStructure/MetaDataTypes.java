package fileStructure;

/**
 * This class defines the string key values for the map in each media object type.
 */
public class MetaDataTypes {
	
	/**
	 * These strings are used by the base mediaObject class
	 */
	public static final String TAGS = "TAGS";
	public static final String CREATOR = "Creator";
	public static final String MEDIA_TYPE = "Type";
	

	/**
	 * These strings are for Audio files
	 */
	public static final String AUDIO_DESCRIPTION = "Audio Description";
	public static final String AUDIO_GENRE = "Audio Genres";
	public static final String AUDIO_LANGUAGE = "Audio Language";
	public static final String AUDIO_LONG_DESCRIPTION = "Audio Long Description";
	public static final String AUDIO_PUBLISHER = "Audio Publisher";
	public static final String AUDIO_RELATION = "Audio Relation";
	public static final String AUDIO_RIGHTS = "Audio Rights";
	

	/**
	 * These strings are for video files
	 */
	public static final String VIDEO_LONG_DESCRIPTION = "Video Long Description";
	public static final String VIDEO_PRODUCER = "Video Producer";
	public static final String VIDEO_RATING = "Video Rating";
	public static final String VIDEO_ACTOR = "Video Actor";
	public static final String VIDEO_DIRECTOR = "Video Director";
	public static final String VIDEO_DESCRIPTION = "Video Description";
	public static final String VIDEO_PUBLISHER = "Video Publisher";
	public static final String VIDEO_LANGUAGE = "Video Language";
	public static final String VIDEO_RELATION = "Video Relation";
	
	/**
	 * These strings are for image files
	 */
	public static final String IMAGE_DATE = "Image Date";
	public static final String IMAGE_LONG_DESCRIPTION = "Image Long Description";
	public static final String IMAGE_STORAGE_MEDIUM = "Image Storage Medium";
	public static final String IMAGE_RATING = "Image Rating";
	public static final String IMAGE_DESCRIPTION = "Image Description";
	public static final String IMAGE_RIGHTS = "Image Rights";
	
	/**
	 * Strings for Photos
	 */
	public static final String PHOTO_ALBUM = "Photo Album";
	
	/**
	 * These Strings are for MusicTracks
	 */
	public static final String MUSICTRACK_ARTIST = "MusicTrack Artist";
	public static final String MUSICTRACK_ALBUM= "MusicTrack Album";
	public static final String MUSICTRACK_ORIGINAL_TRACK_NUMBER = "MusicTrack Original Track Number";
	public static final String MUSICTRACK_PLAYLIST = "MusicTrack Playlist";
	public static final String MUSICTRACK_STORAGE_MEDIUM = "MusicTrack Storage Medium";
	public static final String MUSICTRACK_CONTRIBUTOR = "MusicTrack Contributor";
	public static final String MUSICTRACK_DATE = "MusicTrack Date";
	
	/**
	 * These strings are for folders
	 */
	public static final String FOLDERS_FILE_COUNT = "FOLDERS_FILE_COUNT";
	public static final String FOLDERS_PARENT = "FOLDERS_PARENT";
	public static final String FOLDERITEM_STORAGE_USED = "StorageFolder Storage Used";
	
	/**
	 * These Strings are for playlists
	 */
	public static final String PLAYLIST_ARTIST = "PlayList Artist";
	public static final String PLAYLIST_GENRE = "PlayList Genre";
	public static final String PLAYLIST_LONG_DESCRIPTION = "PlayList Long Description";
	public static final String PLAYLIST_STORAGE_MEDIUM = "PlayList Storage Medium";
	public static final String PLAYLIST_DESCRIPTION = "PlayList Description";
	public static final String PLAYLIST_DATE = "PlayList Date";
	public static final String PLAYLIST_LANGUAGE = "PlayList Languages";
	
	/**
	 * These strings are for text items
	 */
	public static final String TEXTITEM_AUTHOR = "TextITem Authors";
	public static final String TEXTITEM_PROTECTION = "TextITem Protection";
	public static final String TEXTITEM_LONG_DESCRIPTION = "TextITem Long Description";
	public static final String TEXTITEM_STORAGE_MEDIUM = "TextITem Storage Medium";
	public static final String TEXTITEM_RATING = "TextITem Rating";
	public static final String TEXTITEM_DESCRIPTION = "TextITem Description";
	public static final String TEXTITEM_PUBLISHER = "TextITem Publisher";
	public static final String TEXTITEM_CONTRIBUTOR = "TextITem Contributors";
	public static final String TEXTITEM_DATE = "TextITem Date";
	public static final String TEXTITEM_RELATION = "TextITem Relation";
	public static final String TEXTITEM_LANGUAGE = "TextITem Language";
	public static final String TEXTITEM_RIGHTS = "TextITem Rights";
	
	/**
	 * Returns the friendly string value of a given string. Useful for UI purposes. Only works with strings defined in this class.
	 * @param value
	 * @return
	 */
	public static String getFriendlyStringValue(String value){
		String retString = value;
		int index = value.indexOf(' ');
		if(index >= 0){
			retString = value.substring(index+1);
		}
		return retString;
	}
	
}
