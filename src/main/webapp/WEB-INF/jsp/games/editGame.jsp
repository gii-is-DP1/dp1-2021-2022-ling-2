<%@ page session="false" trimDirectiveWhitespaces="true" %> <%@ taglib
prefix="spring" uri="http://www.springframework.org/tags" %> <%@ taglib
prefix="c" uri="http://java.sun.com/jsp/jstl/core" %> <%@ taglib prefix="form"
uri="http://www.springframework.org/tags/form" %> <%@ taglib prefix="petclinic"
tagdir="/WEB-INF/tags" %>

<petclinic:layout pageName="Games">
  <jsp:attribute name="customScript">
    <script>
      $(function (){
        $("date").datepicker({dateFormat: 'yy/mm/dd'});
        $("startTime").timepicker({dateFormat: 'hh/mm/ss'});
        $("finishTime").timepicker({dateFormat: 'hh/mm/ss'});
      })
    </script>
  </jsp:attribute>
  
  <jsp:body>
    <h2>Game</h2>
    <form:form
      modelAttribute="game"
      class="form-horizontal"
      action="/games/save"
    >
      <div class="form-group has-feedback">
        <petclinic:inputField label="Name" name="name"/>
        <petclinic:inputField label="Date" name="date"/>
        <petclinic:inputField label="Start Time" name="startTime"/>
        <petclinic:inputField label="Finish Time" name="finishTime"/>
        <petclinic:inputField label="Comments" name="comments"/>
        <petclinic:inputField label="Spectators Allowed" name="spectatorsAllowed"/>
      </div>

      <div class="form-group">
        <div class="col-sm-offset-2 col-sm-10">
          <input type="hidden" name="id" value="${game.id}" />
          <button class="btn btn-default" type="submit">Save Game</button>
        </div>
      </div>
    </form:form>
  </jsp:body>
</petclinic:layout>
