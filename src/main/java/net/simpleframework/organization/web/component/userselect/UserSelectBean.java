package net.simpleframework.organization.web.component.userselect;

import static net.simpleframework.common.I18n.$m;
import net.simpleframework.common.xml.XmlElement;
import net.simpleframework.mvc.PageDocument;
import net.simpleframework.mvc.component.ui.dictionary.DictionaryBean;

/**
 * 这是一个开源的软件，请在LGPLv3下合法使用、修改或重新发布。
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         http://code.google.com/p/simpleframework/
 *         http://www.simpleframework.net
 */
public class UserSelectBean extends DictionaryBean {

	private int pageItems;

	private boolean showCheckbox = true;

	public UserSelectBean(final PageDocument pageDocument, final XmlElement xmlElement) {
		super(pageDocument, xmlElement);
		setShowHelpTooltip(false);
		setTitle($m("UserSelectBean.0"));
		setMinWidth(360);
		setWidth(360);
		setHeight(445);
		setPageItems(30);
		setHandleClass(DefaultUserSelectHandler.class);
	}

	public int getPageItems() {
		return pageItems;
	}

	public UserSelectBean setPageItems(final int pageItems) {
		this.pageItems = pageItems;
		return this;
	}

	public boolean isShowCheckbox() {
		return showCheckbox;
	}

	public UserSelectBean setShowCheckbox(final boolean showCheckbox) {
		this.showCheckbox = showCheckbox;
		return this;
	}
}
