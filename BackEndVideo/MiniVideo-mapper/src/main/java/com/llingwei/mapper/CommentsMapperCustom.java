package com.llingwei.mapper;

import java.util.List;

import com.llingwei.pojo.Comments;
import com.llingwei.pojo.vo.CommentsVO;
import com.llingwei.utils.MyMapper;

public interface CommentsMapperCustom extends MyMapper<Comments> {
	
	public List<CommentsVO> queryComments(String videoId);
}