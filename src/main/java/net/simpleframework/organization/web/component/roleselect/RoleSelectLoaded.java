package net.simpleframework.organization.web.component.roleselect;

import java.util.Collection;

import net.simpleframework.common.StringUtils;
import net.simpleframework.mvc.DefaultPageHandler;
import net.simpleframework.mvc.PageParameter;
import net.simpleframework.mvc.component.ComponentParameter;
import net.simpleframework.mvc.component.ui.tree.AbstractTreeHandler;
import net.simpleframework.mvc.component.ui.tree.TreeBean;
import net.simpleframework.mvc.component.ui.tree.TreeNode;
import net.simpleframework.mvc.component.ui.tree.TreeNodes;
import net.simpleframework.organization.IRole;
import net.simpleframework.organization.web.component.chartselect.RoleChartSelectBean;

/**
 * 这是一个开源的软件，请在LGPLv3下合法使用、修改或重新发布。
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         http://code.google.com/p/simpleframework/
 *         http://www.simpleframework.net
 */
public class RoleSelectLoaded extends DefaultPageHandler {

	@Override
	public void beforeComponentRender(final PageParameter pParameter) {
		super.beforeComponentRender(pParameter);

		final ComponentParameter nComponentParameter = RoleSelectUtils.get(pParameter);
		final String hashId = nComponentParameter.hashId();
		final String selectName = (String) nComponentParameter.getBeanProperty("name");

		// roleChart选择器
		final RoleChartSelectBean roleChartSelect = (RoleChartSelectBean) pParameter
				.addComponentBean(selectName + "_chart", RoleChartSelectBean.class).setClearAction(
						"false");

		final StringBuilder sb = new StringBuilder();
		sb.append("var s = selects[0];");
		sb.append("$Actions['").append(selectName).append("_tree'].refresh('chartId=' + s.id);");
		sb.append("$Actions['").append(selectName).append("_chart'].trigger.update(s.text);");
		sb.append("return true;");
		roleChartSelect.setJsSelectCallback(sb.toString());

		final String roleChartHandle = (String) nComponentParameter
				.getBeanProperty("roleChartHandler");
		if (StringUtils.hasText(roleChartHandle)) {
			roleChartSelect.setHandleClass(roleChartHandle);
		}

		// 角色树
		pParameter.addComponentBean(selectName + "_tree", TreeBean.class)
				.setContainerId("container_" + hashId).setHandleClass(RoleSelectTree.class)
				.setSelector(".role_select form");
	}

	public static class RoleSelectTree extends AbstractTreeHandler {
		@Override
		public TreeNodes getTreenodes(final ComponentParameter cParameter, final TreeNode parent) {
			final ComponentParameter nComponentParameter = RoleSelectUtils.get(cParameter);
			final IRoleSelectHandle hdl = (IRoleSelectHandle) nComponentParameter
					.getComponentHandler();
			final Collection<? extends IRole> coll = hdl.roles(nComponentParameter, RoleSelectUtils
					.getRoleChart(nComponentParameter),
					(parent == null ? null : (IRole) parent.getDataObject()));
			if (coll != null) {
				final String name = (String) nComponentParameter.getBeanProperty("name");
				final TreeNodes nodes = TreeNodes.of();
				for (final IRole r : coll) {
					final TreeNode tn = new TreeNode((TreeBean) cParameter.componentBean, parent, r);
					tn.setImage(RoleSelectUtils.icon_role(nComponentParameter, r));
					tn.setJsDblclickCallback("selected_" + name + "(branch, ev);");
					nodes.add(tn);
				}
				return nodes;
			}
			return null;
		}
	}
}
