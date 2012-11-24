package net.simpleframework.organization.web;

import java.io.IOException;

import javax.servlet.FilterChain;

import net.simpleframework.common.web.HttpUtils;
import net.simpleframework.mvc.IFilterListener;
import net.simpleframework.mvc.MVCContextFactory;
import net.simpleframework.mvc.PageRequestResponse;

/**
 * 这是一个开源的软件，请在LGPLv3下合法使用、修改或重新发布。
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         http://code.google.com/p/simpleframework/
 *         http://www.simpleframework.net
 */
public class LastUrlListener implements IFilterListener {
	final static String LAST_URL = "$$lastUrl";

	@Override
	public boolean doFilter(final PageRequestResponse rRequest, final FilterChain filterChain)
			throws IOException {
		String accept;
		if (rRequest.isHttpRequest() && !MVCContextFactory.ctx().isSystemUrl(rRequest)
				&& (accept = rRequest.getRequestHeader("Accept")) != null
				&& accept.startsWith("text/html")) {
			rRequest.setSessionAttr(LAST_URL, HttpUtils.getRequestAndQueryStringUrl(rRequest.request));
		}
		return true;
	}

	public static String getLastUrl(final PageRequestResponse rRequest) {
		final String lastUrl = (String) rRequest.getSessionAttr(LAST_URL);
		rRequest.removeSessionAttr(LAST_URL);
		return lastUrl;
	}
}
