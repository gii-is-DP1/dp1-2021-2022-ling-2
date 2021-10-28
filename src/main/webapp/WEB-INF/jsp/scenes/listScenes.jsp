<%@ page language="java" contentType="text/html; charset=utf-8"
pageEncoding="utf-8"%> <%@ page session="false" trimDirectiveWhitespaces="true"
%> <%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %> <%@
taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %> <%@ taglib
prefix="c" uri="http://java.sun.com/jsp/jstl/core" %> <%@ taglib prefix="fn"
uri="http://java.sun.com/jsp/jstl/functions" %> <%@ taglib prefix="petclinic"
tagdir="/WEB-INF/tags" %>

<petclinic:layout pageName="scenes">
  <h2>Scenes</h2>

  <table id="scenesTable" class="table table-striped">
    <thead>
      <tr>
        <th style="width: 150px">Name</th>
        <th style="width: 200px">Modifier</th>
        <th>Actions</th>
      </tr>
    </thead>
    <tbody>
      <c:forEach items="${scenes}" var="scene">
        <tr>
          <td>
            <c:out value="${scene.name}" />
          </td>
          <td>
            <c:out value="${scene.modifier}" />
          </td>
          <td>
            <spring:url value="/scenes/delete/{sceneId}" var="sceneUrl">
              <spring:param name="sceneId" value="${scene.id}" />
            </spring:url>
            <a href="${fn:escapeXml(sceneUrl)}">Delete</a>
          </td>
        </tr>
      </c:forEach>
    </tbody>
    <th><a class="btn btn-default my-2" href="/scenes/new">New scene</a></th>
  </table>
</petclinic:layout>
