package net.simpleframework.organization.web.page.mgr;

import net.simpleframework.common.Convert;
import net.simpleframework.mvc.JavascriptForward;
import net.simpleframework.mvc.PageParameter;
import net.simpleframework.mvc.component.ComponentParameter;
import net.simpleframework.mvc.template.struct.TableRows;
import net.simpleframework.mvc.template.t1.ext.CategoryTableLCTemplatePage;
import net.simpleframework.organization.web.component.deptselect.DeptSelectBean;
import net.simpleframework.organization.web.page.AbstractAccountAttriPage;

/**
 * 这是一个开源的软件，请在LGPLv3下合法使用、修改或重新发布。
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         http://code.google.com/p/simpleframework/
 *         http://www.simpleframework.net
 */
public class AccountEditPage extends AbstractAccountAttriPage {

	@Override
	protected void onInit(final PageParameter pParameter) {
		super.onInit(pParameter);

		addComponentBean(pParameter, "deptSelectDict", DeptSelectBean.class).setBindingId(
				"ue_departmentId").setBindingText("id_departmentText");
	}

	@Override
	public JavascriptForward doSave(final ComponentParameter cParameter) {
		super.doSave(cParameter);
		final JavascriptForward js = new JavascriptForward();
		js.append("$Actions['").append(CategoryTableLCTemplatePage.COMPONENT_TABLE).append("']();");
		if (Convert.toBool(cParameter.getParameter(OPT_NEXT))) {
			js.append(jsFormReset(cParameter));
			js.append("$('ae_accountName').focus();");
		} else {
			js.append("$Actions['accountEdit'].close();");
		}
		return js;
	}

	@Override
	protected boolean show_opt_next(final PageParameter pParameter) {
		return getAccount(pParameter) == null;
	}

	@Override
	protected TableRows tableRows(final PageParameter pParameter) {
		return TableRows.of(r1, r2, r3, r4, r5, r6, r7, r8, r9);
	}
}