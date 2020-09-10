package com.llingwei.controller;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.llingwei.pojo.Users;
import com.llingwei.pojo.UsersReport;
import com.llingwei.pojo.vo.PublisherVideo;
import com.llingwei.pojo.vo.UsersVO;
import com.llingwei.service.UserService;
import com.llingwei.utils.IMoocJSONResult;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;

@RestController
@Api(value="用户相关业务的接口", tags= {"用户相关业务的controller"})
@RequestMapping("/user")
public class UserController extends BasicController {
	
	@Autowired
	private UserService userService;
	
	@ApiOperation(value="用户上传头像", notes="用户上传头像的接口")
	@ApiImplicitParam(name="userId", value="用户id", required=true, 
						dataType="String", paramType="query")
	@PostMapping("/uploadFace")
	public IMoocJSONResult uploadFace(String userId, 
				@RequestParam("file") MultipartFile[] files) throws Exception {
		
		if (StringUtils.isBlank(userId)) {
			return IMoocJSONResult.errorMsg("用户id不能为空...");
		}
		
		// 文件保存的命名空间

		// 具体路径
		String uploadPathDB = "/" + userId + "/face";
		
		FileOutputStream fileOutputStream = null;
		InputStream inputStream = null;
		try {
			if (files != null && files.length > 0) {
				
				String fileName = files[0].getOriginalFilename();
				if (StringUtils.isNotBlank(fileName)) {
					// 文件上传的最终保存路径
					String finalFacePath = FILE_SPACE + uploadPathDB + "/" + fileName;
					// 设置数据库保存的路径
					uploadPathDB += ("/" + fileName);
					
					File outFile = new File(finalFacePath);
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

		Users user = new Users();
		user.setId(userId);
		user.setFaceImage(uploadPathDB);
		userService.updateUserInfo(user);

		return IMoocJSONResult.ok(uploadPathDB);
	}

	@ApiOperation(value="查询用户信息", notes="查询用户信息的接口")
	@ApiImplicitParam(name="userId", value="用户id", required=true,
						dataType="String", paramType="query")
	@PostMapping("/query")
	public IMoocJSONResult query(String userId, String fanId) throws Exception {

		if (StringUtils.isBlank(userId)) {
			return IMoocJSONResult.errorMsg("用户id不能为空...");
		}

		Users userInfo = userService.queryUserInfo(userId);
		UsersVO userVO = new UsersVO();
		BeanUtils.copyProperties(userInfo, userVO);

		userVO.setFollow(userService.queryIfFollow(userId, fanId));

		return IMoocJSONResult.ok(userVO);
	}

	@ApiOperation(value="查询视频发布者信息", notes="查询视频发布者信息的接口")
	@PostMapping("/queryPublisher")
	public IMoocJSONResult queryPublisher(String loginUserId, String videoId,
			String publishUserId) throws Exception {

		if (StringUtils.isBlank(publishUserId)) {
			return IMoocJSONResult.errorMsg("");
		}

		PublisherVideo bean = new PublisherVideo();
		// 1 查询视频发布者的信息
		Users user = userService.queryUserInfo(publishUserId);
		UsersVO publisher = new UsersVO();
		BeanUtils.copyProperties(user, publisher);

		// 2 查询当前登陆者和视频的点赞关系
		boolean like = userService.isUserLikeVideo(loginUserId,videoId);

		// 将上述两块信息封装到PublisherVideo中抛出
		bean.setPublisher(publisher);
		bean.setUserLikeVideo(like);

		return IMoocJSONResult.ok(bean);
	}

	@ApiOperation(value="用户关注", notes="用户关注的接口")
	@PostMapping("/beyourfans")
	public IMoocJSONResult beyourfans(String userId, String fanId) throws Exception {

		if(StringUtils.isBlank(userId) || StringUtils.isBlank(fanId)){
			return IMoocJSONResult.errorMsg("");
		}

		userService.saveUserFanRelation(userId,fanId);

		return IMoocJSONResult.ok("关注成功...");
	}

	@ApiOperation(value="用户取消关注", notes="用户取消关注的接口")
	@PostMapping("/dontbeyourfans")
	public IMoocJSONResult dontbeyourfans(String userId, String fanId) throws Exception {

		if (StringUtils.isBlank(userId) || StringUtils.isBlank(fanId)) {
			return IMoocJSONResult.errorMsg("");
		}

		userService.deleteUserFanRelation(userId, fanId);

		return IMoocJSONResult.ok("取消关注成功...");
	}


	@ApiOperation(value="举报用户", notes="举报用户的接口")
	@PostMapping("/reportUser")
	public IMoocJSONResult reportUser(@RequestBody UsersReport usersReport) throws Exception {

		// 保存举报信息
		userService.reportUser(usersReport);

		return IMoocJSONResult.ok("举报成功...有你平台变得更美好...");
	}
	
}
