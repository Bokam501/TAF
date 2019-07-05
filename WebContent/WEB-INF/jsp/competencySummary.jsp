<script type="text/javascript">
function showCompetencySummaryDetails(dimensionId){
	var urlToGetCompetencySummaryOfSelectedDimensionId = 'competency.summary?competencyId='+dimensionId;
	$.ajax({
		type:"POST",
	 	contentType: "application/json; charset=utf-8",
		url : urlToGetCompetencySummaryOfSelectedDimensionId,
		dataType : 'json',
		cache:false,
		success : function(data) {
			var result=data.Records;
			
			var dimensionId = "";
			var name = "";
			var description = "";
			var status = "";
			var ownerId = "";
			var ownerName = "";
			var createdOn = "";
			var lastModifiedById = "";
			var lastModifiedByName = "";
			var lastModifiedDate = "";
			var numberOfProducts = "";	
			var numberOfTeamMembers = "";	
			var numberOfProductsWithTeamMembers = "";
			var numberOfProductsWithoutTeamMembers = "";
			var numberOfAllocatedMembers = "";
			var numberOfUnallocatedMembers = "";
			
			if(data.Records.length === 0) {
				 $("#spCompetencyName").text("");
			} else {
				$.each(result, function(i,item){ 
					dimensionId = item.dimensionId;
					name = item.competencyName;
					description = item.description;
					if(item.status == 1){
						status = 'Active';
					}else{
						status = 'Inactive';
					}
					ownerId = item.ownerId;
					ownerName = item.ownerName;
					createdOn = item.createdOn;
					lastModifiedById = item.lastModifiedById;
					lastModifiedByName = item.lastModifiedByName;
					lastModifiedDate = item.lastModifiedDate;
					numberOfProducts = item.numberOfProducts;	
					numberOfTeamMembers = item.numberOfTeamMembers;	
					numberOfProductsWithTeamMembers = item.numberOfProductsWithTeamMembers;
					numberOfProductsWithoutTeamMembers = item.numberOfProductsWithoutTeamMembers;
					numberOfAllocatedMembers = item.numberOfAllocatedMembers;
					numberOfUnallocatedMembers = item.numberOfUnallocatedMembers;
				});
			} 
			$("#spCompetencyName").text(name);
			$("#spDescription").text(description);
			$("#spStatus").text(status);
			$("#spCreatedBy").text(ownerName);
			$("#spCreatedOn").text(createdOn);
			$("#spModifiedBy").text(lastModifiedByName);
			$("#spModifiedOn").text(lastModifiedDate);
			$("#spTotalMembers").text(numberOfTeamMembers);
			$("#spAllocatedMembers").text(numberOfAllocatedMembers);
			$("#spUnallocatedMembers").text(numberOfUnallocatedMembers);
			$("#spTotalProducts").text(numberOfProducts);
			$("#spProductWithMember").text(numberOfProductsWithTeamMembers);
			$("#spProductWithoutMember").text(numberOfProductsWithoutTeamMembers);
			
		},
		complete:function(data) {
		}
	});
}
</script>

<div class="row"  id="competencySummaryView">
	<div class="col-md-6">
		<div class="table-scrollable">
			<table class="table table-striped table-hover" id="dfnTable">
				<thead>
				<tr height="30">
					<th style="color:black;font-size:16px;padding-top:3px;padding-bottom:3px">About Competency </th>
					<th>
					</th>
				</tr>
				</thead>
				<tbody>
				<tr>
					<td>Name :
					</td>
					<td><span id="spCompetencyName"></span>
					</td>
				</tr>
				<tr>
					<td>Description  : 
					</td>
					<td><span id="spDescription"></span></td>
				</tr>
				<tr>
					<td>Status :
					</td>
					<td><span id="spStatus"></span>
					</td>
				</tr>
				<tr>
					<td>Created By :
					</td>
					<td><span id="spCreatedBy" ></span>
					</td>
				</tr>
				<tr>
					<td>Created On :
					</td>
					<td><span id="spCreatedOn"></span>
					</td>
				</tr>
				<tr>
					<td>Last Modified By : 
					</td>
					<td><span id="spModifiedBy"></span></td>
				</tr>
				<tr>
					<td>Last Modified On : 
					</td>
					<td><span id="spModifiedOn"></span></td>
				</tr>
				</tbody>
			</table>
		</div>
	</div>
	<div class="col-md-6">
		<div class="table-scrollable">
			<table class="table table-striped table-hover" id="membersTable">
				<thead>
				<tr height="30">
					<th style="color:black;font-size:16px;padding-top:3px;padding-bottom:3px">Members </th>
					<th>
					</th>
				</tr>
				</thead>
				<tbody>
					<tr>
						<td>Total Member(s) :
						</td>
						<td><span class="label label-sm label-proper" id="spTotalMembers"></span></td>
					</tr>
					<tr>
						<td>Number of Allocated Member(s) :
						</td>
						<td><span class="label label-sm label-proper" id="spAllocatedMembers"></span>
						</td>
					</tr>
					<tr>
						<td>Number of Unallocated Member(s) :
						</td>
						<td><span class="label label-sm label-proper" id="spUnallocatedMembers"></span>
						</td>
					</tr>
				</tbody>
			</table>
		</div>
		<div class="table-scrollable">
			<table class="table table-striped table-hover" id="productsTable">
				<thead>
				<tr height="30">
					<th style="color:black;font-size:16px;padding-top:3px;padding-bottom:3px">Products 	</th>
					<th>
					</th>
				</tr>
				</thead>
				<tbody>
					<tr>
						<td>Total Product(s) :
						</td>
						<td><span class="label label-sm label-proper" id="spTotalProducts"></span></td>
					</tr>
					<tr>
						<td>Number of Products with Member(s) :
						</td>
						<td><span class="label label-sm label-proper" id="spProductWithMember"></span></td>
					</tr>
					<tr>
						<td>Number of Products without Member(s) :
						</td>
						<td><span class="label label-sm label-proper" id="spProductWithoutMember"></span></td>
					</tr>
				</tbody>
			</table>
		</div>
	</div>
</div>
<style>
#dfnTable td, #membersTable td, #productsTable td {
	border-top:0px;
} 
#dfnTable th, #membersTable th, #productsTable th {
	border-bottom:1px solid #ddd;
} 
#dfnTable tbody tr td:nth-child(1) {
	width:200px !important;
}
#membersTable tbody tr td:nth-child(1), #produtcsTable tbody tr td:nth-child(1) {
	width:300px !important;
}

/* Medium devices (desktops, 992px and up) */
@media (min-width: 992px) {
 #competencySummaryView {max-height:285px;overflow:auto;}
}
/* Large devices (large desktops, 1200px and up) */
@media (min-width: 1200px) { 
 #competencySummaryView {max-height:285px;overflow:auto;}
}
/* Xtra Large devices (xtra large desktops, 1600px and up) */
@media (min-width: 1500px) { 
 #competencySummaryView {max-height:380px;overflow:auto;}
}
@media (min-width: 1600px) { 
 #competencySummaryView {max-height:500px;overflow:auto;}
}
@media (min-width: 1800px) { 
 #competencySummaryView {max-height:800px;overflow:auto;}
}
</style>