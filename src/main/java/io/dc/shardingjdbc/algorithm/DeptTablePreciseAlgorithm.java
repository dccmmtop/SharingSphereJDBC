package io.dc.shardingjdbc.algorithm;

import org.apache.shardingsphere.api.sharding.standard.PreciseShardingAlgorithm;
import org.apache.shardingsphere.api.sharding.standard.PreciseShardingValue;

import java.math.BigInteger;
import java.util.Collection;

/**
 * @author dc on 2023/12/5
 */
public class DeptTablePreciseAlgorithm implements PreciseShardingAlgorithm<Long> {
    @Override
    public String doSharding(Collection<String> availableTargetNames, PreciseShardingValue<Long> preciseShardingValue) {
        System.out.println("dept 表精确");
        Long cid = preciseShardingValue.getValue();
        String logicTable = preciseShardingValue.getLogicTableName();
        String actualTable = logicTable + "_";
        if (cid % 10 == 1) {
            actualTable += "1";
            System.out.println("在 " + actualTable+ " 中查找");
            return actualTable;
        }
        actualTable += "2";
        System.out.println("在 " + actualTable+ " 中查找");
        return actualTable;
    }
}
