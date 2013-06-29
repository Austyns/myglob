package net.vexelon.myglob.actions;

import android.content.Context;
import net.vexelon.mobileops.IClient;
import net.vexelon.myglob.R;
import net.vexelon.myglob.users.User;

public class InvoiceUpdateAction extends BaseAction {
	
	public InvoiceUpdateAction(Context context, User user) {
		super(context, user);
	}

	@Override
	public ActionResult execute() throws ActionExecuteException {
		ActionResult result = new ActionResult();
		IClient client = this.newClient();
		
		clientLogin(client);
		// TODO Auto-generated method stub
		
		clientLogout(client);

		throw new ActionExecuteException(R.string.dlg_error_msg_decrypt_failed);
	}

}
