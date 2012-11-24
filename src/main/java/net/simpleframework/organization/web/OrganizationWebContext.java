package net.simpleframework.organization.web;

import static net.simpleframework.common.I18n.$m;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

import net.simpleframework.ctx.Module;
import net.simpleframework.mvc.FilterUtils;
import net.simpleframework.mvc.MVCContextFactory;
import net.simpleframework.mvc.MVCEventAdapter;
import net.simpleframework.mvc.ctx.WebModuleFunction;
import net.simpleframework.organization.impl.OrganizationContext;
import net.simpleframework.organization.web.page.mgr.AccountMgrPage;

/**
 * 这是一个开源的软件，请在LGPLv3下合法使用、修改或重新发布。
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         http://code.google.com/p/simpleframework/
 *         http://www.simpleframework.net
 */
public class OrganizationWebContext extends OrganizationContext {

	@Override
	public void onInit() throws Exception {
		super.onInit();

		final ServletContext servletContext = MVCContextFactory.ctx().getServletContext();
		FilterUtils.getFilterListeners(servletContext).add(new LastUrlListener());
		MVCEventAdapter.getInstance(servletContext).addListener(new HttpSessionListener() {
			@Override
			public void sessionCreated(final HttpSessionEvent event) {
			}

			@Override
			public void sessionDestroyed(final HttpSessionEvent event) {
				getAccountMgr().logout(new HttpAccountSession(event.getSession()));
			}
		});
	}

	// protected IOrganizationContext getRemoteContext() {
	// // 呼叫远程接口，目前调用本地
	// return this;
	// }
	//
	// @Override
	// public IAccountManager getAccountMgr() {
	// return getRemoteContext().getAccountMgr();
	// }

	@Override
	protected Module createModule() {
		return super.createModule().setDefaultFunction(
				new WebModuleFunction(AccountMgrPage.class).setName(
						"simple-organization-AccountMgrPage").setText($m("OrganizationWebContext.0")));
	}
}
