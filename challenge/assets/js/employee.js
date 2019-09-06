var xml = $.parseXML("<employees></employees>");
var isEditing = false;
var editRowIndex = -1;

$(xml).find('employees').append("<employee><firstname>Zack</firstname><lastname>Siler</lastname><dependents>1</dependents></employee>");

jQuery(document).ready(function() {
	
	loadEmployeeDetails($(xml));

	$('#addEmployeeModal button').eq(1).on('click', function(e) { 
	
		var firstname = $('#employees-form input').eq(0).val();
		var lastname = $('#employees-form input').eq(1).val();
		var dependents = $('#employees-form input').eq(2).val();
	
		if (isEditing)
		{
			$(xml).find('employee').eq(editRowIndex).find('firstname').text(firstname);
			$(xml).find('employee').eq(editRowIndex).find('lastname').text(lastname);
			$(xml).find('employee').eq(editRowIndex).find('dependents').text(dependents);
			
			isEditing = false;
		}
		else
		{
			$(xml).find('employees').append("<employee><firstname>" + firstname + "</firstname><lastname>" + lastname + "</lastname><dependents>" + dependents + "</dependents></employee>");
		}
		
		loadEmployeeDetails($(xml));
		
		$('#employees-form input').eq(0).val('');
		$('#employees-form input').eq(1).val('');
		$('#employees-form input').eq(2).val('');
	});
});

function loadEmployeeDetails(xml) {

	$('#employee-table tbody').html('');
	
	var employee_bi_weekly_salary = 2000;
	var num_pay_periods = 26;
	var employee_benefit_cost = 1000;
	var employee_dependent_cost = 500;
	var discount = .1;
	var employer_total_cost = 0;
	
	$(xml).find('employee').each(function() {
		
		var firstname = $(this).find('firstname').text();
		var lastname = $(this).find('lastname').text();
		var dependents = $(this).find('dependents').text();
		
		employee_total_benefit_cost = employee_benefit_cost + (employee_dependent_cost * dependents);
		
		if (firstname.charAt(0).toUpperCase() == 'A') {
			employee_total_benefit_cost = employee_total_benefit_cost - (employee_total_benefit_cost * discount);
		}
		
		bi_weekly_benefit_cost = employee_total_benefit_cost / num_pay_periods;
		
		var html = '<tr><td>1</td><td>' + firstname + '</td><td>' + lastname + '</td><td>' + (employee_bi_weekly_salary * num_pay_periods).toFixed(2) + '</td><td>' + dependents + '</td><td>' + employee_bi_weekly_salary + '</td><td>' + bi_weekly_benefit_cost.toFixed(2) + '</td><td>' + (employee_bi_weekly_salary - bi_weekly_benefit_cost ).toFixed(2) + '</td><td><span id="btnDelete" class="glyphicon glyphicon-remove" onclick="removeRow(this)" title="Delete"></span><span id="btnEdit" class="glyphicon glyphicon-wrench" onclick="editRow(this)" title="Edit"></span></td></tr>';
		
		$('#employee-table').append(html);
		
		employer_total_cost
		
	});

}

function removeRow(r) {
	var opts = {
	  lines: 13 // The number of lines to draw
	, length: 28 // The length of each line
	, width: 14 // The line thickness
	, radius: 42 // The radius of the inner circle
	, scale: 1 // Scales overall size of the spinner
	, corners: 1 // Corner roundness (0..1)
	, color: '#000' // #rgb or #rrggbb or array of colors
	, opacity: 0.25 // Opacity of the lines
	, rotate: 0 // The rotation offset
	, direction: 1 // 1: clockwise, -1: counterclockwise
	, speed: 1 // Rounds per second
	, trail: 60 // Afterglow percentage
	, fps: 20 // Frames per second when using setTimeout() as a fallback for CSS
	, zIndex: 2e9 // The z-index (defaults to 2000000000)
	, className: 'spinner' // The CSS class to assign to the spinner
	, top: '50%' // Top position relative to parent
	, left: '50%' // Left position relative to parent
	, shadow: false // Whether to render a shadow
	, hwaccel: false // Whether to use hardware acceleration
	, position: 'absolute' // Element positioning
	}
	var target = document.getElementById('employee-table')
	var spinner = new Spinner(opts).spin(target);
	setTimeout(function(){
	var i = r.parentNode.parentNode.rowIndex;
	document.getElementById("employee-table").deleteRow(i);
	removeFromXml(i - 1);
	spinner.stop();
	}, 3000);
}

function editRow(r) {
	isEditing = true;
	editRowIndex = r.parentNode.parentNode.rowIndex - 1;
	
	var firstname = $(xml).find('employee').eq(editRowIndex).find('firstname').text();
	var lastname = $(xml).find('employee').eq(editRowIndex).find('lastname').text();
	var dependents = $(xml).find('employee').eq(editRowIndex).find('dependents').text();
	
	$('#employees-form input').eq(0).val(firstname);
	$('#employees-form input').eq(1).val(lastname);
	$('#employees-form input').eq(2).val(dependents);
	
	$('#addEmployeeModal').modal('show');
}

function removeFromXml(index)
{
	$(xml).find('employee').eq(index).remove();
}


