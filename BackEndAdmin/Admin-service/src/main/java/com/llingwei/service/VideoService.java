package com.llingwei.service;

import com.llingwei.pojo.Bgm;
import com.llingwei.utils.PagedResult;

public interface VideoService {

    public void addBgm(Bgm bgm);

    public PagedResult queryBgmList(Integer page, Integer pageSize);

    public PagedResult queryReportList(Integer page, Integer pageSize);

    public void updateVideoStatus(String videoId, int status);

    public void deleteBgm(String id);

}
