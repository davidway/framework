/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.leo.action;

/**
 * 
 * @author superleo
 */
public class HelloAction {

	private String message;

	public String hello() {
		message = "superleo " + this.message;
		return "success";
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

}
