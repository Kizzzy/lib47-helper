package cn.kizzzy.id;

import java.util.concurrent.atomic.AtomicInteger;

public class CommonIdGenerator implements IntegerIdGenerator {
    
    private final AtomicInteger id;
    
    public CommonIdGenerator(int id) {
        this.id = new AtomicInteger(id);
    }
    
    @Override
    public int getId() {
        return id.incrementAndGet();
    }
}
