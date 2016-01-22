<!DOCTYPE html>

<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>

<html>
<script
	src="http://ajax.googleapis.com/ajax/libs/angularjs/1.4.8/angular.min.js"></script>
<script
	src="http://cdnjs.cloudflare.com/ajax/libs/angular.js/1.2.16/angular-resource.min.js"></script>
<script
	src="http://ajax.googleapis.com/ajax/libs/angularjs/1.4.8/angular-resource.min.js"></script>
<link href="<c:url value="/resources/css/main.css" />" rel="stylesheet">

<script
	src="<c:url value="/resources/js/controller/mainController.js" />"></script>
<script src="<c:url value="/resources/js/resourceProvider.js" />"></script>
<head>

<meta charset="utf-8">
<title>Welcome</title>
</head>
<body>
	<div ng-app="main" ng-controller="mainCtrl" ng-init="helloWorld()">
		Aktualne wydarzenia:

		<table id="main-container" ng-show="events.length > 0">
			<tr>
				<th>Data</th>
				<th>Nazwa</th>
				<th>0 dni przed</th>
				<th>1 dzien przed</th>
				<th>3 dni przed</th>
				<th>7 dni przed</th>
				<th>14 dni przed</th>
				<th>30 dni przed</th>
			</tr>
			<tr ng-repeat="event in events">
				<td>{{event.eventDate | date:'MM-dd'}}</td>
				<td>{{event.name}}</td>
				<td><input ng-click="onOffCheckbox($index, 0)" type="checkbox" ng-checked="{{hasReminder(event, 0)}}"></input></td>
				<td><input ng-click="onOffCheckbox($index, 1)" type="checkbox" ng-checked="{{hasReminder(event, 1)}}"></input></td>
				<td><input ng-click="onOffCheckbox($index, 3)" type="checkbox" ng-checked="{{hasReminder(event, 3)}}"></input></td>
				<td><input ng-click="onOffCheckbox($index, 7)" type="checkbox" ng-checked="{{hasReminder(event, 7)}}"></input></td>
				<td><input ng-click="onOffCheckbox($index, 14)" type="checkbox" ng-checked="{{hasReminder(event, 14)}}"></input></td>
				<td><input ng-click="onOffCheckbox($index, 30)" type="checkbox" ng-checked="{{hasReminder(event, 30)}}"></input></td>
			</tr>
			<tr>
				<td><input id="newDate" type="date"></input></td>
				<td><input id="newName" type="text"></td>
				<td><input id="newCheck0" type="checkbox"></td>
				<td><input id="newCheck1" type="checkbox"></td>
				<td><input id="newCheck3" type="checkbox"></td>
				<td><input id="newCheck7" type="checkbox"></td>
				<td><input id="newCheck14" type="checkbox"></td>
				<td><input id="newCheck30" type="checkbox"></td>
			</tr>
		</table>
		<div ng-click="addNew()">Dodaj nowe wydarzenie</div>
		<div ng-show="unsavedChanges" ng-click="saveChanges()">Zapisz zmiany</div>
		<div ng-click="sendReminders()">Wyslij przypomnienia</div>

	</div>

	<script>
		
	</script>

</body>
</html>
