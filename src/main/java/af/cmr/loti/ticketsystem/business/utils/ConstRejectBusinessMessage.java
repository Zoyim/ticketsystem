package af.cmr.loti.ticketsystem.business.utils;

/**
 * This class contains constant messages for rejection scenarios in the business
 * layer. These messages are used to provide informative feedback when certain
 * operations fail due to specific conditions.
*/
public class ConstRejectBusinessMessage {
	
	public static String UPDATE_OBJECT_NOT_FOUND = "The object to be modified does not exist in the database";
	public static String DELETE_OBJECT_NOT_FOUND = "The object to be deleted does not exist in the database";
	public static String OBJECT_SEARCHED_NOT_FOUND = "The object to be found does not exist in the database";
}
