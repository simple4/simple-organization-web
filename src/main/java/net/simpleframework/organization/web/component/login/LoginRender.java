package net.simpleframework.organization.web.component.login;

import net.simpleframework.mvc.component.ComponentHtmlRenderEx;
import net.simpleframework.mvc.component.ComponentParameter;
import net.simpleframework.mvc.component.IComponentRegistry;

/**
 * 这是一个开源的软件，请在LGPLv3下合法使用、修改或重新发布。
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         http://code.google.com/p/simpleframework/
 *         http://www.simpleframework.net
 */
public class LoginRender extends ComponentHtmlRenderEx {
	public LoginRender(final IComponentRegistry componentRegistry) {
		super(componentRegistry);
	}

	@Override
	protected String getRelativePath(final ComponentParameter cParameter) {
		final StringBuilder sb = new StringBuilder();
		sb.append("/jsp/login.jsp?").append(LoginUtils.BEAN_ID);
		sb.append("=").append(cParameter.hashId());
		return sb.toString();
	}
}
