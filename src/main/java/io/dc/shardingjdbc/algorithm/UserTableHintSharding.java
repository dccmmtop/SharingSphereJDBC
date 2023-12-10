package io.dc.shardingjdbc.algorithm;

import org.apache.shardingsphere.api.sharding.hint.HintShardingAlgorithm;
import org.apache.shardingsphere.api.sharding.hint.HintShardingValue;

import java.util.Collection;
import java.util.Collections;

/**
 * @author dc on 2023/12/8
 */
public class UserTableHintSharding implements HintShardingAlgorithm<Integer> {
    @Override
    public Collection<String> doSharding(Collection<String> collection, HintShardingValue<Integer> hintShardingValue) {
        // 获取用户指定的表
        Integer index = (Integer) hintShardingValue.getValues().toArray()[0];
        String table = hintShardingValue.getLogicTableName() + "_" + index;
        if (collection.contains(table)) {
            System.out.println("强制路由 选中表 " + table);
            return Collections.singletonList(table);
        }
        throw new RuntimeException("未知的表: " + table);
    }
}
