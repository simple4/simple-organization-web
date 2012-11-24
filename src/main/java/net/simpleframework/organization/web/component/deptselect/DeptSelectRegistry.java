package net.simpleframework.organization.web.component.deptselect;

import java.util.Collection;

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

/**
 * 这是一个开源的软件，请在LGPLv3下合法使用、修改或重新发布。
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         http://code.google.com/p/simpleframework/
 *         http://www.simpleframework.net
 */
@ComponentName(DeptSelectRegistry.deptSelect)
@ComponentBean(DeptSelectBean.class)
@ComponentResourceProvider(DeptSelectResourceProvider.class)
public class DeptSelectRegistry extends DictionaryRegistry {
	public static final String deptSelect = "deptSelect";

	@Override
	public DeptSelectBean createComponentBean(final PageParameter pParameter, final Object data) {
		final DeptSelectBean deptSelect = (DeptSelectBean) super
				.createComponentBean(pParameter, data);

		final ComponentParameter nComponentParameter = ComponentParameter.get(pParameter, deptSelect);

		final String deptSelectName = (String) nComponentParameter.getBeanProperty("name");

		final TreeBean treeBean = (TreeBean) pParameter.addComponentBean(deptSelectName + "_tree",
				TreeBean.class).setHandleClass(DeptTree.class);

		deptSelect.addTreeRef(nComponentParameter, treeBean.getName());
		treeBean.setAttr("__deptSelect", deptSelect);

		return deptSelect;
	}

	public static class DeptTree extends DictionaryTreeHandler {

		@Override
		public TreeNodes getTreenodes(final ComponentParameter cParameter, final TreeNode treeNode) {
			final TreeBean treeBean = (TreeBean) cParameter.componentBean;
			final ComponentParameter nComponentParameter = ComponentParameter.get(cParameter,
					(DeptSelectBean) treeBean.getAttr("__deptSelect"));
			final IDeptSelectHandle hdl = (IDeptSelectHandle) nComponentParameter
					.getComponentHandler();
			IDepartment parent = null;
			if (treeNode != null) {
				parent = (IDepartment) treeNode.getDataObject();
			}
			final Collection<? extends IDepartment> coll = hdl.getDepartments(nComponentParameter,
					treeBean, parent);
			if (coll != null) {
				final TreeNodes nodes = TreeNodes.of();
				for (final IDepartment d : coll) {
					final TreeNode n = new TreeNode(treeBean, treeNode, d);
					n.setImage(DeptSelectUtils.icon_dept(nComponentParameter, d));
					nodes.add(n);
				}
				return nodes;
			}
			return null;
		}
	}
}
