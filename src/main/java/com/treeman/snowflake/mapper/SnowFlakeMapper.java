package com.treeman.snowflake.mapper;

import com.treeman.snowflake.pojo.Snowflake;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface SnowFlakeMapper {
    public boolean insertID(Snowflake snowflake);

    public boolean deleteID(long id);

    public List<Snowflake> selectIDList();

}
