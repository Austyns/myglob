/*
 * The MIT License
 * 
 * Copyright (c) 2010 Petar Petrov
 * 
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * 
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package net.vexelon.myglob;

public class Defs {
	
	public final static String LOG_TAG = "net.vexelon.myglob";
	
	final static int CLR_BUTTON_UPDATE = 0xFFA6D060;
	final static String CLR_TEXT_HIGHLIGHT = "#FF7F00"; //0xFFFF7F00;
	final static String PREFS_NAME = "MyGlobAppPreferences";
	
	final static int MENU_SIGNIN = 10;
	final static int MENU_SIGNOUT = 12;
	final static int MENU_ABOUT = 100;
	
	final static int INTENT_SIGNIN_RQ = 100;
	
	final static String INTENT_EXTRA_KEY = "_SK";
	final static String INTENT_EXTRA_USERNAME = "_USERNAME";
	final static String INTENT_EXTRA_PASSWORD = "_PASSWORD";
	final static String INTENT_EXTRA_SAVECREDENTIALS = "_SAVECREDENTIALS";
	final static String INTENT_EXTRA_LASTRESULT = "_LASTRESULT";
	
	final static String DUMMY_PASSWORD = "$_DUMMY_PASSWORD_^";
	
}
