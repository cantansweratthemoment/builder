package logging;

import java.util.logging.Level;
import java.util.logging.Logger;

public class LoggerService {
    private static final Logger LOGGER = Logger.getLogger(LoggerService.class.getName());

    public static void logInfo(String message) {
        LOGGER.log(Level.INFO, message);
    }

    public static void logError(String message, Throwable e) {
        LOGGER.log(Level.SEVERE, message, e);
    }
}