package com.treeman.snowflake.tool;

import com.treeman.snowflake.exception.SeqException;
import org.springframework.stereotype.Component;

/**
 *
 * Twitter_Snowflake<br>
 * SnowFlake的结构如下(每部分用-分开):<br>
 * 0 - 0000000000 0000000000 0000000000 0000000000 0 - 00000 - 00000 - 000000000000 <br>
 *1位0+41位时间戳+5位数据位+5位机器位+12位序列码
 */


public class SnowFlakeTool {
    /** 开始时间截 (2015-01-01) */
    private final long startTime = 1420041600000L;

    /** 机器id的位数*/
    private final long workBit = 5L;

    /***数据标识的位数*/
    private final long dataBit = 5L;

    /** 序列位数*/
    private final long sequenceBit = 12L;

    /**机器ID向左移12位*/
    private final long workShift = sequenceBit;

    /**数据标识id向左移17位*/
    private final long dataShift = sequenceBit + workBit;

    /**时间戳向左移22位*/
    private final long timeShift = sequenceBit + workBit + dataBit;

    /**最大的机器码*/
    private final long workMark = -1L ^ (-1L <<workBit);

    /**最大的数据码*/
    private final long dataMark = -1L ^ (-1L <<dataBit);

    /**同一毫秒最多生成的序列码*/
    private final long sequenceMark = -1L ^ (-1L <<sequenceBit);

    /**工作id变量*/
    private long workId;
    /***数据码变量*/
    private long dataId;
    /**序列码变量*/
    private long sequence;
    /**上次生成id的时间戳*/
    private long lastTime;

    public SnowFlakeTool(long workId, long dataId) throws SeqException {
        if(workId>workMark||workId<0||dataId>dataMark||dataId<0){
            throw new SeqException("机器id值或数据码不合法,值范围应在0~31之间");
        }
        this.workId=workId;
        this.dataId=dataId;
    }

    /**
     * 生成序列码方法
     * 线程安全
     * @return longId 序列码
     */
    public synchronized long nextNo() throws SeqException {
        long thisTime = System.currentTimeMillis(); //生成当前毫秒
        if(thisTime<lastTime){
            throw new SeqException("系统时间可能回退导致时间戳重复异常");
        }else if(thisTime==lastTime){
            sequence = (sequence+1) & sequenceMark;
            if(sequence == 0){
                thisTime = System.currentTimeMillis();
                while(thisTime<=lastTime){    //等到下一毫米再继续生成id
                    thisTime=System.currentTimeMillis();
                }
            }
        }else{
            sequence = 0L;  //不同毫秒下序列码应从0开始
        }

        lastTime = thisTime;  //记录本次生成id的时间

        long longId = ((thisTime - startTime) << timeShift)
                             | (dataId<<dataShift)
                             | (workId <<workShift)
                             |sequence;
        return longId;

    }

}
