<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="custom" tagdir="/WEB-INF/tags" %>

<html>
<head>
    <title>Find Venues</title>

    <link href="<%=request.getContextPath()%>/style/common.css" type="text/css" rel="stylesheet"/>
    <head>
    </head>
<body>

<jsp:include page="header.jsp">
    <jsp:param value="${memberId}" name="memberId"/>
    <jsp:param value="${artistId}" name="artistId"/>
</jsp:include>

<!-- FIND VENUES --------------------------------------------------------------------------->
<h2 class="underline">
    <img src="<%=request.getContextPath()%>/images/flightrequest.png">
    <label>Find Venues</label>
</h2>

<form:form modelAttribute="findVenuesBean">

    <!-- Search params -->
    <custom:inputField label="Name" name="name" id="name"/>
    <custom:inputField label="City" name="city" id="city"/>
    <custom:inputField label="Country" name="country" id="country"/>
    <custom:select label="Genre" name="genre" itemLabel="name"/>

    <p>
        <input type="submit" value="Go!"/>
    </p>

    <!-- Display results (if any) -->
    <c:if test="${results.size() > 0}">
        <p>
            <b>results</b><br>
            -------------------------<br>
        <table class="dataTable">
            <tr>
                <th>Venue</th>
                <th>Genre</th>
                <th>Avg Score</th>
                <th>Last Update</th>
            </tr>
            <c:forEach items="${results}" var="venue">
                <tr>
                    <td>
                        <a href="<%=request.getContextPath()%>/editVenue/${venue.id}/${artistId}/${memberId}.html">
                            <!-- Displayed as "The Whiskey (Los Angeles - US)" -->
                                ${venue.name} (${venue.address.city} - ${venue.address.country})
                        </a>
                    </td>
                    <td>${venue.genre.name}</td>
                    <td>${venue.calcAvgScore()}</td>
                    <td>${venue.lastChangeDate}</td>
                </tr>
            </c:forEach>
        </table>
    </c:if>


    <p>
        <a href="<%=request.getContextPath()%>/editArtist/${memberId}/${artistId}.html">Back</a>
    </p>
</form:form>
</body>
</html>