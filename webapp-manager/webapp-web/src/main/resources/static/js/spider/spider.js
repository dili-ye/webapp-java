function sendUrl(t) {
	var url = $(t).val();
	console.log(url);
	var data = {"url":url};
	if (url && url.length > 0) {
		$.post(basePath+"/"+service+"/findOneTitleMsg",data,function(result) {
			console.info(result);
		});
	}
}