package net.simpleframework.organization.web.page;

import static net.simpleframework.common.I18n.$m;

import java.util.Date;
import java.util.Enumeration;
import java.util.Map;

import net.simpleframework.common.Convert;
import net.simpleframework.common.bean.BeanUtils;
import net.simpleframework.common.coll.ParameterMap;
import net.simpleframework.common.html.element.EInputType;
import net.simpleframework.common.html.element.InputElement;
import net.simpleframework.mvc.IPageHandler.PageSelector;
import net.simpleframework.mvc.JavascriptForward;
import net.simpleframework.mvc.PageParameter;
import net.simpleframework.mvc.component.ComponentParameter;
import net.simpleframework.mvc.component.base.validation.EValidatorMethod;
import net.simpleframework.mvc.component.base.validation.Validator;
import net.simpleframework.mvc.component.ui.calendar.CalendarBean;
import net.simpleframework.mvc.template.FormTableRowTemplatePage;
import net.simpleframework.mvc.template.struct.CalendarInput;
import net.simpleframework.mvc.template.struct.RowField;
import net.simpleframework.mvc.template.struct.TableRow;
import net.simpleframework.mvc.template.struct.TextButtonInput;
import net.simpleframework.organization.IAccount;
import net.simpleframework.organization.IDepartment;
import net.simpleframework.organization.IOrganizationContext;
import net.simpleframework.organization.IUser;
import net.simpleframework.organization.OrganizationContextFactory;

/**
 * 这是一个开源的软件，请在LGPLv3下合法使用、修改或重新发布。
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         http://code.google.com/p/simpleframework/
 *         http://www.simpleframework.net
 */
public abstract class AbstractAccountAttriPage extends FormTableRowTemplatePage {
	public static IOrganizationContext context = OrganizationContextFactory.get();

	@Override
	protected void onInit(final PageParameter pParameter) {
		super.onInit(pParameter);

		addComponentBean(pParameter, "cal_Birthday", CalendarBean.class);

		addFormValidationBean(pParameter)
				.addValidators(
						new Validator(EValidatorMethod.required,
								"#ae_accountName, #ue_text, #ue_email, #ue_mobile"))
				.addValidators(
						new Validator(EValidatorMethod.min_length, "#ae_accountName, #ue_text", "2"))
				.addValidators(new Validator(EValidatorMethod.email, "#ue_email, #ue_msn"))
				.addValidators(new Validator(EValidatorMethod.mobile_phone, "#ue_mobile"))
				.addValidators(new Validator(EValidatorMethod.phone, "#ue_homePhone, #ue_officePhone"))
				.addValidators(new Validator(EValidatorMethod.date, "#ue_birthday", "yyyy-MM-dd"));
	}

	@Override
	public JavascriptForward doSave(final ComponentParameter cParameter) {
		final ParameterMap userData = new ParameterMap();
		final Enumeration<?> e = cParameter.getParameterNames();
		while (e.hasMoreElements()) {
			final String k = (String) e.nextElement();
			if (k.startsWith("ue_")) {
				userData.put(k.substring(3), cParameter.getParameter(k));
			}
		}
		context.getAccountMgr().doSave(cParameter.getParameter("ae_id"),
				cParameter.getParameter("ae_accountName"), cParameter.getParameter("ae_password"),
				null, userData);
		return super.doSave(cParameter);
	}

	protected IAccount getAccount(final PageParameter pParameter) {
		return context.getAccountMgr().getBean(pParameter.getParameter("accountId"));
	}

	@Override
	public void onLoad(final PageParameter pParameter, final Map<String, Object> dataBinding,
			final PageSelector selector) {
		final IAccount account = getAccount(pParameter);
		IDepartment dept = null;
		if (account != null) {
			dataBinding.put("ae_id", account.getId());
			dataBinding.put("ae_accountName", account.getName());
			dataBinding.put("ae_password", account.getPassword());

			final IUser user = context.getAccountMgr().getUser(account.getId());
			dept = context.getDepartmentMgr().getBean(user.getDepartmentId());
			final Map<String, Object> kv = BeanUtils.toMap(user);
			for (final String k : kv.keySet()) {
				Object o = kv.get(k);
				if (o instanceof Date) {
					o = Convert.toDateString((Date) o, "yyyy-MM-dd");
				}
				dataBinding.put("ue_" + k, o);
			}
		}
		if (dept == null) {
			dept = context.getDepartmentMgr().getBean(pParameter.getParameter("deptId"));
		}
		if (dept != null) {
			dataBinding.put("ue_departmentId", dept.getId());
			dataBinding.put("id_departmentText", dept.getText());
		}
	}

	protected final TableRow r1 = new TableRow(new RowField($m("AccountEditPage.0"),
			new InputElement("ae_id", EInputType.hidden), new InputElement("ae_accountName")),
			new RowField($m("AccountEditPage.1"), new InputElement("ue_text")));

	protected final TableRow r2 = new TableRow(new RowField($m("AccountEditPage.2"),
			new InputElement("ae_password", EInputType.password)), new RowField(
			$m("AccountEditPage.3"), new TextButtonInput("id_departmentText").setHiddenField(
					"ue_departmentId").setOnclick("$Actions['deptSelectDict']();")));

	protected final TableRow r3 = new TableRow(new RowField($m("AccountEditPage.4"),
			new InputElement("ue_email")), new RowField($m("AccountEditPage.5"), new InputElement(
			"ue_mobile")));

	protected final TableRow r4 = new TableRow(new RowField($m("AccountEditPage.6"),
			new InputElement("ue_sex", EInputType.select).setText($m("AccountEditPage.16") + ";"
					+ $m("AccountEditPage.17"))), new RowField($m("AccountEditPage.7"),
			new CalendarInput("ue_birthday").setCalendarComponent("cal_Birthday")));

	protected final TableRow r5 = new TableRow(new RowField($m("AccountEditPage.8"),
			new InputElement("ue_hometown")), new RowField($m("AccountEditPage.9"), new InputElement(
			"ue_postcode")));

	protected final TableRow r6 = new TableRow(new RowField($m("AccountEditPage.10"),
			new InputElement("ue_homePhone")), new RowField($m("AccountEditPage.11"),
			new InputElement("ue_officePhone")));

	protected final TableRow r7 = new TableRow(new RowField($m("AccountEditPage.12"),
			new InputElement("ue_qq")), new RowField($m("AccountEditPage.13"), new InputElement(
			"ue_msn")));

	protected final TableRow r8 = new TableRow(new RowField($m("AccountEditPage.14"),
			new InputElement("ue_address")));

	protected final TableRow r9 = new TableRow(new RowField($m("AccountEditPage.15"),
			new InputElement("ue_description", EInputType.textarea)));
}
