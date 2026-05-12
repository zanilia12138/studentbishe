package com.health.communicate.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.health.communicate.entity.Material;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.util.List;

@Mapper
public interface MaterialMapper extends BaseMapper<Material> {
    List<Material> selectMaterialsWithUserName();
    List<Material> selectMaterialsWithUserNameByTitle(@Param("keyword") String keyword);
    int incrementDownloadCount(@Param("id") Integer id);
}