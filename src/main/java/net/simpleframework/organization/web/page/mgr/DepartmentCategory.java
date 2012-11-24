package net.simpleframework.organization.web.page.mgr;

import static net.simpleframework.common.I18n.$m;
import static net.simpleframework.organization.IAccountManager.ALL;
import static net.simpleframework.organization.IAccountManager.DEPARTMENT_ID;
import static net.simpleframework.organization.IAccountManager.NO_DEPARTMENT_ID;
import static net.simpleframework.organization.IAccountManager.ONLINE_ID;
import static net.simpleframework.organization.IAccountManager.STATE_DELETE_ID;
import static net.simpleframework.organization.IAccountManager.STATE_LOCKED_ID;
import static net.simpleframework.organization.IAccountManager.STATE_NORMAL_ID;
import static net.simpleframework.organization.IAccountManager.STATE_REGISTRATION_ID;

import java.util.Map;

import net.simpleframework.common.Convert;
import net.simpleframework.common.ado.query.IDataQuery;
import net.simpleframework.common.coll.KVMap;
import net.simpleframework.mvc.IPageHandler.PageSelector;
import net.simpleframework.mvc.component.AbstractComponentBean;
import net.simpleframework.mvc.component.ComponentParameter;
import net.simpleframework.mvc.component.ext.category.ctx.CategoryBeanAwareHandler;
import net.simpleframework.mvc.component.ui.propeditor.EInputCompType;
import net.simpleframework.mvc.component.ui.propeditor.InputComp;
import net.simpleframework.mvc.component.ui.propeditor.PropEditorBean;
import net.simpleframework.mvc.component.ui.propeditor.PropField;
import net.simpleframework.mvc.component.ui.tree.TreeBean;
import net.simpleframework.mvc.component.ui.tree.TreeNode;
import net.simpleframework.mvc.component.ui.tree.TreeNodes;
import net.simpleframework.mvc.template.t1.ext.CategoryTableLCTemplatePage;
import net.simpleframework.organization.EAccountStatus;
import net.simpleframework.organization.EDepartmentType;
import net.simpleframework.organization.IDepartment;
import net.simpleframework.organization.IDepartmentManager;
import net.simpleframework.organization.IOrganizationContext;
import net.simpleframework.organization.OrganizationContextFactory;
import net.simpleframework.organization.web.component.deptselect.DeptSelectUtils;

/**
 * 这是一个开源的软件，请在LGPLv3下合法使用、修改或重新发布。
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         http://code.google.com/p/simpleframework/
 *         http://www.simpleframework.net
 */
public class DepartmentCategory extends CategoryBeanAwareHandler<IDepartment> {
	public static final IOrganizationContext context = OrganizationContextFactory.get();

	@Override
	protected IDepartmentManager beanMgr() {
		return context.getDepartmentMgr();
	}

	@Override
	protected IDataQuery<?> categoryBeans(final ComponentParameter cParameter,
			final Object categoryId) {
		return beanMgr().queryChildren(beanMgr().getBean(categoryId));
	}

	@Override
	public TreeNodes getCategoryTreenodes(final ComponentParameter cParameter,
			final TreeBean treeBean, final TreeNode treeNode) {
		final TreeNodes treeNodes = TreeNodes.of();
		if (treeNode == null) {
			final String[] images = new String[] { "users.png", "user_online.png", "users_nodept.png",
					"dept_root.png" };
			int i = 0;
			final String imgBase = getImgBase(cParameter, AccountMgrPage.class);
			for (final int id : new int[] { ALL, ONLINE_ID, NO_DEPARTMENT_ID, DEPARTMENT_ID }) {
				String text = $m("AccountMgrPage." + id);
				if (id == DEPARTMENT_ID) {
					text += "<br /><a class=\"addbtn a2\" onclick=\"$category_action(this).add();Event.stop(event);\">"
							+ $m("Add") + "</a>";
				}
				final TreeNode treeNode2 = new TreeNode(treeBean, treeNode, text);
				treeNode2.setId(String.valueOf(id));
				treeNode2.setJsClickCallback("$Actions['" + CategoryTableLCTemplatePage.COMPONENT_TABLE
						+ "']('deptId=&type=" + id + "');");
				treeNode2.setImage(imgBase + images[i++]);
				treeNode2.setPostfixText(getPostfixText(id));
				treeNode2.setContextMenu("none");
				treeNode2.setSelect(id == ALL);
				treeNode2.setOpened(id == ALL || id == DEPARTMENT_ID);
				treeNodes.add(treeNode2);
			}
		} else {
			if (treeNode.getId().equals(String.valueOf(ALL))) {
				final String imgBase = getImgBase(cParameter, AccountMgrPage.class);
				final String[] images = new String[] { "users_normal.png", "users_regist.png",
						"users_locked.png", "users_delete.png" };
				int i = 0;
				for (final int id : new int[] { STATE_NORMAL_ID, STATE_REGISTRATION_ID,
						STATE_LOCKED_ID, STATE_DELETE_ID }) {
					final TreeNode treeNode2 = new TreeNode(treeBean, treeNode,
							EAccountStatus.values()[i]);
					treeNode2.setId(String.valueOf(id));
					treeNode2.setImage(imgBase + images[i++]);
					treeNode2.setJsClickCallback("$Actions['"
							+ CategoryTableLCTemplatePage.COMPONENT_TABLE + "']('deptId=&type=" + id
							+ "');");
					treeNode2.setPostfixText(getPostfixText(id));
					treeNode2.setContextMenu("none");
					treeNodes.add(treeNode2);
				}
			} else if (treeNode.getId().equals(String.valueOf(DEPARTMENT_ID))) {
				treeNode.setAcceptdrop(true);
				treeNode.setJsClickCallback("$Actions['" + CategoryTableLCTemplatePage.COMPONENT_TABLE
						+ "']('type=" + DEPARTMENT_ID + "');");
				return super.getCategoryTreenodes(cParameter, treeBean, null);
			} else {
				final Object dataObject = treeNode.getDataObject();
				if (dataObject instanceof IDepartment) {
					final IDepartment dept = (IDepartment) dataObject;
					treeNode.setImage(DeptSelectUtils.icon_dept(cParameter, dept));
					treeNode.setPostfixText(getPostfixText(dept));
					treeNode.setJsClickCallback("$Actions['"
							+ CategoryTableLCTemplatePage.COMPONENT_TABLE + "']('deptId=" + dept.getId()
							+ "');");
					return super.getCategoryTreenodes(cParameter, treeBean, treeNode);
				}
			}
		}
		return treeNodes.size() > 0 ? treeNodes : null;
	}

	private String getPostfixText(final Object type) {
		final int c;
		if (type instanceof Integer) {
			c = context.getAccountMgr().query((Integer) type).getCount();
		} else {
			c = context.getAccountMgr().query((IDepartment) type).getCount();
		}
		return c > 0 ? "(" + c + ")" : null;
	}

	@Override
	public TreeNodes getCategoryDictTreenodes(final ComponentParameter cParameter,
			final TreeBean treeBean, final TreeNode treeNode) {
		final Object dept;
		if (treeNode != null && (dept = treeNode.getDataObject()) instanceof IDepartment) {
			treeNode.setImage(DeptSelectUtils.icon_dept(cParameter, (IDepartment) dept));
		}
		return super.getCategoryTreenodes(cParameter, treeBean, treeNode);
	}

	@Override
	protected void onLoaded_dataBinding(final ComponentParameter cParameter,
			final Map<String, Object> dataBinding, final PageSelector selector, final IDepartment dept) {
		if (dept != null) {
			dataBinding.put("department_type", dept.getDepartmentType());
		}
	}

	@Override
	protected void onSave_setProperties(final ComponentParameter cParameter, final IDepartment dept,
			final boolean insert) {
		dept.setDepartmentType(Convert.toEnum(EDepartmentType.class,
				cParameter.getParameter("department_type")));
	}

	@Override
	public KVMap categoryEdit_attri(final ComponentParameter cParameter) {
		return super.categoryEdit_attri(cParameter).add(window_title, $m("AccountMgrPage.10"))
				.add(window_height, 320);
	}

	@Override
	protected AbstractComponentBean categoryEdit_createPropEditor(final ComponentParameter cParameter) {
		final PropEditorBean editor = (PropEditorBean) super
				.categoryEdit_createPropEditor(cParameter);
		final PropField f = new PropField($m("DepartmentCategory.0")).addComponents(new InputComp(
				"department_type").setType(EInputCompType.select).setDefaultValue(
				EDepartmentType.department, EDepartmentType.organization));
		editor.getFormFields().add(1, f);
		return editor;
	}
}
