
$(document).ready(function() {
	$('.summary-hr, .summary-chart, .summary-table').hide();
	
	$("#submit-categories").click(function(event) {
		showHideResults();
		// send request, get data
		$.ajax({
			type: 'GET',
			url: '/fintrack/user/summary!catXml.page?' + $('form').serialize(),
			dataType: 'xml',
			success: function(xml) {
				summaryTotalChart(xml);
				summaryTotalTable(xml);
				summaryByCategoriesChart(xml);
				summaryByCategoriesTable(xml);
			}
		});
		event.preventDefault();
	});

	$("#submit-months").click(function(event) {
		showHideResults();
		// send request, get data
		$.ajax({
			type: 'GET',
			url: '/fintrack/user/summary!monthXml.page?' + $('form').serialize(),
			dataType: 'xml',
			success: function(xml) {
				summaryTotalChart(xml);
				summaryTotalTable(xml);
				summaryByMonthsChart(xml);
				summaryByMonthsTable(xml);
			}
		});
		event.preventDefault();
	});
});

function summaryTotalChart(xml) {
	if ($('.includes-chart:checked').length) {
		// parse xml
		var incomes_line = [];
		var expenses_line = [];
		$(xml).find('total').each(function() {
			expenses_line = [[parseFloat($(this).attr('expenses_amount')), 1]];
			incomes_line = [[parseFloat($(this).attr('incomes_amount')), 2]];
		});
	
		// plot chart
		$('#summary-total-chart').empty();
		$.jqplot('summary-total-chart', [expenses_line, incomes_line], {
		    title: 'Summary Total',
		    legend: {show: true, location: 'ne', xoffset: 20}, 
		    seriesColors:['#aaaabb', '#aabbaa'],
		    seriesDefaults:{
		        renderer: $.jqplot.BarRenderer, 
		        rendererOptions: {barDirection: 'horizontal', barWidth: 15, barPadding: 5, barMargin: 15, shadowAngle: 135}
		    },
		    series:[
		        {label: 'Expenses'}, {label: 'Incomes'}
		    ],
		    axes:{
		        xaxis: {min:0},
		        yaxis: {
		            renderer: $.jqplot.CategoryAxisRenderer, 
		            ticks: ['Expenses', 'Incomes']
		        }
		    }
		});
	}
}

function summaryTotalTable(xml) {
	if ($('.includes-table:checked').length) {
		// parse xml, generate html
		$(xml).find('total').each(function() {
			var sHTML = 
				'<table class="data">' +
				' <tr><th class="inc_h">Incomes</th><th class="inc_h">Count</th><th class="inc_h">Amount</th><th class="exp_h">Expenses</th><th class="exp_h">Count</th><th class="exp_h">Amount</th></tr>' +
				' <tr><td class="inc_l">Total</td><td class="inc_l">' + $(this).attr('incomes_count') + '</td><td class="inc_r">' + $(this).attr('incomes_amount') + '</td><td class="exp_l">Total</td><td class="exp_l">' + $(this).attr('expenses_count') + '</td><td class="exp_r">' + $(this).attr('expenses_amount') + '</td></tr>' +			
				'</table>';
			$('#summary-total-table').html(sHTML);
		});
	}
}

function summaryByCategoriesChart(xml) {
	if ($('.includes-chart:checked').length) {
		// parse xml
		var name_ticks = [];
		var amount_line = [];
		var categories = $(xml).find('category') 
		categories.each(function(index){
			var category = $(this);
			name_ticks.unshift(category.attr('name'));
			amount_line.push([parseFloat(category.attr('amount')), categories.length-index]);
		});
	
		// plot chart
		$('#summary-chart').empty();
		$.jqplot('summary-chart', [amount_line], {
		    title: 'Summary By Categories',
		    legend: {show: true, location: 'ne', xoffset: 20}, 
		    seriesColors:['#aaaabb'],
		    seriesDefaults:{
		        renderer: $.jqplot.BarRenderer, 
		        rendererOptions: {barDirection: 'horizontal', barWidth: 15, barPadding: 5, barMargin: 15, shadowAngle: 135}
		    },
		    series:[
		        {label: 'Expenses'}
		    ],
		    axes:{
		        xaxis:{min:0},
		        yaxis:{
		            renderer: $.jqplot.CategoryAxisRenderer, 
		            ticks: name_ticks
		        }
		    }
		});
	}
}

function summaryByCategoriesTable(xml) {
	if ($('.includes-table:checked').length) {
		// parse xml, generate html
		var sHTML = '<table class="data">\n<tr><th class="exp_h">Category</th><th class="exp_h">Count</th><th class="exp_h">Amount</th></tr>';
		var categories = $(xml).find('category') 
		categories.each(function(index){
			sHTML += '  <tr><td class="exp_l">' + $(this).attr('name') + '</td><td class="exp_l">' + $(this).attr('count') + '</td><td class="exp_r">' + $(this).attr('amount') + '</td></tr>';
		});
		sHTML += '</table>';
		$('#summary-table').html(sHTML);
	}
}

function summaryByMonthsChart(xml) {
	if ($('.includes-chart:checked').length) {
		// parse xml
		var name_ticks = [];
		var incomes_line = [];
		var expenses_line = [];
		$(xml).find('month').each(function() {
			name_ticks.push($(this).attr('name'));
			incomes_line.push(parseFloat($(this).attr('incomes_amount')));
			expenses_line.push(parseFloat($(this).attr('expenses_amount')));
		});

		// plot chart
		$('#summary-chart').empty();
		$.jqplot('summary-chart', [incomes_line, expenses_line], {
		    title: 'Summary By Month',
		    legend: {show: true, location: 'ne', xoffset: 20}, 
		    seriesColors:['#aabbaa', '#aaaabb'],
		    seriesDefaults:{
		        renderer: $.jqplot.BarRenderer, 
		        rendererOptions: {barWidth: name_ticks.length > 12 ? 5 : 15, barPadding: 5, barMargin: 15}
		    },
		    series:[
		        {label: 'Incomes'}, 
		        {label: 'Expenses'}
		    ],
		    axes:{
		        xaxis:{
		            renderer: $.jqplot.CategoryAxisRenderer, 
		            ticks: name_ticks
		        }, 
		        yaxis:{min:0}
		    }
		});
	}
}

function summaryByMonthsTable(xml) {
	if ($('.includes-table:checked').length) {
		// parse xml, generate html
		var sHTML = '<table class="data">\n<tr><th class="inc_h">Incomes</th><th class="inc_h">Count</th><th class="inc_h">Amount</th><th class="exp_h">Expences</th><th class="exp_h">Count</th><th class="exp_h">Amount</th></tr>';
		$(xml).find('month').each(function() {
			sHTML += '  <tr><td class="inc_l">' + $(this).attr('name') + '</td><td class="inc_l">' + $(this).attr('incomes_count') + '</td><td class="inc_r">' + $(this).attr('incomes_amount') + '</td><td class="exp_l">' + $(this).attr('name') + '</td><td class="exp_l">' + $(this).attr('expenses_count') + '</td><td class="exp_r">' + $(this).attr('expenses_amount') + '</td></tr>';
		});
		sHTML += '</table>';
		$('#summary-table').html(sHTML);
	}
}

function showHideResults() {
	$('.summary-hr').hide();
	if ($('.includes-chart:checked').length) {
		$('.summary-hr').show();
		$('.summary-chart').show();
	} else {
		$('.summary-chart').hide();
	}
	if ($('.includes-table:checked').length) {
		$('.summary-hr').show();
		$('.summary-table').show();
	} else {
		$('.summary-table').hide();
	}
}
