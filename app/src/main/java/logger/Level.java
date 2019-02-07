package logger;


public interface Level {

    /**
     * Enable all logging.
     */
     static final int DEBUG = 1;

    /** The Constant INFO. */
    public static final int INFO = 2;

    /** The Constant WARNING. */
    public static final int WARNING = 3;

    /** The Constant ERROR. */
    public static final int ERROR = 4;

    // Trace

    public static final int TRACE = 5;

    /** The Constant OFF. */
    public static final int OFF = 6;


}
