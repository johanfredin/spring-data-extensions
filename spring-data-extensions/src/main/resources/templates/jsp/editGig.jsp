<%@ page contentType="text/html; charset=ISO-8859-1"
         pageEncoding="ISO-8859-1" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring" %>

<html>
<head>
    <title><spring:message code="index.creategig"/></title>

    <script src="http://maps.googleapis.com/maps/api/js?sensor=false&libraries=places&key=AIzaSyDtwepldqpFwcN9nq6gpdEjyPRrhxDshkw"></script>
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.1.0/jquery.min.js"></script>
    <link rel="stylesheet" href="https://ajax.googleapis.com/ajax/libs/jqueryui/1.11.4/themes/smoothness/jquery-ui.css">
    <script src="https://ajax.googleapis.com/ajax/libs/jqueryui/1.11.4/jquery-ui.min.js"></script>

    <script src="<%=request.getContextPath()%>/js/geolocate.js"></script>

    <link href="<%=request.getContextPath()%>/style/common.css" type="text/css" rel="stylesheet"/>

    <script>
        $(function () {

            $("#geocomplete").geocomplete({
                map: ".map_canvas",
                details: "body",
                location: [$("#lat").val(), $("#lng").val()],
                types: ["geocode", "establishment"],
            });

            // Add a date selection ui when pressing the date field
            $("#date").datepicker({
                dateFormat: "yy-mm-dd"
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

    <c:choose>
        <c:when test="${editGigBean.gig.id > 0}">
            <spring:message code="editGig.title.update"/>
        </c:when>
        <c:otherwise>
            <spring:message code="editGig.title.create"/>
        </c:otherwise>
    </c:choose>
</h2>

<form:form commandName="editGigBean">
    <form:hidden path="gig.id"/>
    <table class="formTable">

        <tr>
            <th><spring:message code="global.date"/></th>
            <td><form:input path="schedule.date" id="date"/></td>
            <td><form:errors path="schedule.date" cssClass="errors"/></td>
        </tr>

        <!------------------------------------------------------------------------------------>
        <!----------------------------- VENUE ------------------------------------------------>
        <!------------------------------------------------------------------------------------>

        <tr>
            <th><spring:message code="global.name"/></th>
            <td><form:input path="venue.name" list="existingVenues" id="name"
                            placeholder="New or Existing Venue?"/></td>
            <td>
                <datalist id=existingVenues>
                    <c:forEach items="${existingVenues}" var="existingVenue">
                        <option>${existingVenue}</option>
                    </c:forEach>
                </datalist>
            </td>
            <td><form:errors path="venue.name" cssClass="errors"/></td>
        </tr>

        <tr>
            <th><spring:message code="global.email"/></th>
            <td><form:input path="venue.email" id="email"/></td>
            <td><form:errors path="venue.email" cssClass="errors"/></td>
        </tr>

        <!------------------------------------------------------------------------------------>
        <!----------------------------- SPECIFICATIONS --------------------------------------->
        <!------------------------------------------------------------------------------------>

        <tr>
            <th><spring:message code="editGig.revenue"/></th>
            <td><form:input path="specifications.revenue" id="revenue"/></td>
            <td><form:errors path="specifications.revenue" cssClass="errors"/></td>
        </tr>

        <tr>
            <th><spring:message code="editGig.backline"/></th>
            <td><form:checkbox path="specifications.backline" id="backline"/></td>
            <td><form:errors path="specifications.backline" cssClass="errors"/></td>
        </tr>

        <tr>
            <th><spring:message code="editGig.travelCompensation"/></th>
            <td><form:checkbox path="specifications.travelCompensation" id="travelCompensation"/></td>
            <td><form:errors path="specifications.travelCompensation" cssClass="errors"/></td>
        </tr>

        <tr>
            <th><spring:message code="editGig.foodIncluded"/></th>
            <td><form:checkbox path="specifications.foodIncluded" id="foodIncluded"/></td>
            <td><form:errors path="specifications.foodIncluded" cssClass="errors"/></td>
        </tr>

        <tr>
            <th><spring:message code="editGig.freeBeverages"/></th>
            <td><form:checkbox path="specifications.freeBeverages" id="freeBeverages"/></td>
            <td><form:errors path="specifications.freeBeverages" cssClass="errors"/></td>
        </tr>

        <tr>
            <th><spring:message code="editGig.backstage"/></th>
            <td><form:checkbox path="specifications.backstage" id="backstage"/></td>
            <td><form:errors path="specifications.freeBeverages" cssClass="errors"/></td>
        </tr>

        <tr>
            <th><spring:message code="editGig.noAlcohol"/></th>
            <td><form:checkbox path="specifications.noAlcohol" id="noAlcohol"/></td>
            <td><form:errors path="specifications.noAlcohol" cssClass="errors"/></td>
        </tr>

        <!------------------------------------------------------------------------------------>
        <!----------------------------- ADDRESS ---------------------------------------------->
        <!------------------------------------------------------------------------------------>
        <tr>
            <th><spring:message code="global.address.street"/></th>
            <td><form:input path="address.street" id="route"/></td>
            <td><form:errors path="address.street" cssClass="errors"/></td>
        </tr>

        <tr>
            <th><spring:message code="global.address.zipcode"/></th>
            <td><form:input path="address.zipCode" id="postal_code"/></td>
            <td><form:errors path="address.zipCode" cssClass="errors"/></td>
        </tr>

        <tr>
            <th><spring:message code="global.address.city"/></th>
            <td><form:input path="address.city" id="locality"/></td>
            <td><form:errors path="address.city" cssClass="errors"/></td>
        </tr>

        <tr>
            <th><spring:message code="global.address.stateOrRegion"/></th>
            <td><form:input path="address.stateOrRegion" id="administrative_area_level_1"/></td>
            <td><form:errors path="address.stateOrRegion" cssClass="errors"/></td>
        </tr>

        <tr>
            <th><spring:message code="global.address.country"/></th>
            <td><form:input path="address.country" id="country"/></td>
            <td><form:errors path="address.country" cssClass="errors"/></td>
        </tr>


        <!------------------------------------------------------------------------------------>
        <!----------------------------- SCHEDULE --------------------------------------------->
        <!------------------------------------------------------------------------------------>

        <tr>
            <th><spring:message code="editGig.showupTime"/></th>
            <td><form:input path="schedule.showupTime" id="showupTime"/></td>
            <td><form:errors path="schedule.showupTime" cssClass="errors"/></td>
        </tr>

        <tr>
            <th><spring:message code="editGig.soundcheckTime"/></th>
            <td><form:input path="schedule.timeForSoundcheck" id="timeForSoundcheck"/></td>
            <td><form:errors path="schedule.timeForSoundcheck" cssClass="errors"/></td>
        </tr>

        <tr>
            <th><spring:message code="editGig.openingTime"/></th>
            <td><form:input path="schedule.openingTime" id="openingTime"/></td>
            <td><form:errors path="schedule.openingTime" cssClass="errors"/></td>
        </tr>

        <tr>
            <th><spring:message code="editGig.stageTime"/></th>
            <td><form:input path="schedule.timeForShow" id="timeForShow"/></td>
            <td><form:errors path="schedule.timeForShow" cssClass="errors"/></td>
        </tr>

        <tr>
            <th><spring:message code="editGig.closingTime"/></th>
            <td><form:input path="schedule.closingTime" id="closingTime"/></td>
            <td><form:errors path="schedule.closingTime" cssClass="errors"/></td>
        </tr>

        <!------------------------------------------------------------------------------------>
        <!----------------------------- SIDE NOTES ------------------------------------------->
        <!------------------------------------------------------------------------------------>

        <tr>
            <th><spring:message code="editGig.sideNotes"/></th>
            <td><form:textarea path="specifications.sideNotes" id="sideNotes"/></td>
            <td><form:errors path="specifications.sideNotes" cssClass="errors"/></td>
        </tr>

        <!------------------------------------------------------------------------------------>
        <!-------------------- **SHOULD NOT BE VISIBLE!** ------------------------------------>
        <!------------------------------------------------------------------------------------>
        <tr>
            <td><form:hidden path="address.latitude" id="lat"/></td>
            <td><form:errors path="address.latitude" cssClass="errors"/></td>
        </tr>

        <tr>
            <td><form:hidden path="address.longitude" id="lng"/></td>
            <td><form:errors path="address.longitude" cssClass="errors"/></td>
        </tr>

        <!------------------------------------------------------------------------------------>
        <!----------------------------- CONTACT PERSONS -------------------------------------->
        <!------------------------------------------------------------------------------------>

    </table>

    <c:choose>
        <c:when test="${contactsExist}">
            <h3 class="underline"><spring:message code="global.contacts.title"/></h3>
            <table class="dataTable">
                <tr>
                    <th><spring:message code="global.name"/></th>
                    <th><spring:message code="global.email"/></th>
                    <th><spring:message code="global.contact.phone"/></th>
                    <th><spring:message code="global.contact.role"/></th>
                </tr>
                <c:forEach items="${editGigBean.contacts}" var="contact" varStatus="status">
                    <tr>
                        <td><form:input path="contacts[${status.index}].name" value="${contact.name}"/></td>
                        <td><form:input path="contacts[${status.index}].email" value="${contact.email}"/><form:errors
                                path="contacts[${status.index}].email" value="${contact.email}" cssClass="errors"/></td>
                        <td><form:input path="contacts[${status.index}].phoneNr" value="${contact.phoneNr}"/></td>
                        <td><form:select path="contacts[${status.index}].role"
                                         value="${contact.role}"><form:options/></form:select></td>
                    </tr>
                </c:forEach>
            </table>

        </c:when>
    </c:choose>

    <tr>
        <td>
            <a href="<%=request.getContextPath()%>/addContact/${venueId}/caller=editGig/gigId=${editGigBean.gig.id}.html"><spring:message
                    code="global.contacts.add"/></a>
        </td>
    </tr>

    <tr>
        <td>
            <c:set var="submitText">
                <spring:message code="global.submit"/>
            </c:set>
            <input type="submit" value="${submitText}"/>
            <a href="<%=request.getContextPath()%>/index.html"><spring:message code="global.cancel"/></a></td>
        <td></td>
    </tr>

</form:form>
</body>
</html>