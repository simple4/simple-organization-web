package net.simpleframework.organization.web.component.roleselect;

import java.util.Collection;

import net.simpleframework.mvc.component.ComponentParameter;
import net.simpleframework.mvc.component.ui.dictionary.IDictionaryHandle;
import net.simpleframework.organization.IRole;
import net.simpleframework.organization.IRoleChart;

/**
 * 这是一个开源的软件，请在LGPLv3下合法使用、修改或重新发布。
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         http://code.google.com/p/simpleframework/
 *         http://www.simpleframework.net
 */
public interface IRoleSelectHandle extends IDictionaryHandle {

	/**
	 * 获取指定视图的角色列表
	 * 
	 * @param cParameter
	 * @param roleChart
	 * @param parent
	 * @return
	 */
	Collection<? extends IRole> roles(ComponentParameter cParameter, IRoleChart roleChart,
			IRole parent);
}
