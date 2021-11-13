
<%@ page language="java" contentType="text/html; charset=utf-8"
pageEncoding="utf-8"%> <%@ page session="false" trimDirectiveWhitespaces="true"
%> <%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %> <%@
taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %> <%@ taglib
prefix="c" uri="http://java.sun.com/jsp/jstl/core" %> <%@ taglib prefix="fn"
uri="http://java.sun.com/jsp/jstl/functions" %> <%@ taglib prefix="petclinic"
tagdir="/WEB-INF/tags" %>

<petclinic:layout pageName="games">
    <h2>Games</h2>

    <table id="gamesTable" class="table table-striped">
        <thead>
        <tr>
            <th style="width: 150px;">Name</th>
            <th>Duration</th>
            <th>Date</th>
            <th>Start Time</th>
            <th>Finish Time</th>
            <th>Comments</th>
            <th>Spectators Allowed</th>
        </tr>
        </thead>
        <tbody>
        <c:forEach items="${games}" var="game">
            <tr>
                <td>
                    <c:out value="${game.name}"/></a>
                </td>
                <td>
                    <c:out value="${game.duration}"/>
                </td>
                <td>
                    <c:out value="${game.date}"/>
                </td>
                <td>
                    <c:out value="${game.startTime}"/>
                </td>
                <td>
                    <c:out value="${game.finishTime}"/>
                </td>
                <td>
                    <c:out value="${game.comments}"/>
                </td>
                <td>
                    <c:out value="${game.spectatorsAllowed}"/>
                </td>
                
            </tr>
        </c:forEach>
        </tbody>
    </table>
</petclinic:layout>