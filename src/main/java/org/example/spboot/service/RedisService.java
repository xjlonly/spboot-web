package org.example.spboot.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.lettuce.core.RedisClient;
import io.lettuce.core.api.StatefulConnection;
import io.lettuce.core.api.StatefulRedisConnection;
import io.lettuce.core.api.sync.RedisCommands;
import io.lettuce.core.support.ConnectionPoolSupport;
import org.apache.commons.pool2.impl.DefaultPooledObject;
import org.apache.commons.pool2.impl.GenericKeyedObjectPool;
import org.apache.commons.pool2.impl.GenericObjectPool;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.ExtendedBeanInfoFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.convert.converter.ConditionalGenericConverter;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.util.Map;
import java.util.TreeMap;

@Component
public class RedisService {

    final Logger logger = LoggerFactory.getLogger(getClass());
    @Autowired
    RedisClient redisClient;

    @Autowired
    ObjectMapper objectMapper;

    GenericObjectPool<StatefulRedisConnection<String,String>> redisConnectionPool;

    @PostConstruct
    public void  init(){
        GenericObjectPoolConfig<StatefulRedisConnection<String,String>> config = new GenericObjectPoolConfig<>();
        config.setMaxIdle(5);
        config.setMaxTotal(20);
        config.setTestOnReturn(true);
        config.setTestWhileIdle(true);
        this.redisConnectionPool = ConnectionPoolSupport.createGenericObjectPool(()->redisClient.connect(),config);
    }

    @PreDestroy
    public void shutdown(){
        this.redisConnectionPool.close();
        this.redisClient.shutdown();
    }

    public <T> T executeSync(SyncCommandCallback<T> commandCallback) {
        try(StatefulRedisConnection<String,String> connection = redisConnectionPool.borrowObject()){
            connection.setAutoFlushCommands(true);
            RedisCommands<String,String> commands = connection.sync();
            return commandCallback.doInConnection(commands);
        }catch (Exception e){
            logger.warn("executeSync redis failed.", e);
            throw new RuntimeException(e);
        }
    }

    public String set(String key,String value){
        return executeSync(commands -> commands.set(key,value));
    }

    public String get(String key){
        return executeSync(commands -> commands.get(key));
    }

    public <T> String set(String key, T value){
        try {
            String v = objectMapper.writeValueAsString(value);
            return executeSync(commands -> commands.set(key,v));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    public <T> T get(String key, Class<T> clazz){
        var value = executeSync(commands -> commands.get(key));
        if(value != null){
            try {
                return  objectMapper.readValue(value,clazz);
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public boolean hset(String key,String field, String value){
        return executeSync(commands -> commands.hset(key, field, value));
    }

    public String hget(String key, String field){
        return executeSync(commands -> commands.hget(key,field));
    }

    public Map<String, String> hgetall(String key){
        return executeSync(commands -> commands.hgetall(key));
    }
}
