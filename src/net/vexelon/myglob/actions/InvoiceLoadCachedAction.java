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
package net.vexelon.myglob.actions;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.xml.sax.SAXException;

import android.content.Context;
import net.vexelon.mobileops.GLBInvoiceXMLParser;
import net.vexelon.myglob.configuration.Defs;
import net.vexelon.myglob.users.User;

public class InvoiceLoadCachedAction extends BaseAction {
	
	public InvoiceLoadCachedAction(Context context, User user) {
		super(context, user);
	}

	@Override
	public ActionResult execute() throws ActionExecuteException {
		ActionResult result = new ActionResult();
		
		InputStream source = null;
		try {
			String storageName = String.format(Defs.ISTORAGE_XML_FORMAT, _user.getPhoneNumber());
			source = _context.openFileInput(storageName);
			// parse XML
			GLBInvoiceXMLParser xmlParser = new GLBInvoiceXMLParser(source);
			List<Map<String, String>> invoiceInfo = xmlParser.build();
			// hack - we need to display the date
			for (Map<String, String> row : invoiceInfo) {
//				map.put(GLBInvoiceXMLParser.TAG_DATE, invoiceDate);
				row.put(GLBInvoiceXMLParser.TAG_DATE, Long.toString(new Date().getTime()));
			}			
			// prep result object
			result.setResult(invoiceInfo);
			
		} catch (SAXException e) {
			throw new ActionExecuteException(e);
		} catch (FileNotFoundException e) {
			throw new ActionExecuteException(e);
		} finally {
			try { if ( source != null ) source.close(); } catch (IOException e) {}
		}

		return result;
	}

}
