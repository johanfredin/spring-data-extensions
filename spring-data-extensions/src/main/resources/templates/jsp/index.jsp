<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="custom" tagdir="/WEB-INF/tags" %>

<html>
<head>
    <title>Index</title>
    <link href="<%=request.getContextPath()%>/style/common.css" type="text/css" rel="stylesheet"/>
    <style type="text/css">
    </style>
    <head>
    </head>
<body>

<jsp:include page="header.jsp">
    <jsp:param value="${member.id}" name="memberId"/>
</jsp:include>

<h2>Welcome ${member.userName}!</h2>

<!--ARTISTS ---------------------------------------------------------------------------------------->
<h2 class="underline">
    <img src="<%=request.getContextPath()%>/images/flightrequest.png">
    <label>My Artists</label>
</h2>

<p>
    <a class="button" href="<%=request.getContextPath()%>/editArtist/${member.id}/0.html">Create Artist</a>
</p>

<table class="dataTable">
    <tr>
        <th>Name</th>
        <th>Last Update</th>
        <th>Members</th>
        <th>Pending Requests</th>
        <th>Upcoming Gigs</th>
    </tr>
    <c:forEach items="${member.artists}" var="artist">
        <tr>
            <td><a href="<%=request.getContextPath()%>/editArtist/${member.id}/${artist.id}.html">${artist.name}</a>
            </td>
            <td>${artist.getDisplayLastChangeDate()}</td>
            <td>${artist.members.size()}</td>
            <td>${artist.requests.size()}</td>
            <td>${artist.gigs.size()}</td>
        </tr>
    </c:forEach>
</table>


<h2 class="underline">
    <img src="<%=request.getContextPath()%>/images/flightrequest.png">
    <label>Notifications</label>
</h2>
<table class="dataTable">
    <c:forEach items="${member.sentMessages}" var="message">
        <tr>
            <td>${message.getSummary()}</td>
        </tr>
    </c:forEach>
</table>
</body>

</html>