package io.dc.shardingjdbc;

import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import io.dc.shardingjdbc.mapper.*;
import io.dc.shardingjdbc.model.*;
import org.apache.shardingsphere.api.hint.HintManager;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

@SpringBootTest
class ShardingJdbcApplicationTests {
    @Resource
    private CourseMapper courseMapper;
    @Resource
    private MessageMapper messageMapper;
    @Resource
    private UserMapper userMapper;
    @Resource
    private DictMapper dictMapper;
    @Resource
    private DeptMapper deptMapper;

    @Test
    public void addCourse() {
        for (int i = 1; i <= 200; i++) {
            Course c = new Course();

            // 注意，因为使用了雪花算法生成cid，这里不要手动设置
            //  c.setCid(Long.valueOf(i));
            c.setCname("xxx");
            c.setUserId(Long.valueOf(1000 + i));
            c.setCstatus("1");

            courseMapper.insert(c);
        }
    }

    @Test
    public void queryCourse() {
        System.out.println("全表查询");
        QueryWrapper<Course> wrapper = new QueryWrapper<>();
        wrapper.orderByDesc("cid").orderByDesc("user_id");
        List<Course> courseList = courseMapper.selectList(wrapper);
        for (Course course : courseList) {
            System.out.println(course);
        }
    }

    @Test
    public void queryCourseById() {
        System.out.println("按照Id 查询");
        QueryWrapper<Course> wrapper = new QueryWrapper<>();
        wrapper.eq("cid", 1733435340907712513L);
        List<Course> courseList = courseMapper.selectList(wrapper);
        System.out.println("按照ID");
        for (Course course : courseList) {
            System.out.println(course);
        }
        wrapper.clear();
        System.out.println("in 查询");
        wrapper.in("cid", Arrays.asList(1733435340953849857L, 1733435340844797954L));
        courseList = courseMapper.selectList(wrapper);
        for (Course course : courseList) {
            System.out.println(course);
        }
    }
    @Test
    public void queryCourseByRange() {
        System.out.println("按照Id范围查询");
        QueryWrapper<Course> wrapper = new QueryWrapper<>();
        wrapper.between("cid", 938561391969701888L, 938561392535932929L);
        List<Course> courseList = courseMapper.selectList(wrapper);
        for (Course course : courseList) {
            System.out.println(course);
        }
        // 在 inline 策略下，不支持范围查询
    }


    @Test
    public void insertDept() {
        for (int i = 0; i < 100; i++) {
            Dept dept = new Dept();
            dept.setUserNum((i +1) * 200);
            dept.setName("测试标准策略_" + i);
            deptMapper.insert(dept);
        }
    }

    @Test
    public void queryDept() {
        QueryWrapper<Dept> wrapper = new QueryWrapper<>();
        wrapper.eq("did",940635075672801281L);
        List<Dept> deptList = deptMapper.selectList(wrapper);
        for (Dept dept : deptList) {
            System.out.println(dept);
        }
    }
    @Test
    public void queryDeptRange() {
        QueryWrapper<Dept> wrapper = new QueryWrapper<>();
        wrapper.between("did",940635075672801281L, 940635076750737408L);
        List<Dept> deptList = deptMapper.selectList(wrapper);
        for (Dept dept : deptList) {
            System.out.println(dept);
        }
    }




    /**
     * 复合策略
     * <p>
     * 将消息类型为1的，并且时间在 2023-12-01 之前的插入 m1库 message_1 表中
     * 将消息类型为2的，并且时间在 2023-12-01 之后的插入 m2库 message_2 表中
     * 其他情况数据在 m1 库中的 message_3
     */
    @Test
    public void insertMessage() {
        List<Date> dateList = new ArrayList<>();
        dateList.add(DateUtil.parse("20231101"));
        dateList.add(DateUtil.parse("20231102"));
        dateList.add(DateUtil.parse("20231103"));
        dateList.add(DateUtil.parse("20231201"));
        dateList.add(DateUtil.parse("20231202"));
        dateList.add(DateUtil.parse("20231203"));
        dateList.add(DateUtil.parse("20231204"));

        for (int i = 0; i < 100; i++) {
            Message message = new Message();
            message.setBody("测试分库分表" + i);
            message.setType((long) (i % 3));
            Date date = dateList.get(i % dateList.size());
            message.setCreatedTime(date.getTime());
            message.setCreatedTimeStr(DateUtil.format(date, "yyyy-MM-dd HH:mm:ss"));
            messageMapper.insert(message);
        }
    }

    /**
     * 符合规则1，应该只在 m1:message_1 中查找
     */
    @Test
    public void queryMessageEq() {
        QueryWrapper<Message> wrapper = new QueryWrapper<>();
        wrapper.eq("type", 1L).eq("created_time", DateUtil.parse("20231102").getTime());
        List<Message> messages = messageMapper.selectList(wrapper);
        for (Message message : messages) {
            System.out.println(message);
        }
    }

    /**
     * =  和 in 仍然属于精确查找，type = 1 and created_time in (20231102,20231203)
     * 应该在 m1:message_1 和 m1:message_3 查找
     */
    @Test
    public void queryMessageIn() {
        QueryWrapper<Message> wrapper = new QueryWrapper<>();
        wrapper.eq("type", 1L);
        wrapper.in("created_time", DateUtil.parse("20231102").getTime(), DateUtil.parse("20231203").getTime());
        List<Message> messages = messageMapper.selectList(wrapper);
        for (Message message : messages) {
            System.out.println(message);
        }
    }

    /**
     * =  和 in 仍然属于精确查找，type = 2 and created_time in (20231102,20231203)
     * 应该在 m2:message_2 和 m1:message_3 查找
     * 但是实际输出的SQL 是 m1:message_2 m1:messsage_3  m2:message_2 m3:message_3
     * 我猜测他的工作原理如下：
     * 1. 先根据要查找的数据找出数据库： 2 和 20231102 结合得出要在 m1 中查找;  2 和 20231203 结合得出要在 m2 中查找
     * 2. 再根据要查找的数据找出表：  2 和 20231102 结合得出要在 message_3 中查找;  2 和 20231203 结合得出要在 message_2 中查找
     * 3. [m1 m2] 和 [message_2  message_3] 交叉结合 即： m1:message_2  m1:message_3  m2:message_2 m2:message_3
     * 所以是4条sql。该规则适用于上面 queryMessageIn 方法
     */
    @Test
    public void queryMessageIn2() {
        QueryWrapper<Message> wrapper = new QueryWrapper<>();
        wrapper.eq("type", 2L);
        wrapper.in("created_time", DateUtil.parse("20231102").getTime(), DateUtil.parse("20231203").getTime());
        List<Message> messages = messageMapper.selectList(wrapper);
        for (Message message : messages) {
            System.out.println(message);
        }
    }

    @Test
    public void queryMessageRange() {
        QueryWrapper<Message> wrapper = new QueryWrapper<>();
        wrapper.eq("type", 2L);
        wrapper.between("created_time", DateUtil.parse("20231202").getTime(), DateUtil.parse("20231204").getTime());
        List<Message> messages = messageMapper.selectList(wrapper);
        for (Message message : messages) {
            System.out.println(message);
        }
    }

    @Test
    public void queryMessageInAndRange() {
        QueryWrapper<Message> wrapper = new QueryWrapper<>();
        wrapper.in("type", 2L, 1L);
        wrapper.between("created_time", DateUtil.parse("20231202").getTime(), DateUtil.parse("20231204").getTime());
        List<Message> messages = messageMapper.selectList(wrapper);
        for (Message message : messages) {
            System.out.println(message);
        }
    }

    @Test
    public void queryMessageRangeAndRange() {
        QueryWrapper<Message> wrapper = new QueryWrapper<>();
        wrapper.between("type", 0L, 2L);
        wrapper.between("created_time", DateUtil.parse("20231202").getTime(), DateUtil.parse("20231204").getTime());
        List<Message> messages = messageMapper.selectList(wrapper);
        for (Message message : messages) {
            System.out.println(message);
        }
    }

    /**
     * 强制路由策略插入数据
     */
    @Test
    public void insertUser() {
        HintManager hintManager1 = HintManager.getInstance();
        // 用户直接指定选择哪个库那个表进行插入,查询写法一致，不再编写示例
        hintManager1.addTableShardingValue("user", 1);
        hintManager1.addDatabaseShardingValue("user", 1);
        for (int i = 1; i < 50; i++) {
            User user = new User();
            user.setAge(i);
            user.setName("测试强制路由_" + i);
            user.setSex(i % 2);

            userMapper.insert(user);
        }
        hintManager1.close();

        HintManager hintManager2 = HintManager.getInstance();
        hintManager2.addTableShardingValue("user", 2);
        hintManager2.addDatabaseShardingValue("user", 2);
        for (int i = 50; i < 100; i++) {
            User user = new User();
            user.setAge(i);
            user.setName("测试强制路由_" + i);
            user.setSex(i % 2);

            userMapper.insert(user);
        }
        hintManager2.close();
    }

    /**
     * 测试读写分离插入
     */
    @Test
    public void insertDict() {
        Dict dict = new Dict();
        dict.setType(1);
        dict.setKeyword(1);
        dict.setValue("使用账号密码登录");
        dictMapper.insert(dict);

        dict = new Dict();
        dict.setType(1);
        dict.setKeyword(2);
        dict.setValue("使用短信验证码登录");
        dictMapper.insert(dict);

        dict = new Dict();
        dict.setType(2);
        dict.setKeyword(1);
        dict.setValue("开启设备绑定");
        dictMapper.insert(dict);

        dict = new Dict();
        dict.setType(2);
        dict.setKeyword(2);
        dict.setValue("关闭设备绑定");
        dictMapper.insert(dict);
    }
    /**
     * 测试读写分离查询
     */
    @Test
    public void queryDict() {
        QueryWrapper<Dict> wrapper = new QueryWrapper<>();
        wrapper.eq("type", 2).eq("keyword", 2);
        List<Dict> dicts = dictMapper.selectList(wrapper);
        for (Dict dict : dicts) {
            System.out.println(dict);
        }
    }
}
