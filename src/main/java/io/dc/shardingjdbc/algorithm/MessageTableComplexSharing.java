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
 * 将消息类型为1的，并且时间在 2023-12-01 之前的插入 m1库 message_1 表中
 * 将消息类型为2的，并且时间在 2023-12-01 之后的插入 m2库 message_2 表中
 * 其他情况数据在 m1 库中的 message_3
 */
public class MessageTableComplexSharing implements ComplexKeysShardingAlgorithm<Long> {
    @Override
    public Collection<String> doSharding(Collection<String> collection, ComplexKeysShardingValue<Long> complexKeysShardingValue) {
        // 获取精确查找的字段
        Collection<Long> typeList = complexKeysShardingValue.getColumnNameAndShardingValuesMap().get("type");
        Collection<Long> createdTimeList = complexKeysShardingValue.getColumnNameAndShardingValuesMap().get("created_time");

        // 获取范围类型的字段
        Range<Long> typeRange = complexKeysShardingValue.getColumnNameAndRangeValuesMap().get("type");
        Range<Long> createdTimeRange = complexKeysShardingValue.getColumnNameAndRangeValuesMap().get("created_time");

        Collection<String> tables = new ArrayList<>();
        String logicTable = complexKeysShardingValue.getLogicTableName();
        long now = DateUtil.parse("20231201").getTime();
        // 规则1： 两个字段都是精确值, 数据插入时唯一命中的。因为不可能插入一个范围吧
        if (!ObjectUtils.isEmpty(typeList) && !ObjectUtils.isEmpty(createdTimeList)) {
            System.out.println("选表： type 和 created_time 都是精确查找");
            for (Long type : typeList) {
                for (Long createTime : createdTimeList) {
                    if (type.equals(1L) && createTime.compareTo(now) < 0) {
                        tables.add(logicTable + "_1");
                    } else if (type.equals(2L) && createTime.compareTo(now) > 0) {
                        tables.add(logicTable + "_2");
                    } else {
                        tables.add(logicTable + "_3");
                    }
                }
            }
            System.out.println("选中的表有：");
            for (String table : tables) {
                System.out.println(table);
            }
            return tables;
        }


        // 规则2: type 是精确值， created_time 是范围值, 如：type = 1 and (created_time between ('20231202','20231204'))
        if (!ObjectUtils.isEmpty(typeList) && !ObjectUtils.isEmpty(createdTimeRange)) {
            System.out.println("选库： type 是精确查找, created_time 是范围查找");
            Long min = createdTimeRange.lowerEndpoint();
            Long max = createdTimeRange.upperEndpoint();
            // 根据分片规则
            for (Long type : typeList) {
                if (type.equals(1L) && max.compareTo(now) < 0) {
                    tables.add(logicTable + "_1");
                } else if (type.equals(2L) && min.compareTo(now) > 0) {
                    tables.add(logicTable + "_2");
                } else {
                    tables.add(logicTable + "_3");
                }
            }
            System.out.println("选中的表有：");
            for (String table : tables) {
                System.out.println(table);
            }
            return tables;
        }

        System.out.println("选表：其他情况在所有的数据库中查找");
        return collection;
    }
}