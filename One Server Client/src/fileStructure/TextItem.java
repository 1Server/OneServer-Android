package fileStructure;

import java.util.ArrayList;
import java.util.List;

import org.teleal.cling.support.model.Res;

/**
 * This class is an implementation of the textItem object found in the content directory 1 specification.
 * @author kuszewskij
 *
 */
public class TextItem extends MediaObject{

	/**
	 * Basic constructor for a text item. Only adds meta data values that belong to the subset of the base mediaObject class.
	 * @param classType The upnp class type.
	 * @param Id The unique identifier of this object
	 * @param parentId The unique identifier of this objects parent.
	 * @param creator The creator of this text item.
	 * @param title The title of this text item.
	 * @param resources The resources used to generate this text item.
	 */
	public TextItem(String classType, String Id, String parentId,
			String creator, String title, List<Res> resources){
		super(classType, Id, parentId, creator, title, resources,MediaObject.mediaType.textItem);
	}
	
	/**
	 * Detailed constructor for this class. Adds all possible meta data values for a text item.
	 * @param classType The upnp class type.
	 * @param Id The unique identifier of this object
	 * @param parentId The unique identifier of this objects parent.
	 * @param creator The creator of this text item.
	 * @param title The title of this text item.
	 * @param resources The resources used to generate this text item.
	 * @param authors The authors of this text item.
	 * @param publishers The publishers of this text item.
	 * @param contributors The contributors of this text item.
	 * @param relations The relations of this text item.
	 * @param language The languages used in this text item.
	 * @param rights The rights of this text item.
	 * @param protection The protection value of this text item.
	 * @param longDescription The long description of this text item.
	 * @param storageMedium The storage medium of this text item.
	 * @param rating The rating of this text item.
	 * @param description The description of this text item.
	 * @param date The date of this text item.
	 */
	@SuppressWarnings("serial")
	public TextItem(String classType, String Id, String parentId,
			String creator, String title, List<Res> resources,
			ArrayList<String> authors, ArrayList<String> publishers, ArrayList<String> contributors, ArrayList<String> relations, ArrayList<String> language,
			ArrayList<String> rights, final String protection, final String longDescription, final String storageMedium, final String rating,
			final String description, final String date) {
		super(classType, Id, parentId, creator, title, resources,MediaObject.mediaType.textItem);
		

		super.getMetaData().put(MetaDataTypes.TEXTITEM_AUTHOR, authors);
		super.getMetaData().put(MetaDataTypes.TEXTITEM_PUBLISHER, publishers);
		super.getMetaData().put(MetaDataTypes.TEXTITEM_CONTRIBUTOR, contributors);
		super.getMetaData().put(MetaDataTypes.TEXTITEM_RELATION, relations);
		super.getMetaData().put(MetaDataTypes.TEXTITEM_LANGUAGE, language);
		super.getMetaData().put(MetaDataTypes.TEXTITEM_RIGHTS, rights);
		
		super.getMetaData().put(MetaDataTypes.TEXTITEM_PROTECTION, new ArrayList<String>(){{add(protection);}});
		super.getMetaData().put(MetaDataTypes.TEXTITEM_LONG_DESCRIPTION, new ArrayList<String>(){{add(longDescription);}});
		super.getMetaData().put(MetaDataTypes.TEXTITEM_STORAGE_MEDIUM, new ArrayList<String>(){{add(storageMedium);}});
		super.getMetaData().put(MetaDataTypes.TEXTITEM_RATING, new ArrayList<String>(){{add(rating);}});
		super.getMetaData().put(MetaDataTypes.TEXTITEM_DESCRIPTION, new ArrayList<String>(){{add(description);}});
		super.getMetaData().put(MetaDataTypes.TEXTITEM_DATE, new ArrayList<String>(){{add(date);}});
	}
	
	/**
	 * Creates a TextItem from a cling TextItem. 
	 * @param ti The cling TextItem.
	 */
	public TextItem(org.teleal.cling.support.model.item.TextItem ti){
		super(ti.getClazz().getValue(), ti.getId(), ti.getParentID(), ti.getCreator(), ti.getTitle(), ti.getResources(),MediaObject.mediaType.videoItem);
		if(ti.getAuthors()!=null){
			ArrayList<String> al = new ArrayList<String>();
			for(int i = 0; i < ti.getAuthors().length;i++){
				al.add(ti.getAuthors()[i].getName());
			}
			setAuthors(al);
		}
		if(ti.getPublishers()!=null){
			ArrayList<String> al = new ArrayList<String>();
			for(int i = 0; i < ti.getPublishers().length;i++){
				al.add(ti.getPublishers()[i].getName());
			}
			setPublisher(al);
		}
		if(ti.getContributors()!=null){
			ArrayList<String> al = new ArrayList<String>();
			for(int i = 0; i < ti.getContributors().length;i++){
				al.add(ti.getContributors()[i].getName());
			}
			setContributor(al);
		}
		if(ti.getRelations()!=null){
			ArrayList<String> al = new ArrayList<String>();
			for(int i = 0; i < ti.getRelations().length;i++){
				al.add(ti.getRelations()[i].toString());
			}
			setRelations(al);
		}
		if(ti.getLanguage()!=null){
			ArrayList<String> al = new ArrayList<String>();
			al.add(ti.getLanguage());
			setLanguages(al);
		}
		if(ti.getRights()!=null){
			ArrayList<String> al = new ArrayList<String>();
			for(int i = 0; i < ti.getRights().length;i++){
				al.add(ti.getRights()[i]);
			}
			setRights(al);
		}
		if(ti.getLongDescription()!=null){
			setLongDescription(ti.getLongDescription());
		}
		if(ti.getStorageMedium()!=null){
			setStorageMedium(ti.getStorageMedium().toString());
		}
		if(ti.getRating()!=null){
			setRating(ti.getRating());
		}
		if(ti.getDescription()!=null){
			setDescription(ti.getDescription());
		}
		if(ti.getDate()!=null){
			setDate(ti.getDate());
		}
	}

	/**
	 * Returns the authors of this text item.
	 * @return The authors of this text item.
	 */
	public String getAuthor(){
		return this.getValueOfMetaDataType(MetaDataTypes.TEXTITEM_AUTHOR);
	}
	
	/**
	 * Sets the author property of this text item.
	 * @param authors The author property of this text item.
	 */
	public void setAuthors(ArrayList<String> authors){
		this.getMetaData().put(MetaDataTypes.TEXTITEM_AUTHOR, authors);
	}
	
	/**
	 * Returns the publishers of this text item.
	 * @return The publishers of this text item.
	 */
	public String getPublisher(){
		return this.getValueOfMetaDataType(MetaDataTypes.TEXTITEM_PUBLISHER);
	}
	
	/**
	 * Sets the publisher property of this text item.
	 * @param publishers The publisher property of this text item.
	 */
	public void setPublisher(ArrayList<String> publishers){
		this.getMetaData().put(MetaDataTypes.TEXTITEM_PUBLISHER, publishers);
	}
	
	/**
	 * Returns the contributors of this text item.
	 * @return The contributors of this text item.
	 */
	public String getContributor(){
		return this.getValueOfMetaDataType(MetaDataTypes.TEXTITEM_CONTRIBUTOR);
	}
	
	/**
	 * Sets the contributor property of this text item.
	 * @param contributors The contributor property of this text item.
	 */
	public void setContributor(ArrayList<String> contributors){
		this.getMetaData().put(MetaDataTypes.TEXTITEM_CONTRIBUTOR, contributors);
	}
	
	/**
	 * Returns the relation of this text item.
	 * @return The relations of this text item.
	 */
	public String getRelations(){
		return this.getValueOfMetaDataType(MetaDataTypes.TEXTITEM_RELATION);
	}
	
	/**
	 * Sets the relation property of this text item.
	 * @param relaions The relation property of this text item.
	 */
	public void setRelations(ArrayList<String> relations){
		this.getMetaData().put(MetaDataTypes.TEXTITEM_RELATION, relations);
	}
	
	/**
	 * Returns the languages of this text item.
	 * @return The languages of this text item.
	 */
	public String getLanguages(){
		return this.getValueOfMetaDataType(MetaDataTypes.TEXTITEM_LANGUAGE);
	}
	
	/**
	 * Sets the language property of this text item.
	 * @param language The language property of this text item.
	 */
	public void setLanguages(ArrayList<String> languages){
		this.getMetaData().put(MetaDataTypes.TEXTITEM_LANGUAGE, languages);
	}
	
	/**
	 * Returns the rights of this text item.
	 * @return The rights of this text item.
	 */
	public String getRights(){
		return this.getValueOfMetaDataType(MetaDataTypes.TEXTITEM_RIGHTS);
	}
	
	/**
	 * Sets the rights property of this text item.
	 * @param rights The rights property of this text item.
	 */
	public void setRights(ArrayList<String> rights){
		this.getMetaData().put(MetaDataTypes.TEXTITEM_RIGHTS,  rights);
	} 
	
	/**
	 * Returns the meta data containing the protection of the text item. 
	 * @return the protection of the text item.
	 */
	public String getProtection(){
		return this.getValueOfMetaDataType(MetaDataTypes.TEXTITEM_PROTECTION);
	}
	
	/**
	 * Sets the protection of the text item.
	 * @param protection The protection of the text item.
	 */
	@SuppressWarnings("serial")
	public void setProtection(final String protection){
		this.getMetaData().put(MetaDataTypes.TEXTITEM_PROTECTION, new ArrayList<String>(){{add(protection);}});
	}
	
	/**
	 * Returns the meta data containing the long description of the text item. 
	 * @return A String containing the long description of the text item.
	 */
	public String getLongDescription(){
		return this.getValueOfMetaDataType(MetaDataTypes.TEXTITEM_LONG_DESCRIPTION);
	}
	
	/**
	 * Sets the long description of the text item.
	 * @param longDescription The long description of the text item.
	 */
	@SuppressWarnings("serial")
	public void setLongDescription(final String longDescription){
		this.getMetaData().put(MetaDataTypes.TEXTITEM_LONG_DESCRIPTION, new ArrayList<String>(){{add(longDescription);}});
	}
	
	/**
	 * Returns the meta data containing the storage medium of the text item. 
	 * @return the storage medium of the text item.
	 */
	public String getStorageMedium(){
		return this.getValueOfMetaDataType(MetaDataTypes.TEXTITEM_STORAGE_MEDIUM);
	}
	
	/**
	 * Sets the storage medium property of the text item.
	 * @param storageMedium The storage medium property of the text item.
	 */
	@SuppressWarnings("serial")
	public void setStorageMedium(final String storageMedium){
		this.getMetaData().put(MetaDataTypes.TEXTITEM_STORAGE_MEDIUM, new ArrayList<String>(){{add(storageMedium);}});
	}
	
	/**
	 * Returns the meta data containing the rating of the text item. 
	 * @return the rating of the text item.
	 */
	public String getRating(){
		return this.getValueOfMetaDataType(MetaDataTypes.TEXTITEM_RATING);
	}
	
	/**
	 * Sets the rating property of the text item.
	 * @param rating The rating property of the text item.
	 */
	@SuppressWarnings("serial")
	public void setRating(final String rating){
		this.getMetaData().put(MetaDataTypes.TEXTITEM_RATING, new ArrayList<String>(){{add(rating);}});
	}
	
	/**
	 * Returns the meta data containing the description of the text item. 
	 * @return the description of the text item.
	 */
	public String getDescription(){
		return this.getValueOfMetaDataType(MetaDataTypes.TEXTITEM_DESCRIPTION);
	}
	
	/**
	 * Sets the description property of the text item.
	 * @param description The description property of the text item.
	 */
	@SuppressWarnings("serial")
	public void setDescription(final String description){
		this.getMetaData().put(MetaDataTypes.TEXTITEM_DESCRIPTION, new ArrayList<String>(){{add(description);}});
	}
	
	/**
	 * Returns the meta data containing the date of the text item. 
	 * @return the date of the text item.
	 */
	public String getDate(){
		return this.getValueOfMetaDataType(MetaDataTypes.TEXTITEM_DATE);
	}
	
	/**
	 * Sets the date property of the text item.
	 * @param date The date property of the text item.
	 */
	@SuppressWarnings("serial")
	public void setDate(final String date){
		this.getMetaData().put(MetaDataTypes.TEXTITEM_DATE, new ArrayList<String>(){{add(date);}});
	}
}
