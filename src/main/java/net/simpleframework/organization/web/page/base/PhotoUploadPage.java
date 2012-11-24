package net.simpleframework.organization.web.page.base;

import static net.simpleframework.common.I18n.$m;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;

import net.simpleframework.common.AlgorithmUtils;
import net.simpleframework.common.ID;
import net.simpleframework.mvc.AbstractMVCPage;
import net.simpleframework.mvc.AbstractUrlForward;
import net.simpleframework.mvc.IMultipartFile;
import net.simpleframework.mvc.MVCUtils;
import net.simpleframework.mvc.MultipartPageRequest;
import net.simpleframework.mvc.PageParameter;
import net.simpleframework.mvc.PageRequestResponse;
import net.simpleframework.mvc.UrlForward;
import net.simpleframework.mvc.component.ComponentParameter;
import net.simpleframework.mvc.component.base.submit.SubmitBean;
import net.simpleframework.mvc.component.base.validation.EValidatorMethod;
import net.simpleframework.mvc.component.base.validation.Validator;
import net.simpleframework.organization.IAccount;
import net.simpleframework.organization.OrganizationException;

/**
 * 这是一个开源的软件，请在LGPLv3下合法使用、修改或重新发布。
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         http://code.google.com/p/simpleframework/
 *         http://www.simpleframework.net
 */
public class PhotoUploadPage extends AbstractAccountPage {

	@Override
	protected void onInit(final PageParameter pParameter) {
		super.onInit(pParameter);

		addComponentBean(pParameter, "uploadPhoto", SubmitBean.class).setFormName("uploadPhoto")
				.setBinary(true).setConfirmMessage($m("Confirm.Post")).setHandleMethod("upload")
				.setHandleClass(PhotoUploadPage.class);
		addValidationBean(pParameter, "uploadValidation").setTriggerSelector("#btnUploadPhoto")
				.addValidators(
						new Validator(EValidatorMethod.file, "#user_photo")
								.setArgs("jpg,jpeg,bmp,gif,png"));
	}

	public AbstractUrlForward upload(final ComponentParameter cParameter) {
		final IAccount account = getAccount(cParameter);
		final ID accountId = account.getId();
		final MultipartPageRequest request = (MultipartPageRequest) cParameter.request;
		final IMultipartFile multipart = request.getFile("user_photo");
		if (multipart.getSize() > 1024 * 1024) {
			return new UrlForward(AbstractMVCPage.uriFor(PhotoUploadResultPage.class, "accountId="
					+ accountId + "&error=size"));
		} else {
			try {
				context.getUserMgr().updatePhoto(accountId, multipart.getInputStream());
				deletePhoto(cParameter, accountId);
				return new UrlForward(uriFor(
						PhotoUploadResultPage.class,
						"accountId="
								+ accountId
								+ "&src="
								+ AlgorithmUtils.base64Encode((get(PhotoPage.class).getPhotoUrl(cParameter,
										account)
										+ "?c=" + System.currentTimeMillis()).getBytes())));
			} catch (final IOException e) {
				throw OrganizationException.of(e);
			}
		}
	}

	private void deletePhoto(final PageRequestResponse requestResponse, final Object userId) {
		final File photoCache = new File(MVCUtils.getRealPath(MVCUtils.getPageResourcePath()
				+ "/images/photo-cache/"));
		if (!photoCache.exists()) {
			return;
		}
		for (final File photo : photoCache.listFiles(new FilenameFilter() {
			@Override
			public boolean accept(final File dir, final String name) {
				return name.startsWith(userId + "_");
			}
		})) {
			photo.delete();
		}
	}
}
