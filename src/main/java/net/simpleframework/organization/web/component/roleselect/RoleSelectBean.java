package net.simpleframework.organization.web.component.roleselect;

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
public class RoleSelectBean extends DictionaryBean {

	private String roleChartHandler;

	/**
	 * 缺省的视图，默认为全局视图
	 */
	private String defaultRoleChart;

	public RoleSelectBean(final PageDocument pageDocument, final XmlElement xmlElement) {
		super(pageDocument, xmlElement);
		setTitle($m("RoleSelectBean.0"));
		setContentStyle("padding: 0;");
		setWidth(280);
		setHeight(360);
		setHandleClass(DefaultRoleSelectHandler.class);
	}

	public String getDefaultRoleChart() {
		return defaultRoleChart;
	}

	public RoleSelectBean setDefaultRoleChart(final String defaultRoleChart) {
		this.defaultRoleChart = defaultRoleChart;
		return this;
	}

	public String getRoleChartHandler() {
		return roleChartHandler;
	}

	public RoleSelectBean setRoleChartHandler(final String roleChartHandler) {
		this.roleChartHandler = roleChartHandler;
		return this;
	}
}
