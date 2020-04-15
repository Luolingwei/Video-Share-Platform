package com.imooc.service.Impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.imooc.enums.BGMOperatorTypeEnum;
import com.imooc.mapper.BgmMapper;
import com.imooc.pojo.Bgm;
import com.imooc.pojo.BgmExample;
import com.imooc.service.VideoService;
import com.imooc.service.web.util.ZKCurator;
import com.imooc.utils.PagedResult;
import org.n3r.idworker.Sid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class VideoServiceImpl implements VideoService {

    @Autowired
    private BgmMapper bgmMapper;

    @Autowired
    private Sid sid;

    @Autowired
    private ZKCurator zkCurator;

    @Override
    public void addBgm(Bgm bgm) {
        String id = sid.nextShort();
        bgm.setId(id);
        bgmMapper.insert(bgm);
        zkCurator.sendBgmOperator(id, BGMOperatorTypeEnum.ADD.type);
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
    public void deleteBgm(String id) {
        bgmMapper.deleteByPrimaryKey(id);
        zkCurator.sendBgmOperator(id, BGMOperatorTypeEnum.DELETE.type);
    }
}
