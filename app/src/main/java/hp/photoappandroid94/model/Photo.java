package hp.photoappandroid94.model;

import android.media.Image;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @author: Havan Patel, and TPULLIS TRAVIS B
 */
public class Photo implements Serializable {

	private static final long serialVersionUID = 1L;

	private List<Tag> tags;
	private String caption;
	private String imageFile;
	private String people;
	private String location;

	/**
	 * photo default constructor
	 * @param imageFile file
	 */
	public Photo(String imageFile) {
		this.imageFile = imageFile;
		tags = new ArrayList<Tag>();
		caption = "";
		people = "";
		location = "";
	}

	/**
	 * set people to photo
	 * @param people string
	 */
	public void setPeople(String people) {
		this.people = people;
	}

	/**
	 * set location to photo
	 * @param location string
	 */
	public void setLocation(String location) {
		this.location = location;
	}

	/**
	 * return image
	 * @return Image from file
	 */
	public Image getPic() {
		return null;
	}

	/**
	 * get people of photo
	 * @return people as string
	 */
	public String getPeopleTag() {
		return people;
	}

	/**
	 * get location of photo
	 * @return location as string
	 */
	public String getLocationTag() {
		return location;
	}

	/**
	 *
	 * @return tags list
	 */
	public List<Tag> getTags() {
		return tags;
	}

	/**
	 * return file of image
	 * @return File of image
	 */
	public String getimageFile() {
		return imageFile;
	}
}