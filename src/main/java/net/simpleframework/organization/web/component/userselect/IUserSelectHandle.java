package net.simpleframework.organization.web.component.userselect;

import net.simpleframework.common.ado.query.IDataQuery;
import net.simpleframework.mvc.component.ComponentParameter;
import net.simpleframework.mvc.component.ui.dictionary.IDictionaryHandle;
import net.simpleframework.mvc.component.ui.pager.TablePagerBean;
import net.simpleframework.organization.IUser;

/**
 * 这是一个开源的软件，请在LGPLv3下合法使用、修改或重新发布。
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         http://code.google.com/p/simpleframework/
 *         http://www.simpleframework.net
 */
public interface IUserSelectHandle extends IDictionaryHandle {

	/**
	 * 获取用户列表，缺省实现获取所有用户
	 * 
	 * @param cParameter
	 * @param tablePager
	 * @return
	 */
	IDataQuery<? extends IUser> users(ComponentParameter cParameter, TablePagerBean tablePager);
}
