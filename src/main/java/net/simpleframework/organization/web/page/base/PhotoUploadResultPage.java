package net.simpleframework.organization.web.page.base;

import java.io.IOException;
import java.util.Map;

import net.simpleframework.common.AlgorithmUtils;
import net.simpleframework.common.StringUtils;
import net.simpleframework.mvc.PageParameter;

/**
 * 这是一个开源的软件，请在LGPLv3下合法使用、修改或重新发布。
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         http://code.google.com/p/simpleframework/
 *         http://www.simpleframework.net
 */
public class PhotoUploadResultPage extends AbstractAccountPage {

	@Override
	protected String toHtml(final PageParameter pParameter, final Map<String, Object> variables,
			final String variable) throws IOException {
		final StringBuilder sb = new StringBuilder();
		sb.append("<html><head></head><body><div class='PhotoUploadResultPage'>");
		final String error = pParameter.getParameter("error");
		if ("size".equals(error)) {
			sb.append("<div class='info'>#(PhotoUploadResultPage.0)</div>");
		} else {
			final String src = pParameter.getParameter("src");
			if (StringUtils.hasText(src)) {
				sb.append("<div class='info'>#(PhotoUploadResultPage.2)</div>");
				sb.append("<script type='text/javascript'>");
				sb.append("(function() {");
				sb.append("parent.$('user_edit_photo_image').src = '")
						.append(new String(AlgorithmUtils.base64Decode(src))).append("';");
				sb.append("})();");
				sb.append("</script>");
			}
		}
		sb.append("<p><input type='button' value='#(PhotoUploadResultPage.1)' onclick='history.back();'/></p>");
		sb.append("</div></body></html>");
		return sb.toString();
	}
}
