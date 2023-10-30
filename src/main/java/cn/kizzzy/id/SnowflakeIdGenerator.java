package cn.kizzzy.id;

public class SnowflakeIdGenerator implements LongIdGenerator {
    private static final long sequenceBits = 12L;
    private static final long workerIdBits = 5L;
    private static final long datacenterIdBits = 5L;
    
    private static final long maxWorkerId = ~(-1L << workerIdBits);
    private static final long maxDatacenterId = ~(-1L << datacenterIdBits);
    
    private static final long sequenceMask = ~(-1L << sequenceBits);
    
    private static final long workerIdShift = sequenceBits;
    private static final long datacenterIdShift = sequenceBits + workerIdBits;
    private static final long timestampLeftShift = sequenceBits + workerIdBits + datacenterIdBits;
    
    private static final long twepoch = 1527782400000L;
    
    private long lastTimestamp;
    private long sequence;
    private long datacenterId;
    private long workerId;
    
    /**
     * 初始化ID
     */
    public SnowflakeIdGenerator(long datacenterId, long workerId) {
        if (datacenterId <= 0) {
            throw new IllegalArgumentException("datacenterId must greater than 0");
        }
        
        if (workerId <= 0) {
            throw new IllegalArgumentException("workerId must greater than 0");
        }
        
        this.datacenterId = datacenterId;
        this.workerId = workerId;
    }
    
    /**
     * 获取ID
     */
    @Override
    public long getId() {
        return getIdImpl();
    }
    
    /**
     * 获取下一个ID
     */
    private synchronized long getIdImpl() {
        long timestamp = System.currentTimeMillis();
        if (timestamp < lastTimestamp) {
            throw new InternalError("time go back");
        }
        
        if (timestamp == lastTimestamp) {
            sequence = (sequence + 1) & sequenceMask;
            if (sequence == 0) {
                timestamp = tilNextMillis(lastTimestamp);
            }
        } else {
            sequence = 0;
        }
        
        lastTimestamp = timestamp;
        
        return ((timestamp - twepoch) << timestampLeftShift) |
            (datacenterId << datacenterIdShift) |
            (workerId << workerIdShift) |
            sequence;
    }
    
    /**
     * 循环到下一秒
     */
    private long tilNextMillis(long lastTime) {
        long now = System.currentTimeMillis();
        while (now <= lastTime) {
            now = System.currentTimeMillis();
        }
        return now;
    }
}
