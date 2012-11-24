<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="net.simpleframework.mvc.component.ComponentParameter"%>
<%@ page import="net.simpleframework.organization.web.component.userselect.UserSelectUtils"%>
<%@ page import="net.simpleframework.mvc.component.ui.dictionary.DictionaryRender"%>
<%@ page import="net.simpleframework.common.Convert"%>
<% 
	final ComponentParameter nComponentParameter = UserSelectUtils.get(request, response);
	final String hashId = nComponentParameter.hashId();
	final String componentName = (String) nComponentParameter.getBeanProperty("name");
%>
<div class="user_select simple_window_tcb">
  <div id="users_<%=hashId%>"></div>
  <div class="b">
    <input type="button" class="button2" value="#(Button.Ok)"
      onclick="$Actions['<%=componentName%>'].doDblclick();" /> <input type="button"
      value="#(Button.Cancel)" onclick="$Actions['<%=componentName%>'].close();" />
  </div>
</div>
<script type="text/javascript">
  $ready(function() {
    var us = $Actions['<%=componentName%>'];
    var w = us.window;
    w.content.setStyle("overflow:hidden;");
    var tp = $Actions["<%=componentName + "_tablePager"%>"];
    var s = function() {
      tp.setHeight(w.content.getHeight() - 130);
    };
    s();
    w.observe("resize:ended", s);
    
    us.doDblclick = function(d) {
      var selects = new Array();
      var arr = tp.checkArr(w.content);
      if (arr && arr.length > 0) {
        arr.each(function(d2) {
          selects.push({
            id : tp.rowId(d2),
            text : d2.readAttribute("userText")
          });
        });
      } else {
        if (d) {
          selects.push({
            id : tp.rowId(d),
            text : d.readAttribute("userText")
          });
        }
      }
      if (selects.length == 0) {
        alert("#(user_select.0)");
      }
      <%=DictionaryRender.genSelectCallback(nComponentParameter, "selects")%>
  	};
  });
</script>