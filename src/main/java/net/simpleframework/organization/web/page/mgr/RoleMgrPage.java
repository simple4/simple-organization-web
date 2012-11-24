package net.simpleframework.organization.web.page.mgr;

import static net.simpleframework.common.I18n.$m;

import java.io.IOException;
import java.util.Map;

import net.simpleframework.common.html.element.LinkElement;
import net.simpleframework.ctx.permission.IPermissionHandler;
import net.simpleframework.mvc.PageParameter;
import net.simpleframework.mvc.component.base.ajaxrequest.AjaxRequestBean;
import net.simpleframework.mvc.component.base.jspinclude.PageIncludeBean;
import net.simpleframework.mvc.component.ext.category.CategoryBean;
import net.simpleframework.mvc.component.ui.menu.MenuBean;
import net.simpleframework.mvc.template.struct.NavigationButtons;
import net.simpleframework.mvc.template.struct.TabButton;
import net.simpleframework.mvc.template.struct.TabButtons;
import net.simpleframework.mvc.template.t1.ResizedLCTemplatePage;
import net.simpleframework.organization.IOrganizationContext;
import net.simpleframework.organization.OrganizationContextFactory;
import net.simpleframework.organization.web.page.mgr.RoleChartCategory.DeptContextMenu;

/**
 * 这是一个开源的软件，请在LGPLv3下合法使用、修改或重新发布。
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         http://code.google.com/p/simpleframework/
 *         http://www.simpleframework.net
 */
public class RoleMgrPage extends ResizedLCTemplatePage {
	public static IOrganizationContext context = OrganizationContextFactory.get();

	@Override
	protected void onInit(final PageParameter pParameter) {
		super.onInit(pParameter);

		addImportCSS(getCssResourceHomePath(pParameter) + "/role_mgr.css");

		// 创建roleChart tree
		addComponentBean(pParameter, "roleChartCategory", CategoryBean.class).setDraggable(false)
				.setContainerId("idRoleChartCategory").setHandleClass(RoleChartCategory.class);

		addComponentBean(pParameter, "roleChartCategory_DeptMenu", MenuBean.class).setHandleClass(
				DeptContextMenu.class);

		// 创建role tree
		addComponentBean(pParameter, "roleCategory", CategoryBean.class).setContainerId(
				"idRoleCategory").setHandleClass(RoleCategory.class);

		addComponentBean(pParameter, "roleMemberVal", PageIncludeBean.class).setPageUrl(
				uriFor(RoleMemberPage.class)).setContainerId("idRoleMemberVal");
		addComponentBean(pParameter, "ajaxRoleMemberVal", AjaxRequestBean.class).setUrlForward(
				uriFor(RoleMemberPage.class)).setUpdateContainerId("idRoleMemberVal");
	}

	@Override
	public String getRole(final PageParameter pParameter) {
		return IPermissionHandler.sj_all_account;
	}

	@Override
	protected String toHtml(final PageParameter pParameter, final Map<String, Object> variables,
			final String variable) throws IOException {
		if ("content_left".equals(variable)) {
			return "<div id='idRoleChartCategory' style='padding: 6px;'></div>";
		}
		return null;
	}

	@Override
	public NavigationButtons getNavigationBar(final PageParameter pParameter) {
		return super.getNavigationBar(pParameter).append(new LinkElement($m("RoleMgrPage.0")));
	}

	@Override
	protected TabButtons getTabButtons(final PageParameter pParameter) {
		return TabButtons.of(new TabButton($m("AccountMgrPage.0"), uriFor(AccountMgrPage.class)),
				new TabButton($m("RoleMgrPage.0"), uriFor(RoleMgrPage.class)));
	}
}
