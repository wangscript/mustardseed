/*
 *author: HermitWayne
 */
(function($) {
    $.fn.formValid = function (validator, getter){
	if (!getter)
	    getter = valid_getter;
	var ret = validator(getter(this));
	for (var _ in ret) {
	    return ret;
	}
	return;
    }
    function valid_getter(form) {
	var obj = $(form).serializeArray();
	var ret = {};
	for (var i = 0; i < obj.length; i += 1) {
	    var name = obj[i]["name"];
	    var value = obj[i]["value"];
	    if (!ret[name])
		ret[name] = new Array();
	    ret[name].push(value);
	}
	return ret;
    }
})(jQuery);
