function sendUrl(t) {
	var url = $(t).val();
	console.log(url);
	var func = $(t).removeAttr("onblur");
	var data = {
		"url" : url
	};
	if (url && url.length > 0) {
		$.post(basePath + "/" + service + "/findOneTitleMsg", data, function(
				result) {
			console.info(result);
			if (result.status == 200) {
				var data = JSON.parse(result.data);
				console.log(data);
				$("#wordcloud").attr(
						"src",
						basePath + "file/download?serviceType=default&picPath="
								+ data.picPath);
			} else {
				alert("error!!!");
			}
			$(t).attr("onblur", "sendUrl(this)");
		});
	}
}