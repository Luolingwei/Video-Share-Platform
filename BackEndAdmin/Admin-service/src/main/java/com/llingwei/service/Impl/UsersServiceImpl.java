package com.llingwei.service.Impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.llingwei.mapper.UsersMapper;
import com.llingwei.pojo.Users;
import com.llingwei.pojo.UsersExample;
import com.llingwei.pojo.UsersExample.Criteria;
import com.llingwei.service.UsersService;
import com.llingwei.utils.PagedResult;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UsersServiceImpl implements UsersService {

    @Autowired
    private UsersMapper usersMapper;

    @Override
    public PagedResult queryUsers(Users user, Integer page, Integer pageSize) {

        String username = "";
        String nickname = "";
        if (user!=null){
            username = user.getUsername();
            nickname = user.getNickname();
        }

        PageHelper.startPage(page,pageSize);
        UsersExample example = new UsersExample();

        // 模糊查询
        Criteria usersCriteria = example.createCriteria();
        if (StringUtils.isNotBlank(username)){
            usersCriteria.andUsernameLike("%"+username+"%");
        }
        if (StringUtils.isNotBlank(nickname)){
            usersCriteria.andNicknameLike("%"+nickname+"%");
        }

        List<Users> list = usersMapper.selectByExample(example);

        PageInfo<Users> pageList = new PageInfo<>(list);
        PagedResult pagedResult = new PagedResult();
        pagedResult.setPage(page);
        pagedResult.setTotal(pageList.getPages());
        pagedResult.setRecords(pageList.getTotal());
        pagedResult.setRows(list);

        return pagedResult;
    }


}
