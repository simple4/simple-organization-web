package net.simpleframework.organization.web.component.userselect;

import static net.simpleframework.common.I18n.$m;

import java.util.Map;

import net.simpleframework.common.ado.query.IDataQuery;
import net.simpleframework.common.coll.KVMap;
import net.simpleframework.common.html.element.ETextAlign;
import net.simpleframework.mvc.DefaultPageHandler;
import net.simpleframework.mvc.PageParameter;
import net.simpleframework.mvc.component.ComponentParameter;
import net.simpleframework.mvc.component.ui.pager.AbstractTablePagerSchema;
import net.simpleframework.mvc.component.ui.pager.EPagerBarLayout;
import net.simpleframework.mvc.component.ui.pager.TablePagerBean;
import net.simpleframework.mvc.component.ui.pager.TablePagerColumn;
import net.simpleframework.mvc.component.ui.pager.db.AbstractDbTablePagerHandler;
import net.simpleframework.organization.IDepartment;
import net.simpleframework.organization.IUser;
import net.simpleframework.organization.OrganizationContextFactory;

/**
 * 这是一个开源的软件，请在LGPLv3下合法使用、修改或重新发布。
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         http://code.google.com/p/simpleframework/
 *         http://www.simpleframework.net
 */
public class UserSelectLoaded extends DefaultPageHandler {

	@Override
	public void beforeComponentRender(final PageParameter pParameter) {
		final ComponentParameter cParameter = UserSelectUtils.get(pParameter);
		final UserSelectBean userSelect = (UserSelectBean) cParameter.componentBean;

		final String userSelectName = (String) cParameter.getBeanProperty("name");

		final TablePagerBean tablePager = (TablePagerBean) pParameter
				.addComponentBean(userSelectName + "_tablePager", TablePagerBean.class)
				.setJsRowDblclick("$Actions['" + userSelectName + "'].doDblclick(item);")
				.setShowLineNo(true).setPagerBarLayout(EPagerBarLayout.top).setExportAction("false")
				.setContainerId("users_" + userSelect.hashId()).setHandleClass(UserList.class);
		tablePager.addColumn(
				new TablePagerColumn("text", $m("UserSelectLoaded.0")).setTextAlign(ETextAlign.left)
						.setWidth(120)).addColumn(
				new TablePagerColumn("departmentText", $m("UserSelectLoaded.1"))
						.setColumnSqlName("departmentId"));
	}

	public static class UserList extends AbstractDbTablePagerHandler {

		@Override
		public Object getBeanProperty(final ComponentParameter cParameter, final String beanProperty) {
			if ("pageItems".equals(beanProperty) || "showCheckbox".equals(beanProperty)) {
				final ComponentParameter nComponentParameter = UserSelectUtils.get(cParameter);
				return nComponentParameter.getBeanProperty(beanProperty);
			}
			return super.getBeanProperty(cParameter, beanProperty);
		}

		@Override
		public Map<String, Object> getFormParameters(final ComponentParameter cParameter) {
			return ((KVMap) super.getFormParameters(cParameter)).add(UserSelectUtils.BEAN_ID,
					cParameter.getParameter(UserSelectUtils.BEAN_ID));
		}

		@Override
		public IDataQuery<?> createDataObjectQuery(final ComponentParameter cParameter) {
			final ComponentParameter nComponentParameter = UserSelectUtils.get(cParameter);
			return ((IUserSelectHandle) nComponentParameter.getComponentHandler()).users(
					nComponentParameter, (TablePagerBean) cParameter.componentBean);
		}

		@Override
		public AbstractTablePagerSchema createTablePagerSchema() {
			return new DefaultDbTablePagerSchema() {
				@Override
				public Map<String, Object> getRowData(final ComponentParameter cParameter,
						final Object dataObject) {
					final IUser user = (IUser) dataObject;
					final KVMap kv = new KVMap();
					kv.put("text", user.getText());
					final IDepartment dept = OrganizationContextFactory.get().getDepartmentMgr()
							.getBean(user.getDepartmentId());
					if (dept != null) {
						kv.put("departmentText", dept.getText());
					}
					return kv;
				}

				@Override
				public Map<String, Object> getRowAttributes(final ComponentParameter compParameter,
						final Object dataObject) {
					final Map<String, Object> attributes = super.getRowAttributes(compParameter,
							dataObject);
					final IUser user = (IUser) dataObject;
					attributes.put("userText", user.getText());
					return attributes;
				}
			};
		}
	}
}
