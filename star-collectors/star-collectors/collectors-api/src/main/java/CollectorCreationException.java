import javax.annotation.Nonnull;

public class CollectorCreationException extends RuntimeException {

    /**
     *
     * @param exceptionMessage This is the message which will show each time a collector isn't created properly.
     *
     */
    public CollectorCreationException(final @Nonnull String exceptionMessage) {
        super(exceptionMessage);
    }
}
