package com.ripple.usercenter.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ripple.usercenter.model.domain.User;
import com.ripple.usercenter.service.UserService;
import com.ripple.usercenter.mapper.UserMapper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import javax.annotation.Resource;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author 花海
 * @description 针对表【user(用户)】的数据库操作Service实现
 * @createDate 2023-11-03 22:03:34
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User>
        implements UserService {

    @Resource
    private UserMapper userMapper;

    @Override
    public long userRegister(String userAccount, String userPassword, String checkPassword) {
        // 1. 校验 isAnyBlank方法用于校验字符串是否为 null 、空
        if (StringUtils.isAnyBlank(userAccount, userPassword, checkPassword)) {
            return -1; //有一项不满足校验就返回-1
        }
        if (userAccount.length() < 4) {
            return -1;
        }
        if (userPassword.length() < 8 || checkPassword.length() < 8) {
            return -1;
        }
        //账户不能重复，查询数据库中的用户
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("userAccount", userAccount);
        long count = userMapper.selectCount(queryWrapper);
        // 如果该账户已经有人注册了
        if (count > 0) {
            return -1;
        }
        //账户不能包含特殊字符
        // 只允许字母和数字 // String regEx ="[^a-zA-Z0-9]";
        // 清除掉所有特殊字符
        String validPattern = "[` ~!@#$%^&*()+=|{}':;',\\\\[\\\\].<>/?~！@#￥%……&*（）——+|{}【】‘；：”“’。，、？]";
        Matcher matcher = Pattern.compile(validPattern).matcher(userAccount);
        if (matcher.find()) {
            return -1;
        }
        //密码和校验密码相同
        if (!userPassword.equals(checkPassword)) {
            return -1;
        }

        // 2. 加密密码
        final String SALT = "bobo"; //搅屎棍，让密码更加复杂 知道盐才能解密，防解密
        // 使用Spring提供的加密工具类，得到加密密码
        String encryptPassword = DigestUtils.md5DigestAsHex((SALT + userPassword).getBytes());

        // 3. 插入用户数据到数据库
        User user = new User();
        user.setUserAccount(userAccount);
        user.setUserPassword(encryptPassword);
        boolean saveResult = this.save(user);
        if (!saveResult) {
            return -1;
        }
        return user.getId();
        //如果上面没有插入成功，那么用户 主键id 为 null，此时返回 null ，而 UserService 接口中的方该法返回值是 long
        // 会导致数据类型 自动拆箱失败，报错
    }
}




