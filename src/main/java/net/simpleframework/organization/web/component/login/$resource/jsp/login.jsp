<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="net.simpleframework.organization.web.component.login.LoginUtils"%>
<%@ page import="net.simpleframework.mvc.component.ComponentParameter"%>
<%@ page import="net.simpleframework.mvc.component.ComponentUtils"%>
<%
	final ComponentParameter nComponentParameter = LoginUtils.get(request, response);
	final String beanId = nComponentParameter.hashId();
%>
<form id="_loginForm">
  <input type="hidden" id="<%=LoginUtils.BEAN_ID%>" name="<%=LoginUtils.BEAN_ID%>"
    value="<%=beanId%>" /> <input type="hidden" id="_accountType" name="_accountType" />
  <div class="lm">
    <a id="_accountMenu" class="right_down_menu"></a>
  </div>
  <div>
    <input id="_accountName" name="_accountName" type="text" />
  </div>
  <div><label>#(login.0)</label></div>
  <div>
    <input id="_passwordName" name="_passwordName" type="password" />
  </div>
  <div><%=LoginUtils.getToolbar(nComponentParameter)%></div>
  <div style="text-align: <%=nComponentParameter.getBeanProperty("actionAlign")%>">
    <input id="_loginBtn" class="button2" type="submit" value="#(login.3)"
      onclick="$Actions['arLogin']();" />
    <%
    	if ((Boolean) nComponentParameter
    			.getBeanProperty("showResetAction")) {
    		out.append("<input type='reset' onclick=\"this.up('form').reset();\" />");
    	}
    %>
  </div>
</form>
<script type="text/javascript">
  var _AccountTypeMSG = {
    "normal" : "#(login.4)", 
    "email" : "#(login.5)", 
    "mobile" : "#(login.6)"
  };
    
  function _changeAccountType(type) {
    if (!type) {
      type = "normal";
    }
    document.setCookie("_account_type", type, 24 * 365);
    
    $("_accountType").value = type;
    var m = $("_accountMenu");
    m.update(_AccountTypeMSG[type]);
    m.up().setStyle(
        "background: url('<%=ComponentUtils
					.getCssResourceHomePath(nComponentParameter)%>/images/" + type + ".png') no-repeat 0 center;");
	}

  function _save_cookie() {
		document.setCookie("_account_name", $F("_accountName").stringToHex(), 24 * 365);
		var _autoLogin = $("_autoLogin");
		if (_autoLogin && $F(_autoLogin) == "true") {
			document.setCookie("_account_pwd", $F("_passwordName"), 24 * 14);
		}
	}
  
	$ready(function() {
		var name = document.getCookie("_account_name");
		if (name) {
			$("_accountName").value = name.hexToString();
		}
		_changeAccountType(document.getCookie("_account_type"));
	});
</script>
