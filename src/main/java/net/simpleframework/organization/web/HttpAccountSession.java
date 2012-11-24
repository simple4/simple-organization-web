package net.simpleframework.organization.web;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import net.simpleframework.common.StringUtils;
import net.simpleframework.common.web.HttpUtils;
import net.simpleframework.mvc.PageRequestResponse;
import net.simpleframework.organization.IAccount;
import net.simpleframework.organization.IAccountSession;
import net.simpleframework.organization.OrganizationContextFactory;

/**
 * 这是一个开源的软件，请在LGPLv3下合法使用、修改或重新发布。
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         http://code.google.com/p/simpleframework/
 *         http://www.simpleframework.net
 */
public class HttpAccountSession implements IAccountSession {
	private HttpServletRequest request;

	private HttpServletResponse response;

	private final HttpSession httpSession;

	public HttpAccountSession(final PageRequestResponse rRequest) {
		httpSession = rRequest.getSession();
		request = rRequest.request;
		response = rRequest.response;
	}

	public HttpAccountSession(final HttpSession httpSession) {
		this.httpSession = httpSession;
	}

	@Override
	public Object getLogin() {
		return httpSession.getAttribute(LOGIN_KEY);
	}

	@Override
	public void setLogin(final Object login) {
		if (login == null) {
			logout();
		} else {
			httpSession.setAttribute(LOGIN_KEY, login);
		}
	}

	@Override
	public Object autoLogin() {
		IAccount login = null;
		final String pwd = HttpUtils.getCookie(request, "_account_pwd");
		if (StringUtils.hasText(pwd) && getLogin() == null) {
			login = OrganizationContextFactory
					.get()
					.getAccountMgr()
					.getAccountByName(
							StringUtils.decodeHexString(HttpUtils.getCookie(request, "_account_name")));
		}
		return login != null ? login.getId() : null;
	}

	@Override
	public void logout() {
		httpSession.removeAttribute(LOGIN_KEY);
		if (response != null) {
			HttpUtils.addCookie(response, "_account_pwd", null);
		}
	}

	@Override
	public long getOnlineMillis() {
		return System.currentTimeMillis() - httpSession.getLastAccessedTime();
	}

	@Override
	public String getRemoteAddr() {
		return HttpUtils.getRemoteAddr(request);
	}
}
