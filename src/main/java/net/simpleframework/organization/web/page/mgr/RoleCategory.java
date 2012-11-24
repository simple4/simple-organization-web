package net.simpleframework.organization.web.page.mgr;

import static net.simpleframework.common.I18n.$m;

import java.util.Map;

import net.simpleframework.common.Convert;
import net.simpleframework.common.StringUtils;
import net.simpleframework.common.ado.query.IDataQuery;
import net.simpleframework.common.coll.KVMap;
import net.simpleframework.common.html.element.SpanElement;
import net.simpleframework.mvc.IPageHandler.PageSelector;
import net.simpleframework.mvc.PageRequestResponse;
import net.simpleframework.mvc.component.AbstractComponentBean;
import net.simpleframework.mvc.component.ComponentParameter;
import net.simpleframework.mvc.component.ext.category.ctx.CategoryBeanAwareHandler;
import net.simpleframework.mvc.component.ui.propeditor.EInputCompType;
import net.simpleframework.mvc.component.ui.propeditor.InputComp;
import net.simpleframework.mvc.component.ui.propeditor.PropEditorBean;
import net.simpleframework.mvc.component.ui.propeditor.PropField;
import net.simpleframework.mvc.component.ui.tree.TreeBean;
import net.simpleframework.mvc.component.ui.tree.TreeNode;
import net.simpleframework.mvc.component.ui.tree.TreeNodes;
import net.simpleframework.organization.ERoleType;
import net.simpleframework.organization.IDepartment;
import net.simpleframework.organization.IOrganizationContext;
import net.simpleframework.organization.IRole;
import net.simpleframework.organization.IRoleChart;
import net.simpleframework.organization.IRoleChartManager;
import net.simpleframework.organization.IRoleManager;
import net.simpleframework.organization.OrganizationContextFactory;
import net.simpleframework.organization.web.component.chartselect.RoleChartSelectUtils;
import net.simpleframework.organization.web.component.roleselect.RoleSelectUtils;

/**
 * 这是一个开源的软件，请在LGPLv3下合法使用、修改或重新发布。
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         http://code.google.com/p/simpleframework/
 *         http://www.simpleframework.net
 */
public class RoleCategory extends CategoryBeanAwareHandler<IRole> {

	public static final IOrganizationContext context = OrganizationContextFactory.get();

	@Override
	protected IRoleManager beanMgr() {
		return context.getRoleMgr();
	}

	private IRoleChart getRoleChart(final PageRequestResponse rRequest) {
		IRoleChart roleChart = (IRoleChart) rRequest.getRequestAttr("@chartId");
		if (roleChart != null) {
			return roleChart;
		}
		final IRoleChartManager roleChartMgr = context.getRoleChartMgr();
		roleChart = roleChartMgr.getBean(rRequest.getParameter("chartId"));
		if (roleChart == null) {
			roleChart = roleChartMgr.getSystemChart();
		}
		rRequest.setRequestAttr("@chartId", roleChart);
		return roleChart;
	}

	@Override
	protected IDataQuery<?> categoryBeans(final ComponentParameter cParameter,
			final Object categoryId) {
		final IRoleManager roleMgr = beanMgr();
		final IRole parent = roleMgr.getBean(categoryId);
		if (parent == null) {
			return roleMgr.queryRoot(getRoleChart(cParameter));
		} else {
			return roleMgr.queryChildren(parent);
		}
	}

	@Override
	public Map<String, Object> getFormParameters(final ComponentParameter cParameter) {
		return ((KVMap) super.getFormParameters(cParameter)).add("chartId", getRoleChart(cParameter)
				.getId());
	}

	@Override
	public TreeNodes getCategoryTreenodes(final ComponentParameter cParameter,
			final TreeBean treeBean, final TreeNode parent) {
		if (parent == null) {
			final TreeNodes nodes = TreeNodes.of();
			String text = $m("RoleCategory.0");
			text += "<br /><a class=\"addbtn a2\" onclick=\"$category_action(this).add();\">"
					+ $m("RoleCategory.1") + "</a>";
			final TreeNode tn = new TreeNode(treeBean, parent, text);
			tn.setContextMenu("none");
			final IRoleChart chart = getRoleChart(cParameter);
			tn.setImage(RoleChartSelectUtils.icon_chart(cParameter, chart));
			tn.setOpened(true);
			tn.setAcceptdrop(true);
			nodes.add(tn);
			return nodes;
		} else {
			final Object o = parent.getDataObject();
			if (o instanceof IRole) {
				final IRole role = (IRole) o;
				parent.setImage(RoleSelectUtils.icon_role(cParameter, role));
				if (role.getRoleType() == ERoleType.normal) {
					final int count = context.getRoleMemberMgr().queryMembers(role).getCount();
					if (count > 0) {
						parent.setPostfixText("(" + count + ")");
					}
				}
				parent.setJsClickCallback("$Actions['ajaxRoleMemberVal']('roleId="
						+ ((IRole) o).getId() + "');");
			}
			return super.getCategoryTreenodes(cParameter, treeBean, parent);
		}
	}

	@Override
	public TreeNodes getCategoryDictTreenodes(final ComponentParameter cParameter,
			final TreeBean treeBean, final TreeNode treeNode) {
		Object o;
		if (treeNode != null && (o = treeNode.getDataObject()) instanceof IRole) {
			treeNode.setImage(RoleSelectUtils.icon_role(cParameter, (IRole) o));
		}
		return super.getCategoryTreenodes(cParameter, treeBean, treeNode);
	}

	@Override
	protected void onLoaded_dataBinding(final ComponentParameter cParameter,
			final Map<String, Object> dataBinding, final PageSelector selector, final IRole role) {
		if (role != null) {
			dataBinding.put("role_type", role.getRoleType());
			// 该字段不能编辑
			selector.disabledSelector = "#role_type";
		}
	}

	@Override
	protected void onSave_setProperties(final ComponentParameter cParameter, final IRole role,
			final boolean insert) {
		if (insert) {
			role.setRoleChartId(getRoleChart(cParameter).getId());
		}
		final String role_type = cParameter.getParameter("role_type");
		if (StringUtils.hasText(role_type)) {
			role.setRoleType(Convert.toEnum(ERoleType.class, role_type));
		}
	}

	@Override
	protected String[] getContextMenuKeys() {
		return new String[] { "Add", "Edit", "Delete", "-", "Refresh", "-", "Move" };
	}

	@Override
	public Map<String, Object> toJSON(final ComponentParameter cParameter) {
		final IRoleChart roleChart = getRoleChart(cParameter);
		final StringBuilder sb = new StringBuilder();
		sb.append("<div class='nav_arrow'>");
		if (roleChart.getDepartmentId() == null) {
			sb.append($m("RoleChartCategory.0")).append(SpanElement.NAV);
		} else {
			final IDepartment dept = context.getDepartmentMgr().getBean(roleChart.getDepartmentId());
			if (dept != null) {
				sb.append(dept.getText()).append(SpanElement.NAV);
			}
		}
		sb.append(roleChart.getText());
		sb.append("</div>");
		return ((KVMap) super.toJSON(cParameter)).add("title", sb.toString());
	}

	@Override
	protected AbstractComponentBean categoryEdit_createPropEditor(final ComponentParameter cParameter) {
		final PropEditorBean editor = (PropEditorBean) super
				.categoryEdit_createPropEditor(cParameter);
		editor.getFormFields().add(
				2,
				new PropField($m("RoleCategory.2")).addComponents(new InputComp("role_type").setType(
						EInputCompType.select).setDefaultValue(ERoleType.normal, ERoleType.handle,
						ERoleType.script)));
		return editor;
	}

	@Override
	public KVMap categoryEdit_attri(final ComponentParameter cParameter) {
		return super.categoryEdit_attri(cParameter).add(window_title, $m("RoleCategory.3"))
				.add(window_height, 320);
	}
}
