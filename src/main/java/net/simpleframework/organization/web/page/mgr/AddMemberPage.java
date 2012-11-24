package net.simpleframework.organization.web.page.mgr;

import static net.simpleframework.common.I18n.$m;

import java.util.ArrayList;

import net.simpleframework.common.Convert;
import net.simpleframework.common.ID;
import net.simpleframework.common.StringUtils;
import net.simpleframework.common.bean.IIdBeanAware;
import net.simpleframework.common.html.js.EJavascriptEvent;
import net.simpleframework.ctx.ado.IBeanManagerAware;
import net.simpleframework.mvc.JavascriptForward;
import net.simpleframework.mvc.PageParameter;
import net.simpleframework.mvc.component.ComponentParameter;
import net.simpleframework.mvc.component.base.validation.EValidatorMethod;
import net.simpleframework.mvc.component.base.validation.Validator;
import net.simpleframework.mvc.component.ui.propeditor.EInputCompType;
import net.simpleframework.mvc.component.ui.propeditor.InputComp;
import net.simpleframework.mvc.component.ui.propeditor.PropEditorBean;
import net.simpleframework.mvc.component.ui.propeditor.PropField;
import net.simpleframework.mvc.template.FormPropEditorTemplatePage;
import net.simpleframework.organization.ERoleMemberType;
import net.simpleframework.organization.IOrganizationContext;
import net.simpleframework.organization.IRole;
import net.simpleframework.organization.IRoleManager;
import net.simpleframework.organization.IRoleMember;
import net.simpleframework.organization.IRoleMemberManager;
import net.simpleframework.organization.OrganizationContextFactory;
import net.simpleframework.organization.web.component.roleselect.RoleSelectBean;
import net.simpleframework.organization.web.component.userselect.UserSelectBean;

/**
 * 这是一个开源的软件，请在LGPLv3下合法使用、修改或重新发布。
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         http://code.google.com/p/simpleframework/
 *         http://www.simpleframework.net
 */
public class AddMemberPage extends FormPropEditorTemplatePage {

	public static IOrganizationContext context = OrganizationContextFactory.get();

	@Override
	protected void onInit(final PageParameter pParameter) {
		super.onInit(pParameter);

		// 验证
		addFormValidationBean(pParameter).addValidators(
				new Validator(EValidatorMethod.required, "#member_val"));

		// 用户选择字典
		addComponentBean(pParameter, "dictUserSelect", UserSelectBean.class)
				.setBindingId("member_id").setBindingText("member_val");
		// 角色选择字典
		addComponentBean(pParameter, "dictRoleSelect", RoleSelectBean.class)
				.setBindingId("member_id").setBindingText("member_val");
	}

	@Override
	protected void initPropEditor(final PageParameter pParameter, final PropEditorBean propEditor) {
		final IRole role = context.getRoleMgr().getBean(pParameter.getParameter("roleId"));
		final PropField f1 = new PropField($m("AddMemberPage.0")).addComponents(
				new InputComp("roleId").setType(EInputCompType.hidden).setDefaultValue(
						String.valueOf(role.getId())),
				new InputComp("member_type").setType(EInputCompType.select).setDefaultValue(
						ERoleMemberType.user, ERoleMemberType.role));
		final PropField f2 = new PropField($m("AddMemberPage.1")).addComponents(
				new InputComp("member_id").setType(EInputCompType.hidden),
				new InputComp("member_val").setType(EInputCompType.textButton).addEvent(
						EJavascriptEvent.click,
						"$Actions[$F('member_type') == '" + ERoleMemberType.user.ordinal()
								+ "' ? 'dictUserSelect' : 'dictRoleSelect']();"));
		final PropField f3 = new PropField($m("AddMemberPage.2")).addComponents(new InputComp(
				"member_primary").setType(EInputCompType.checkbox));
		final PropField f4 = new PropField($m("Description")).addComponents(new InputComp(
				"member_description").setType(EInputCompType.textarea).setAttributes("rows:6"));
		propEditor.getFormFields().append(f1, f2, f3, f4);
	}

	@Override
	public JavascriptForward doSave(final ComponentParameter cParameter) {
		final IRoleManager roleMgr = context.getRoleMgr();

		final IRole role = roleMgr.getBean(cParameter.getParameter("roleId"));
		final ERoleMemberType mType = Convert.toEnum(ERoleMemberType.class,
				cParameter.getParameter("member_type"));

		final boolean primary = Convert.toBool(cParameter.getParameter("member_primary"));
		final String description = cParameter.getParameter("member_description");
		final IBeanManagerAware<?> mgr = (mType == ERoleMemberType.user ? context.getUserMgr()
				: roleMgr);
		final IRoleMemberManager roleMemberMgr = context.getRoleMemberMgr();
		final ArrayList<IRoleMember> beans = new ArrayList<IRoleMember>();
		for (final String id : StringUtils.split(cParameter.getParameter("member_id"), ",")) {
			final ID mId = ((IIdBeanAware) mgr.getBean(id)).getId();
			final IRoleMember rm = roleMemberMgr.createBean();
			rm.setRoleId(role.getId());
			rm.setMemberType(mType);
			rm.setMemberId(mId);
			rm.setPrimaryRole(primary);
			rm.setDescription(description);
			beans.add(rm);
		}
		roleMemberMgr.insert(beans.toArray(new IRoleMember[beans.size()]));

		return super.doSave(cParameter).append("$Actions['ajaxRoleMemberVal']('roleId=")
				.append(role.getId()).append("')");
	}
}
