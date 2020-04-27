package com.taskonline.sys.mapper.expand;

import com.taskonline.sys.entity.Administrator;
import com.taskonline.sys.mapper.AdministratorMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Component;

@Component
@Mapper
public interface ExAdministratorMapper extends AdministratorMapper {
   /*
   * 继承自动生成的管理员Mapper，添加管理员登录等方法
   * */

    //管理员登录，密码已在service被哈希
    @Select("select * from administrator where aname=#{aname} and password=#{password}")
    Administrator selectByNamePassword(@Param("aname") String aname, @Param("password") String password);
}
