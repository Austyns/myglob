/*
 * MyGlob Android Application
 * 
 * Copyright (C) 2010 Petar Petrov
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
package net.vexelon.myglob.crypto;

import java.security.NoSuchAlgorithmException;

public interface Crypto {
	
	public byte[] createSecretKey(int keysize) throws NoSuchAlgorithmException;	
	
	public byte[] encrypt(byte[] input, byte[] secretKey, byte[] iv)
		throws Exception;
	
	public byte[] decrypt(byte[] input, byte[] secretKey, byte[] iv)
		throws Exception;

}
