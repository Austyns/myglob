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

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.util.Log;
import net.vexelon.mobileops.InvoiceException;
import net.vexelon.mobileops.GLBInvoiceXMLParser;
import net.vexelon.mobileops.HttpClientException;
import net.vexelon.mobileops.IClient;
import net.vexelon.myglob.R;
import net.vexelon.myglob.configuration.Defs;
import net.vexelon.myglob.users.User;
import net.vexelon.myglob.utils.Utils;

public class InvoiceSummaryUpdateAction extends BaseAction {
	
	public InvoiceSummaryUpdateAction(Context context, User user) {
		super(context, user);
	}

	@Override
	public ActionResult execute() throws ActionExecuteException {
		ActionResult result = new ActionResult();
		result.setCheckedOn(new Date());
		
		IClient client = newClient();
		clientLogin(client);
		
		try {
			Map<String, String> invoiceSummary = client.getInvoiceSummary();
			// clean up cache
//			String lookForStorageName = String.format(Defs.ISTORAGE_XML_FORMAT, 
//					_user.getPhoneNumber(), "");
//			String[] storageFiles = _context.fileList();
//			for (String fileName : storageFiles) {
//				if (fileName.startsWith(lookForStorageName)) {
//					if (!_context.deleteFile(fileName)) {
//						Log.w(Defs.LOG_TAG, "Failed deleting - " + fileName);
//					}
//				}
//			}
//			// cache on local device storage
//			String storageName = String.format(Defs.ISTORAGE_XML_FORMAT, 
//					_user.getPhoneNumber(), 
//					client.getInvoiceDateTime());
//			Utils.writeToInternalStorage(_context, new ByteArrayInputStream(invoiceData), storageName);
		
			// prep result object
			result.setBytesCount(client.getDownloadedBytesCount());
			result.setResult(invoiceSummary);
			// update user info
			updateUserResult(result);
			
		} catch (InvoiceException e) {
			throw new ActionExecuteException(R.string.err_invoice_globul, e);
		} catch (HttpClientException e) {
			throw new ActionExecuteException(e);
		} finally {
			// Make sure we (attempt to) logout.
			clientLogout(client);
		}

		return result;
	}

}
