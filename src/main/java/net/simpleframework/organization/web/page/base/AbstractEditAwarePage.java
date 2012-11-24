package net.simpleframework.organization.web.page.base;

import static net.simpleframework.common.I18n.$m;
import net.simpleframework.mvc.IForward;
import net.simpleframework.mvc.JavascriptForward;
import net.simpleframework.mvc.MVCContextFactory;
import net.simpleframework.mvc.PageParameter;
import net.simpleframework.mvc.component.ComponentParameter;
import net.simpleframework.mvc.component.ui.tabs.TabItem;
import net.simpleframework.mvc.component.ui.tabs.TabsBean;
import net.simpleframework.mvc.component.ui.window.WindowBean;
import net.simpleframework.mvc.template.AbstractTemplatePage;
import net.simpleframework.organization.OrganizationContextFactory;
import net.simpleframework.organization.web.HttpAccountSession;

/**
 * 这是一个开源的软件，请在LGPLv3下合法使用、修改或重新发布。
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         http://code.google.com/p/simpleframework/
 *         http://www.simpleframework.net
 */
public abstract class AbstractEditAwarePage extends AbstractTemplatePage {

	@Override
	protected void onInit(final PageParameter pParameter) {
		super.onInit(pParameter);

		addAjaxRequest(pParameter, "ajaxLogout").setHandleMethod("logout");

		addAjaxRequest(pParameter, "taAttri_1", UserAttriPage.class);
		addAjaxRequest(pParameter, "taAttri_2", AccountStatPage.class);
		addAjaxRequest(pParameter, "taAttri_3", AccountPasswordPage.class);
		addAjaxRequest(pParameter, "taAttri_4", PhotoPage.class);

		addComponentBean(pParameter, "taAttri", TabsBean.class)
				.addTab(
						new TabItem($m("AbstractEditAwarePage.0")).setContentRef("taAttri_1").setCache(
								true))
				.addTab(
						new TabItem($m("AbstractEditAwarePage.1")).setContentRef("taAttri_2").setCache(
								true))
				.addTab(
						new TabItem($m("AbstractEditAwarePage.2")).setContentRef("taAttri_3").setCache(
								true))
				.addTab(
						new TabItem($m("AbstractEditAwarePage.3")).setContentRef("taAttri_4").setCache(
								true));

		addComponentBean(pParameter, "editUserWindow", WindowBean.class).setContentRef("taAttri")
				.setTitle($m("AbstractEditAwarePage.4")).setHeight(480).setWidth(640);
	}

	/**
	 * AjaxRequest组件的调用方法
	 * 
	 * @param cParameter
	 * @return
	 */
	public IForward logout(final ComponentParameter cParameter) {
		OrganizationContextFactory.get().getAccountMgr().logout(new HttpAccountSession(cParameter));
		return new JavascriptForward("$Actions.loc('").append(
				MVCContextFactory.config().getLoginUrl()).append("');");
	}
}
