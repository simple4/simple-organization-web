package net.simpleframework.organization.web.component.login;

import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.simpleframework.common.StringUtils;
import net.simpleframework.common.html.element.SpanElement;
import net.simpleframework.mvc.PageRequestResponse;
import net.simpleframework.mvc.component.ComponentParameter;

/**
 * 这是一个开源的软件，请在LGPLv3下合法使用、修改或重新发布。
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         http://code.google.com/p/simpleframework/
 *         http://www.simpleframework.net
 */
public abstract class LoginUtils {
	public static final String BEAN_ID = "login_@bid";

	public static ComponentParameter get(final PageRequestResponse rRequest) {
		return ComponentParameter.get(rRequest, BEAN_ID);
	}

	public static ComponentParameter get(final HttpServletRequest request,
			final HttpServletResponse response) {
		return ComponentParameter.get(request, response, BEAN_ID);
	}

	public static String getToolbar(final ComponentParameter nComponentParameter) {
		final ArrayList<String> al = new ArrayList<String>();
		final StringBuilder sb = new StringBuilder();
		sb.append("<table cellpadding='0' cellspacing='0' style='width: 100%;'><tr>");
		sb.append("<td>");
		if ((Boolean) nComponentParameter.getBeanProperty("showAutoLogin")) {
			sb.append("<input type='checkbox' id='_autoLogin' name='_autoLogin'");
			sb.append(" value='true' style='vertical-align:middle;'/>");
			sb.append("<label style='cursor: pointer; vertical-align:middle;'");
			sb.append(" for='_autoLogin'>#(login.1)</label>");
		}
		sb.append("</td>");
		sb.append("<td align='right'>");
		if ((Boolean) nComponentParameter.getBeanProperty("showGetPassword")) {
			final StringBuilder sb2 = new StringBuilder();
			sb2.append("<a onclick=\"$Actions['getPasswordWindow']();\">#(login.2)</a>");
			al.add(sb2.toString());
		}
		sb.append(StringUtils.join(al, SpanElement.SEP.toString()));
		sb.append("</td>");
		sb.append("</tr></table>");
		return sb.toString();
	}
}
