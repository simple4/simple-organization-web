package net.simpleframework.organization.web.component.roleselect;

import net.simpleframework.mvc.PageParameter;
import net.simpleframework.mvc.component.ComponentBean;
import net.simpleframework.mvc.component.ComponentName;
import net.simpleframework.mvc.component.ComponentResourceProvider;
import net.simpleframework.mvc.component.ComponentUtils;
import net.simpleframework.mvc.component.base.ajaxrequest.AjaxRequestBean;
import net.simpleframework.mvc.component.ui.dictionary.DictionaryRegistry;

/**
 * 这是一个开源的软件，请在LGPLv3下合法使用、修改或重新发布。
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         http://code.google.com/p/simpleframework/
 *         http://www.simpleframework.net
 */
@ComponentName(RoleSelectRegistry.roleSelect)
@ComponentBean(RoleSelectBean.class)
@ComponentResourceProvider(RoleSelectResourceProvider.class)
public class RoleSelectRegistry extends DictionaryRegistry {
	public static final String roleSelect = "roleSelect";

	@Override
	public RoleSelectBean createComponentBean(final PageParameter pParameter, final Object data) {
		final RoleSelectBean roleSelect = (RoleSelectBean) super
				.createComponentBean(pParameter, data);

		final AjaxRequestBean ajaxRequest = (AjaxRequestBean) roleSelect.getAttr("$$ajaxRequest");
		if (ajaxRequest != null) {
			ajaxRequest.setUrlForward(ComponentUtils.getResourceHomePath(RoleSelectBean.class)
					+ "/jsp/role_select.jsp?" + RoleSelectUtils.BEAN_ID + "=" + roleSelect.hashId());
		}
		return roleSelect;
	}
}
