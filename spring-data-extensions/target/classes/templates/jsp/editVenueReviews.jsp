<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="custom" tagdir="/WEB-INF/tags" %>

<html>
<head>
    <title>Reviews</title>

    <script src="http://maps.googleapis.com/maps/api/js?sensor=false&libraries=places&key=AIzaSyDtwepldqpFwcN9nq6gpdEjyPRrhxDshkw"></script>
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.1.0/jquery.min.js"></script>
    <script src="https://ajax.googleapis.com/ajax/libs/jqueryui/1.11.4/jquery-ui.min.js"></script>

    <link href="<%=request.getContextPath()%>/style/common.css" type="text/css" rel="stylesheet"/>
    <style type="text/css">
    </style>
    <head>
        <title>Reviews</title>
        <script type="text/javascript">

            $(function () {

                // Set new lastChangeDate on submit
                $("#submit").on("click", function () {

                });

            });
        </script>

    </head>
<body>

<jsp:include page="header.jsp">
    <jsp:param value="${member.id}" name="memberId"/>
    <jsp:param value="${artist.id}" name="artistId"/>
</jsp:include>

<h2 class="underline">
    <img src="<%=request.getContextPath()%>/images/flightrequest.png">
    <label>Reviews for ${venueName}</label>
</h2>

<!-- ADD NEW CONTACT --------------------------------------------------------------------------->
<form:form modelAttribute="editReviewBean">
    <form:hidden path="review.id"/>
    <form:hidden path="review.lastChangeDate" id="lastChangeDate"/>

    <c:forEach items="${reviews}" var="currentReview">
        <!-- Don't display the review the artist we are using wrote amongst these reviews, that review will be in the bottom -->
        <c:if test="${currentReview.id} != ${review.id}">
            <p>
            Score: ${currentReview.score} <br><br>
            <i>By</i> <a
                href="<%=request.getContextPath()%>/editArtist/${memberId}/${artistId}.html">${currentReview.artist.name}</a>
            <i>${currentReview.getDisplayLastChangeDate()}</i><br><br>
            ${currentReview.review}<br>
        </c:if>
    </c:forEach>
    <p>

        <!-- Add or Edit your review -->
        <c:choose>
        <c:when test="${review.id > 0}">
        Edit your review?<br>
        </c:when>
        <c:otherwise>
        Write a Review<br>
        </c:otherwise>
        </c:choose>
        ---------------------<br>
            <custom:inputField label="Score" name="review.score"/><br>
            <custom:inputTextArea label="Review" name="review.review"/><br>


        <c:set var="submitText">
            <spring:message code="global.submit"/>
        </c:set>

    <p>
        <input type="submit" value="${submitText}" id="submit"/> <a
            href="<%=request.getContextPath()%>/editVenue/${venueId}/${artistId}/${memberId}.html">Cancel</a>
    </p>
</form:form>
</body>
</html>