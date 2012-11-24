package net.simpleframework.organization.web.component.chartselect;

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
public class RoleChartSelectBean extends DictionaryBean {
	private boolean showGlobalChart = true;

	public RoleChartSelectBean(final PageDocument pageDocument, final XmlElement xmlElement) {
		super(pageDocument, xmlElement);

		setTitle($m("RoleChartSelectBean.0"));
		setWidth(280);
		setHeight(360);
		setHandleClass(DefaultRoleChartSelectHandler.class);
	}

	public boolean isShowGlobalChart() {
		return showGlobalChart;
	}

	public void setShowGlobalChart(final boolean showGlobalChart) {
		this.showGlobalChart = showGlobalChart;
	}
}
