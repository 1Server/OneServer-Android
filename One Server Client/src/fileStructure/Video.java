package fileStructure;

import java.util.ArrayList;
import java.util.List;

import org.teleal.cling.support.model.Res;

/**
 * This class represents a video media object.
 * @author kuszewskij
 *
 */
public class Video extends MediaObject {

	
	/**
	 * Basic constructor for an image item. Does not initialize any metadata values that are specific to this media type
	 * @param classType The upnp class type.
	 * @param Id The unique identifier of this object
	 * @param parentId The unique identifier of this objects parent.
	 * @param creator The creator of this image.
	 * @param title The title of this image.
	 * @param resources The resources used to generate this image.
	 */
	public Video(final String classType,String Id, String parentId, final String creator, String title, List<Res> resources){
		super(classType, Id, parentId, creator, title, resources,MediaObject.mediaType.videoItem);
	}
	
	/**
	 * Basic constructor for a video item. Does not initialize any metadata values that are specific to this media type
	 * @param classType The upnp class type.
	 * @param Id The unique identifier of this object
	 * @param parentId The unique identifier of this objects parent.
	 * @param creator The creator of this image.
	 * @param title The title of this image.
	 * @param resources The resources used to generate this image.
	 * @param longDescription The long description of this video item.
	 * @param producers The producers of this video item.
	 * @param rating The rating of this video item.
	 * @param actors The actors of this video item.
	 * @param directors The directors of the video.
	 * @param description The description of the video.
	 * @param publishers The publishers of this video.
	 * @param language The language used in this video.
	 * @param relation The relations of this video.
	 */
	@SuppressWarnings("serial")
	public Video(final String classType,String Id, String parentId, final String creator, String title, List<Res> resources
			,final String longDescription, ArrayList<String> producers, final String rating, ArrayList<String> actors, ArrayList<String> directors
			, final String description, ArrayList<String> publishers, final String language, ArrayList<String> relation){
		super(classType, Id, parentId, creator, title, resources,MediaObject.mediaType.videoItem);

		super.getMetaData().put(MetaDataTypes.VIDEO_LONG_DESCRIPTION, new ArrayList<String>(){{add(longDescription);}});
		super.getMetaData().put(MetaDataTypes.VIDEO_RATING, new ArrayList<String>(){{add(rating);}});
		super.getMetaData().put(MetaDataTypes.VIDEO_DESCRIPTION, new ArrayList<String>(){{add(description);}});
		super.getMetaData().put(MetaDataTypes.VIDEO_LANGUAGE, new ArrayList<String>(){{add(language);}});

		super.getMetaData().put(MetaDataTypes.VIDEO_PRODUCER, producers);
		super.getMetaData().put(MetaDataTypes.VIDEO_ACTOR, actors);
		super.getMetaData().put(MetaDataTypes.VIDEO_DIRECTOR, directors);
		super.getMetaData().put(MetaDataTypes.VIDEO_PUBLISHER, publishers);
		super.getMetaData().put(MetaDataTypes.VIDEO_RELATION, relation);
	}
	
	/**
	 * Creates a Video from a cling videoItem
	 * @param vi
	 */
	public Video(org.teleal.cling.support.model.item.VideoItem vi){
		super(vi.getClazz().getValue(), vi.getId(), vi.getParentID(), vi.getCreator(), vi.getTitle(), vi.getResources(),MediaObject.mediaType.videoItem);
		if(vi.getLongDescription()!=null){
			setLongDescription(vi.getLongDescription());
		}
		if(vi.getProducers()!=null){
			ArrayList<String> al = new ArrayList<String>();
			for(int i = 0; i < vi.getProducers().length;i++){
				al.add(vi.getProducers()[i].getName());
			}
			setProducers(al);
		}
		if(vi.getRating()!=null){
			setRating(vi.getRating());
		}
		if(vi.getActors()!= null){
			ArrayList<String> al = new ArrayList<String>();
			for(int i = 0; i < vi.getActors().length; i++){
				al.add(vi.getActors()[i].getName());
			}
			setActors(al);
		}
		if(vi.getDirectors()!= null){
			ArrayList<String> al = new ArrayList<String>();
			for(int i = 0; i < vi.getDirectors().length; i++){
				al.add(vi.getDirectors()[i].getName());
			}
			setDirectors(al);
		}
		if(vi.getDescription()!=null){
			setDescription(vi.getDescription());
		}
		if(vi.getPublishers()!= null){
			ArrayList<String> al = new ArrayList<String>();
			for(int i = 0; i < vi.getPublishers().length; i++){
				al.add(vi.getPublishers()[i].getName());
			}
			setPublishers(al);
		}
		if(vi.getLanguage()!=null){
			setLanguage(vi.getLanguage());
		}
		if(vi.getRelations()!=null){
			ArrayList<String> al = new ArrayList<String>();
			for(int i = 0; i < vi.getRelations().length; i++){
				al.add(vi.getRelations()[i].toString());
			}
			setRelation(al);
		}
	}

	/**
	 * Returns the long description of this video.
	 * @return The long description of this video.
	 */
	public String getLongDescription(){
		return this.getValueOfMetaDataType(MetaDataTypes.VIDEO_LONG_DESCRIPTION);
	}
	
	/**
	 * Sets the long description of this video.
	 * @param longDescription The long description of this video.
	 */
	@SuppressWarnings("serial")
	public void setLongDescription(final String longDescription){
		this.getMetaData().put(MetaDataTypes.VIDEO_LONG_DESCRIPTION, new ArrayList<String>(){{add(longDescription);}});
	}
	
	/**
	 * Returns the rating of this video.
	 * @return The rating of this video.
	 */
	public String getRating(){
		return this.getValueOfMetaDataType(MetaDataTypes.VIDEO_RATING);
	}
	
	/**
	 * Sets the long description of this video.
	 * @param rating The long description of this video.
	 */
	@SuppressWarnings("serial")
	public void setRating(final String rating){
		this.getMetaData().put(MetaDataTypes.VIDEO_RATING, new ArrayList<String>(){{add(rating);}});
	}
	
	/**
	 * Returns the description of this video.
	 * @return The description of this video.
	 */
	public String getDescription(){
		return this.getValueOfMetaDataType(MetaDataTypes.VIDEO_DESCRIPTION);
	}
	
	/**
	 * Sets the long description of this video.
	 * @param description The long description of this video.
	 */
	@SuppressWarnings("serial")
	public void setDescription(final String description){
		this.getMetaData().put(MetaDataTypes.VIDEO_DESCRIPTION, new ArrayList<String>(){{add(description);}});
	}
	
	/**
	 * Returns the language used in this video.
	 * @return The language used in this video.
	 */
	public String getLanguage(){
		return this.getValueOfMetaDataType(MetaDataTypes.VIDEO_LANGUAGE);
	}
	
	/**
	 * Sets the language property of this video.
	 * @param language The language property of this video.
	 */
	@SuppressWarnings("serial")
	public void setLanguage(final String language){
		this.getMetaData().put(MetaDataTypes.VIDEO_LANGUAGE, new ArrayList<String>(){{add(language);}});
	}
	
	/**
	 * Returns the language used in this video.
	 * @return The language used in this video.
	 */
	public String getProducers(){
		return this.getValueOfMetaDataType(MetaDataTypes.VIDEO_PRODUCER);
	}
	
	/**
	 * Sets the language property of this video.
	 * @param language The language property of this video.
	 */
	public void setProducers(ArrayList<String> producers){
		this.getMetaData().put(MetaDataTypes.VIDEO_PRODUCER, producers);
	}
	
	/**
	 * Returns the actors in this video.
	 * @return The actors in this video.
	 */
	public String getActors(){
		return this.getValueOfMetaDataType(MetaDataTypes.VIDEO_ACTOR);
	}
	
	/**
	 * Sets the language property of this video.
	 * @param language The language property of this video.
	 */
	public void setActors(ArrayList<String> actors){
		this.getMetaData().put(MetaDataTypes.VIDEO_ACTOR, actors);
	}
	
	/**
	 * Returns the directors of this video.
	 * @return The directors of this video.
	 */
	public String getDirectors(){
		return this.getValueOfMetaDataType(MetaDataTypes.VIDEO_DIRECTOR);
	}
	
	/**
	 * Sets the directors property of this video.
	 * @param producers The producers property of this video.
	 */
	public void setDirectors(ArrayList<String> directors){
		this.getMetaData().put(MetaDataTypes.VIDEO_DIRECTOR, directors);
	}
	
	/**
	 * Returns the publishers of this video.
	 * @return The publishers of this video.
	 */
	public String getPublishers(){
		return this.getValueOfMetaDataType(MetaDataTypes.VIDEO_PUBLISHER);
	}
	
	/**
	 * Sets the publisher property of this video.
	 * @param publishers The publisher property of this video.
	 */
	public void setPublishers(ArrayList<String> publishers){
		this.getMetaData().put(MetaDataTypes.VIDEO_PUBLISHER, publishers);
	}
	
	/**
	 * Returns the relation property of this video.
	 * @return The relation property of this video.
	 */
	public String getRelation(){
		return this.getValueOfMetaDataType(MetaDataTypes.VIDEO_RELATION);
	}
	
	/**
	 * Sets the relation property of this video.
	 * @param relations The relation property of this video.
	 */
	public void setRelation(ArrayList<String> relations){
		this.getMetaData().put(MetaDataTypes.VIDEO_RELATION, relations);
	}
}


