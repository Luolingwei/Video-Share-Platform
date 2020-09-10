package com.llingwei.service;

import com.llingwei.pojo.Users;
import com.llingwei.utils.PagedResult;

public interface UsersService {

    public PagedResult queryUsers(Users user, Integer page, Integer pageSize);

}
