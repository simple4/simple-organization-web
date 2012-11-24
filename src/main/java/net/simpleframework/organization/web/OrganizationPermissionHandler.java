package net.simpleframework.organization.web;

import static net.simpleframework.common.I18n.$m;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

import net.simpleframework.common.ID;
import net.simpleframework.common.bean.IIdBeanAware;
import net.simpleframework.ctx.permission.PermissionRole;
import net.simpleframework.ctx.permission.PermissionUser;
import net.simpleframework.mvc.PageRequestResponse;
import net.simpleframework.mvc.ctx.permission.DefaultPagePermissionHandler;
import net.simpleframework.organization.EAccountStatus;
import net.simpleframework.organization.EAccountType;
import net.simpleframework.organization.EDepartmentType;
import net.simpleframework.organization.IAccount;
import net.simpleframework.organization.IAccountManager;
import net.simpleframework.organization.IDepartment;
import net.simpleframework.organization.IDepartmentManager;
import net.simpleframework.organization.IOrganizationContext;
import net.simpleframework.organization.IRole;
import net.simpleframework.organization.IRoleManager;
import net.simpleframework.organization.IUser;
import net.simpleframework.organization.OrganizationContextFactory;
import net.simpleframework.organization.OrganizationException;

/**
 * 这是一个开源的软件，请在LGPLv3下合法使用、修改或重新发布。
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         http://code.google.com/p/simpleframework/
 *         http://www.simpleframework.net
 */
public class OrganizationPermissionHandler extends DefaultPagePermissionHandler {

	public static final IOrganizationContext context = OrganizationContextFactory.get();

	@Override
	public boolean isMember(final Object user, final Object role, final Map<String, Object> variables) {
		final IRoleManager roleMgr = context.getRoleMgr();
		final IUser oUser = getUserObject(user);
		if (roleMgr.isAdmin(oUser, variables)) {
			return true;
		}
		return roleMgr.isMember(oUser, getRoleObject(role), variables);
	}

	protected IUser getUserObject(Object o) {
		if (o instanceof IUser) {
			return (IUser) o;
		}
		if (o instanceof String) {
			o = context.getAccountMgr().getAccountByName((String) o);
		}
		if (o instanceof IAccount) {
			o = ((IAccount) o).getId();
		}
		return context.getUserMgr().getBean(o);
	}

	@Override
	public PermissionUser getUser(final Object user) {
		final IUser oUser = getUserObject(user);
		if (oUser == null) {
			return super.getUser(user);
		}
		return new PermissionUser() {
			@Override
			public ID getId() {
				return oUser.getId();
			}

			@Override
			public String getName() {
				if (user instanceof String) {
					return (String) user;
				}
				final IAccount account = context.getAccountMgr().getBean(
						user instanceof IIdBeanAware ? ((IIdBeanAware) user).getId() : user);
				return account != null ? account.getName() : null;
			}

			@Override
			public String getText() {
				return oUser.getText();
			}

			@Override
			public ID getOrgId() {
				final IDepartmentManager deptMgr = context.getDepartmentMgr();
				IDepartment dept = deptMgr.getBean(oUser.getDepartmentId());
				if (dept == null) {
					return null;
				}
				while (dept.getDepartmentType() != EDepartmentType.organization) {
					final IDepartment dept2 = deptMgr.getBean(dept.getParentId());
					if (dept2 != null) {
						dept = dept2;
					} else {
						break;
					}
				}
				return dept.getId();
			}

			@Override
			public InputStream getPhotoStream() {
				return context.getUserMgr().getPhoto(oUser);
			}

			private static final long serialVersionUID = -2824016565752293671L;
		};
	}

	@Override
	public Collection<ID> users(final Object role, final Map<String, Object> variables) {
		final ArrayList<ID> al = new ArrayList<ID>();
		final IRole oRole = getRoleObject(role);
		final Collection<? extends IUser> users = context.getRoleMgr().users(oRole, variables);
		if (users != null) {
			for (final IUser user : users) {
				al.add(user.getId());
			}
		}
		return al;
	}

	protected IRole getRoleObject(final Object o) {
		if (o instanceof IRole) {
			return (IRole) o;
		}
		final IRoleManager roleMgr = context.getRoleMgr();
		if (o instanceof String) {
			return roleMgr.getRoleByName((String) o);
		}
		return roleMgr.getBean(o);
	}

	@Override
	public PermissionRole getRole(final Object role) {
		final IRole oRole = getRoleObject(role);
		if (oRole == null) {
			return super.getRole(role);
		}
		return new PermissionRole() {
			@Override
			public ID getId() {
				return oRole.getId();
			}

			@Override
			public String getName() {
				return oRole.getName();
			}

			@Override
			public String getText() {
				return oRole.getText();
			}

			private static final long serialVersionUID = 4548851646225261207L;
		};
	}

	@Override
	public ID getLoginId(final PageRequestResponse rRequest) {
		return context.getAccountMgr().getLoginId(new HttpAccountSession(rRequest));
	}

	public static final String ACCOUNT_TYPE = "accountType";

	@Override
	public void login(final PageRequestResponse rRequest, final String login, final String password,
			final Map<String, Object> params) {
		final HttpAccountSession accountSession = new HttpAccountSession(rRequest);
		final IAccountManager accountMgr = context.getAccountMgr();
		final ID loginId = accountMgr.getLoginId(accountSession);
		if (loginId != null) {
			throw OrganizationException.of($m("OrganizationPermission.0"));
		}

		EAccountType accountType = null;
		if (params != null) {
			accountType = (EAccountType) params.get(ACCOUNT_TYPE);
		}
		if (accountType == null) {
			accountType = EAccountType.normal;
		}

		IAccount account = null;
		if (accountType == EAccountType.normal) {
			account = accountMgr.getAccountByName(login);
		}
		if (account == null) {
			throw OrganizationException.of($m("OrganizationPermission.1"));
		} else {
			if (!accountMgr.verifyPassword(account, password)) {
				throw OrganizationException.of($m("OrganizationPermission.2")).putVal("password",
						Boolean.TRUE);
			} else {
				final EAccountStatus status = account.getStatus();
				if (status == EAccountStatus.normal) {
					accountMgr.setLogin(accountSession, account.getId());
				} else if (status == EAccountStatus.locked) {
					throw OrganizationException.of($m("OrganizationPermission.3"));
				} else if (status == EAccountStatus.registration) {
					throw OrganizationException.of($m("OrganizationPermission.4"));
				} else if (status == EAccountStatus.delete) {
					throw OrganizationException.of($m("OrganizationPermission.5"));
				}
			}
		}
	}

	@Override
	public String getLoginRedirectUrl(final PageRequestResponse rRequest, final String roleName) {
		final HttpAccountSession accountSession = new HttpAccountSession(rRequest);
		final Object autoLogin = accountSession.autoLogin();
		if (autoLogin != null) {
			context.getAccountMgr().setLogin(accountSession, autoLogin);
			return null;
		}
		return super.getLoginRedirectUrl(rRequest, roleName);
	}
}
