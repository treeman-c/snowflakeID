package com.treeman.snowflake.pojo;


public class Snowflake {

  private long id;
  private long timeBit;
  private long workBit;
  private long dataBit;
  private long sequence;


  public long getId() {
    return id;
  }

  public void setId(long id) {
    this.id = id;
  }


  public long getTimeBit() {
    return timeBit;
  }

  public void setTimeBit(long timeBit) {
    this.timeBit = timeBit;
  }


  public long getWorkBit() {
    return workBit;
  }

  public void setWorkBit(long workBit) {
    this.workBit = workBit;
  }


  public long getDataBit() {
    return dataBit;
  }

  public void setDataBit(long dataBit) {
    this.dataBit = dataBit;
  }


  public long getSequence() {
    return sequence;
  }

  public void setSequence(long sequence) {
    this.sequence = sequence;
  }

}
