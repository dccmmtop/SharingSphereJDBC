package io.dc.shardingjdbc.algorithm;

import org.apache.shardingsphere.api.sharding.standard.PreciseShardingAlgorithm;
import org.apache.shardingsphere.api.sharding.standard.PreciseShardingValue;

import java.util.Collection;

/**
 * @author dc on 2023/12/5
 * PreciseShardingAlgorithm<T> 实现精确查询分片算法 T 是分片键的类型
 */
public class DeptDsPreciseAlgorithm implements PreciseShardingAlgorithm<Long> {
    /**
     *
     * @param databases 已经配置的数据源
     * @param preciseShardingValue 分片键相关信息， 包含分片键和逻辑表
     * @return 从那个库中查询(插入)。因为这是精确分片，只会返回一个值
     */
    @Override
    public String doSharding(Collection<String> databases, PreciseShardingValue<Long> preciseShardingValue) {
        System.out.println("dept 库精确");
        // 获取分片键的值
        Long cid = preciseShardingValue.getValue();
        if(cid % 10 == 1){
            System.out.println("在m1库中查找");
            return "m1";
        }
        System.out.println("在m2库中查找");
        return "m2";
    }
}
