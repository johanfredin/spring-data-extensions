<%@taglib uri="http://www.springframework.org/tags" prefix="spring" %>

<div class="header">
    <div class="HeaderLeft">
        <h1><a href="<%= request.getContextPath() %>/index/${memberId}.html"><spring:message
                code="application.title"/></a></h1>
    </div>

    <div class="HeaderRight">
        <a href="?lang=sv"><img src="<%= request.getContextPath() %>/images/sweden.png"></a> <a href="?lang=en"><img
            src="<%= request.getContextPath() %>/images/england.png"></a>
    </div>
    <div class="clear"></div>
</div>
<div class="footer">
    <!-- Banner links -->
    <a href="<%=request.getContextPath()%>/savedVenues/${artistId}/${memberId}.html">My Saved Venues</a> |
    <a href="<%=request.getContextPath()%>/findVenues/${artistId}/${memberId}.html">Find Venues</a> |
    <a href="<%=request.getContextPath()%>/about.html">About Gigmanager</a> |
    <a href="<%=request.getContextPath()%>/editMember/${memberId}.html">User Settings</a> |
    <a href="<%=request.getContextPath()%>/login.html">Log Out</a> |
    Notifications
</div>