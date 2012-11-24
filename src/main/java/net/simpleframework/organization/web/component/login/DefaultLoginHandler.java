package net.simpleframework.organization.web.component.login;

import net.simpleframework.common.Convert;
import net.simpleframework.common.StringUtils;
import net.simpleframework.common.coll.KVMap;
import net.simpleframework.mvc.IForward;
import net.simpleframework.mvc.JavascriptForward;
import net.simpleframework.mvc.component.AbstractComponentHandler;
import net.simpleframework.mvc.component.ComponentParameter;
import net.simpleframework.organization.EAccountType;
import net.simpleframework.organization.OrganizationException;
import net.simpleframework.organization.web.LastUrlListener;
import net.simpleframework.organization.web.OrganizationPermissionHandler;

/**
 * 这是一个开源的软件，请在LGPLv3下合法使用、修改或重新发布。
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         http://code.google.com/p/simpleframework/
 *         http://www.simpleframework.net
 */
public class DefaultLoginHandler extends AbstractComponentHandler implements ILoginHandler {

	@Override
	public IForward login(final ComponentParameter cParameter) {
		final JavascriptForward js = new JavascriptForward();
		try {
			permission().login(
					cParameter,
					cParameter.getParameter("_accountName"),
					cParameter.getParameter("_passwordName"),
					new KVMap().add(OrganizationPermissionHandler.ACCOUNT_TYPE,
							Convert.toEnum(EAccountType.class, cParameter.getParameter("_accountType"))));

			final String loginCallback = (String) cParameter.getBeanProperty("jsLoginCallback");
			if (StringUtils.hasText(loginCallback)) {
				js.append(loginCallback);
			} else {
				final String lastUrl = LastUrlListener.getLastUrl(cParameter);
				final String loginForward = StringUtils.hasText(lastUrl) ? lastUrl
						: (String) cParameter.getBeanProperty("loginForward");
				js.append("$Actions.loc('").append(loginForward).append("');");
			}
			js.append("_save_cookie();");

		} catch (final OrganizationException e) {
			final boolean password = Convert.toBool(e.getVal("password"));
			js.append("Validation.insertAfter('");
			js.append(password ? "_passwordName" : "_accountName").append("', '")
					.append(e.getMessage()).append("');");
		}

		return js;
	}
}
