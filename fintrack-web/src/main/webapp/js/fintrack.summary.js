
$(document).ready(function() {
	$('.summary-chart').css({height: '0px'});
	summaryTotalChart();
	summaryByCategoriesChart();
	summaryByMonthsChart();
});

function summaryTotalChart() {
	if (!$('#summary-total-chart').length) {
		return;
	}
	
	// search DOM
	var incomes_line = [];
	var expenses_line = [];
	var total = $('tr.total_values'); 
	total.each(function(index, element) {
		var tds = $(this).children();
		expenses_line = [[parseFloat($(tds[5]).text()), 1]];
		incomes_line = [[parseFloat($(tds[2]).text()), 2]];
	});
	if ((!expenses_line || incomes_line[0][0] == 0) && (!expenses_line || expenses_line[0][0] == 0)) {
		return;
	}

	// plot chart
	$('#summary-total-chart').css({height: '180px'}).empty();
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

function summaryByCategoriesChart() {
	if ($("#summary-categories-chart").length == 0) {
		return;
	}
	
	// search DOM
	var name_ticks = [];
	var amount_line = [];
	var categories = $('tr.category_values'); 
	categories.each(function(index) {
		var tds = $(this).children();
		name_ticks.unshift($(tds[0]).text());
		amount_line.push([parseFloat($(tds[2]).text()), categories.length-index]);
	});
	if (!categories.length) {
		return;
	}

	// plot chart
	$('#summary-categories-chart').css({height: '360px'}).empty();
	$.jqplot('summary-categories-chart', [amount_line], {
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

function summaryByMonthsChart() {
	if ($('#summary-months-chart').length == 0) {
		return;
	}
	
	// search DOM
	var name_ticks = [];
	var incomes_line = [];
	var expenses_line = [];
	var months = $('tr.month_values'); 
	months.each(function() {
		var tds = $(this).children();
		name_ticks.push($(tds[0]).text());
		incomes_line.push(parseFloat($(tds[2]).text()));
		expenses_line.push(parseFloat($(tds[5]).text()));
	});
	if (!months.length) {
		return;
	}

	// plot chart
	$('#summary-months-chart').css({height: '360px'}).empty();
	$.jqplot('summary-months-chart', [incomes_line, expenses_line], {
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
