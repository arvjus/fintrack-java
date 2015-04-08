
$(document).ready(function() {
    $('input.focus').focus();  
    $('input.back').click(function(event) {
    	$('form').attr('action', '/fintrack/user/list!back.page');
    });  
});
