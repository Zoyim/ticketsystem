package af.cmr.loti.ticketsystem.business.utils;

/**
 * This class contains constant values used as keys for DAOs and services in the
 * Ticket System application. It organizes these keys into nested classes for DAOs and
 * service keys.
 */

public class ConstsValues {
	
	/**
	 * This inner class contains constant keys for DAOs used in the application.
	 * Each key represents a specific DAO used for database access.
	 */
	public static final class ConstsDAO {
		public static final String TICKET_DAO_KEY = "ticketDAO";
		public static final String USER_DAO_KEY = "userDAO";
	}

	/**
	 * This inner class contains constant keys for services used in the application.
	 * Each key represents a specific service used for business logic.
	 */
	public static final class ServiceKeys {
		public static final String TICKET_SERVICE_KEY = "ticketService";
		public static final String USER_SERVICE_KEY = "userService";
	}
}
