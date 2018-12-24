<%@ page contentType="text/html; charset=ISO-8859-1"
         pageEncoding="ISO-8859-1" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<%@ taglib prefix="custom" tagdir="/WEB-INF/tags" %>

<html>
<head>
    <title>Venue</title>

    <script src="http://maps.googleapis.com/maps/api/js?sensor=false&libraries=places&key=AIzaSyDtwepldqpFwcN9nq6gpdEjyPRrhxDshkw"></script>
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.1.0/jquery.min.js"></script>
    <link rel="stylesheet" href="https://ajax.googleapis.com/ajax/libs/jqueryui/1.11.4/themes/smoothness/jquery-ui.css">
    <script src="https://ajax.googleapis.com/ajax/libs/jqueryui/1.11.4/jquery-ui.min.js"></script>

    <script src="<%=request.getContextPath()%>/js/geolocate.js"></script>
    <link href="<%=request.getContextPath()%>/style/common.css" type="text/css" rel="stylesheet"/>

    <script>
        $(function () {

            // Helper function to automatically set requestDate field to todays date
            var updateDate = function () {
                $("#requestDate").val($.datepicker.formatDate("yy-mm-dd", new Date()));
            };


            $("#geocomplete").geocomplete({
                map: ".map_canvas",
                details: "body",
                location: [$("#lat").val(), $("#lng").val()],
                types: ["geocode", "establishment"],
            });

            // Add a date selection ui when pressing the date field
            $("#requestDate").datepicker({
                dateFormat: "yy-mm-dd"
            });

            $("#submit").on("click", function () {
                console.log("setLastDate was called!");
                $("#lastChangeUserId").val(${artistId});
            });

            // Add a selection changed listener to the request status,
            // Once changed the request date should automatically update to todays date
            $("#requestStatus").on("change", updateDate);
        });
    </script>

</head>
<body>

<jsp:include page="header.jsp">
    <jsp:param value="${memberId}" name="memberId"/>
    <jsp:param value="${artistId}" name="artistId"/>
</jsp:include>

<input id="geocomplete" type="text" placeholder="Find a Venue on GMAPS"/>

<h2 class="underline">

    <c:choose>
        <c:when test="${editVenueVean.venue.id > 0}">
            Edit Venue
        </c:when>
        <c:otherwise>
            Create Venue
        </c:otherwise>
    </c:choose>
</h2>

<form:form modelAttribute="editVenueBean">

<!------------------------------------------------------------------------------------>
<!-------------------- **SHOULD NOT BE VISIBLE!** ------------------------------------>
<!------------------------------------------------------------------------------------>
<form:hidden path="venue.id"/>
<form:hidden path="venue.lastChangedByUsedId" id="lastChangeUserId"/>
<form:hidden path="venue.lastChangeDate" id="lastChangeDate"/>
<form:hidden path="address.id"/>
<form:hidden path="venue.address.latitude" id="lat"/>
<form:hidden path="venue.address.longitude" id="lng"/>


<!----------------------------- VENUE ------------------------------------------------>
<custom:inputField label="Name" name="venue.name" id="name"/>
<custom:inputChoiseTag label="Email" name="venue.email" conditionViolated="${isExistingEmail}"
                       errorMessage="Already a venue with that email"/>
<custom:inputField label="Phone Nr" name="venue.phoneNr" id="international_phone_number"/>
<custom:inputField label="Website" name="venue.url" id="website"/>
<custom:inputField label="Capacity" name="venue.capacity" id="capacity"/>
<custom:select label="Genre" name="venue.genre" itemLabel="name"/>

<!----------------------------- ADDRESS ---------------------------------------------->

<h3 class="underline">Address</h3>

<custom:inputField label="Street" name="address.street" id="street"/>
<custom:inputField label="Zipcode" name="address.zipCode" id="zipCode"/>
<custom:inputField label="City" name="address.city" id="city"/>
<custom:inputField label="State/Region" name="address.stateOrRegion"/>
<custom:inputField label="Country" name="address.country" id="country"/>

<!------------------------------------------------------------------------------------>
<!----------------------------- CONTACT PERSONS -------------------------------------->
<!------------------------------------------------------------------------------------>

<c:choose>
    <c:when test="${editVenueBean.venue.contacts.size() > 0}">
        <h3 class="underline">Contacts</h3>
        <table class="dataTable">
            <tr>
                <th>Name</th>
                <th>Email</th>
                <th>Phone Nr</th>
                <th>Role</th>
                <th>Edit</th>
                <th>Delete</th>
            </tr>
            <c:forEach items="${contacts}" var="contact">
                <td>${contact.name}</td>
                <td>${contact.email}</td>
                <td>${contact.phoneNr}</td>
                <td>${contact.role}</td>
                <td><a href="<%=request.getContextPath()%>/editContact/${venue.id}/${contact.id}.html">E</a></td>
                <td><a href="<%=request.getContextPath()%>/deleteContact/${contact.id}.html"><font color="red">X</font></a>
                </td>
            </c:forEach>
        </table>
    </c:when>
</c:choose>

<p><a href="<%=request.getContextPath()%>/addContact/${venue.id}/${artistId}/${memberId}/0.html">Add Contact</a></p>


<!------------------------------------------------------------------------------------>
<!----------------------------- REVIEWS ---------------------------------------------->
<!------------------------------------------------------------------------------------>


<c:if test="${reviews.size() > 0}">
    <p>
        <b>Reviews</b><br>
        ---------------<br>
    <p>
        Score: <label>${reviews[0].score}</label><br><br>
        <i>
            By</i> <a
            href="<%=request.getContextPath()%>/editArtist/${memberId}/${artistId}.html">${reviews[0].artist.name}</a>
        <i>${latestReview.getDisplayLastChangeDate()}
        </i><br><br>
            ${reviews[0].review}<br>
    </p>
</c:if>

<!-- Offer the opportunity to create a venue if the artist has not already written one -->
<c:if test="${reviewId <= 0}">
    <p>
        <a class="button"
           href="<%=request.getContextPath()%>/editVenueReviews/${venue.id}/${artistId}/${memberId}/${reviewId}.html">
            Write A Review</a>
    </p>
</c:if>

<a href="<%=request.getContextPath()%>/editVenueReviews/${venue.id}/${artistId}/${memberId}/${reviewId}.html">See more
    reviews</a>


<!------------------------------------------------------------------------------------>
<!----------------------------- ARTISTS ---------------------------------------------->
<!------------------------------------------------------------------------------------>

<c:choose>
    <c:when test="${artists.size() > 0}">
        <h3 class="underline">Artists who have performed at ${editVenueBean.venue.name}</h3>
        <table class="dataTable">
            <tr>
                <th>Name</th>
                <th>Genre</th>
                <th>City</th>
                <th>Country</th>
            </tr>
            <c:forEach items="${artists}" var="artist">

                <td><a href="<%=request.getContextPath()%>/viewArtist/${memberId}/${artist.id}.html">${artist.name}</a>
                </td>
                <td>Not implemented</td>
                <td>${artist.city}</td>
                <td>${artist.country}</td>

            </c:forEach>
        </table>
    </c:when>
</c:choose>

<p>
    <c:set var="submitText">
        <spring:message code="global.submit"/>
    </c:set>
    <input type="submit" value="${submitText}" id="submit"/> <a
        href="<%=request.getContextPath()%>/editArtist/${memberId}/${artistId}.html"><spring:message
        code="global.cancel"/></a>

    </form:form>

<div class="map_canvas"></div>
</body>
</html>