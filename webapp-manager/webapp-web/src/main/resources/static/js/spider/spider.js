function sendUrl(t) {
	var url = $(t).val();
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
				var imgSrc = basePath
						+ "file/download?serviceType=default&picPath="
						+ encodeURI(data.picPath);
				var img = new Image();
				img.src = imgSrc;
				img.onload = function() {
					console.log("load img .....src:" + this.src);
					if (this.width > 0 && this.height > 0) {
						var scale = 250 / this.width;
						var width = 250;
						var height = this.height * scale;
						$("#wordcloud").append("<img src='" + imgSrc + "' width='" + width
										+ "px' height='" + height + "px'/>");
					} else {
						this.src = imgSrc;
					}
				}
			} else {
				alert("error!!!");
			}
			$(t).attr("onblur", "sendUrl(this)");
		});
	}
}