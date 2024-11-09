/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Exception.java to edit this template
 */
package util.exception;

/**
 *
 * @author eliseoh
 */
public class GuestErrorException extends Exception {

    /**
     * Creates a new instance of <code>GuestErrorException</code> without detail
     * message.
     */
    public GuestErrorException() {
    }

    /**
     * Constructs an instance of <code>GuestErrorException</code> with the
     * specified detail message.
     *
     * @param msg the detail message.
     */
    public GuestErrorException(String msg) {
        super(msg);
    }
}
