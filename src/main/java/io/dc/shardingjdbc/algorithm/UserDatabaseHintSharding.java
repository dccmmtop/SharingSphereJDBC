package io.dc.shardingjdbc.algorithm;

import org.apache.shardingsphere.api.sharding.hint.HintShardingAlgorithm;
import org.apache.shardingsphere.api.sharding.hint.HintShardingValue;

import java.util.Collection;
import java.util.Collections;

/**
 * @author dc on 2023/12/8
 */
public class UserDatabaseHintSharding implements HintShardingAlgorithm<Integer> {
    @Override
    public Collection<String> doSharding(Collection<String> collection, HintShardingValue<Integer> hintShardingValue) {
        // 获取用户传指定的库
        Integer index = (Integer) hintShardingValue.getValues().toArray()[0];
        String database = "m" + index;
        System.out.println("强制路由 选中数据库 " + database);
        if (collection.contains(database)) {
            return Collections.singletonList(database);
        }
        throw new RuntimeException("未知的数据库: " + database);
    }
}
