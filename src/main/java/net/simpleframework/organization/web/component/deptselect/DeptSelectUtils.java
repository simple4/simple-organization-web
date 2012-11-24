package net.simpleframework.organization.web.component.deptselect;

import net.simpleframework.mvc.PageParameter;
import net.simpleframework.mvc.component.ComponentUtils;
import net.simpleframework.organization.EDepartmentType;
import net.simpleframework.organization.IDepartment;

/**
 * 这是一个开源的软件，请在LGPLv3下合法使用、修改或重新发布。
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         http://code.google.com/p/simpleframework/
 *         http://www.simpleframework.net
 */
public abstract class DeptSelectUtils {

	public static String icon_dept(final PageParameter pParameter, final IDepartment dept) {
		final String imgBase = ComponentUtils
				.getCssResourceHomePath(pParameter, DeptSelectBean.class) + "/images/";
		return imgBase
				+ (dept.getDepartmentType() == EDepartmentType.organization ? "org.gif" : "dept.png");
	}
}
