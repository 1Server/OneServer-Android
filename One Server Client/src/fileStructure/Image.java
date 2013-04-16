package fileStructure;

import java.util.ArrayList;
import java.util.List;

import org.teleal.cling.support.model.Res;

/**
 * This class represents an image media object
 * @author kuszewskij
 *
 */
public class Image extends MediaObject {

	/**
	 * Basic constructor for an image item. Does not initialize any metadata values that are specific to this media type
	 * @param classType The upnp class type.
	 * @param Id The unique identifier of this object
	 * @param parentId The unique identifier of this objects parent.
	 * @param creator The creator of this image.
	 * @param title The title of this image.
	 * @param resources The resources used to generate this image.
	 */
	public Image(final String classType,String Id, String parentId, final String creator, String title, List<Res> resources){
		super(classType, Id, parentId, creator, title, resources,MediaObject.mediaType.imageItem);
	}
	
	/**
	 * Detailed constructor for this class. Initializes all of the possible values for an image item as defined in the upnp specification
	 * @param classType The upnp class type.
	 * @param Id The unique identifier of this object
	 * @param parentId The unique identifier of this objects parent.
	 * @param creator The creator of this image.
	 * @param title The title of this image.
	 * @param resources The resources used to generate this image.
	 * @param album The albums this image belongs to.
	 * @param date The date of this image.
	 * @param longDescription The long description of this image.
	 * @param storageMedium The storage medium of this image eg. CD, DVD etc...
	 * @param rating The rating of this image.
	 * @param description The description of this image.
	 * @param rights The rights of this image.
	 */
	@SuppressWarnings("serial")
	public Image(final String classType,String Id, String parentId, final String creator, String title, List<Res> resources
			, final String date, final String longDescription, final String storageMedium, final String rating, final String description, ArrayList<String> rights){
		super(classType, Id, parentId, creator, title, resources,MediaObject.mediaType.imageItem);
		super.getMetaData().put(MetaDataTypes.IMAGE_RIGHTS, rights);
		super.getMetaData().put(MetaDataTypes.IMAGE_DATE, new ArrayList<String>(){{add(date);}});
		super.getMetaData().put(MetaDataTypes.IMAGE_DESCRIPTION, new ArrayList<String>(){{add(longDescription);}});
		super.getMetaData().put(MetaDataTypes.IMAGE_STORAGE_MEDIUM, new ArrayList<String>(){{add(storageMedium);}});
		super.getMetaData().put(MetaDataTypes.IMAGE_RATING, new ArrayList<String>(){{add(rating);}});
		super.getMetaData().put(MetaDataTypes.IMAGE_DESCRIPTION, new ArrayList<String>(){{add(description);}});
	}

	/**
	 * Returns the rights this image has.
	 * @return The rights this image has.
	 */
	public String getRights(){
		return this.getValueOfMetaDataType(MetaDataTypes.IMAGE_RIGHTS);
	}
	
	/**
	 * Sets the value for the rights property for this image.
	 * @param rights The rights this image has.
	 */
	public void setRights(ArrayList<String> rights){
		this.getMetaData().put(MetaDataTypes.IMAGE_RIGHTS, rights);
	}
	
	/**
	 * Returns the date of this image.
	 * @return The date of this image.
	 */
	public String getDate(){
		return this.getValueOfMetaDataType(MetaDataTypes.IMAGE_DATE);
	}
	
	/**
	 * Sets the date of this image.
	 * @param date The date of this image belongs.
	 */
	@SuppressWarnings("serial")
	public void setDate(final String date){
		this.getMetaData().put(MetaDataTypes.IMAGE_DATE, new ArrayList<String>(){{add(date);}});
	}
	
	/**
	 * Returns the date of this image.
	 * @return The date of this image.
	 */
	public String getLongDescription(){
		return this.getValueOfMetaDataType(MetaDataTypes.IMAGE_LONG_DESCRIPTION);
	}
	
	/**
	 * Sets the long description of this image.
	 * @param longDescription The long description of this image belongs.
	 */
	@SuppressWarnings("serial")
	public void setLongDescription(final String longDescription){
		this.getMetaData().put(MetaDataTypes.IMAGE_LONG_DESCRIPTION, new ArrayList<String>(){{add(longDescription);}});
	}
	
	/**
	 * Returns the storage medium of this image.
	 * @return The storage medium of this image.
	 */
	public String getStorageMedium(){
		return this.getValueOfMetaDataType(MetaDataTypes.IMAGE_STORAGE_MEDIUM);
	}
	
	/**
	 * Sets the storage medium of this image.
	 * @param storageMedium The storage medium of this image belongs.
	 */
	@SuppressWarnings("serial")
	public void setStorageMedium(final String storageMedium){
		this.getMetaData().put(MetaDataTypes.IMAGE_STORAGE_MEDIUM, new ArrayList<String>(){{add(storageMedium);}});
	}
	
	/**
	 * Returns the rating of this image.
	 * @return The rating of this image.
	 */
	public String getRating(){
		return this.getValueOfMetaDataType(MetaDataTypes.IMAGE_RATING);
	}
	
	/**
	 * Sets the rating of this image.
	 * @param rating The rating of this image.
	 */
	@SuppressWarnings("serial")
	public void setRating(final String rating){
		this.getMetaData().put(MetaDataTypes.IMAGE_RATING, new ArrayList<String>(){{add(rating);}});
	}
	
	/**
	 * Returns the description of this image.
	 * @return The description of this image.
	 */
	public String getDescription(){
		return this.getValueOfMetaDataType(MetaDataTypes.IMAGE_DESCRIPTION);
	}
	
	/**
	 * Sets the description of this image.
	 * @param description The description of this image.
	 */
	@SuppressWarnings("serial")
	public void setDescription(final String description){
		this.getMetaData().put(MetaDataTypes.IMAGE_DESCRIPTION, new ArrayList<String>(){{add(description);}});
	}
	
}
