<%@taglib uri="http://www.springframework.org/tags" prefix="spring" %>

<div class="header">
    <div class="HeaderLeft">
        <h1><a href="<%= request.getContextPath()%>/login.html"><spring:message code="application.title"/></a></h1>
    </div>

    <div class="HeaderRight">
        <a href="?lang=sv"><img src="<%= request.getContextPath() %>/images/sweden.png"></a> <a href="?lang=en"><img
            src="<%= request.getContextPath() %>/images/england.png"></a>
    </div>
    <div class="clear"></div>
</div>
