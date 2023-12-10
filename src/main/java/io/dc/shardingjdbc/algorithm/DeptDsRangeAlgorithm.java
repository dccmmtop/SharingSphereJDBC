package io.dc.shardingjdbc.algorithm;

import org.apache.shardingsphere.api.sharding.standard.RangeShardingAlgorithm;
import org.apache.shardingsphere.api.sharding.standard.RangeShardingValue;

import java.util.Collection;

/**
 * @author dc on 2023/12/5
 * 实现 RangeShardingAlgorithm<Long> 接口
 */
public class DeptDsRangeAlgorithm implements RangeShardingAlgorithm<Long> {
    /**
     * @param databases 配置的数据源
     * @param rangeShardingValue 分片相关信息
     * @return 因为是范围查询，可能要在多个数据源中才能找到所有数据，所以返回值是集合
     */
    @Override
    public Collection<String> doSharding(Collection<String> databases, RangeShardingValue<Long> rangeShardingValue) {
        // 获取范围查询中范围的最小值
        Long min = rangeShardingValue.getValueRange().lowerEndpoint();
        // 获取范围查询中范围的最大值
        Long max = rangeShardingValue.getValueRange().upperEndpoint();
        // 获取逻辑表名
        String logicTableName = rangeShardingValue.getLogicTableName();
        // 获取分片键列的名字
        String columnName = rangeShardingValue.getColumnName();

        // 范围查询需要所有的库中查询， 暂时没有想到好的案例，没有用到上面几个变量, 仅仅做演示
        return databases;
    }
}
