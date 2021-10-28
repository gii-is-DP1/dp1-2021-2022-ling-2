<%@ page session="false" trimDirectiveWhitespaces="true" %> <%@ taglib
prefix="spring" uri="http://www.springframework.org/tags" %> <%@ taglib
prefix="c" uri="http://java.sun.com/jsp/jstl/core" %> <%@ taglib prefix="form"
uri="http://www.springframework.org/tags/form" %> <%@ taglib prefix="petclinic"
tagdir="/WEB-INF/tags" %>

<petclinic:layout pageName="Scenes">
  <jsp:body>
    <h2>Scene</h2>

    <form:form
      modelAttribute="scene"
      class="form-horizontal"
      action="/scenes/save"
    >
      <div class="form-group has-feedback">
        <petclinic:inputField label="Name" name="name" />
        <petclinic:inputField label="Modifier" name="modifier" />
      </div>

      <div class="form-group">
        <div class="col-sm-offset-2 col-sm-10">
          <input type="hidden" name="id" value="${scene.id}" />
          <button class="btn btn-default" type="submit">Save scene</button>
        </div>
      </div>
    </form:form>
  </jsp:body>
</petclinic:layout>
