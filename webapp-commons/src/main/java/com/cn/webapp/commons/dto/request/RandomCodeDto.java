package com.cn.webapp.commons.dto.request;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class RandomCodeDto {
	private Long codeId;
	private String name;
	private String value;
	private String suffix;
	private Integer count;
	private Boolean isORCode;
	private Boolean isEmail;
	private Boolean isPhone;

}
