package com.llingwei.service.impl;

import java.util.Date;
import java.util.List;

import org.n3r.idworker.Sid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.llingwei.mapper.CommentsMapper;
import com.llingwei.mapper.CommentsMapperCustom;
import com.llingwei.mapper.SearchRecordsMapper;
import com.llingwei.mapper.UsersLikeVideosMapper;
import com.llingwei.mapper.UsersMapper;
import com.llingwei.mapper.VideosMapper;
import com.llingwei.mapper.VideosMapperCustom;
import com.llingwei.pojo.Comments;
import com.llingwei.pojo.SearchRecords;
import com.llingwei.pojo.UsersLikeVideos;
import com.llingwei.pojo.Videos;
import com.llingwei.pojo.vo.CommentsVO;
import com.llingwei.pojo.vo.VideosVO;
import com.llingwei.service.VideoService;
import com.llingwei.utils.PagedResult;
import com.llingwei.utils.TimeAgoUtils;

import tk.mybatis.mapper.entity.Example;
import tk.mybatis.mapper.entity.Example.Criteria;

@Service
public class VideoServiceImpl implements VideoService {

	@Autowired
	private VideosMapper videosMapper;
	
	@Autowired
	private UsersMapper usersMapper;
	
	@Autowired
	private VideosMapperCustom videosMapperCustom;
	
	@Autowired
	private SearchRecordsMapper searchRecordsMapper; 
	
	@Autowired
	private UsersLikeVideosMapper usersLikeVideosMapper; 
	
	@Autowired
	private CommentsMapper commentMapper; 
	
	@Autowired
	private CommentsMapperCustom commentMapperCustom;
	
	@Autowired
	private Sid sid;

	@Transactional(propagation = Propagation.REQUIRED)
	@Override
	public String saveVideo(Videos video) {

		String id = sid.nextShort();
		video.setId(id);
		videosMapper.insertSelective(video);

		return id;
	}

	@Transactional(propagation = Propagation.REQUIRED)
	@Override
	public void updateVideo(String videoId, String coverPath) {
		
		Videos video = new Videos();
		video.setId(videoId);
		video.setCoverPath(coverPath);
		videosMapper.updateByPrimaryKeySelective(video);
	}

	@Transactional(propagation = Propagation.REQUIRED)
	@Override
	public PagedResult getAllVideos(Videos video, Integer isSaveRecord, Integer page, Integer pageSize) {

		// 保存热搜词
		String desc = video.getVideoDesc();
		String userId = video.getUserId();
		if (isSaveRecord != null && isSaveRecord == 1) {
			SearchRecords record = new SearchRecords();
			String recordId = sid.nextShort();
			record.setId(recordId);
			record.setContent(desc);
			searchRecordsMapper.insert(record);
		}

		PageHelper.startPage(page,pageSize);
		List<VideosVO> list = videosMapperCustom.queryAllVideos(desc,userId);

		PageInfo<VideosVO> pageList = new PageInfo<>(list);
		PagedResult pagedResult = new PagedResult();
		pagedResult.setPage(page);
		pagedResult.setTotal(pageList.getPages());
		pagedResult.setRecords(pageList.getTotal());
		pagedResult.setRows(list);

		return pagedResult;
	}
	
	@Transactional(propagation = Propagation.SUPPORTS)
	@Override
	public PagedResult queryMyLikeVideos(String userId, Integer page, Integer pageSize) {

		PageHelper.startPage(page,pageSize);
		List<VideosVO> list = videosMapperCustom.queryMyLikeVideos(userId);

		PageInfo<VideosVO> pageList = new PageInfo<>(list);
		PagedResult pagedResult = new PagedResult();
		pagedResult.setPage(page);
		pagedResult.setTotal(pageList.getPages());
		pagedResult.setRecords(pageList.getTotal());
		pagedResult.setRows(list);

		return pagedResult;
	}
	
	@Transactional(propagation = Propagation.SUPPORTS)
	@Override
	public PagedResult queryMyFollowVideos(String userId, Integer page, Integer pageSize) {

		PageHelper.startPage(page,pageSize);
		List<VideosVO> list = videosMapperCustom.queryMyFollowVideos(userId);

		PageInfo<VideosVO> pageList = new PageInfo<>(list);
		PagedResult pagedResult = new PagedResult();
		pagedResult.setPage(page);
		pagedResult.setTotal(pageList.getPages());
		pagedResult.setRecords(pageList.getTotal());
		pagedResult.setRows(list);

		return pagedResult;
	}

	@Transactional(propagation = Propagation.SUPPORTS)
	@Override
	public List<String> getHotwords() {
		return searchRecordsMapper.getHotwords();
	}


	@Transactional(propagation = Propagation.REQUIRED)
	@Override
	public void userLikeVideo(String userId, String videoId, String videoCreaterId) {

		// 1 保存用户和视频的喜欢点赞关联关系表
		UsersLikeVideos usersLikeVideos = new UsersLikeVideos();
		String likeid = sid.nextShort();
		usersLikeVideos.setId(likeid);
		usersLikeVideos.setUserId(userId);
		usersLikeVideos.setVideoId(videoId);
		usersLikeVideosMapper.insert(usersLikeVideos);

		// 2 视频喜欢数量累加
		videosMapperCustom.addVideoLikeCount(videoId);

		// 3 用户受喜欢数量的累加
		usersMapper.addReceiveLikeCount(videoCreaterId);

	}

	@Transactional(propagation = Propagation.REQUIRED)
	@Override
	public void userUnLikeVideo(String userId, String videoId, String videoCreaterId) {

		// 1 删除用户和视频的喜欢点赞关联关系表
		Example example = new Example(UsersLikeVideos.class);
		Criteria criteria = example.createCriteria();
		criteria.andEqualTo("userId",userId);
		criteria.andEqualTo("videoId",videoId);
		usersLikeVideosMapper.deleteByExample(example);

		// 2 视频喜欢数量累减
		videosMapperCustom.reduceVideoLikeCount(videoId);

		// 3 用户受喜欢数量的累减
		usersMapper.reduceReceiveLikeCount(videoCreaterId);
		
	}
	
	@Transactional(propagation = Propagation.REQUIRED)
	@Override
	public void saveComment(Comments comment) {
		String id = sid.nextShort();
		comment.setId(id);
		comment.setCreateTime(new Date());
		commentMapper.insert(comment);
	}
	
	@Transactional(propagation = Propagation.SUPPORTS)
	@Override
	public PagedResult getAllComments(String videoId, Integer page, Integer pageSize) {

		PageHelper.startPage(page,pageSize);
		List<CommentsVO> list = commentMapperCustom.queryComments(videoId);

		for (CommentsVO vo: list){
			String timeAgo = TimeAgoUtils.format(vo.getCreateTime());
			vo.setTimeAgoStr(timeAgo);
		}

		PageInfo<CommentsVO> pageList = new PageInfo<>(list);
		PagedResult pagedResult = new PagedResult();
		pagedResult.setPage(page);
		pagedResult.setTotal(pageList.getPages());
		pagedResult.setRecords(pageList.getTotal());
		pagedResult.setRows(list);

		return pagedResult;

	}

}
