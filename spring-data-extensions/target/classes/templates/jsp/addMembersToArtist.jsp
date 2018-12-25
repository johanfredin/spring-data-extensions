<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="custom" tagdir="/WEB-INF/tags" %>

<html>
<head>
    <title>Add Members To Artist</title>
    <script src="http://maps.googleapis.com/maps/api/js?sensor=false&libraries=places&key=AIzaSyDtwepldqpFwcN9nq6gpdEjyPRrhxDshkw"></script>
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.1.0/jquery.min.js"></script>
    <script src="https://ajax.googleapis.com/ajax/libs/jqueryui/1.11.4/jquery-ui.min.js"></script>
    <link href="<%=request.getContextPath()%>/style/common.css" type="text/css" rel="stylesheet"/>
    <style type="text/css">
    </style>
    <head>
        <script type="text/javascript">
            $(function () {


            });
        </script>

    </head>
<body>

<jsp:include page="header.jsp">
    <jsp:param value="${memberId}" name="memberId"/>
    <jsp:param value="${artistId}" name="artistId"/>
</jsp:include>

<!------------------------------------------------------------------------------------------>
<!-- ARTIST DATA --------------------------------------------------------------------------->
<!------------------------------------------------------------------------------------------>

<h2 class="underline">
    <img src="<%=request.getContextPath()%>/images/flightrequest.png">
    Add Members to Artist
</h2>

<form:form modelAttribute="addMembersToArtistBean">

    <custom:inputField label="Name or Email" name="nameOrEmail"/> <input type="submit" value="Search"/>

    <p>
    <c:forEach items="${results}" var="member">
        User Name: <a
            href="<%=request.getContextPath()%>/viewMember/${memberId}/${artistId}/${member.id}.html">'${member.name}'</a><br>
        Email:     ${member.email}"<br>
        User Role: ${member.role}<br>
        Name:      ${member.name}<br>
        City:      ${member.city}<br>
        Country:   ${member.country}<br>

        <!----------------------------------------------------------------------------------------------------------->
        <!--SUBMIT ......-------------------------------------------------------------------------------------------->
        <!----------------------------------------------------------------------------------------------------------->
        <p>
            <a class="button"
               href="<%=request.getContextPath()%>/addMembersToArtist/${memberId}/${artistId}/${member.id}.html">Add
                Members</a>
        </p>

    </c:forEach>

    <a href="<%=request.getContextPath()%>/editArtist/${memberId}/${artistId}.html">Back To Artist</a>
</form:form>
</body>
</html>