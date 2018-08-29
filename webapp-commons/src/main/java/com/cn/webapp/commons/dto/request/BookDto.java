package com.cn.webapp.commons.dto.request;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class BookDto implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1022219443492176232L;
	private String bookId;
	private String from;
	@JsonIgnore
	private String url;
	private String name;
	private String imgSrc;
}
