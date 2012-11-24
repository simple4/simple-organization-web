package net.simpleframework.organization.web.page.base;

import static net.simpleframework.common.I18n.$m;
import net.simpleframework.common.Convert;
import net.simpleframework.mvc.IForward;
import net.simpleframework.mvc.JavascriptForward;
import net.simpleframework.mvc.PageParameter;
import net.simpleframework.mvc.component.ComponentParameter;
import net.simpleframework.mvc.component.base.validation.EValidatorMethod;
import net.simpleframework.mvc.component.base.validation.EWarnType;
import net.simpleframework.mvc.component.base.validation.ValidationBean;
import net.simpleframework.mvc.component.base.validation.Validator;
import net.simpleframework.mvc.component.ui.pwdstrength.PwdStrengthBean;
import net.simpleframework.organization.IAccount;
import net.simpleframework.organization.IAccountManager;
import net.simpleframework.organization.impl.Account;

/**
 * 这是一个开源的软件，请在LGPLv3下合法使用、修改或重新发布。
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         http://code.google.com/p/simpleframework/
 *         http://www.simpleframework.net
 */
public class AccountPasswordPage extends AbstractAccountPage {

	@Override
	protected void onInit(final PageParameter pParameter) {
		super.onInit(pParameter);

		addComponentBean(pParameter, "pwdStrength", PwdStrengthBean.class).setPwdInput(
				"user_password").setContainerId("pwd_strength");

		addComponentBean(pParameter, "userpwdValidation", ValidationBean.class)
				.setTriggerSelector("#_userpwd_save")
				.setWarnType(EWarnType.insertAfter)
				.addValidators(
						new Validator(EValidatorMethod.required,
								"#user_old_password, #user_password, #user_password2"))
				.addValidators(
						new Validator(EValidatorMethod.equals, "#user_password2")
								.setArgs("#user_password"));

		addAjaxRequest(pParameter, "ajaxEditPassword").setConfirmMessage($m("Confirm.Post"))
				.setHandleMethod("saveAction").setSelector(".AccountPasswordPage");
	}

	public IForward saveAction(final ComponentParameter cParameter) {
		final JavascriptForward js = new JavascriptForward(
				"Validation.clearInsert(['user_old_password']);");
		final IAccountManager accountMgr = context.getAccountMgr();
		final IAccount account = getAccount(cParameter);
		final String oldpassword = cParameter.getParameter("user_old_password");
		if (!accountMgr.verifyPassword(account, oldpassword)) {
			js.append("Validation.insertAfter('user_old_password', '")
					.append($m("AccountPasswordPage.6")).append("');");
		} else {
			account.setPassword(Account.encrypt(cParameter.getParameter("user_password")));
			accountMgr.update(new String[] { "password" }, account);
			if (Convert.toBool(cParameter.getParameter("user_SendMail"), false)) {
			}
			js.append("alert($MessageConst['SaveOK']);");
		}
		return js;
	}
}
