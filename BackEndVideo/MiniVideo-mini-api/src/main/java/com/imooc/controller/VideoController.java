package com.imooc.controller;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.Date;
import java.util.UUID;

import com.imooc.enums.BGMOperatorTypeEnum;
import com.imooc.mapper.BgmMapper;
import com.imooc.pojo.Users;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.imooc.enums.VideoStatusEnum;
import com.imooc.pojo.Bgm;
import com.imooc.pojo.Comments;
import com.imooc.pojo.Videos;
import com.imooc.service.BgmService;
import com.imooc.service.VideoService;
import com.imooc.utils.FetchVideoCover;
import com.imooc.utils.IMoocJSONResult;
import com.imooc.utils.MergeVideoMp3;
import com.imooc.utils.PagedResult;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;


@RestController
@Api(value="视频相关业务的接口", tags= {"视频相关业务的controller"})
@RequestMapping("/video")
public class VideoController extends BasicController {
	
	@Autowired
	private BgmService bgmService;
	
	@Autowired
	private VideoService videoService;
	
	@ApiOperation(value="上传视频", notes="上传视频的接口")
	@ApiImplicitParams({
		@ApiImplicitParam(name="userId", value="用户id", required=true, 
				dataType="String", paramType="form"),
		@ApiImplicitParam(name="bgmId", value="背景音乐id", required=false, 
				dataType="String", paramType="form"),
		@ApiImplicitParam(name="videoSeconds", value="背景音乐播放长度", required=true, 
				dataType="String", paramType="form"),
		@ApiImplicitParam(name="videoWidth", value="视频宽度", required=true, 
				dataType="String", paramType="form"),
		@ApiImplicitParam(name="videoHeight", value="视频高度", required=true, 
				dataType="String", paramType="form"),
		@ApiImplicitParam(name="desc", value="视频描述", required=false, 
				dataType="String", paramType="form")
	})
	@PostMapping(value="/upload", headers="content-type=multipart/form-data")
	public IMoocJSONResult upload(String userId, 
				String bgmId, double videoSeconds, 
				int videoWidth, int videoHeight,
				String desc,
				@ApiParam(value="短视频", required=true)
				MultipartFile file) throws Exception {

		if (StringUtils.isBlank(userId)) {
			return IMoocJSONResult.errorMsg("用户id不能为空...");
		}

		// 具体路径
		String uploadPathDB = "/" + userId + "/video";
		String coverPathDB = "/" + userId + "/video";

		FileOutputStream fileOutputStream = null;
		InputStream inputStream = null;
		String finalVideoPath = "";

		try {
			if (file != null) {

				String fileName = file.getOriginalFilename();
				int lastPointIdx = fileName.lastIndexOf(".");
				String fileNamePrefix = fileName.substring(0,lastPointIdx);

				if (StringUtils.isNotBlank(fileName)) {
					// 文件上传的最终保存路径
					finalVideoPath = FILE_SPACE + uploadPathDB + "/" + fileName;
					// 设置数据库保存的路径
					uploadPathDB += ("/" + fileName);
					coverPathDB += ("/" + fileNamePrefix + ".jpg");

					File outFile = new File(finalVideoPath);
					if (outFile.getParentFile() != null || !outFile.getParentFile().isDirectory()) {
						// 创建父文件夹
						outFile.getParentFile().mkdirs();
					}

					fileOutputStream = new FileOutputStream(outFile);
					inputStream = file.getInputStream();
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

		// 判断bgm是否为空，如果不为空，将存储的视频(无声)和bgm合成新的视频, 并更新uploadPathDB和finalVideoPath
		if (StringUtils.isNoneBlank(bgmId)){
			Bgm bgm = bgmService.queryBgmById(bgmId);
			String mp3InpputPath = FILE_SPACE + bgm.getPath();
			MergeVideoMp3 tool = new MergeVideoMp3(FFMPEG_EXE);
			String videoInputPath = finalVideoPath;
			String videoOutputName = UUID.randomUUID().toString() + ".mp4";
			uploadPathDB = "/" + userId + "/video" +  "/" + videoOutputName;
			finalVideoPath = FILE_SPACE + uploadPathDB;
			tool.convertor(videoInputPath,mp3InpputPath,videoSeconds,finalVideoPath);
		}

		System.out.println("uploadPathDB: "+ uploadPathDB);
		System.out.println("videoOutputPath: "+ finalVideoPath);

		// 对视频截取封面
		String finalCoverPath = FILE_SPACE + coverPathDB;
		FetchVideoCover fetcher = new FetchVideoCover(FFMPEG_EXE);
		fetcher.getCover(finalVideoPath,finalCoverPath);

		// 保存视频信息到数据库
		Videos video = new Videos();
		video.setAudioId(bgmId);
		video.setUserId(userId);
		video.setVideoSeconds((float)videoSeconds);
		video.setVideoHeight(videoHeight);
		video.setVideoWidth(videoWidth);
		video.setVideoDesc(desc);
		video.setVideoPath(uploadPathDB);
		video.setStatus(VideoStatusEnum.SUCCESS.value);
		video.setCreateTime(new Date());
		video.setCoverPath(coverPathDB);

		String videoId = videoService.saveVideo(video);
		return IMoocJSONResult.ok(videoId);
	}


//	@ApiOperation(value="上传封面", notes="上传封面的接口")
//	@ApiImplicitParams({
//			@ApiImplicitParam(name="userId", value="用户id", required=true,
//					dataType="String", paramType="form"),
//			@ApiImplicitParam(name="videoId", value="视频主键id", required=true,
//				dataType="String", paramType="form")
//	})
//	@PostMapping(value="/uploadCover", headers="content-type=multipart/form-data")
//	public IMoocJSONResult uploadCover( String userId,
//				String videoId,
//				@ApiParam(value="视频封面", required=true)
//				MultipartFile file) throws Exception {
//
//		if (StringUtils.isBlank(videoId) || StringUtils.isBlank(userId)) {
//			return IMoocJSONResult.errorMsg("视频id和用户id不能为空...");
//		}
//
//		// 具体路径
//		String uploadPathDB = "/" + userId + "/video";
//
//		FileOutputStream fileOutputStream = null;
//		InputStream inputStream = null;
//		String finalCoverPath = "";
//
//		try {
//			if (file != null) {
//
//				String fileName = file.getOriginalFilename();
//				if (StringUtils.isNotBlank(fileName)) {
//					// 文件上传的最终保存路径
//					finalCoverPath = FILE_SPACE + uploadPathDB + "/" + fileName;
//					// 设置数据库保存的路径
//					uploadPathDB += ("/" + fileName);
//
//					File outFile = new File(finalCoverPath);
//					if (outFile.getParentFile() != null || !outFile.getParentFile().isDirectory()) {
//						// 创建父文件夹
//						outFile.getParentFile().mkdirs();
//					}
//
//					fileOutputStream = new FileOutputStream(outFile);
//					inputStream = file.getInputStream();
//					IOUtils.copy(inputStream, fileOutputStream);
//				}
//
//			} else {
//				return IMoocJSONResult.errorMsg("上传出错...");
//			}
//		} catch (Exception e) {
//			e.printStackTrace();
//			return IMoocJSONResult.errorMsg("上传出错...");
//		} finally {
//			if (fileOutputStream != null) {
//				fileOutputStream.flush();
//				fileOutputStream.close();
//			}
//		}
//
//		videoService.updateVideo(videoId,uploadPathDB);
//
//		return IMoocJSONResult.ok();
//	}


	/**
	 *
	 * @Description: 分页和搜索查询视频列表
	 * isSaveRecord：1 - 需要保存
	 * 				 0 - 不需要保存 ，或者为空的时候
	 *
	 * 	用Videos对象传入多个视频相关的筛选条件，首页展示时传入空对象，模糊查询全部 (desc="")
	 * 	每次分页的对象是所有和搜索匹配的视频
	 * 	只有从搜索跳转到主页时isSaveRecord=1, 保存搜索记录到数据库，其他(上拉刷新, 下拉刷新)都不保存当前的searchContent, 因为跳转过来已经保存过
	 */
	@ApiOperation(value="请求视频列表", notes="请求视频列表的接口")
	@PostMapping(value="/showAll")
	public IMoocJSONResult showAll(@RequestBody Videos video, Integer isSaveRecord,
								   Integer page, Integer pageSize) throws Exception {

		if (page == null) {
			page = 1;
		}

		if (pageSize == null) {
			pageSize = PAGE_SIZE;
		}

		PagedResult result = videoService.getAllVideos(video, isSaveRecord, page, pageSize);
		return IMoocJSONResult.ok(result);
	}


	/**
	 * @Description: 我关注的人发的视频
	 */
	@ApiOperation(value="请求用户关注的人的视频列表", notes="请求用户关注的人的视频列表的接口")
	@PostMapping("/showMyFollow")
	public IMoocJSONResult showMyFollow(String userId, Integer page, Integer pageSize) throws Exception {

		if (StringUtils.isBlank(userId)) {
			return IMoocJSONResult.ok();
		}

		if (page == null) {
			page = 1;
		}

		if (pageSize == null) {
			pageSize = PAGE_SIZE;
		}

		PagedResult videosList = videoService.queryMyFollowVideos(userId, page, pageSize);

		return IMoocJSONResult.ok(videosList);
	}

	/**
	 * @Description: 我收藏(点赞)过的视频列表
	 */
	@ApiOperation(value="请求用户喜欢的视频列表", notes="请求用户喜欢的视频列表的接口")
	@PostMapping("/showMyLike")
	public IMoocJSONResult showMyLike(String userId, Integer page, Integer pageSize) throws Exception {

		if (StringUtils.isBlank(userId)) {
			return IMoocJSONResult.ok();
		}

		if (page == null) {
			page = 1;
		}

		if (pageSize == null) {
			pageSize = PAGE_SIZE;
		}

		PagedResult videosList = videoService.queryMyLikeVideos(userId, page, pageSize);

		return IMoocJSONResult.ok(videosList);
	}

	@ApiOperation(value="请求热搜词", notes="请求热搜词的接口")
	@PostMapping(value="/hot")
	public IMoocJSONResult hot() throws Exception {
		return IMoocJSONResult.ok(videoService.getHotwords());
	}

	@ApiOperation(value="用户喜欢视频", notes="用户喜欢视频的接口")
	@PostMapping(value="/userLike")
	public IMoocJSONResult userLike(String userId, String videoId, String videoCreaterId)
			throws Exception {
		videoService.userLikeVideo(userId, videoId, videoCreaterId);
		return IMoocJSONResult.ok();
	}

	@ApiOperation(value="用户取消喜欢视频", notes="用户取消喜欢视频的接口")
	@PostMapping(value="/userUnLike")
	public IMoocJSONResult userUnLike(String userId, String videoId, String videoCreaterId) throws Exception {
		videoService.userUnLikeVideo(userId, videoId, videoCreaterId);
		return IMoocJSONResult.ok();
	}

	@ApiOperation(value="保存用户评论", notes="保存用户评论的接口")
	@PostMapping("/saveComment")
	public IMoocJSONResult saveComment(@RequestBody Comments comment, String fatherCommentId, String toUserId) throws Exception {

		if (!StringUtils.isBlank(fatherCommentId)){
			comment.setFatherCommentId(fatherCommentId);
		}

		if (!StringUtils.isBlank(toUserId)){
			comment.setToUserId(toUserId);
		}

		videoService.saveComment(comment);
		return IMoocJSONResult.ok();
	}

	@ApiImplicitParams({
			@ApiImplicitParam(name="videoId", value="视频id", required=true,
					dataType="String", paramType="form"),
			@ApiImplicitParam(name="page", value="请求的第几页", required=false,
					dataType="integer", paramType="form"),
			@ApiImplicitParam(name="pageSize", value="每页的大小", required=false,
					dataType="integer", paramType="form")
	})
	@ApiOperation(value="请求视频的评论", notes="请求视频评论的接口")
	@PostMapping("/getVideoComments")
	public IMoocJSONResult getVideoComments(String videoId, Integer page, Integer pageSize) throws Exception {

		if (StringUtils.isBlank(videoId)) {
			return IMoocJSONResult.ok();
		}

		// 分页查询视频列表，时间顺序倒序排序
		if (page == null) {
			page = 1;
		}

		if (pageSize == null) {
			pageSize = 3;
		}

		PagedResult list = videoService.getAllComments(videoId, page, pageSize);

		return IMoocJSONResult.ok(list);

	}
	
}
