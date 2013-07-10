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

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.xml.sax.SAXException;

import android.content.Context;
import android.util.Log;
import net.vexelon.mobileops.GLBInvoiceXMLParser;
import net.vexelon.mobileops.HttpClientException;
import net.vexelon.mobileops.IClient;
import net.vexelon.myglob.configuration.Defs;
import net.vexelon.myglob.users.User;
import net.vexelon.myglob.utils.Utils;

public class InvoiceUpdateAction extends BaseAction {
	
	public InvoiceUpdateAction(Context context, User user) {
		super(context, user);
	}

	@Override
	public ActionResult execute() throws ActionExecuteException {
		ActionResult result = new ActionResult();
		result.setCheckedOn(new Date());
		
		IClient client = newClient();
		clientLogin(client);
		
		try {
			byte[] invoiceData = client.getInvoiceData();
			// clean up cache
			String lookForStorageName = String.format(Defs.ISTORAGE_XML_FORMAT, 
					_user.getPhoneNumber(), "");
			String[] storageFiles = _context.fileList();
			for (String fileName : storageFiles) {
				if (fileName.startsWith(lookForStorageName)) {
					if (!_context.deleteFile(fileName)) {
						Log.w(Defs.LOG_TAG, "Failed deleting - " + fileName);
					}
				}
			}
			// cache on local device storage
			String storageName = String.format(Defs.ISTORAGE_XML_FORMAT, 
					_user.getPhoneNumber(), 
					client.getInvoiceDateTime());
			Utils.writeToInternalStorage(_context, new ByteArrayInputStream(invoiceData), storageName);
			// parse XML
			GLBInvoiceXMLParser xmlParser = new GLBInvoiceXMLParser(new ByteArrayInputStream(invoiceData));
			List<Map<String, String>> invoiceInfo = xmlParser.build();
			// hack - we need to display the date
			for (Map<String, String> row : invoiceInfo) {
				row.put(GLBInvoiceXMLParser.TAG_DATE, Long.toString(client.getInvoiceDateTime()));
			}			
			// prep result object
			result.setBytesCount(client.getDownloadedBytesCount());
			result.setResult(invoiceInfo);
			// update user info
			updateUserResult(result);
			
		} catch (HttpClientException e) {
			throw new ActionExecuteException(e);
		} catch (SAXException e) {
			throw new ActionExecuteException(e);
		} catch (IOException e) {
			throw new ActionExecuteException(e);
		} finally {
			// Make sure we (attempt to) logout.
			clientLogout(client);
		}

		return result;
	}

}
