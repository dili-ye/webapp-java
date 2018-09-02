package com.cn.webapp.commons.pub.util;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Callable;
import java.util.concurrent.CompletionService;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorCompletionService;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.function.Predicate;

import org.apache.commons.lang3.RandomUtils;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;

public class ThreadForViableResult {

	public static <T> T execute(Set<Callable<T>> calls, int serviceSize, Predicate<T> p) {
		ExecutorService es = Executors.newFixedThreadPool(serviceSize);
		final BlockingQueue<Future<T>> queue = new LinkedBlockingQueue<>();
		final CompletionService<T> service = new ExecutorCompletionService<>(es, queue);
		T t = null;
		calls.forEach(c -> {
			service.submit(c);
		});
		try {
			for (int i = 0; i < calls.size(); i++) {
				t = service.take().get();
				if (p.test(t)) {
					break;
				}
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (ExecutionException e) {
			e.printStackTrace();
		} finally {
			// 这里需要立刻停止线程，并且清空queue队列中的内存
			es.shutdownNow();
			queue.clear();
		}
		return t;
	}

	public static void main(String[] args) {
		HashMap<Integer, String> map = Maps.newHashMap();
		for (int i = 0; i < 100; i++) {
			map.put(i, String.valueOf(i));
		}

		Set<Callable<Map<Integer, String>>> sets = Sets.newHashSet();
		for (int i = 0; i < 5; i++) {
			sets.add(new Callable<Map<Integer, String>>() {
				@Override
				public Map<Integer, String> call() throws Exception {
					HashMap<Integer, String> newHashMap = Maps.newHashMap();
					int divisor = RandomUtils.nextInt(3, 6);
					System.err.println("divisor:" + divisor);
					map.forEach((k, v) -> {
						if (k % divisor == 0) {
							newHashMap.put(k, v);
						}
					});
					return newHashMap;
				}
			});
		}
		Predicate<Map<Integer, String>> p = m -> m.size() > 3;
		Map<Integer, String> result = ThreadForViableResult.execute(sets, 3, p);
		result.forEach((k, v) -> {
			System.out.println(k + "----" + v);
		});
	}

}
