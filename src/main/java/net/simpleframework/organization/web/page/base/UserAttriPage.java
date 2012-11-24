package net.simpleframework.organization.web.page.base;

import static net.simpleframework.common.I18n.$m;

import java.util.Map;

import net.simpleframework.mvc.IPageHandler.PageSelector;
import net.simpleframework.mvc.JavascriptForward;
import net.simpleframework.mvc.PageParameter;
import net.simpleframework.mvc.component.ComponentParameter;
import net.simpleframework.mvc.template.struct.TableRows;
import net.simpleframework.organization.web.page.AbstractAccountAttriPage;

/**
 * 这是一个开源的软件，请在LGPLv3下合法使用、修改或重新发布。
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         http://code.google.com/p/simpleframework/
 *         http://www.simpleframework.net
 */
public class UserAttriPage extends AbstractAccountAttriPage {

	@Override
	public JavascriptForward doSave(final ComponentParameter cParameter) {
		super.doSave(cParameter);
		return new JavascriptForward("alert('").append($m("UserAttriPage.0")).append("');");
	}

	@Override
	public void onLoad(final PageParameter pParameter, final Map<String, Object> dataBinding,
			final PageSelector selector) {
		super.onLoad(pParameter, dataBinding, selector);
		selector.readonlySelector = "#ae_accountName";
	}

	@Override
	protected TableRows tableRows(final PageParameter pParameter) {
		return TableRows.of(r1, r3, r4, r5, r6, r7, r8, r9);
	}
}
