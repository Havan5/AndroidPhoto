package hp.photoappandroid94.model;
/**
*@author: Havan Patel, and TPULLIS TRAVIS B
*/

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Album implements Serializable {

	private static final long serialVersionUID = 1L;

	private String albumName;
	private List<Photo> photos;

	/**
	 * album default constructor
	 * @param albumName string
	 */
	public Album(String albumName) {
		this.albumName = albumName;
		this.photos = new ArrayList<Photo>();
	}

	/**
	 * set album name
	 * @param name string
	 */
	public void setName(String name) {
		this.albumName = name;
	}

	/**
	 * total phots is album
	 * @return int size
	 */
	public int totalPhotos() {
		return photos.size();
	}

	/**
	 * get Album name
	 * @return String album name
	 */
	public String getAlbumName() {
		return albumName;
	}

	/**
	 * get all photo list in a album
	 * @return list of photo
	 */
	public List<Photo> getPhotos() {
		return photos;
	}

	/**
	 * add photo to list
	 * @param p photo
	 */
	public void addPhoto(Photo p){ photos.add(p); }

	/**
	 * to string for album
	 */
	public String toString() {
		return getAlbumName();
	}
}
