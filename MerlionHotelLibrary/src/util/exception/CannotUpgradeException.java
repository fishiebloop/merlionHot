/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Exception.java to edit this template
 */
package util.exception;

/**
 *
 * @author eliseoh
 */
public class CannotUpgradeException extends Exception {

    /**
     * Creates a new instance of <code>CannotUpgradeException</code> without
     * detail message.
     */
    public CannotUpgradeException() {
    }

    /**
     * Constructs an instance of <code>CannotUpgradeException</code> with the
     * specified detail message.
     *
     * @param msg the detail message.
     */
    public CannotUpgradeException(String msg) {
        super(msg);
    }
}
