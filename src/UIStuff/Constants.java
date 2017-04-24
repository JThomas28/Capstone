package UIStuff;

/**
 * Constants contains all strings used in the UI for easy access and
 * manipulation.
 * 
 * @author JonathanThomas
 * @version 4/28/17
 */
public interface Constants
{
	// title
	public static final String FRAME_TITLE = "Ring Reader";

	// images and webpage
	public static final String TITLE_IMAGE = "thomas_profile.png";
	public static final String PATH_TO_BACKGROUND_IMAGE = "forestBackground.jpg";
	public static final String WEBPAGE = "https://www.ncdc.noaa.gov/temp-and-precip/state-temps/";

	// basic strings
	public static final String NEW = "New";
	public static final String NEW_UPLOAD = "New Upload";
	public static final String NEW_THRESHOLD = "New Threshold";
	public static final String ANALYZE_NEW = "Analyze new image?";
	public static final String CONFIRM = "Confirm";
	public static final String CHOOSE_IMAGE_TEXT = "Choose Image To Upload";
	public static final String BROWSE = "Browse...";
	public static final String ZIPCODE_TEXT = "Enter Zipcode where tree was found:";
	public static final String PATH_TEXTFIELD = "Path To Image:";
	public static final String GO = "Go";
	public static final String MORE_INFO = "More Info";
	public static final String UP = "Up";
	public static final String DOWN = "Down";
	public static final String LEFT = "Left";
	public static final String RIGHT = "Right";
	public static final String SELECT_EDGES = "Select which edges to measure to";
	public static final String CHOOSE_EDGES = "Choose edges";
	public static final String ADD_DEATH_YEAR = "Add Death Year";
	public static final String ENTER_ESTIMATE = "Enter estimated death year";
	public static final String YEAR_REQUIREMENT = "Year must be more than 1000";
	public static final String YEAR_NUMBER = "Year must be an integer";
	public static final String MORE_THAN_AVERAGE = "Greater than average growth years: ";

	// other strings
	public static final String AVAILABLE_FILE_FORMATS = "<html>Available file formats include:<br>"
			+ ".png and .jpg</html>";
	public static final String NOT_VALID_FILE = "Please choose a valid '*.jpg' or '*.png' image file";
	public static final String INFORMATION = "<html>The age results are shown below.<br><br>"
			+ "If you feel as though the results are inaccurate, it is most likely "
			+ "due to a threshold that was too dark.<br><br>" + "You may choose a new threshold value by choosing"
			+ "'New Threshold' from the drop down menu bar "
			+ "or you can analyze a new image by selecting 'New Upload</html>";
}
