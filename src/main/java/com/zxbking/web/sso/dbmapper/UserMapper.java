package com.zxbking.web.sso.dbmapper;

import com.zxbking.web.sso.dbmodel.User;
import com.zxbking.web.sso.dbmodel.UserExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface UserMapper {
    /**
     * 根据条件计数
     *
     * @param example
     */
    int countByExample(UserExample example);

    /**
     *
     * @param example
     */
    int deleteByExample(UserExample example);

    /**
     * 根据主键删除数据库的记录
     *
     * @param id
     */
    int deleteByPrimaryKey(String id);

    /**
     * 插入数据库记录
     *
     * @param record
     */
    int insert(User record);

    /**
     * 插入数据库记录
     *
     * @param record
     */
    int insertSelective(User record);

    /**
     * 根据条件查询列表
     *
     * @param example
     */
    List<User> selectByExample(UserExample example);

    /**
     * 根据主键获取一条数据库记录
     *
     * @param id
     */
    User selectByPrimaryKey(String id);

    /**
     * 选择性更新数据库记录
     *
     * @param record
     * @param example
     */
    int updateByExampleSelective(@Param("record") User record, @Param("example") UserExample example);

    /**
     * 选择性更新数据库记录
     *
     * @param record
     * @param example
     */
    int updateByExample(@Param("record") User record, @Param("example") UserExample example);

    /**
     * 根据主键来更新部分数据库记录
     *
     * @param record
     */
    int updateByPrimaryKeySelective(User record);

    /**
     * 根据主键来更新数据库记录
     *
     * @param record
     */
    int updateByPrimaryKey(User record);
}