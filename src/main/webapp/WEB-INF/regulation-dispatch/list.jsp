<%--

    Copyright © 2014 Instituto Superior Técnico

    This file is part of the Regulation Dispatch module.

    The Regulation Dispatch module is free software: you can redistribute it and/or modify
    it under the terms of the GNU Lesser General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    The Regulation Dispatch module is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU Lesser General Public License for more details.

    You should have received a copy of the GNU Lesser General Public License
    along with MGP Viewer.  If not, see <http://www.gnu.org/licenses/>.

--%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<% final String contextPath = request.getContextPath(); %>
<script src='<%= contextPath + "/bennu-portal/js/angular.min.js" %>'></script>
<script src='<%= contextPath + "/bennu-scheduler-ui/js/libs/moment/moment.min.js" %>'></script>
<script src='<%= contextPath + "/webjars/jquery-ui/1.11.1/jquery-ui.js" %>'></script>

<style>
<!--
.ordered-column {
    text-align: center;
    cursor: pointer;
}

.ordered-column:hover {
    color: #6ba4d5;
}
-->
</style>


<spring:url var="searchUrl" value="/regulation-dispatch"/>
<form id="searchForm" class="form-inline" role="form" action="${searchUrl}" method="GET">
	<div class="form-group">
		<label class="sr-only" for="searchString">
			<spring:message code="label.search" text="Search"/>
		</label>
		<input name="searchString" type="text" class="form-control" id="searchString" value="${searchString}"
			placeholder="<spring:message code='label.search' text='Search'/>"></input>
	</div>
	<button id="searchRequest" class="btn btn-default"><spring:message code="label.search" text="Search"/></button>
</form>

<br/>

<h3 id="NoResults" style="display: none;"><spring:message code="label.search.empty" text="No available results." /></h3>
<div id="RegulationDispatchList" ng-app="" ng-controller="RegulationDispatchListController" style="display: none;">
    <table class="table table-hover">
        <thead>
            <tr>
				<th class="ordered-column" ng-click="sortFunctionBy('reference')" width="10%"><spring:message code="label.regulation.dispatch.reference" text="Reference"/></th>
				<th class="ordered-column" ng-click="sortFunctionBy('emissionDate')" width="10%"><spring:message code="label.regulation.dispatch.emissionDate" text="Emission Date"/></th>
				<th class="ordered-column" ng-click="sortFunctionBy('instanceDescription')" width="30%"><spring:message code="label.regulation.dispatch.instanceDescription" text="Description"/></th>
				<th class="ordered-column" ng-click="sortFunctionBy('requestorUserName')" width="22%"><spring:message code="label.regulation.dispatch.requestorUser" text="Requestor"/></th>
				<th class="ordered-column" ng-click="sortFunctionBy('regulationReference')" width="15%"><spring:message code="label.regulation.dispatch.regulationReference" text="Reference"/></th>
				<th width="13%"></th>
            </tr>
        </thead>
        <tbody id="projectsSummary" style="font-size:90%;">
            <tr ng-repeat="r in regulationDispatches | orderBy:sortFunction:desc">
            	<td ng-style="set_color('reference')">{{ r.reference }}</td>
            	<td ng-style="set_color('emissionDate')">{{ r.emissionDate }}</td>
            	<td ng-style="set_color('instanceDescription')">{{ r.instanceDescription }}</td>
            	<td ng-style="set_color('requestorUserName')">
            		<img class="img-circle" width="40" height="40" src="{{ r.requestorUserAvatarUrl }}"/>
            		&nbsp;&nbsp;
                	{{ r.requestorUserName }}
            	</td>
            	<td ng-style="set_color('regulationReference')">{{ r.regulationReference }}</td>
            	<td ng-if="r.hasMainDocument">
            		<a href="{{ actionLinkPrefix }}{{r.id}}/view"><img src="<%= request.getContextPath() + "/images/view.gif" %>" alt="View"></a>
            		<a href="{{ actionLinkPrefix }}{{r.id}}/edit"><img src="<%= request.getContextPath() + "/images/edit.gif" %>" alt="Edit"></a>
            		<a href="{{ actionLinkPrefix }}{{r.id}}/delete"><img src="<%= request.getContextPath() + "/images/delete.gif" %>" alt="Delete"></a>
            		<a href="{{ actionLinkPrefix }}{{r.id}}/document"><img src="<%= request.getContextPath() + "/images/document.gif" %>" alt="Document"></a>
            	</td>
            	<td ng-if="!r.hasMainDocument">
            		<a href="{{ actionLinkPrefix }}{{r.id}}/view"><img src="<%= request.getContextPath() + "/images/view.gif" %>" alt="View"></a>
            		<a href="{{ actionLinkPrefix }}{{r.id}}/edit"><img src="<%= request.getContextPath() + "/images/edit.gif" %>" alt="Edit"></a>
            		<a href="{{ actionLinkPrefix }}{{r.id}}/delete"><img src="<%= request.getContextPath() + "/images/delete.gif" %>" alt="Delete"></a>
            	</td>
            </tr>
        </tbody>
    </table>
</div>

<script type="text/javascript">
	var stuff = ${searchResult};
	var contextPath = '<%= contextPath %>';

	function RegulationDispatchListController($scope) {
	    $scope.desc = true; 
	    $scope.orderByField
	    $scope.sortedColumn = 'reference';
	    $scope.regulationDispatches = stuff;
	    $scope.actionLinkPrefix = contextPath + '/regulation-dispatch/';
	    
	    $scope.sortNumericValues = function (column) {
	    	return function (r) {
	    		return r[column];
	    	}
	    }
	    
	    $scope.sortFunction = $scope.sortNumericValues;

	    $scope.sortFunctionBy = function (column) {
	    	if(column === $scope.sortedColumn) {
	    		$scope.desc = !$scope.desc;
	    	} else if(column != $scope.sortedColumn && !$scope.desc) {
	    		$scope.desc = true;
	    	}
	    	$scope.sortedColumn = column;
	    	$scope.sortFunction = $scope.sortNumericValues(column);
	    }

	    $scope.set_color = function (column) {
			if (column == $scope.sortedColumn) {
	    	    return { backgroundColor: "#f5f5f5" }
			}
		}
	}

	$(document).ready(function() {
		if (stuff.length == 0) {
            $('#NoResults').show();
            $('#RegulationDispatchList').hide();
        } else {
            $('#NoResults').hide();
            $('#RegulationDispatchList').show();
        }
	});
</script>
