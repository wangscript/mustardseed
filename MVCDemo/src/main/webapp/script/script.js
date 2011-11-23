$(document).ready(function() {
    $("#formValid").submit(function(e) {
	var err = $("#formValid").formValid(main);
	if (err) {
	    var msg = ""
	    for (var i in err) {
		for (var j = 0; j < err[i].length; j += 1)
		    msg += err[i][j] + "<br/>";
	    }
            $("#validResult").html(msg);
            return confirm("是否忽略客户端验证？");
        }
        return true;
    });
});