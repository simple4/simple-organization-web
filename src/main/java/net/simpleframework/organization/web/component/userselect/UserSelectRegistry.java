package net.simpleframework.organization.web.component.userselect;

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
@ComponentName(UserSelectRegistry.userSelect)
@ComponentBean(UserSelectBean.class)
@ComponentResourceProvider(UserSelectResourceProvider.class)
public class UserSelectRegistry extends DictionaryRegistry {
	public static final String userSelect = "userSelect";

	@Override
	public UserSelectBean createComponentBean(final PageParameter pageParameter, final Object data) {
		final UserSelectBean userSelect = (UserSelectBean) super.createComponentBean(pageParameter,
				data);
		userSelect.setContentStyle("padding: 0;");
		final AjaxRequestBean ajaxRequest = (AjaxRequestBean) userSelect.getAttr("$$ajaxRequest");
		if (ajaxRequest != null) {
			ajaxRequest.setUrlForward(ComponentUtils.getResourceHomePath(UserSelectBean.class)
					+ "/jsp/user_select.jsp?" + UserSelectUtils.BEAN_ID + "=" + userSelect.hashId());
		}
		return userSelect;
	}
}
