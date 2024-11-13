/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Exception.java to edit this template
 */
package util.exception;

import javax.xml.ws.WebFault;

/**
 *
 * @author eliseoh
 */
@WebFault(name = "RoomTypeErrorException")
public class RoomTypeErrorException extends Exception{

    /**
     * Creates a new instance of <code>RoomTypeErrorException</code> without
     * detail message.
     */
    public RoomTypeErrorException() {
    }

    /**
     * Constructs an instance of <code>RoomTypeErrorException</code> with the
     * specified detail message.
     *
     * @param msg the detail message.
     */
    public RoomTypeErrorException(String msg) {
        super(msg);
    }

}
