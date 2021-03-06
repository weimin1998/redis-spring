package com.weimin.springdataredis;

import com.weimin.springdataredis.pojo.Student;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.*;

import java.util.*;
import java.util.concurrent.TimeUnit;

@SpringBootTest
class SpringDataRedisApplicationTests {

    @Autowired
    RedisTemplate redisTemplate;

    @Autowired
    StringRedisTemplate stringRedisTemplate;
    @Test
    void contextLoads() {
        /*ValueOperations valueOperations = redisTemplate.opsForValue();
        valueOperations.set("name","weimin");

        Object name = valueOperations.get("name");
        System.out.println(name);*/

        ValueOperations<String, String> stringStringValueOperations = stringRedisTemplate.opsForValue();
        stringStringValueOperations.set("名字","魏敏");
        String s = stringStringValueOperations.get("名字");
        System.out.println(s);
    }

    /**
     * 序列化
     */
    @Test
    public void test(){
        Student weimin = new Student("魏敏", 1);
        ValueOperations valueOperations = redisTemplate.opsForValue();

        valueOperations.set("student",weimin);
        Object student = valueOperations.get("student");

        System.out.println(student);
    }

    /**
     * String
     */
    @Test
    public void string(){
        ValueOperations valueOperations = redisTemplate.opsForValue();
        valueOperations.set("string","string");


        Map<String,String> map = new HashMap<>();
        map.put("hehe","呵呵");
        map.put("haha","哈哈");
        map.put("hoho","吼吼");

        valueOperations.multiSet(map);
        List multiGet = valueOperations.multiGet(Arrays.asList("hehe", "haha", "hoho"));

        System.out.println(multiGet);

        // 层级目录
        valueOperations.set("user:01:name","weimin");
    }

    /**
     * hash
     */
    @Test
    public void hash(){
        HashOperations hash = redisTemplate.opsForHash();

        // redis的key  hash的key  hash的值
        hash.put("token","1","1");
        hash.put("token","2","2");
        hash.put("token","3","3");
        hash.put("token","4","4");
        hash.put("token","5","5");

        Object token = hash.get("token", "3");
        System.out.println(token);


        Map<String,String> map = new HashMap<>();
        map.put("hehe","呵呵");
        map.put("haha","哈哈");
        map.put("hoho","吼吼");
        hash.putAll("token",map);

        List list = hash.multiGet("token", Arrays.asList("1", "2", "3", "4", "5"));

        System.out.println(list);
    }

    /**
     * list
     */
    @Test
    public void list(){
        ListOperations list = redisTemplate.opsForList();

        list.leftPush("studentList","weimin");
        list.leftPush("studentList","yaya");
        list.leftPush("studentList","xiaoming");
        list.leftPush("studentList","xiaohong");
        list.leftPush("studentList","stu");

        List studentList = list.range("studentList", 0, 5);

        int size = studentList.size();
        System.out.println(studentList);
        System.out.println(size);

        // 添加到 哪个数据的左边， 不存在的话不会报错，但是也插不进去
        list.leftPush("studentList","qqqqq","qqqqqqq");

    }


    /**
     * 通用命令
     */
    @Test
    public void common(){
        Set keys = redisTemplate.keys("*");

        System.out.println(keys);
    }

    /**
     * 失效时间
     */
    @Test
    public void time(){
        ValueOperations valueOperations = redisTemplate.opsForValue();


        valueOperations.set("code","test",30, TimeUnit.SECONDS);

        Long time = redisTemplate.getExpire("code");


        // 给已存在的key设置时间
        redisTemplate.expire("code",30,TimeUnit.SECONDS);
    }
}
