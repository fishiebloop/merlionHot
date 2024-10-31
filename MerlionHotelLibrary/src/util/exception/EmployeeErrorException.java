/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Exception.java to edit this template
 */
package util.exception;

/**
 *
 * @author eliseoh
 */
public class EmployeeErrorException extends Exception {

    /**
     * Creates a new instance of <code>EmployeeErrorException</code> without
     * detail message.
     */
    public EmployeeErrorException() {
    }

    /**
     * Constructs an instance of <code>EmployeeErrorException</code> with the
     * specified detail message.
     *
     * @param msg the detail message.
     */
    public EmployeeErrorException(String msg) {
        super(msg);
    }
}
