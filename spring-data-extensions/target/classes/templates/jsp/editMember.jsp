<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="custom" tagdir="/WEB-INF/tags" %>

<html>
<head>
    <title>User Settings</title>

    <link href="<%=request.getContextPath()%>/style/common.css" type="text/css" rel="stylesheet"/>
    <head>
    </head>
<body>

<jsp:include page="header.jsp">
    <jsp:param value="${memberId}" name="memberId"/>
</jsp:include>

<!-- ADD NEW CONTACT --------------------------------------------------------------------------->
<h2 class="underline">
    <img src="<%=request.getContextPath()%>/images/flightrequest.png">
    <label>Edit User</label>
</h2>
<form:form modelAttribute="editMemberBean">
    <form:hidden path="member.id"/>
    <form:hidden path="member.password"/>

    <custom:inputChoiseTag label="User Name" name="member.name" id="name"
                           conditionViolated="${isUserNameExists}" errorMessage="Already a user with that username!"/>
    <custom:inputChoiseTag label="Email" name="member.email" id="email" conditionViolated="${isUserEmailExists}"
                           errorMessage="Already a user with that email!"/>
    <custom:inputField label="Real Name" name="member.name" id="realName"/>
    <custom:inputField label="City" name="member.city" id="city"/>
    <custom:inputField label="Country" name="member.country" id="country"/>

    <p>
        Role: <form:radiobuttons path="member.role" itemLabel="role"/>
    </p>

    <p>
        <b>Change Password</b><br>
        -------------------------
    </p>
    <custom:passwordValidationTag label="Current Password" name="currentPassword"
                                  conditionViolated="${isCurrentPasswordIncorrect}" errorMessage="Incorrect password"/>
    <custom:passwordTag label="New Password" name="newPassword"/>
    <custom:passwordValidationTag label="Repeat Password" name="repeatPassword"
                                  conditionViolated="${isRepeatPasswordIncorrect}"
                                  errorMessage="Passwords don't match"/>

    <p>
    <b>Artists</b><br>
    -------------------------<br>
    <c:if test="${artists.size() > 0}">

        <table class="dataTable">
            <tr>
                <th>Name</th>
                <th>Remove</th>
            </tr>
            <c:forEach items="${artists}" var="artist">
                <tr>
                    <td>
                        <a href="<%=request.getContextPath()%>/editArtist/${memberId}/${artist.id}.html">${artist.name}</a>
                    </td>
                    <td><a href="<%=request.getContextPath()%>/deleteArtist/${artist.id}.html"><font
                            color="red">X</font></a></td>
                </tr>
            </c:forEach>
        </table>
    </c:if>


    <p>
        <input type="submit" value="Save Changes"/> <a href="<%=request.getContextPath()%>/index/${memberId}.html">Cancel</a>
    </p>
</form:form>
</body>
</html>