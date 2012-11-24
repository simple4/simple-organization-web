package net.simpleframework.organization.web.component.login;

import net.simpleframework.common.html.element.ETextAlign;
import net.simpleframework.common.xml.XmlElement;
import net.simpleframework.mvc.PageDocument;
import net.simpleframework.mvc.component.AbstractContainerBean;

/**
 * 这是一个开源的软件，请在LGPLv3下合法使用、修改或重新发布。
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         http://code.google.com/p/simpleframework/
 *         http://www.simpleframework.net
 */
public class LoginBean extends AbstractContainerBean {

	private boolean showAutoLogin = true;

	private boolean showResetAction = true;

	private boolean showGetPassword = true;

	private ETextAlign actionAlign;

	private String loginForward = "/";

	private String jsLoginCallback;

	public LoginBean(final PageDocument pageDocument, final XmlElement element) {
		super(pageDocument, element);
		setHandleClass(DefaultLoginHandler.class);
	}

	public LoginBean(final PageDocument pageDocument) {
		this(pageDocument, null);
	}

	public boolean isShowAutoLogin() {
		return showAutoLogin;
	}

	public LoginBean setShowAutoLogin(final boolean showAutoLogin) {
		this.showAutoLogin = showAutoLogin;
		return this;
	}

	public boolean isShowResetAction() {
		return showResetAction;
	}

	public LoginBean setShowResetAction(final boolean showResetAction) {
		this.showResetAction = showResetAction;
		return this;
	}

	public String getLoginForward() {
		return loginForward;
	}

	public LoginBean setLoginForward(final String loginForward) {
		this.loginForward = loginForward;
		return this;
	}

	public ETextAlign getActionAlign() {
		return actionAlign == null ? ETextAlign.right : actionAlign;
	}

	public LoginBean setActionAlign(final ETextAlign actionAlign) {
		this.actionAlign = actionAlign;
		return this;
	}

	public boolean isShowGetPassword() {
		return showGetPassword;
	}

	public LoginBean setShowGetPassword(final boolean showGetPassword) {
		this.showGetPassword = showGetPassword;
		return this;
	}

	public String getJsLoginCallback() {
		return jsLoginCallback;
	}

	public void setJsLoginCallback(final String jsLoginCallback) {
		this.jsLoginCallback = jsLoginCallback;
	}

	@Override
	protected String[] elementAttributes() {
		return new String[] { "loginForward", "jsLoginCallback" };
	}
}
