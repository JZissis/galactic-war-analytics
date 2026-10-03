package gr.ioanniszisis.helldivers.galactic.war.analytics.platform.config.exception;

/**
 * Thrown when the HellDivers API responds successfully but with no body, so there is
 * nothing to map. Treated as a bad gateway response rather than an internal error.
 */
public class HelldiversApiEmptyResponseException extends RuntimeException {

    /**
     * Creates the exception.
     *
     * @param message describes which call returned the empty body
     */
    public HelldiversApiEmptyResponseException(String message) {
        super(message);
    }
}