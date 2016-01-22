var app = angular.module('main', [ 'resourceProvider' ]);
app
		.controller(
				'mainCtrl',
				function($scope, MainService) {
					$scope.events = null;
					$scope.remindersMatrix = null;
					$scope.unsavedChanges = false;
					$scope.unsavedIds = null;
					$scope.translateMatrix = null;
					$scope.helloWorld = function() {
						MainService.helloWorld(function(results) {
							$scope.events = results;
							initializeRemindersMatrix(results);
						});
					}

					function initializeRemindersMatrix(results) {
						var arr = new Array(results.length);
						$scope.translateMatrix = [];
						for (var i = 0; i < results.length; i++) {
							$scope.translateMatrix[i] = results[i].id;
							arr[i] = [ $scope.hasReminder(results[i], 0),
									$scope.hasReminder(results[i], 1),
									$scope.hasReminder(results[i], 3),
									$scope.hasReminder(results[i], 7),
									$scope.hasReminder(results[i], 14),
									$scope.hasReminder(results[i], 30) ];
						}
						$scope.remindersMatrix = arr;
						$scope.unsavedIds = [];
					}

					$scope.hasReminder = function(event, db) {
						var i;
						for (i = 0; i < event.reminders.length; i++) {
							var reminder = event.reminders[i];
							if (reminder.daysBefore == db) {
								return true;
							}
						}
						return false;
					}

					$scope.onOffCheckbox = function(event, db) {
						var dbId = getDbId(db);
						$scope.remindersMatrix[event][dbId] = !$scope.remindersMatrix[event][dbId];
						// console.log($scope.remindersMatrix);
						$scope.unsavedChanges = true;
						addChange(event)
					}

					function addChange(id) {
						var i;
						var shouldAdd = true;
						for (i = 0; i < $scope.unsavedIds.length; i++) {
							if ($scope.unsavedIds[i] == id) {
								shouldAdd = false;
								break;
							}
						}
						if (shouldAdd) {
							$scope.unsavedIds.push(id);
						}
					}

					function getDbId(db) {
						if (db == 0)
							return 0;
						if (db == 1)
							return 1;
						if (db == 3)
							return 2;
						if (db == 7)
							return 3;
						if (db == 14)
							return 4;
						if (db == 30)
							return 5;

					}

					$scope.saveChanges = function() {
						var str = createSendString();
						MainService.save(str, function(result) {
							$scope.helloWorld;
							alert("done");
						});
					}

					function createSendString() {
						var res = "";
						for (i = 0; i < $scope.unsavedIds.length; i++) {
							var tempRes = $scope.translateMatrix[$scope.unsavedIds[i]];
							for (j = 0; j < 6; j++) {
								var tempRes2 = $scope.remindersMatrix[$scope.unsavedIds[i]][j];
								tempRes = tempRes + ";" + tempRes2;
							}
							res = res + tempRes + ":";
						}
						return res;
					}

					$scope.addNew = function() {
						var str = document.getElementById("newCheck0").checked
								+ ";"
								+ document.getElementById("newCheck1").checked
								+ ";"
								+ document.getElementById("newCheck3").checked
								+ ";"
								+ document.getElementById("newCheck7").checked
								+ ";"
								+ document.getElementById("newCheck14").checked
								+ ";"
								+ document.getElementById("newCheck30").checked
								+ ";"
								+ document.getElementById("newDate").value
								+ ";"
								+ document.getElementById("newName").value
								+ ";";
						MainService.add(str, function(result) {
							$scope.helloWorld;
							alert("done");
						});
					}

					$scope.sendReminders = function() {
						MainService.send(function(result) {
							$scope.helloWorld;
							alert("done");
						});
					}
				}

		).factory('MainService', [ 'Resource', function(Resource) {

			return Resource(':path/:requestedValue', {
				path : '@path',
				requestedValue : '@requestedValue'
			}, {
				"helloWorld" : {
					method : 'PUT',
					params : {
						path : 'hello'
					},
					isArray : true
				},
				"save" : {
					method : 'PUT',
					params : {
						path : 'save',
						requestedValue : '@requestedValue'
					}
				},
				"add" : {
					method : 'PUT',
					params : {
						path : 'add',
						requestedValue : '@requestedValue'
					}
				},
				"send" : {
					method : 'PUT',
					params : {
						path : 'send'
					}
				}
			});

		} ]);