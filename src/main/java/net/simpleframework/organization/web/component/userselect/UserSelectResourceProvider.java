package net.simpleframework.organization.web.component.userselect;

import net.simpleframework.mvc.PageParameter;
import net.simpleframework.mvc.component.IComponentRegistry;
import net.simpleframework.mvc.component.ui.dictionary.DictionaryResourceProvider;

/**
 * 这是一个开源的软件，请在LGPLv3下合法使用、修改或重新发布。
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         http://code.google.com/p/simpleframework/
 *         http://www.simpleframework.net
 */
public class UserSelectResourceProvider extends DictionaryResourceProvider {

	public UserSelectResourceProvider(final IComponentRegistry componentRegistry) {
		super(componentRegistry);
	}

	@Override
	public String[] getCssPath(final PageParameter pParameter) {
		return new String[] { getCssResourceHomePath(pParameter) + "/user_select.css" };
	}
}
