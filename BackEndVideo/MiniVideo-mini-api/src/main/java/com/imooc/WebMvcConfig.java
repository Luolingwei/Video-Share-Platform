package com.imooc;

import com.imooc.controller.interceptor.MiniInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

//import com.imooc.controller.interceptor.MiniInterceptor;

@Configuration
public class WebMvcConfig extends WebMvcConfigurerAdapter {

	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		registry.addResourceHandler("/**")
				.addResourceLocations("classpath:/META-INF/resources/")
				.addResourceLocations("file:/Users/luolingwei/Desktop/Program/WeChatMiniVideo/Video-Share-Platform/UserFilesDB/");
	}

	/**
	 * registry为注册中心, 可以添加拦截器
	 * addPathPatterns方法添加监听接口
	 * @param registry
	 */
	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		registry.addInterceptor(miniInterceptor()).addPathPatterns("/user/**")
													.addPathPatterns("/bgm/**")
													.addPathPatterns("/video/upload","/video/userLike","/video/userUnLike", "/video/saveComment")
													.excludePathPatterns("/user/queryPublisher");
		super.addInterceptors(registry);
	}

	/**
	 * 注册拦截器
	 * @return
	 */
	@Bean
	public MiniInterceptor miniInterceptor(){
		return new MiniInterceptor();
	}

	@Bean(initMethod = "init")
	public ZKCuratorClient zkCuratorClient(){
		return new ZKCuratorClient();
	}

}
