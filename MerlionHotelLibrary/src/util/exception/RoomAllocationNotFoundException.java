/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Exception.java to edit this template
 */
package util.exception;

/**
 *
 * @author eliseoh
 */
public class RoomAllocationNotFoundException extends Exception {

    /**
     * Creates a new instance of <code>RoomErrorException</code> without detail
     * message.
     */
    public RoomAllocationNotFoundException() {
    }

    /**
     * Constructs an instance of <code>RoomErrorException</code> with the
     * specified detail message.
     *
     * @param msg the detail message.
     */
    public RoomAllocationNotFoundException(String msg) {
        super(msg);
    }
}
