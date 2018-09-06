function sendUrl(t) {
	var url = $(t).val();
	var func = $(t).removeAttr("onblur");
	var actionType = $(t).attr("action-Type");
	var data = {
		"url" : url
	};
	if (url && url.length > 0 && JSON.stringify(result_data_all) == "{}") {
		$.post(basePath + service + "/" + actionType, data, function(result) {
			if (result && result.status == 200) {
				var data = JSON.parse(result.data);
				result_data_all = data;
				var spider = eval('(' + actionType + ')')
				new spider().load(data);
			} else {
				alert("error!!!");
			}
			$(t).attr("onblur", "sendUrl(this)");
		});
	}
}

function create_lines(lines_data) {
	var line_cavans = $("<cavans></cavans>");
}

function create_pie(pie_data) {
	var pie_cavans = $("<cavans></cavans>");
}