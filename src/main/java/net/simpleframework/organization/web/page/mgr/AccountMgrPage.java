package net.simpleframework.organization.web.page.mgr;

import static net.simpleframework.common.I18n.$m;

import java.util.Map;

import net.simpleframework.common.Convert;
import net.simpleframework.common.StringUtils;
import net.simpleframework.common.ado.query.IDataQuery;
import net.simpleframework.common.coll.KVMap;
import net.simpleframework.common.html.element.ETextAlign;
import net.simpleframework.common.html.element.ElementList;
import net.simpleframework.common.html.element.LabelElement;
import net.simpleframework.common.html.element.LinkElement;
import net.simpleframework.ctx.permission.IPermissionHandler;
import net.simpleframework.mvc.IForward;
import net.simpleframework.mvc.JavascriptForward;
import net.simpleframework.mvc.PageParameter;
import net.simpleframework.mvc.component.ComponentParameter;
import net.simpleframework.mvc.component.base.ajaxrequest.AjaxRequestBean;
import net.simpleframework.mvc.component.ui.pager.AbstractTablePagerSchema;
import net.simpleframework.mvc.component.ui.pager.TablePagerColumn;
import net.simpleframework.mvc.component.ui.window.WindowBean;
import net.simpleframework.mvc.template.struct.LinkButton;
import net.simpleframework.mvc.template.struct.LinkButtons;
import net.simpleframework.mvc.template.struct.NavigationButtons;
import net.simpleframework.mvc.template.struct.TabButton;
import net.simpleframework.mvc.template.struct.TabButtons;
import net.simpleframework.mvc.template.t1.ext.CategoryTableLCTemplatePage;
import net.simpleframework.mvc.template.t1.ext.CategoryTablePagerHandler;
import net.simpleframework.organization.EAccountStatus;
import net.simpleframework.organization.IAccount;
import net.simpleframework.organization.IAccountManager;
import net.simpleframework.organization.IDepartment;
import net.simpleframework.organization.IOrganizationContext;
import net.simpleframework.organization.IUser;
import net.simpleframework.organization.OrganizationContextFactory;

/**
 * 这是一个开源的软件，请在LGPLv3下合法使用、修改或重新发布。
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         http://code.google.com/p/simpleframework/
 *         http://www.simpleframework.net
 */
public class AccountMgrPage extends CategoryTableLCTemplatePage {
	public static IOrganizationContext context = OrganizationContextFactory.get();

	@Override
	protected void onInit(final PageParameter pParameter) {
		super.onInit(pParameter);

		addCategoryBean(pParameter, DepartmentCategory.class);

		// 账号列表
		addTablePagerBean(pParameter, AccountList.class)
				.addColumn(
						new TablePagerColumn("name", $m("AccountMgrPage.1"), 120)
								.setTextAlign(ETextAlign.left))
				.addColumn(
						new TablePagerColumn("u.text", $m("AccountMgrPage.2"), 120)
								.setTextAlign(ETextAlign.left))
				.addColumn(new TablePagerColumn("createDate", $m("AccountMgrPage.3"), 120))
				.addColumn(new TablePagerColumn("lastLoginDate", $m("AccountMgrPage.4"), 120))
				.addColumn(new TablePagerColumn("status", $m("AccountMgrPage.5"), 80))
				.addColumn(new TablePagerColumn("loginTimes", $m("AccountMgrPage.6"), 80))
				.addColumn(new TablePagerColumn("u.email", $m("AccountMgrPage.7"), 130))
				.addColumn(new TablePagerColumn("u.mobile", $m("AccountMgrPage.8"), 130))
				.addColumn(TablePagerColumn.BLANK);

		// 添加账号
		addComponentBean(pParameter, "accountEditPage", AjaxRequestBean.class).setUrlForward(
				uriFor(AccountEditPage.class));
		addComponentBean(pParameter, "accountEdit", WindowBean.class)
				.setContentRef("accountEditPage").setTitle($m("AccountMgrPage.9")).setHeight(450)
				.setWidth(640);

		// 删除账号
		addAjaxRequest(pParameter, "deleteAccount").setConfirmMessage($m("Confirm.Delete"))
				.setHandleMethod("doDeleteAccount");
		// 取消删除
		addAjaxRequest(pParameter, "undeleteAccount").setHandleMethod("doUndeleteAccount");
	}

	@Override
	public String getRole(final PageParameter pParameter) {
		return IPermissionHandler.sj_all_account;
	}

	public IForward doDeleteAccount(final ComponentParameter cParameter) {
		final Object[] ids = StringUtils.split(cParameter.getParameter("id"));
		if (ids != null) {
			context.getAccountMgr().delete(ids);
		}
		return new JavascriptForward("$Actions['").append(COMPONENT_TABLE).append("']();");
	}

	public IForward doUndeleteAccount(final ComponentParameter cParameter) {
		final Object[] ids = StringUtils.split(cParameter.getParameter("id"));
		if (ids != null) {
			context.getAccountMgr().undelete(ids);
		}
		return new JavascriptForward("$Actions['").append(COMPONENT_TABLE).append("']();");
	}

	@Override
	protected LinkButtons getToolbarButtons(final PageParameter pParameter) {
		final Object s = pParameter.getRequestAttr("select_category");
		final LinkButton add = new LinkButton($m("AccountMgrPage.11"));
		final LinkButtons btns = LinkButtons.of(add, LinkButton.SEP).append(
				act_btn("deleteAccount", $m("Delete")));
		if (s instanceof IDepartment) {
			add.setOnclick("$Actions['accountEdit']('deptId=" + ((IDepartment) s).getId() + "');");
		} else {
			add.setOnclick("$Actions['accountEdit']();");
			final int type = Convert.toInt(s);
			if (type == IAccountManager.STATE_DELETE_ID) {
				btns.append(LinkButton.SEP, act_btn("undeleteAccount", $m("AccountMgrPage.12")));
			}
		}
		return btns;
	}

	public static class AccountList extends CategoryTablePagerHandler {

		@Override
		public IDataQuery<?> createDataObjectQuery(final ComponentParameter cParameter) {
			final String deptId = cParameter.getParameter("deptId");
			IDepartment dept;
			if (StringUtils.hasText(deptId)
					&& (dept = context.getDepartmentMgr().getBean(deptId)) != null) {
				cParameter.setRequestAttr("select_category", dept);
				return context.getAccountMgr().query(dept);
			} else {
				final int type = Convert.toInt(cParameter.getParameter("type"), IAccountManager.ALL);
				cParameter.setRequestAttr("select_category", type);
				return context.getAccountMgr().query(type);
			}
		}

		@Override
		public Map<String, Object> getFormParameters(final ComponentParameter cParameter) {
			final Map<String, Object> m = super.getFormParameters(cParameter);
			final Object s = cParameter.getRequestAttr("select_category");
			if (s instanceof IDepartment) {
				m.put("deptId", ((IDepartment) s).getId());
			} else {
				m.put("type", s);
			}
			return m;
		}

		@Override
		protected ElementList navigationTitle(final ComponentParameter cParameter) {
			final ElementList eles = ElementList.of(new LinkElement($m("AccountMgrPage."
					+ IAccountManager.ALL)).setOnclick("$Actions['" + COMPONENT_TABLE
					+ "']('deptId=&type=" + IAccountManager.ALL + "');"));
			final Object s = cParameter.getRequestAttr("select_category");
			if (s instanceof IDepartment) {
				eles.append(new LabelElement(s));
			} else {
				final int type = (Integer) s;
				if (type != IAccountManager.ALL) {
					if (type >= IAccountManager.STATE_DELETE_ID
							&& type <= IAccountManager.STATE_NORMAL_ID) {
						eles.append(new LabelElement(
								EAccountStatus.values()[IAccountManager.STATE_NORMAL_ID - type]));
					} else {
						eles.append(new LabelElement($m("AccountMgrPage." + type)));
					}
				}
			}
			return eles;
		}

		@Override
		public AbstractTablePagerSchema createTablePagerSchema() {
			return new DefaultTablePagerSchema() {
				@Override
				public Map<String, Object> getRowData(final ComponentParameter cParameter,
						final Object dataObject) {
					final IAccount account = (IAccount) dataObject;
					final IUser user = context.getAccountMgr().getUser(account.getId());
					final KVMap kv = new KVMap();
					kv.add(
							"name",
							account.getStatus() != EAccountStatus.delete ? new LinkElement(account
									.getName()).setOnclick("$Actions['accountEdit']('accountId="
									+ account.getId() + "');") : account.getName());
					kv.add("createDate", account.getCreateDate());
					kv.add("lastLoginDate", account.getLastLoginDate());
					kv.add("status", account.getStatus());
					kv.add("loginTimes", account.getLoginTimes());
					kv.add("u.text", user.getText());
					kv.add("u.email", user.getEmail());
					kv.add("u.mobile", user.getMobile());
					return kv;
				}
			};
		}
	}

	@Override
	public NavigationButtons getNavigationBar(final PageParameter pParameter) {
		return super.getNavigationBar(pParameter).append(new LinkElement($m("AccountMgrPage.0")));
	}

	@Override
	protected TabButtons getTabButtons(final PageParameter pParameter) {
		return TabButtons.of(new TabButton($m("AccountMgrPage.0"), uriFor(AccountMgrPage.class)),
				new TabButton($m("RoleMgrPage.0"), uriFor(RoleMgrPage.class)));
	}
}
