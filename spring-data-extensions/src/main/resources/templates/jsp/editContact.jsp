<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="customWithChoise" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="custom" tagdir="/WEB-INF/tags" %>

<html>
<head>
    <link href="<%=request.getContextPath()%>/style/common.css" type="text/css" rel="stylesheet"/>
    <style type="text/css">
    </style>
    <head>
    </head>
<body>

<jsp:include page="header.jsp">
    <jsp:param value="${member.id}" name="memberId"/>
    <jsp:param value="${artist.id}" name="artistId"/>
</jsp:include>

<!-- ADD NEW CONTACT --------------------------------------------------------------------------->
<h2 class="underline">
    <img src="<%=request.getContextPath()%>/images/flightrequest.png">
    <spring:message code="global.contacts.add"/>
</h2>

<form:form commandName="addContactBean">
    <form:hidden path="venueId"/>

    <custom:inputField label="global.name" name="contact.name"/>
    <custom:inputField label="global.email" name="contact.email"/>
    <custom:inputField label="global.contact.phone" name="contact.phoneNr"/>
    <custom:inputField label="global.email" name="contact.email"/>

    <spring:message code="global.contact.role"/>
    <form:select path="contact.role">
        <form:options/>
    </form:select>

    <c:set var="submitText">
        <spring:message code="global.submit"/>
    </c:set>
    <input type="submit" value="${submitText}"/>

    <c:choose>
        <c:when test="${gigId > 0}">
            <a href="<%=request.getContextPath()%>/${callerView}/${gigId}.html"><spring:message
                    code="global.cancel"/></a>
        </c:when>
        <c:otherwise>
            <a
                    href="<%=request.getContextPath()%>/${callerView}/${addContactBean.venueId}.html"><spring:message
                    code="global.cancel"/></a>
        </c:otherwise>
    </c:choose>

</form:form>
</body>
</html>