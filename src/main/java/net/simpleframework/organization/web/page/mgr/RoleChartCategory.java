package net.simpleframework.organization.web.page.mgr;

import static net.simpleframework.common.I18n.$m;
import net.simpleframework.common.ado.query.DataQueryUtils;
import net.simpleframework.common.coll.KVMap;
import net.simpleframework.mvc.component.AbstractComponentBean;
import net.simpleframework.mvc.component.ComponentParameter;
import net.simpleframework.mvc.component.ext.category.ctx.CategoryBeanAwareHandler;
import net.simpleframework.mvc.component.ui.menu.AbstractMenuHandler;
import net.simpleframework.mvc.component.ui.menu.MenuBean;
import net.simpleframework.mvc.component.ui.menu.MenuItem;
import net.simpleframework.mvc.component.ui.menu.MenuItems;
import net.simpleframework.mvc.component.ui.propeditor.PropEditorBean;
import net.simpleframework.mvc.component.ui.tree.ITreeNodeAttributesCallback;
import net.simpleframework.mvc.component.ui.tree.TreeBean;
import net.simpleframework.mvc.component.ui.tree.TreeNode;
import net.simpleframework.mvc.component.ui.tree.TreeNodes;
import net.simpleframework.organization.IDepartment;
import net.simpleframework.organization.IOrganizationContext;
import net.simpleframework.organization.IRoleChart;
import net.simpleframework.organization.IRoleChartManager;
import net.simpleframework.organization.IRoleManager;
import net.simpleframework.organization.OrganizationContextFactory;
import net.simpleframework.organization.web.component.chartselect.RoleChartSelectUtils;

/**
 * 这是一个开源的软件，请在LGPLv3下合法使用、修改或重新发布。
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         http://code.google.com/p/simpleframework/
 *         http://www.simpleframework.net
 */
public class RoleChartCategory extends CategoryBeanAwareHandler<IRoleChart> {

	public static final IOrganizationContext context = OrganizationContextFactory.get();

	@Override
	protected IRoleChartManager beanMgr() {
		return context.getRoleChartMgr();
	}

	@Override
	public TreeNodes getCategoryTreenodes(final ComponentParameter cParameter,
			final TreeBean treeBean, final TreeNode parent) {
		final TreeNodes nodes = TreeNodes.of();
		if (parent == null) {
			String text = $m("RoleChartCategory.0");
			text += "<br/><a class=\"addbtn a2\" onclick=\"$category_action(this).add();Event.stop(event);\">"
					+ $m("RoleChartCategory.1") + "</a>";
			TreeNode tn = new TreeNode(treeBean, parent, text);
			tn.setOpened(true);
			tn.setImage(RoleChartSelectUtils.icon_chart(cParameter, "chart_g.png"));
			tn.setContextMenu("none");
			nodes.add(tn);
			tn = new TreeNode(treeBean, parent, $m("RoleChartCategory.2"));
			tn.setOpened(true);
			tn.setImage(RoleChartSelectUtils.icon_chart(cParameter, "chart_d.png"));
			tn.setContextMenu("none");
			nodes.add(tn);
		} else {
			final String image = parent.getImage();
			if (image != null) {
				if (image.endsWith("chart_g.png")) {
					nodes.addAll(rolecharts(cParameter, treeBean, parent, null));
				} else if (image.endsWith("chart_d.png")) {
					nodes.addAll(departments(cParameter, treeBean, parent, null));
				} else {
					final Object dept = parent != null ? parent.getDataObject() : null;
					if (dept instanceof IDepartment) {
						nodes.addAll(rolecharts(cParameter, treeBean, parent, (IDepartment) dept));
						nodes.addAll(departments(cParameter, treeBean, parent, (IDepartment) dept));
					}
				}
			}
		}
		return nodes.size() > 0 ? nodes : null;
	}

	private TreeNodes rolecharts(final ComponentParameter cParameter, final TreeBean treeBean,
			final TreeNode parent, final IDepartment dept) {
		final String contextMenu = treeBean.getContextMenu();
		final IRoleManager roleMgr = context.getRoleMgr();
		return RoleChartSelectUtils.rolecharts(cParameter, treeBean, parent,
				DataQueryUtils.toList(beanMgr().query(dept)), new ITreeNodeAttributesCallback() {
					@Override
					public void setAttributes(final TreeNode tn) {
						tn.setContextMenu(contextMenu);
						final IRoleChart chart = (IRoleChart) tn.getDataObject();
						final int c = roleMgr.queryRoles(chart).getCount();
						if (c > 0) {
							tn.setPostfixText("(" + c + ")");
						}
						tn.setJsClickCallback("$Actions['roleCategory']('chartId=" + chart.getId()
								+ "');");
					}
				});
	}

	private TreeNodes departments(final ComponentParameter cParameter, final TreeBean treeBean,
			final TreeNode parent, final IDepartment dept) {
		return RoleChartSelectUtils.departments(cParameter, treeBean, parent,
				DataQueryUtils.toList(context.getDepartmentMgr().queryChildren(dept)),
				new ITreeNodeAttributesCallback() {
					@Override
					public void setAttributes(final TreeNode tn) {
						tn.setContextMenu("roleChartCategory_DeptMenu");
					}
				});
	}

	@Override
	protected String[] getContextMenuKeys() {
		return new String[] { "Edit", "Delete", "-", "Refresh", "-", "Move" };
	}

	@Override
	protected void onSave_setProperties(final ComponentParameter cParameter,
			final IRoleChart roleChart, final boolean insert) {
		if (insert) {
			final IDepartment dept = context.getDepartmentMgr().getBean(
					cParameter.getParameter(PARAM_CATEGORY_PARENTID));
			if (dept != null) {
				roleChart.setDepartmentId(dept.getId());
			}
		}
	}

	@Override
	public KVMap categoryEdit_attri(final ComponentParameter cParameter) {
		return super.categoryEdit_attri(cParameter).add(window_title, $m("RoleMgrPage.1"))
				.add(window_height, 260);
	}

	@Override
	protected AbstractComponentBean categoryEdit_createPropEditor(final ComponentParameter cParameter) {
		final PropEditorBean editor = (PropEditorBean) super
				.categoryEdit_createPropEditor(cParameter);
		editor.getFormFields().remove(2);
		return editor;
	}

	public static class DeptContextMenu extends AbstractMenuHandler {
		@Override
		public MenuItems getMenuItems(final ComponentParameter cParameter, final MenuItem menuItem) {
			if (menuItem == null) {
				final MenuBean menuBean = (MenuBean) cParameter.componentBean;
				return MenuItems.of().append(
						new MenuItem(menuBean).setTitle($m("RoleChartCategory.1")).setJsSelectCallback(
								"$category_action(item).add();"));
			}
			return null;
		}
	}
}
