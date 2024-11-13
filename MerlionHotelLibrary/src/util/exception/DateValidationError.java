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
@WebFault(name = "DateValidationError")
public class DateValidationError extends Exception{

    /**
     * Creates a new instance of <code>DateValidationError</code> without detail
     * message.
     */
    public DateValidationError() {
    }

    /**
     * Constructs an instance of <code>DateValidationError</code> with the
     * specified detail message.
     *
     * @param msg the detail message.
     */
    public DateValidationError(String msg) {
        super(msg);
    }
}
