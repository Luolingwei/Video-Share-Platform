package com.llingwei.service.Impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.llingwei.enums.BGMOperatorTypeEnum;
import com.llingwei.mapper.BgmMapper;
import com.llingwei.mapper.UsersReportMapperCustom;
import com.llingwei.mapper.VideosMapper;
import com.llingwei.pojo.*;
import com.llingwei.pojo.vo.Reports;
import com.llingwei.service.VideoService;
import com.llingwei.service.web.util.ZKCurator;
import com.llingwei.utils.JsonUtils;
import com.llingwei.utils.PagedResult;
import org.apache.commons.io.FileUtils;
import org.n3r.idworker.Sid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class VideoServiceImpl implements VideoService {

    @Autowired
    private BgmMapper bgmMapper;

    @Autowired
    private UsersReportMapperCustom usersReportMapperCustom;

    @Autowired
    private VideosMapper videosMapper;

    @Autowired
    private Sid sid;

    @Autowired
    private ZKCurator zkCurator;

    @Override
    public void addBgm(Bgm bgm) {
        String id = sid.nextShort();
        bgm.setId(id);
        bgmMapper.insert(bgm);

        Map<String,String> map = new HashMap<>();
        map.put("operType",BGMOperatorTypeEnum.ADD.type);
        map.put("path",bgm.getPath());
        zkCurator.sendBgmOperator(id, JsonUtils.objectToJson(map));
    }

    @Override
    public PagedResult queryBgmList(Integer page, Integer pageSize) {
        PageHelper.startPage(page,pageSize);
        BgmExample example = new BgmExample();
        List<Bgm> list = bgmMapper.selectByExample(example);

        PageInfo<Bgm> pageList = new PageInfo<>(list);
        PagedResult pagedResult = new PagedResult();
        pagedResult.setPage(page);
        pagedResult.setTotal(pageList.getPages());
        pagedResult.setRecords(pageList.getTotal());
        pagedResult.setRows(list);

        return pagedResult;
    }

    @Override
    public PagedResult queryReportList(Integer page, Integer pageSize) {

        PageHelper.startPage(page,pageSize);
        List<Reports> list = usersReportMapperCustom.selectAllVideoReport();

        PageInfo<Reports> pageList = new PageInfo<>(list);
        PagedResult pagedResult = new PagedResult();
        pagedResult.setPage(page);
        pagedResult.setTotal(pageList.getPages());
        pagedResult.setRecords(pageList.getTotal());
        pagedResult.setRows(list);

        return pagedResult;
    }

    @Override
    public void updateVideoStatus(String videoId, int status) {

        Videos video = new Videos();
        video.setId(videoId);
        video.setStatus(status);
        videosMapper.updateByPrimaryKeySelective(video);

    }

    @Override
    public void deleteBgm(String id) {
        // 存在问题: 在删除之后向zookeeper发送消息，SpringBoot端收到之后无法查到对应的Bgm了
        // 解决: 直接将对应的信息写进zookeeper，放入一个map，存储操作类型和bgmPath
        Bgm bgm = bgmMapper.selectByPrimaryKey(id);
        bgmMapper.deleteByPrimaryKey(id);

        // 删除后台管理端的视频文件
        String deletePath = "/Users/luolingwei/Desktop/Program/WeChatMiniVideo/Video-Share-Platform/UserFilesDB" + "/mvc-bgm" + bgm.getPath();
        File file = new File(deletePath);
        try {
            FileUtils.forceDelete(file);
        } catch (IOException e) {
            e.printStackTrace();
        }

        // 向zookeeper发送创建节点请求
        Map<String,String> map = new HashMap<>();
        map.put("operType",BGMOperatorTypeEnum.DELETE.type);
        map.put("path",bgm.getPath());
        zkCurator.sendBgmOperator(id, JsonUtils.objectToJson(map));
    }
}
