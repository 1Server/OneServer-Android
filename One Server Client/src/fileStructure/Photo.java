package fileStructure;

import java.util.ArrayList;
import java.util.List;

import org.teleal.cling.support.model.Res;

/**
 * This class represents the Photo object defined by the upup specification.
 * @author kuszewskij
 *
 */
public class Photo extends Image{

	/**
	 * Basic constructor for a photo item. Does not initialize any metadata values that are specific to this media type
	 * @param classType The upnp class type.
	 * @param Id The unique identifier of this object
	 * @param parentId The unique identifier of this objects parent.
	 * @param creator The creator of this image.
	 * @param title The title of this image.
	 * @param resources The resources used to generate this image.
	 */
	public Photo(String classType, String Id, String parentId, String creator,
			String title, List<Res> resources) {
		super(classType, Id, parentId, creator, title, resources);
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
	public Photo(final String classType,String Id, String parentId, final String creator, String title, List<Res> resources
			,ArrayList<String> album, final String date, final String longDescription, final String storageMedium, final String rating, final String description, ArrayList<String> rights){
		super(classType, Id, parentId, creator, title, resources,date,longDescription,storageMedium,rating,description, rights);
		this.type = MediaObject.mediaType.Photo;
		super.getMetaData().put(MetaDataTypes.PHOTO_ALBUM, album);
	}
	
	/**
	 * Creates a Photo Media Object from a cling media object
	 * @param p The photo item containing meta data created by cling to create the Photo from.
	 */
	public Photo(org.teleal.cling.support.model.item.Photo p){
		super(p.getClazz().getValue(), p.getId(), p.getParentID(), p.getCreator(), p.getTitle(), p.getResources());
		this.type = MediaObject.mediaType.Photo;
		if(p.getAlbum() != null){
			ArrayList<String> al = new ArrayList<String>();
			al.add(p.getAlbum());
			setAlbum(al);
		}
		if(p.getDate()!=null){
			setDate(p.getDate());
		}
		if(p.getLongDescription()!=null){
			setLongDescription(p.getLongDescription());
		}
		if(p.getStorageMedium()!=null){
			setStorageMedium(p.getStorageMedium().toString());
		}
		if(p.getRating()!=null){
			setRating(p.getRating());
		}
		if(p.getDescription()!=null){
			setDescription(p.getDescription());
		}
		if(p.getRights()!=null){
			ArrayList<String> al = new ArrayList<String>();
			for(int i = 0; i < p.getRights().length;i++){
				al.add(p.getRights()[i]);
			}
			setRights(al);
		}
		
	}
	
	/**
	 * Returns the albums this image belongs to.
	 * @return The albums this image belongs to.
	 */
	public String getAlbum(){
		return this.getValueOfMetaDataType(MetaDataTypes.PHOTO_ALBUM);
	}
	
	/**
	 * Sets the value for the album property for this image.
	 * @param albums The albums this image belongs to.
	 */
	public void setAlbum(ArrayList<String> albums){
		this.getMetaData().put(MetaDataTypes.PHOTO_ALBUM, albums);
	}
}
