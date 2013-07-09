/*
 * The MIT License
 *
 * Copyright (c) 2013 Petar Petrov
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
package net.vexelon.mobileops;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;

/**
 * Parser invoice XML file into key/value collection.
 * We might have more than one account info therefore we parse all available rows.
 */
public class GLBInvoiceXMLParser extends DefaultHandler {
	
	public static final String TAG_ROWDATA = "ROWDATA";
	public static final String TAG_ROW = "ROW";
	
	public static final String TAG_INVNUM = "InvNum";
	public static final String TAG_MSISDN = "MSISDN";
	public static final String TAG_FIXED_TAG = "FixCha";
	public static final String TAG_DISCOUNT = "Disc";
	public static final String TAG_TOTAL_NO_VAT = "Tot-VAT";
	public static final String TAG_VAT = "VatAm";
	public static final String TAG_TOTALVAT = "TotDueAm";
	public static final String TAG_TOTAL = "TotServ";	
	
	protected List<Map<String, String>> accountsList;
	protected InputStream inputStream;
	
	private StringBuilder buffer;
	private boolean parsingRow = false;
	Map<String,String> currentMap;
	
	public GLBInvoiceXMLParser(InputStream inputStream) {
		this.inputStream = inputStream;
	}
	
	public List<Map<String, String>> build() throws SAXException {
		accountsList = new ArrayList<Map<String,String>>();
		buffer = new StringBuilder(50);
	    try {
		    SAXParserFactory saxFactory = SAXParserFactory.newInstance();
		    SAXParser saxParser;
			saxParser = saxFactory.newSAXParser();
		    XMLReader saxReader = saxParser.getXMLReader();
		    saxReader.setContentHandler(this);
			saxReader.parse(new InputSource(inputStream));
		} catch (ParserConfigurationException e) {
			throw new SAXException("Invoice data reader failed!", e);
		} catch (IOException e) {
			throw new SAXException("Invoice data reader failed!", e);
		}
		
		return accountsList;
	}
	
	@Override
	public void startElement(String uri, String localName, String qName,
			Attributes attributes) throws SAXException {
		if (localName.equals(TAG_ROW)) {
			parsingRow = true;
			currentMap = new HashMap<String, String>();
		}
	}
	
	@Override
	public void endElement(String uri, String localName, String qName)
			throws SAXException {
		if (localName.equals(TAG_ROW)) {
			parsingRow = false;
			accountsList.add(currentMap);
		} else if (parsingRow) {
			currentMap.put(localName, buffer.toString());
		}
		buffer.setLength(0);
	}
	
	@Override
	public void characters(char[] ch, int start, int length)
			throws SAXException {
		if (parsingRow) {
			buffer.append(ch, start, length);
		}
	}

}
