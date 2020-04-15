package com.imooc.controller;
import com.imooc.pojo.Bgm;
import com.imooc.service.VideoService;
import com.imooc.utils.PagedResult;
import org.apache.commons.io.IOUtils;
import com.imooc.bean.AdminUser;
import com.imooc.utils.IMoocJSONResult;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.UUID;


@Controller
@RequestMapping("video")
public class VideoController extends BasicController{

	@Autowired
	private VideoService videoService;

	@GetMapping("/showBgmList")
	public String showBgmList() {
		return "video/bgmList";
	}

	@GetMapping("/showAddBgm")
	public String login() {
		return "video/addBgm";
	}

	@PostMapping("/queryBgmList")
	@ResponseBody
	public PagedResult queryBgmList(Integer page) {
		return videoService.queryBgmList(page, PAGE_SIZE);
	}

	@PostMapping("/addBgm")
	@ResponseBody
	public IMoocJSONResult addBgm(Bgm bgm) {

		videoService.addBgm(bgm);
		return IMoocJSONResult.ok();

	}

	@PostMapping("/delBgm")
	@ResponseBody
	public IMoocJSONResult delBgm(String bgmId) {

		videoService.deleteBgm(bgmId);
		return IMoocJSONResult.ok();

	}

	@PostMapping("/bgmUpload")
	@ResponseBody
	public IMoocJSONResult bgmUpload(@RequestParam("file") MultipartFile[] files) throws Exception {


		// 文件保存的命名空间
		String FILE_SPACE = "/Users/luolingwei/Desktop/Program/WeChatMiniVideo/Video-Share-Platform/UserFilesDB/mvc-bgm";

		// 具体路径
		String uploadPathDB = "/" + "bgm";

		FileOutputStream fileOutputStream = null;
		InputStream inputStream = null;
		try {
			if (files != null && files.length > 0) {

				String fileName = files[0].getOriginalFilename();
				if (StringUtils.isNotBlank(fileName)) {
					// 文件上传的最终保存路径
					String finalPath = FILE_SPACE + uploadPathDB + "/" + fileName;
					// 设置数据库保存的路径
					uploadPathDB += ("/" + fileName);

					File outFile = new File(finalPath);
					if (outFile.getParentFile() != null || !outFile.getParentFile().isDirectory()) {
						// 创建父文件夹
						outFile.getParentFile().mkdirs();
					}

					fileOutputStream = new FileOutputStream(outFile);
					inputStream = files[0].getInputStream();
					IOUtils.copy(inputStream, fileOutputStream);
				}

			} else {
				return IMoocJSONResult.errorMsg("上传出错...");
			}
		} catch (Exception e) {
			e.printStackTrace();
			return IMoocJSONResult.errorMsg("上传出错...");
		} finally {
			if (fileOutputStream != null) {
				fileOutputStream.flush();
				fileOutputStream.close();
			}
		}

		return IMoocJSONResult.ok(uploadPathDB);
	}


}
