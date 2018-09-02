package com.cn.webapp.proxy.init;

import java.lang.reflect.Field;
import java.util.Collection;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.cloud.netflix.feign.EnableFeignClients;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import com.cn.webapp.commons.annotation.Reserved;
import com.cn.webapp.proxy.Proxy;
import com.cn.webapp.proxy.context.ProxyContext;
import com.cn.webapp.service.BaseService;
import com.cn.webapp.service.context.ServiceContext;

@Component("webappWebRunner")
@Order(-100)
public class WebappWebRunner implements ApplicationRunner {

	@Resource
	ProxyContext proxyContext;

/*	@Resource
	BaseContext baseContext;
*/
	@Resource
	ServiceContext serviceContext;

	@Override
	public void run(ApplicationArguments args) throws Exception {
		/*Object bootBean = baseContext.getBootBean();
		if (bootBean != null && bootBean.getClass().getAnnotation(EnableFeignClients.class) != null) {
			return;
		}*/
		resolveProxy();
	}

	protected void resolveProxy() {
		Collection<BaseService> services = serviceContext.getBeans(null);
		if (services.size() == 0) {
			return;
		}
		for (BaseService service : services) {
			Field[] fields = service.getClass().getDeclaredFields();
			for (Field f : fields) {
				if (f.getAnnotation(Reserved.class) != null) {
					Proxy bean = proxyContext.getBean(f.getType());
					f.setAccessible(true);
					try {
						f.set(service, f.getType().cast(bean));
					} catch (Exception e) {
					}
					f.setAccessible(false);
				}
			}
		}

	}
}
