package io.dc.shardingjdbc.algorithm;

import org.apache.shardingsphere.api.sharding.standard.RangeShardingAlgorithm;
import org.apache.shardingsphere.api.sharding.standard.RangeShardingValue;

import java.util.Collection;

/**
 * @author dc on 2023/12/5
 */
public class DeptTableRangeAlgorithm implements RangeShardingAlgorithm<Long> {
    @Override
    public Collection<String> doSharding(Collection<String> tableNames, RangeShardingValue<Long> rangeShardingValue) {
        System.out.println("===========range================");
        // 范围查询应该在所有表中都查一遍
        return tableNames;
    }
}
