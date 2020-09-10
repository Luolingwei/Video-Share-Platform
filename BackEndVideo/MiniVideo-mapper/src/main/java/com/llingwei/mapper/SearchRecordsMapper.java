package com.llingwei.mapper;

import java.util.List;

import com.llingwei.pojo.SearchRecords;
import com.llingwei.utils.MyMapper;

public interface SearchRecordsMapper extends MyMapper<SearchRecords> {
	
	public List<String> getHotwords();
}