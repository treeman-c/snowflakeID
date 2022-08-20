package com.treeman.snowflake.server.impl;

import com.treeman.snowflake.mapper.SnowFlakeMapper;
import com.treeman.snowflake.pojo.Snowflake;
import com.treeman.snowflake.server.ISnowFlakeServer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SnowFlakeServerImpl implements ISnowFlakeServer {
    @Autowired
    private SnowFlakeMapper snowFlakeMapper;

    @Override
    public boolean insertID(Snowflake snowflake) {
        return snowFlakeMapper.insertID(snowflake);
    }

    @Override
    public boolean deleteID(long id) {
        return snowFlakeMapper.deleteID(id);
    }

    @Override
    public List<Snowflake> selectIDList() {
        return snowFlakeMapper.selectIDList();
    }
}
