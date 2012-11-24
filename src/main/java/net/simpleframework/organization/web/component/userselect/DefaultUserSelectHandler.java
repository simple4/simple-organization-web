package net.simpleframework.organization.web.component.userselect;

import net.simpleframework.common.ado.query.IDataQuery;
import net.simpleframework.mvc.component.ComponentParameter;
import net.simpleframework.mvc.component.ui.dictionary.AbstractDictionaryHandler;
import net.simpleframework.mvc.component.ui.pager.TablePagerBean;
import net.simpleframework.organization.IUser;
import net.simpleframework.organization.OrganizationContextFactory;

/**
 * 这是一个开源的软件，请在LGPLv3下合法使用、修改或重新发布。
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         http://code.google.com/p/simpleframework/
 *         http://www.simpleframework.net
 */
public class DefaultUserSelectHandler extends AbstractDictionaryHandler implements
		IUserSelectHandle {

	@Override
	public IDataQuery<? extends IUser> users(final ComponentParameter cParameter,
			final TablePagerBean tablePager) {
		return OrganizationContextFactory.get().getUserMgr().query();
	}
}
