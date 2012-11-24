package net.simpleframework.organization.web.component.chartselect;

import static net.simpleframework.common.I18n.$m;

import java.util.Map;

import net.simpleframework.common.coll.KVMap;
import net.simpleframework.mvc.PageParameter;
import net.simpleframework.mvc.component.ComponentBean;
import net.simpleframework.mvc.component.ComponentName;
import net.simpleframework.mvc.component.ComponentParameter;
import net.simpleframework.mvc.component.ComponentResourceProvider;
import net.simpleframework.mvc.component.ui.dictionary.DictionaryRegistry;
import net.simpleframework.mvc.component.ui.dictionary.DictionaryTreeHandler;
import net.simpleframework.mvc.component.ui.tree.TreeBean;
import net.simpleframework.mvc.component.ui.tree.TreeNode;
import net.simpleframework.mvc.component.ui.tree.TreeNodes;
import net.simpleframework.organization.IDepartment;
import net.simpleframework.organization.IRoleChart;

/**
 * 这是一个开源的软件，请在LGPLv3下合法使用、修改或重新发布。
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         http://code.google.com/p/simpleframework/
 *         http://www.simpleframework.net
 */
@ComponentName(RoleChartSelectRegistry.roleChartSelect)
@ComponentBean(RoleChartSelectBean.class)
@ComponentResourceProvider(RoleChartSelectResourceProvider.class)
public class RoleChartSelectRegistry extends DictionaryRegistry {
	public static final String roleChartSelect = "roleChartSelect";

	@Override
	public RoleChartSelectBean createComponentBean(final PageParameter pParameter, final Object data) {
		final RoleChartSelectBean roleChart = (RoleChartSelectBean) super.createComponentBean(
				pParameter, data);

		final ComponentParameter nComponentParameter = ComponentParameter.get(pParameter, roleChart);

		final String chartSelectName = (String) nComponentParameter.getBeanProperty("name");

		final TreeBean treeBean = (TreeBean) pParameter.addComponentBean(chartSelectName + "_tree",
				TreeBean.class).setHandleClass(RoleChartTree.class);

		roleChart.addTreeRef(nComponentParameter, treeBean.getName());
		treeBean.setAttr("__roleChart", roleChart);
		return roleChart;
	}

	public static class RoleChartTree extends DictionaryTreeHandler {

		@Override
		public TreeNodes getTreenodes(final ComponentParameter cParameter, final TreeNode parent) {
			final TreeNodes nodes = TreeNodes.of();
			final TreeBean treeBean = (TreeBean) cParameter.componentBean;
			final ComponentParameter nComponentParameter = ComponentParameter.get(cParameter,
					(RoleChartSelectBean) treeBean.getAttr("__roleChart"));
			final IRoleChartSelectHandle hdl = (IRoleChartSelectHandle) nComponentParameter
					.getComponentHandler();
			if (parent == null) {
				final boolean showGlobalChart = (Boolean) nComponentParameter
						.getBeanProperty("showGlobalChart");
				if (showGlobalChart) {
					final TreeNode n = new TreeNode(treeBean, null, $m("RoleChartSelectRegistry.0"));
					n.setImage(RoleChartSelectUtils.icon_chart(nComponentParameter, "chart_g.png"));
					n.setOpened(true);
					nodes.add(n);
				}
				final TreeNode n = new TreeNode(treeBean, null, $m("RoleChartSelectRegistry.1"));
				n.setImage(RoleChartSelectUtils.icon_chart(nComponentParameter, "chart_d.png"));
				n.setOpened(true);
				nodes.add(n);
			} else {
				final String image = parent.getImage();
				if (image != null) {
					if (image.endsWith("chart_g.png")) {
						nodes.addAll(RoleChartSelectUtils.rolecharts(cParameter, treeBean, parent,
								hdl.getRoleCharts(cParameter, treeBean, null), null));
					} else if (image.endsWith("chart_d.png")) {
						nodes.addAll(RoleChartSelectUtils.departments(nComponentParameter, treeBean,
								parent, hdl.getDepartments(nComponentParameter, treeBean, null), null));
					} else {
						final Object o = parent.getDataObject();
						if (o instanceof IDepartment) {
							nodes.addAll(RoleChartSelectUtils.rolecharts(cParameter, treeBean, parent,
									hdl.getRoleCharts(cParameter, treeBean, (IDepartment) o), null));
							nodes.addAll(RoleChartSelectUtils.departments(nComponentParameter, treeBean,
									parent,
									hdl.getDepartments(nComponentParameter, treeBean, (IDepartment) o), null));
						}
					}
				}
			}
			return nodes;
		}

		@Override
		public Map<String, Object> getTreenodeAttributes(final ComponentParameter cParameter,
				final TreeNode treeNode) {
			final KVMap kv = (KVMap) super.getTreenodeAttributes(cParameter, treeNode);
			if (treeNode == null || !(treeNode.getDataObject() instanceof IRoleChart)) {
				disabled_selected(kv);
			}
			return kv;
		}
	}
}
