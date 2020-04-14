package com.imooc.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RestController;

@Controller
public class BasicController {
	
	// 文件保存的命名空间
	public static final String FILE_SPACE = "/Users/luolingwei/Desktop/Program/WeChatMiniVideo/Video-Share-Platform/UserFilesDB";
	
	// 每页分页的记录数
	public static final Integer PAGE_SIZE = 10;
	
}
