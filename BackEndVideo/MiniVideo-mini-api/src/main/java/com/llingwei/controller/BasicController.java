package com.llingwei.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import com.llingwei.utils.RedisOperator;

@RestController
public class BasicController {
	
	@Autowired
	public RedisOperator redis;
	
	public static final String USER_REDIS_SESSION = "user-redis-session";
	
	// 文件保存的命名空间
	public static final String FILE_SPACE = "/Users/luolingwei/Desktop/Program/WeChatMiniVideo/Video-Share-Platform/UserFilesDB";
	
	// ffmpeg所在目录
	public static final String FFMPEG_EXE = "/Users/luolingwei/Application/ffmpeg/ffmpeg";

//	// SpringBoot服务器地址
//	public static final String AppServerUrl = "http://192.168.1.178:8081";

	// 每页分页的记录数
	public static final Integer PAGE_SIZE = 7;




}
