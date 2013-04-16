package helpers;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import fileStructure.MediaObject;

public class Search {

	private enum CATEGORIES{Title, Genre, Publish_Date, Actor, Artist, File};

	private final static String EXACT_MATCH_REGEX = "(^\")&&(\"$)";

	private final static String GENRE_MATCH_REGEX = "genre=";

	private final static String DATE_PUBLISH_REGEX = "publish=";
	
	private final static String ARTIST_MATCH_REGEX = "artist=";
	
	private final static String ACTOR_MATCH_REGEX = "actor=";
	
	private final static String FILE_MATCH_REGEX = "file=";

	/**
	 * This method takes in the search string from the app and sorts it the correct search categories
	 * @param searchString Search string that is to be used in the search parameter
	 * @return A list of media objects that makes up the directory.
	 */
	public static String search(String searchString){

		List<MediaObject> newListing = new ArrayList<MediaObject>();

		//Seperate the search string into multiple search values
		Scanner scan = new Scanner(searchString);
		scan.useDelimiter(",");

		String parameters = "";

		while(scan.hasNext()){
			boolean match = false;
			String temp = scan.next();
			if(temp.matches(EXACT_MATCH_REGEX)){
				temp = temp.replaceAll("(\")", "");
				parameters += searchCategory(temp, CATEGORIES.Title);
				match = true;
			}

			if(temp.contains(GENRE_MATCH_REGEX)){
				temp = temp.replace(GENRE_MATCH_REGEX, "");
				parameters += searchCategory(temp, CATEGORIES.Genre);
				match = true;
			}

			if(temp.contains(DATE_PUBLISH_REGEX)){
				temp = temp.replace(DATE_PUBLISH_REGEX, "");
				parameters += searchCategory(temp, CATEGORIES.Publish_Date);
				match = true;
			}
			
			if(temp.contains(ACTOR_MATCH_REGEX)){
				temp = temp.replace(ACTOR_MATCH_REGEX, "");
				parameters += searchCategory(temp, CATEGORIES.Actor);
				match = true;
			}
			
			if(temp.contains(ARTIST_MATCH_REGEX)){
				temp = temp.replace(ARTIST_MATCH_REGEX, "");
				parameters += searchCategory(temp, CATEGORIES.Artist);
				match = true;
			}
			
			if(temp.contains(FILE_MATCH_REGEX)){
				temp = temp.replace(FILE_MATCH_REGEX, "");
				parameters += searchCategory(temp, CATEGORIES.File);
				match = true;
			}
			
			if(!match){
				parameters += searchCategory(temp, CATEGORIES.Title);
			}
			if(scan.hasNext()){
				parameters += " && ";
			}
		}


		//TODO Need to return a list of media objects.
		return parameters;
	}

	private static String searchCategory(String searchString, CATEGORIES category){

		String temp = "";

		switch(category){

		case Title:
			temp += "Title=\"" + searchString + "\"";
			break;
		case Artist:
			temp += "Artist=\"" + searchString + "\"";
			break;
		case Actor:
			temp += "Actor=\"" + searchString + "\"";
			break;
		case Genre:
			temp += "Genre=\"" + searchString + "\"";
			break;
		case Publish_Date:
			temp += "PublishDate=\"" + searchString + "\"";
			break;
		case File:
			temp += "File=\"" + searchString + "\"";
			break;
		default:

		}


		return temp;
	}

}
