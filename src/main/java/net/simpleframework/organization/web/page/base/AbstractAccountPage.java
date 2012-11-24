package net.simpleframework.organization.web.page.base;

import net.simpleframework.common.coll.KVMap;
import net.simpleframework.mvc.PageParameter;
import net.simpleframework.mvc.template.AbstractTemplatePage;
import net.simpleframework.organization.IAccount;
import net.simpleframework.organization.IOrganizationContext;
import net.simpleframework.organization.OrganizationContextFactory;

/**
 * 这是一个开源的软件，请在LGPLv3下合法使用、修改或重新发布。
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         http://code.google.com/p/simpleframework/
 *         http://www.simpleframework.net
 */
public abstract class AbstractAccountPage extends AbstractTemplatePage {
	public static IOrganizationContext context = OrganizationContextFactory.get();

	@Override
	protected void onInit(final PageParameter pParameter) {
		super.onInit(pParameter);

		addImportCSS(getCssResourceHomePath(pParameter, AbstractAccountPage.class)
				+ "/account_edit.css");
	}

	public String buildFormHidden(final PageParameter pParameter) {
		return buildInputHidden(pParameter, "accountId");
	}

	protected IAccount getAccount(final PageParameter pParameter) {
		return context.getAccountMgr().getBean(pParameter.getParameter("accountId"));
	}

	@Override
	public KVMap createVariables(final PageParameter pParameter) {
		final KVMap variables = (KVMap) super.createVariables(pParameter);
		final IAccount account = getAccount(pParameter);
		variables.put("account", account);
		// variables.put("user", client.accountMgr().getUser(account.getId()));
		return variables;
	}
}
