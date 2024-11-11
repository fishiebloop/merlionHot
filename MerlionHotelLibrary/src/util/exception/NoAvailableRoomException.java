package util.exception;

public class NoAvailableRoomException extends Exception {
    
    private static final long serialVersionUID = 1L;
    private boolean upgradeAvailable;

    /**
     * Creates a new instance of <code>NoAvailableRoomException</code> without detail
     * message.
     */
    public NoAvailableRoomException() {
    }

    /**
     * Constructs an instance of <code>NoAvailableRoomException</code> with the
     * specified detail message.
     *
     * @param msg the detail message.
     */
    public NoAvailableRoomException(String msg) {
        super(msg);
    }

    /**
     * Constructs an instance of <code>NoAvailableRoomException</code> with the
     * specified detail message and upgrade status.
     *
     * @param msg the detail message.
     * @param upgradeAvailable true if an upgrade is available, false otherwise.
     */
    public NoAvailableRoomException(String msg, boolean upgradeAvailable) {
        super(msg);
        this.upgradeAvailable = upgradeAvailable;
    }

    /**
     * Returns whether an upgrade is available.
     *
     * @return true if an upgrade is available, false otherwise.
     */
    public boolean isUpgradeAvailable() {
        return upgradeAvailable;
    }

    /**
     * Sets whether an upgrade is available.
     *
     * @param upgradeAvailable true if an upgrade is available, false otherwise.
     */
    public void setUpgradeAvailable(boolean upgradeAvailable) {
        this.upgradeAvailable = upgradeAvailable;
    }
}
