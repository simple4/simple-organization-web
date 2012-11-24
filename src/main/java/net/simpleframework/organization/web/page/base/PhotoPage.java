package net.simpleframework.organization.web.page.base;

import net.simpleframework.mvc.PageParameter;
import net.simpleframework.organization.IAccount;

/**
 * 这是一个开源的软件，请在LGPLv3下合法使用、修改或重新发布。
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         http://code.google.com/p/simpleframework/
 *         http://www.simpleframework.net
 */
public class PhotoPage extends AbstractAccountPage {

	public String getPhotoUrl(final PageParameter pParameter, final IAccount account) {
		return permission().getPhotoUrl(pParameter, account.getId(), 164, 164);
	}

	public String getUploadUrl(final PageParameter pParameter, final IAccount account) {
		return pParameter.getContextPath()
				+ uriFor(PhotoUploadPage.class, "accountId=" + account.getId());
	}
}
