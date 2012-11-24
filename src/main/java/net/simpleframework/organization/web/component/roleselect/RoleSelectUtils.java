package net.simpleframework.organization.web.component.roleselect;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.simpleframework.mvc.PageParameter;
import net.simpleframework.mvc.PageRequestResponse;
import net.simpleframework.mvc.component.ComponentParameter;
import net.simpleframework.mvc.component.ComponentUtils;
import net.simpleframework.organization.ERoleMark;
import net.simpleframework.organization.ERoleType;
import net.simpleframework.organization.IRole;
import net.simpleframework.organization.IRoleChart;
import net.simpleframework.organization.IRoleChartManager;
import net.simpleframework.organization.OrganizationContextFactory;

/**
 * 这是一个开源的软件，请在LGPLv3下合法使用、修改或重新发布。
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         http://code.google.com/p/simpleframework/
 *         http://www.simpleframework.net
 */
public abstract class RoleSelectUtils {
	public static final String BEAN_ID = "roleselect_@bid";

	public static ComponentParameter get(final PageRequestResponse rRequest) {
		return ComponentParameter.get(rRequest, BEAN_ID);
	}

	public static ComponentParameter get(final HttpServletRequest request,
			final HttpServletResponse response) {
		return ComponentParameter.get(request, response, BEAN_ID);
	}

	public static IRoleChart getRoleChart(final ComponentParameter cParameter) {
		final IRoleChartManager roleChartMgr = OrganizationContextFactory.get().getRoleChartMgr();
		IRoleChart roleChart = roleChartMgr.getBean(cParameter.getParameter("chartId"));
		if (roleChart == null) {
			roleChart = roleChartMgr.getRoleChartByName((String) cParameter
					.getBeanProperty("defaultRoleChart"));
		}
		return roleChart != null ? roleChart : roleChartMgr.getSystemChart();
	}

	public static String icon_role(final PageParameter pParameter, final IRole role) {
		final String imgBase = ComponentUtils
				.getCssResourceHomePath(pParameter, RoleSelectBean.class) + "/images/";
		String img;
		final ERoleType rt = role.getRoleType();
		if (rt == ERoleType.handle) {
			img = "role_handle";
		} else if (rt == ERoleType.script) {
			img = "role_script";
		} else {
			img = "role";
		}
		if (role.getRoleMark() == ERoleMark.builtIn) {
			img += "_lock";
		}
		return imgBase + img + ".png";
	}
}
