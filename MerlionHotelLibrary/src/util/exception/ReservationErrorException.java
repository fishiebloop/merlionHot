/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Exception.java to edit this template
 */
package util.exception;

/**
 *
 * @author eliseoh
 */
public class ReservationErrorException extends Exception{

    /**
     * Creates a new instance of <code>ReservationErrorException</code> without
     * detail message.
     */
    public ReservationErrorException() {
    }

    /**
     * Constructs an instance of <code>ReservationErrorException</code> with the
     * specified detail message.
     *
     * @param msg the detail message.
     */
    public ReservationErrorException(String msg) {
        super(msg);
    }
}
