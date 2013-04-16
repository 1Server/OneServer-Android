package helpers;

import fileStructure.MediaObject;
import fileStructure.MusicTrack;
import fileStructure.Photo;
import fileStructure.TextItem;
import fileStructure.Video;

public class ItemToDIDLConverter {

	/**
	 * The header for the item in string form
	 */
	private static final String HEADER = 
			"<DIDL-Lite xmlns:dc=\"http://purl.org/dc/elements/1.1/\"" +
					"xmlns:upnp=\"urn:schemas-upnp-org:metadata-1-0/upnp/\"" +
					"xmlns=\"urn:schemas-upnp-org:metadata-1-0/DIDL-Lite\">" +
					"<item id=\"\" restricted=\"false\">";
	
	/**
	 * The end of the item in string form.
	 */
	private static final String CLOSER = "</DIDL-Lite>";
	
	/**
	 * Creates a representation of a media object in DIDL-Lite form.
	 * @param mo
	 * @return
	 */
	public static String getDIDLString(MediaObject mo){
		String retString = HEADER;
		
		//place the title
		retString += "<dc:title>"+mo.getTitle()+"</dc:title>";
		
		//place the class
		retString += "<upnp:class>"+mo.getClassType()+"</upnp:class>";
		
		//add the specific meta data values depending on the type of media.
		switch(mo.getMediaType()){
		case Photo:
			retString += "<upnp:longDescription>"+((Photo)mo).getLongDescription()+"</upnp:longDescription>";
			retString += "<upnp:storageMedium>"+((Photo)mo).getStorageMedium()+"</upnp:storageMedium>";
			retString += "<upnp:rating>"+((Photo)mo).getRating()+"</upnp:rating>";
			retString += "<dc:description>"+((Photo)mo).getDescription()+"</dc:description>";
			retString += "<dc:date>"+((Photo)mo).getDate()+"</dc:date>";
			retString += "<dc:rights>"+((Photo)mo).getRights()+"</dc:rights>";
			retString += "<upnp:album>"+((Photo)mo).getAlbum()+"</upnp:album>";
			break;
		case musicTrack:
			//audioItem attributes
			retString += "<upnp:genre>"+((MusicTrack)mo).getGenres()+"</upnp:genre>";
			retString += "<dc:description>"+((MusicTrack)mo).getDescription()+"</dc:description>";
			retString += "<upnp:longDescription>"+((MusicTrack)mo).getLongDescription()+"</upnp:longDescription>";
			retString += "<dc:publisher>"+((MusicTrack)mo).getPublishers()+"</dc:publisher>";
			retString += "<dc:language>"+((MusicTrack)mo).getLanguage()+"</dc:language>";
			retString += "<dc:relation>"+((MusicTrack)mo).getRelation()+"</dc:relation>";
			retString += "<dc:rights>"+((MusicTrack)mo).getRights()+"</dc:rights>";
			
			//musicTrack attributes
			retString += "<upnp:artist>"+((MusicTrack)mo).getArtsts()+"</upnp:artist>";
			retString += "<upnp:album>"+((MusicTrack)mo).getAlbum()+"</upnp:album>";
			retString += "<upnp:originalTrackNumber>"+((MusicTrack)mo).getMusicTrackNumber()+"</upnp:originalTrackNumber>";
			retString += "<upnp:playlist>"+((MusicTrack)mo).getPlaylist()+"</upnp:playlist>";
			retString += "<upnp:storageMedium>"+((MusicTrack)mo).getStorageMedium()+"</upnp:storageMedium>";
			retString += "<dc:contributor>"+((MusicTrack)mo).getContributor()+"</dc:contributor>";
			retString += "<dc:date>"+((MusicTrack)mo).getDate()+"</dc:date>";
			break;
		case videoItem:
			retString += "<upnp:longDescription>"+((Video)mo).getLongDescription()+"</upnp:longDescription>";
			retString += "<upnp:producer>"+((Video)mo).getProducers()+"</upnp:producer>";
			retString += "<upnp:rating>"+((Video)mo).getRating()+"</upnp:rating>";
			retString += "<upnp:actor>"+((Video)mo).getActors()+"</upnp:actor>";
			retString += "<upnp:director>"+((Video)mo).getDirectors()+"</upnp:director>";
			retString += "<dc:description>"+((Video)mo).getDescription()+"</dc:description>";
			retString += "<dc:publisher>"+((Video)mo).getPublishers()+"</dc:publisher>";
			retString += "<dc:language>"+((Video)mo).getLanguage()+"</dc:language>";
			retString += "<dc:relation>"+((Video)mo).getRelation()+"</dc:relation>";
			break;
		case textItem:
			retString += "<upnp:author>"+((TextItem)mo).getAuthor()+"</upnp:author>";
			retString += "<upnp:protection>"+((TextItem)mo).getProtection()+"</upnp:protection>";
			retString += "<upnp:longDescription>"+((TextItem)mo).getLongDescription()+"</upnp:longDescription>";
			retString += "<upnp:storageMedium>"+((TextItem)mo).getStorageMedium()+"</upnp:storageMedium>";
			retString += "<upnp:rating>"+((TextItem)mo).getRating()+"</upnp:rating>";

			retString += "<dc:description>"+((TextItem)mo).getDescription()+"</dc:description>";
			retString += "<dc:publisher>"+((TextItem)mo).getPublisher()+"</dc:publisher>";
			retString += "<dc:contributor>"+((TextItem)mo).getContributor()+"</dc:contributor>";
			retString += "<dc:date>"+((TextItem)mo).getDate()+"</dc:date>";
			retString += "<dc:relation>"+((TextItem)mo).getRelations()+"</dc:relation>";
			retString += "<dc:language>"+((TextItem)mo).getLanguages()+"</dc:language>";
			retString += "<dc:rights>"+((TextItem)mo).getRights()+"</dc:rights>";
			break;
		}
		retString += CLOSER;
		return retString;
	}
}
