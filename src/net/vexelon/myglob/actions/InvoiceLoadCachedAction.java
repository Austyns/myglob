/*
 * MyGlob Android Application
 * 
 * Copyright (C) 2013 Petar Petrov
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
package net.vexelon.myglob.actions;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.xml.sax.SAXException;

import android.content.Context;
import android.util.Log;
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
		String invoiceDateTime = Long.toString(new Date().getTime());
		try {
			String storageName = String.format(Defs.ISTORAGE_XML_FORMAT, _user.getPhoneNumber(), "");
			String[] storageFiles = _context.fileList();
			for (String fileName : storageFiles) {
				if (Defs.LOG_ENABLED) {
					Log.d(Defs.LOG_TAG, "Found inv.storage: " + fileName);
				}
				if (fileName.startsWith(storageName)) {
					invoiceDateTime = fileName.replace(storageName, "");
					storageName = fileName;
					if (Defs.LOG_ENABLED) {
						Log.d(Defs.LOG_TAG, "Found inv.parsed-datetime: " + invoiceDateTime);
					}					
					break;
				}
			}
			source = _context.openFileInput(storageName);
			// parse XML
			GLBInvoiceXMLParser xmlParser = new GLBInvoiceXMLParser(source);
			List<Map<String, String>> invoiceInfo = xmlParser.build();
			// hack - we need to display the date
			for (Map<String, String> row : invoiceInfo) {
				row.put(GLBInvoiceXMLParser.TAG_DATE, invoiceDateTime);
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
