package com.treeman.snowflake.server;

import com.treeman.snowflake.pojo.Snowflake;

import java.util.List;

public interface ISnowFlakeServer {

    public boolean insertID(Snowflake snowflake);

    public boolean deleteID(long id);

    public List<Snowflake> selectIDList();
}
