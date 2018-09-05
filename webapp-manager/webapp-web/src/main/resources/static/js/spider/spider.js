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
				var imgSrc = basePath
						+ "file/download?serviceType=default&picPath="
						+ encodeURI(data.picPath);
				loadUserData(data.data);
				loadImg(imgSrc);
			} else {
				alert("error!!!");
			}
			$(t).attr("onblur", "sendUrl(this)");
		});
	}
}


var baiduTieba_loader = function(){
	
	
	loader:function(data){
		
	},
	loaderUserData
}


function loadImg(imgSrc) {
	var img = new Image();
	img.src = imgSrc;
	img.onload = function() {
		console.log("load img .....src:" + this.src);
		if (this.width > 0 && this.height > 0) {
			var width = 500;
			var scale = width / this.width;
			var height = this.height * scale;
			$("#wordcloud").append(
					"<img src='" + imgSrc + "' width='" + width
							+ "px' height='" + height + "px'/>");
		} else {
			this.src = imgSrc;
		}
	}
}

function changeUserTable(t) {
	var $this = $(t);
	if ($this.val() == 'hide') {
		$("#userTable").css("display", "none");
	} else {
		$("#userTable").css("display", "");
	}
}

function create_lines(lines_data) {
	var line_cavans = $("<cavans></cavans>");
}

function create_pie(pie_data) {
	var pie_cavans = $("<cavans></cavans>");
}