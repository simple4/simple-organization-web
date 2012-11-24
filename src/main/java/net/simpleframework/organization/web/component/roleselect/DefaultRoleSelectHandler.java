package net.simpleframework.organization.web.component.roleselect;

import java.util.Collection;

import net.simpleframework.common.ado.query.DataQueryUtils;
import net.simpleframework.mvc.component.ComponentParameter;
import net.simpleframework.mvc.component.ui.dictionary.AbstractDictionaryHandler;
import net.simpleframework.organization.IOrganizationContext;
import net.simpleframework.organization.IRole;
import net.simpleframework.organization.IRoleChart;
import net.simpleframework.organization.OrganizationContextFactory;

/**
 * 这是一个开源的软件，请在LGPLv3下合法使用、修改或重新发布。
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         http://code.google.com/p/simpleframework/
 *         http://www.simpleframework.net
 */
public class DefaultRoleSelectHandler extends AbstractDictionaryHandler implements
		IRoleSelectHandle {
	public static IOrganizationContext context = OrganizationContextFactory.get();

	@Override
	public Collection<? extends IRole> roles(final ComponentParameter cParameter,
			final IRoleChart roleChart, final IRole parent) {
		return DataQueryUtils.toList(parent == null ? context.getRoleMgr().queryRoot(roleChart)
				: context.getRoleMgr().queryChildren(parent));
	}
}
