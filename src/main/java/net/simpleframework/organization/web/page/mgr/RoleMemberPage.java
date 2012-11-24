package net.simpleframework.organization.web.page.mgr;

import static net.simpleframework.common.I18n.$m;

import java.io.IOException;
import java.util.Map;

import net.simpleframework.common.StringUtils;
import net.simpleframework.common.ado.query.IDataQuery;
import net.simpleframework.common.coll.KVMap;
import net.simpleframework.ctx.ado.IBeanManagerAware;
import net.simpleframework.mvc.AbstractMVCPage;
import net.simpleframework.mvc.IForward;
import net.simpleframework.mvc.JavascriptForward;
import net.simpleframework.mvc.PageParameter;
import net.simpleframework.mvc.PageRequestResponse;
import net.simpleframework.mvc.component.ComponentParameter;
import net.simpleframework.mvc.component.ui.pager.AbstractTablePagerSchema;
import net.simpleframework.mvc.component.ui.pager.EPagerBarLayout;
import net.simpleframework.mvc.component.ui.pager.TablePagerBean;
import net.simpleframework.mvc.component.ui.pager.TablePagerColumn;
import net.simpleframework.mvc.component.ui.pager.db.AbstractDbTablePagerHandler;
import net.simpleframework.mvc.component.ui.window.WindowBean;
import net.simpleframework.mvc.template.AbstractTemplatePage;
import net.simpleframework.organization.ERoleMemberType;
import net.simpleframework.organization.ERoleType;
import net.simpleframework.organization.IOrganizationContext;
import net.simpleframework.organization.IRole;
import net.simpleframework.organization.IRoleHandler;
import net.simpleframework.organization.IRoleManager;
import net.simpleframework.organization.IRoleMember;
import net.simpleframework.organization.IRoleMemberManager;
import net.simpleframework.organization.OrganizationContextFactory;

/**
 * 这是一个开源的软件，请在LGPLv3下合法使用、修改或重新发布。
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         http://code.google.com/p/simpleframework/
 *         http://www.simpleframework.net
 */
public class RoleMemberPage extends AbstractTemplatePage {
	public static IOrganizationContext context = OrganizationContextFactory.get();

	@Override
	protected void onInit(final PageParameter pParameter) {
		super.onInit(pParameter);

		addAjaxRequest(pParameter, "ajax_addMemberPage", AddMemberPage.class);
		addComponentBean(pParameter, "addMemberWindow", WindowBean.class)
				.setContentRef("ajax_addMemberPage").setTitle($m("RoleMemberPage.1")).setHeight(300)
				.setWidth(320);

		// 删除成员
		addAjaxRequest(pParameter, "ajax_deleteMember").setConfirmMessage($m("Confirm.Delete"))
				.setHandleMethod("doMemberDelete");

		addAjaxRequest(pParameter, "ajax_editPrimaryRole").setHandleMethod("doPrimaryRole");

		// 成员列表
		final TablePagerBean tablePager = (TablePagerBean) addComponentBean(pParameter,
				"memberTable", TablePagerBean.class).setShowLineNo(true).setShowBorder(true)
				.setPagerBarLayout(EPagerBarLayout.none).setPageItems(Integer.MAX_VALUE)
				.setContainerId("idMemberTable").setHandleClass(MemberTable.class);
		tablePager.addColumn(new TablePagerColumn("memberType", $m("RoleMemberPage.2"), 120))
				.addColumn(new TablePagerColumn("memberId", $m("RoleMemberPage.3"), 120))
				.addColumn(new TablePagerColumn("primaryRole", $m("RoleMemberPage.4"), 120))
				.addColumn(new TablePagerColumn("description", $m("Description")).setSort(false));

		// 保存规则角色
		addAjaxRequest(pParameter, "ajax_roleSave").setHandleMethod("doRoleSave");

	}

	public IForward doMemberDelete(final ComponentParameter cParameter) {
		final Object[] ids = StringUtils.split(cParameter.getParameter("mId"), ";");
		if (ids != null) {
			context.getRoleMemberMgr().delete(ids);
		}
		return new JavascriptForward("$Actions['memberTable']();");
	}

	public IForward doPrimaryRole(final ComponentParameter cParameter) {
		final IRoleMemberManager roleMemberMgr = context.getRoleMemberMgr();
		roleMemberMgr.setPrimary(roleMemberMgr.getBean(cParameter.getParameter("mId")));
		return new JavascriptForward("$Actions['memberTable']();");
	}

	public IForward doRoleSave(final ComponentParameter cParameter) {
		final IRoleManager roleMgr = context.getRoleMgr();
		final IRole role = roleMgr.getBean(cParameter.getParameter("roleId"));
		if (role != null) {
			final String ruleValue = cParameter.getParameter("role_ruleValue");
			if (role.getRoleType() == ERoleType.handle) {
				role.setRuleHandler(ruleValue);
				roleMgr.update(new String[] { "rulehandler" }, role);
			} else {
				role.setRuleScript(ruleValue);
				roleMgr.update(new String[] { "rulescript" }, role);
			}
			return new JavascriptForward("alert('").append($m("RoleMemberPage.12")).append("');");
		} else {
			return null;
		}
	}

	private IRole roleCache(final PageRequestResponse rRequest) {
		IRole role = (IRole) rRequest.getRequestAttr("@roleId");
		if (role != null) {
			return role;
		}
		final IRoleManager roleMgr = context.getRoleMgr();
		role = roleMgr.getBean(rRequest.getParameter("roleId"));
		if (role != null) {
			rRequest.setRequestAttr("@roleId", role);
		}
		return role;
	}

	@Override
	protected String toHtml(final PageParameter pParameter, final Map<String, Object> variables,
			final String variable) throws IOException {
		final StringBuilder sb = new StringBuilder();
		sb.append("<div class='tb'>");
		sb.append("<div class='nav_arrow'>");
		final IRole role = roleCache(pParameter);
		ERoleType rt = null;
		if (role != null) {
			rt = role.getRoleType();
			sb.append(role.getText());
			sb.append("&nbsp;&nbsp;(&nbsp;<span class='rt'>").append(rt).append("</span>&nbsp;)");
		} else {
			sb.append("#(RoleMemberPage.0)");
		}

		sb.append("</div><div class='btn'>");
		final String aClass = "simple_btn simple_btn_all";
		if (rt == ERoleType.normal) {
			sb.append("<a class='").append(aClass)
					.append("' onclick=\"$Actions['addMemberWindow']('roleId=").append(role.getId())
					.append("');\">#(RoleMemberPage.1)</a>");
			sb.append("&nbsp;<a class='").append(aClass)
					.append("' onclick=\"this.up('.RoleMgrPage').deleteMember();\">#(Delete)</a>");
		} else if (rt == ERoleType.handle) {
			sb.append("<a class='").append(aClass).append("' onclick=\"")
					.append("$Actions['ajax_roleSave']($Form('#idRoleMemberVal .rule'));")
					.append("\">#(RoleMemberPage.8)</a>");
		} else if (rt == ERoleType.script) {
			sb.append("<a class='").append(aClass).append("' onclick=\"")
					.append("$Actions['ajax_roleSave']($Form('#idRoleMemberVal .rule'));")
					.append("\">#(RoleMemberPage.8)</a>");
			sb.append("&nbsp;<a class='").append(aClass).append("' onclick=\"")
					.append("\">#(RoleMemberPage.9)</a>");
		}
		sb.append("</div></div>");

		if (rt == ERoleType.normal) {
			sb.append("<div id='idMemberTable'></div>");
		} else {
			sb.append("<div  class='rule'>");
			if (role != null) {
				sb.append("<input type='hidden' name='roleId' value='");
				sb.append(role.getId()).append("' />");
			}
			if (rt == ERoleType.handle) {
				sb.append("<div class='t'>#(RoleMemberPage.10)&nbsp;")
						.append(IRoleHandler.class.getName()).append("</div>");
				sb.append("<div class='c'><textarea name='role_ruleValue' rows='1'>");
				sb.append(StringUtils.blank(role.getRuleHandler())).append("</textarea>");
			} else if (rt == ERoleType.script) {
				sb.append("<div class='t'>#(RoleMemberPage.11)</div>");
				sb.append("<div class='c'><textarea name='role_ruleValue' rows='14'>");
				sb.append(StringUtils.blank(role.getRuleScript())).append("</textarea>");
			}
			sb.append("</div></div>");
		}
		return sb.toString();
	}

	public static class MemberTable extends AbstractDbTablePagerHandler {

		@Override
		public Map<String, Object> getFormParameters(final ComponentParameter cParameter) {
			final IRole role = AbstractMVCPage.get(RoleMemberPage.class).roleCache(cParameter);
			return ((KVMap) super.getFormParameters(cParameter)).add("roleId", role.getId());
		}

		@Override
		public IDataQuery<?> createDataObjectQuery(final ComponentParameter cParameter) {
			final IRole role = AbstractMVCPage.get(RoleMemberPage.class).roleCache(cParameter);
			return context.getRoleMgr().members(role);
		}

		@Override
		public AbstractTablePagerSchema createTablePagerSchema() {
			return new DefaultDbTablePagerSchema() {
				@Override
				public Map<String, Object> getRowData(final ComponentParameter cParameter,
						final Object dataObject) {
					final IRoleMember rm = (IRoleMember) dataObject;
					final KVMap kv = new KVMap();
					final ERoleMemberType mType = rm.getMemberType();
					kv.put("memberType", mType);
					final IBeanManagerAware<?> mgr = (mType == ERoleMemberType.user ? context
							.getUserMgr() : context.getRoleMgr());
					kv.put("memberId", mgr.getBean(rm.getMemberId()));
					if (mType == ERoleMemberType.user) {
						final StringBuilder sb = new StringBuilder();
						final boolean pr = rm.isPrimaryRole();
						sb.append(pr ? $m("RoleMemberPage.5") : $m("RoleMemberPage.6"));
						if (!pr) {
							sb.append("&nbsp;&nbsp;<a class='simple_btn simple_btn_all' ")
									.append("onclick=\"$Actions['ajax_editPrimaryRole']('mId=")
									.append(rm.getId()).append("');\">#(RoleMemberPage.7)</a>");
						}
						kv.put("primaryRole", sb.toString());
					}
					kv.put("description", rm.getDescription());
					return kv;
				}
			};
		}
	}
}
