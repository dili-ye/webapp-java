package com.cn.webapp.proxy.local;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cn.webapp.commons.annotation.ReserveProxy;
import com.cn.webapp.commons.dto.Request;
import com.cn.webapp.commons.dto.Response;
import com.cn.webapp.commons.dto.request.BookDto;
import com.cn.webapp.commons.exception.ScriptException;
import com.cn.webapp.commons.pub.util.PythonProcesser;
import com.cn.webapp.commons.pub.util.UUIDUtil;
import com.cn.webapp.proxy.BookProxy;
import com.google.common.collect.Maps;

@ReserveProxy
public class BookProxyImpl implements BookProxy {

	private static final Logger logger = LoggerFactory.getLogger(BookProxyImpl.class);
	private static final String commandName = "book.py";

	private Map<String, List<BookDto>> bookMap = Maps.newHashMap();

	@Override
	public Response<?> search(Request request) throws ScriptException {
		Map<String, String> context = request.getContext();
		String bookName = context.getOrDefault("bookName", "");
		long startTime = System.currentTimeMillis();
		if (!"".equals(bookName)) {
			if (this.bookMap.containsKey(bookName)) {
				return Response.of(bookMap.get(bookName));
			}
			List<String> result = PythonProcesser.execute(commandName, new String[] { "-s", bookName });
			logger.info("script execute time :{} ms, result size :{}", System.currentTimeMillis() - startTime,
					result.size());
			if (result.size() > 0) {
				List<BookDto> books = new ArrayList<>(result.size());
				for (String s : result) {
					String[] split = s.replaceAll("'", "").split(",");
					if (split.length == 3) {
						String name = split[1].substring(split[1].lastIndexOf("/") + 1, split[1].lastIndexOf("."));
						books.add(BookDto.builder().bookId(UUIDUtil.getUUID32()).from(split[0]).url(split[1]).name(name).imgSrc(split[2]).build());
					} else if (split.length == 4) {
						books.add(
								BookDto.builder().bookId(UUIDUtil.getUUID32()).from(split[0]).url(split[1]).name(split[2]).imgSrc(split[3]).build());
					} else {
						// TODO 还有
					}
				}
				/*if (books.size() > 0) {
					Queue<BookDto> temp = Queues.newLinkedBlockingDeque(books);
					new SpiderThread().execute(new Runnable() {
						@Override
						public void run() {
							while(temp.size()>0) {
								BookDto dto = temp.poll();
							}
						}
					});
				}*/
				this.bookMap.put(bookName, books);
				return Response.of(books);
			}
		}
		return Response.of(198, "null result");
	}
}
