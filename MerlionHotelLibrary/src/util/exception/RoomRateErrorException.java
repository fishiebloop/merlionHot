/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Exception.java to edit this template
 */
package util.exception;

/**
 *
 * @author eliseoh
 */
public class RoomRateErrorException extends Exception{

    /**
     * Creates a new instance of <code>RoomTypeErrorException</code> without
     * detail message.
     */
    public RoomRateErrorException() {
    }

    /**
     * Constructs an instance of <code>RoomTypeErrorException</code> with the
     * specified detail message.
     *
     * @param msg the detail message.
     */
    public RoomRateErrorException(String msg) {
        super(msg);
    }

}
