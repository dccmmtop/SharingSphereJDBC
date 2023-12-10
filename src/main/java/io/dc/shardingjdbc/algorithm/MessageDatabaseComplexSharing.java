package io.dc.shardingjdbc.algorithm;

import cn.hutool.core.date.DateUtil;
import com.google.common.collect.Range;
import org.apache.shardingsphere.api.sharding.complex.ComplexKeysShardingAlgorithm;
import org.apache.shardingsphere.api.sharding.complex.ComplexKeysShardingValue;
import org.springframework.util.ObjectUtils;

import java.util.ArrayList;
import java.util.Collection;

/**
 * @author dc on 2023/12/6
 */

public class MessageDatabaseComplexSharing implements ComplexKeysShardingAlgorithm<Long> {
    /**
     * 将消息类型为1的，并且时间在 2023-12-01 之前的插入 m1库 message_1 表中
     * 将消息类型为2的，并且时间在 2023-12-01 之后的插入 m2库 message_2 表中
     * 其他情况数据在 m1 库中的 message_3
     */
    @Override
    public Collection<String> doSharding(Collection<String> collection, ComplexKeysShardingValue<Long> complexKeysShardingValue) {
        // 获取精确查找的字段
        Collection<Long> typeList = complexKeysShardingValue.getColumnNameAndShardingValuesMap().get("type");
        Collection<Long> createdTimeList = complexKeysShardingValue.getColumnNameAndShardingValuesMap().get("created_time");

        // 获取范围类型的字段
        Range<Long> typeRange = complexKeysShardingValue.getColumnNameAndRangeValuesMap().get("type");
        Range<Long> createdTimeRange = complexKeysShardingValue.getColumnNameAndRangeValuesMap().get("created_time");

        Collection<String> databases = new ArrayList<>();
        long now = DateUtil.parse("20231201").getTime();
        // 两个字段都是精确值
        if (!ObjectUtils.isEmpty(typeList) && !ObjectUtils.isEmpty(createdTimeList)) {
            System.out.println("选库： type 和 created_time 都是精确查找");
            for (Long type : typeList) {
                for (Long createTime : createdTimeList) {
                    if (type.equals(1L) && createTime.compareTo(now) < 0) {
                        databases.add("m1");
                    } else if (type.equals(2L) && createTime.compareTo(now) > 0) {
                        databases.add("m2");
                    } else {
                        databases.add("m1");
                    }

                }
            }
            System.out.println("选中的库有");
            for (String database : databases) {
                System.out.println(database);
            }
            return databases;
        }


        // type 是精确值， created_time 是范围值, 如：
        // type = 1 and (created_time between ('20231202','20231204'))
        if (!ObjectUtils.isEmpty(typeList) && !ObjectUtils.isEmpty(createdTimeRange)) {
            System.out.println("选库： type 是精确查找, created_time 是范围查找");
            Long min = createdTimeRange.lowerEndpoint();
            Long max = createdTimeRange.upperEndpoint();
            // 根据分片规则
            for (Long type : typeList) {
                if (type.equals(1L) && max.compareTo(now) < 0) {
                    databases.add("m1");
                } else if (type.equals(2L) && min.compareTo(now) > 0) {
                    databases.add("m2");
                } else {
                    databases.add("m1");
                }
            }
            System.out.println("选中的库有");
            for (String database : databases) {
                System.out.println(database);
            }
            return databases;
        }

        System.out.println("选库：其他情况在所有的数据库中查找");
        return collection;
    }
}
