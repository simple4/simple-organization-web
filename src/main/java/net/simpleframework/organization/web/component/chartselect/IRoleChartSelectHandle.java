package net.simpleframework.organization.web.component.chartselect;

import java.util.Collection;

import net.simpleframework.mvc.component.ComponentParameter;
import net.simpleframework.mvc.component.ui.dictionary.IDictionaryHandle;
import net.simpleframework.mvc.component.ui.tree.TreeBean;
import net.simpleframework.organization.IDepartment;
import net.simpleframework.organization.IRoleChart;

/**
 * 这是一个开源的软件，请在LGPLv3下合法使用、修改或重新发布。
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         http://code.google.com/p/simpleframework/
 *         http://www.simpleframework.net
 */
public interface IRoleChartSelectHandle extends IDictionaryHandle {

	/**
	 * 获取角色试图
	 * 
	 * @param cParameter
	 * @param treeBean
	 * @param department
	 *           department==null返回全局视图
	 * @return
	 */
	Collection<? extends IRoleChart> getRoleCharts(ComponentParameter cParameter, TreeBean treeBean,
			IDepartment department);

	/**
	 * 获取部门树
	 * 
	 * @param cParameter
	 * @param treeBean
	 * @param parent
	 * @return
	 */
	Collection<? extends IDepartment> getDepartments(ComponentParameter cParameter,
			TreeBean treeBean, IDepartment parent);
}
