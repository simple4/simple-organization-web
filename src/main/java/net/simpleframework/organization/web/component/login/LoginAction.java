package net.simpleframework.organization.web.component.login;

import net.simpleframework.mvc.IForward;
import net.simpleframework.mvc.component.ComponentParameter;
import net.simpleframework.mvc.component.base.ajaxrequest.DefaultAjaxRequestHandler;

/**
 * 这是一个开源的软件，请在LGPLv3下合法使用、修改或重新发布。
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         http://code.google.com/p/simpleframework/
 *         http://www.simpleframework.net
 */
public class LoginAction extends DefaultAjaxRequestHandler {

	public IForward login(final ComponentParameter cParameter) {
		final ComponentParameter nComponentParameter = LoginUtils.get(cParameter);
		return ((ILoginHandler) nComponentParameter.getComponentHandler()).login(nComponentParameter);
	}
}
