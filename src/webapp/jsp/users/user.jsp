<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib tagdir="/WEB-INF/tags" prefix="ctag" %>


<ctag:layout title="User detail">
    <h1>User detail</h1>
    <c:out value="${user}" />
</ctag:layout>
