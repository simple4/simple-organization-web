package net.simpleframework.organization.web.component.deptselect;

import java.util.Collection;

import net.simpleframework.common.ado.query.DataQueryUtils;
import net.simpleframework.mvc.component.ComponentParameter;
import net.simpleframework.mvc.component.ui.dictionary.AbstractDictionaryHandler;
import net.simpleframework.mvc.component.ui.tree.TreeBean;
import net.simpleframework.organization.IDepartment;
import net.simpleframework.organization.OrganizationContextFactory;

/**
 * 这是一个开源的软件，请在LGPLv3下合法使用、修改或重新发布。
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         http://code.google.com/p/simpleframework/
 *         http://www.simpleframework.net
 */
public class DefaultDeptSelectHandler extends AbstractDictionaryHandler implements
		IDeptSelectHandle {

	@Override
	public Collection<? extends IDepartment> getDepartments(final ComponentParameter cParameter,
			final TreeBean treeBean, final IDepartment parent) {
		return DataQueryUtils.toList(OrganizationContextFactory.get().getDepartmentMgr()
				.queryChildren(parent));
	}
}
