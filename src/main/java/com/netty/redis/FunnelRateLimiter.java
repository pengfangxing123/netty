package com.netty.redis;

import java.util.HashMap;
import java.util.Map;

/**
 * @author 86136
 */
public class FunnelRateLimiter {
    static class Funnel {
        //容量
        int capacity;
        //流出速率
        float leakingRate;
        //剩余容量
        int leftQuota;
        //计算起始时间
        long leakingTs;
        public Funnel(int capacity, float leakingRate) {
            this.capacity = capacity;
            this.leakingRate = leakingRate;
            this.leftQuota = capacity;
            this.leakingTs = System.currentTimeMillis();
        }
        void makeSpace() {
            long nowTs = System.currentTimeMillis();
            long deltaTs = nowTs - leakingTs;
            int deltaQuota = (int) (deltaTs * leakingRate);
            if (deltaQuota < 0) {
                //间隔时间太长，整数数字过大溢出
                this.leftQuota = capacity;
                this.leakingTs = nowTs;
                return;
            }
            if (deltaQuota < 1) {
                //腾出空间太小，最小单位是1
                return;
            }
            //剩余容量 = 当前容量 + 流出速率 * 间隔时间
            this.leftQuota += deltaQuota;
            this.leakingTs = nowTs;
            if (this.leftQuota > this.capacity) {
                this.leftQuota = this.capacity;
            }
        }
        //判断是否能加入交易
        //这些操作没有考虑线程安全的问题
        boolean watering(int quota) {
            makeSpace();
            if (this.leftQuota >= quota) {
                this.leftQuota -= quota;
                return true;
            }
            return false;
        }
    }
    private Map<String, Funnel> funnels = new HashMap<>();

    public boolean isActionAllowed(String userId, String actionKey, int capacity, float leakingRate) {
        String key = String.format("%s:%s", userId, actionKey);
        Funnel funnel = funnels.get(key);
        if (funnel == null) {
            funnel = new Funnel(capacity, leakingRate);
            funnels.put(key, funnel);
        }
        //需要1个quota
        return funnel.watering(1);
    }
}
