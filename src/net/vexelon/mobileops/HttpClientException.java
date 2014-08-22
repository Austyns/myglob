/*
 * MyGlob Android Application
 * 
 * Copyright (C) 2012 Petar Petrov
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 */
package net.vexelon.mobileops;

@SuppressWarnings("serial")
public class HttpClientException extends Exception {
	
	private int statusCode = -1;
	
	public HttpClientException(String message) {
		super(message);
	}
	
	public HttpClientException(String message, int statusCode) {
		super(message);
		
		this.statusCode = statusCode;
	}	
	
	public HttpClientException(String message, Throwable t) {
		super(message, t);
	}

	public int getStatusCode() {
		return this.statusCode;
	}	
}
