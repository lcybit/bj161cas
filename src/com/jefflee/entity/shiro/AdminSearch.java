package com.jefflee.entity.shiro;

import lombok.Data;

/**
 * Created by TGL on 2018/4/24.
 */
@Data
public class AdminSearch {
    private String fullname;
    private long roleId;
    private String username;

    private String nickname;
    private String sex;
    private String status;
    private String createTimeStart;
    private String createTimeEnd;
    private String operation;
}
