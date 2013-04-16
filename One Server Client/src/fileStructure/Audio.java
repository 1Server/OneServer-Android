package fileStructure;

import java.util.ArrayList;
import java.util.List;

import org.teleal.cling.support.model.Res;

/**
 * This class represents an Audio media object as defined by the upnp specification.
 * @author kuszewskij
 *
 */
public class Audio extends MediaObject {

	
	/**
	 * Detailed constructor for this class. 
	 * @param classType The class type of the object
	 * @param Id The unique identifier of this object
	 * @param parentId The unique identifier of this objects parent.
	 * @param creator The creator of this audio item
	 * @param title The title of this audio Item.
	 * @param resources The resources used in this audio item.
	 * @param description The description of this audio item.
	 * @param genres The genre of this audio item.
	 * @param language The language of this audio item.
	 * @param longDescription The long description of this audio item
	 * @param publishers The publishers of this audio item. can have multiple values.
	 * @param relation The relation of this audio item.
	 * @param rights The rights of this audio item. Can have multiple values.
	 */
	@SuppressWarnings("serial")
	public Audio(final String classType,String Id, String parentId, final String creator, String title, List<Res> resources
			,final String description, ArrayList<String> genres, final String language, final String longDescription,ArrayList<String> publishers
			,final String relation, ArrayList<String> rights){
		super(classType, Id, parentId, creator, title, resources,MediaObject.mediaType.audioItem);
		super.getMetaData().put(MetaDataTypes.AUDIO_DESCRIPTION, new ArrayList<String>(){{add(description);}});
		super.getMetaData().put(MetaDataTypes.AUDIO_GENRE, genres);
		super.getMetaData().put(MetaDataTypes.AUDIO_LANGUAGE, new ArrayList<String>(){{add(language);}});
		super.getMetaData().put(MetaDataTypes.AUDIO_LONG_DESCRIPTION, new ArrayList<String>(){{add(longDescription);}});
		super.getMetaData().put(MetaDataTypes.AUDIO_PUBLISHER, publishers);
		super.getMetaData().put(MetaDataTypes.AUDIO_RELATION, new ArrayList<String>(){{add(relation);}});
		super.getMetaData().put(MetaDataTypes.AUDIO_RIGHTS, rights);
		
	}
	
	/**
	 * Default constructor. Doesn't add any specific metadata to audio type/
	 * @param classType The class type of the object
	 * @param Id The unique identifier of this object
	 * @param parentId The unique identifier of this objects parent.
	 * @param creator The creator of this audio item
	 * @param title The title of this audio Item.
	 * @param resources The resources used in this audio item.
	 */
	public Audio(final String classType,String Id, String parentId, final String creator, String title, List<Res> resources){
		super(classType, Id, parentId, creator, title, resources,MediaObject.mediaType.audioItem);
	}

	/**
	 * Returns the meta data containing the description of the audio file 
	 * @return A String containing the description of the audio file
	 */
	public String getDescription(){
		return this.getValueOfMetaDataType(MetaDataTypes.AUDIO_DESCRIPTION);
	}
	
	/**
	 * Sets the description of the Audio Item.
	 * @param description The description of the Audio Item.
	 */
	@SuppressWarnings("serial")
	public void setDescription(final String description){
		this.getMetaData().put(MetaDataTypes.AUDIO_DESCRIPTION, new ArrayList<String>(){{add(description);}});
	}
	
	/**
	 * Returns the meta data containing the description of the audio file 
	 * @return A String containing the description of the audio file
	 */
	public String getGenres(){
		return this.getValueOfMetaDataType(MetaDataTypes.AUDIO_GENRE);
	}
	
	/**
	 * Sets the Genres of the Audio Item.
	 * @param genres The genres of the Audio Item.
	 */
	public void setGenres(ArrayList<String> genres){
		this.getMetaData().put(MetaDataTypes.AUDIO_GENRE, genres);
	}
	
	/**
	 * Returns the meta data containing the language of the audio file 
	 * @return A String containing the language of the audio file
	 */
	public String getLanguage(){
		return this.getValueOfMetaDataType(MetaDataTypes.AUDIO_LANGUAGE);
	}
	
	/**
	 * Sets the language of the Audio Item.
	 * @param language The language of the Audio Item.
	 */
	@SuppressWarnings("serial")
	public void setLanguage(final String language){
		this.getMetaData().put(MetaDataTypes.AUDIO_LANGUAGE, new ArrayList<String>(){{add(language);}});
	}

	/**
	 * Returns the meta data containing the language of the audio file 
	 * @return A String containing the language of the audio file
	 */
	public String getLongDescription(){
		return this.getValueOfMetaDataType(MetaDataTypes.AUDIO_LONG_DESCRIPTION);
	}
	
	/**
	 * Sets the description of the Audio Item.
	 * @param longDescription The description of the Audio Item.
	 */
	@SuppressWarnings("serial")
	public void setLongDescription(final String longDescription){
		this.getMetaData().put(MetaDataTypes.AUDIO_LONG_DESCRIPTION, new ArrayList<String>(){{add(longDescription);}});
	}
	
	/**
	 * Returns the meta data containing the publishers of the audio file 
	 * @return A String containing the description of the audio file
	 */
	public String getPublishers(){
		return this.getValueOfMetaDataType(MetaDataTypes.AUDIO_PUBLISHER);
	}
	
	/**
	 * Sets the publishers of the Audio Item.
	 * @param publishers The publishers of the Audio Item.
	 */
	public void setPublishers(ArrayList<String> publishers){
		this.getMetaData().put(MetaDataTypes.AUDIO_PUBLISHER, publishers);
	}
	
	/**
	 * Returns the relation of this audio item.
	 * @return the relation of the audio item.
	 */
	public String getRelation(){
		return this.getValueOfMetaDataType(MetaDataTypes.AUDIO_RELATION);
	}
	
	/**
	 * Sets the description of the Audio Item.
	 * @param description The description of the Audio Item.
	 */
	public void setRelation(final ArrayList<String> al){
		this.getMetaData().put(MetaDataTypes.AUDIO_RELATION, al);
	}
	
	/**
	 * Returns the rights of this audio item.
	 * @return The rights of this audio item.
	 */
	public String getRights(){
		return this.getValueOfMetaDataType(MetaDataTypes.AUDIO_RIGHTS);
	}
	
	/**
	 * Sets the description of the Audio Item.
	 * @param description The description of the Audio Item.
	 */
	public void setRights(ArrayList<String> rights){
		this.getMetaData().put(MetaDataTypes.AUDIO_RIGHTS, rights);
	}
}
