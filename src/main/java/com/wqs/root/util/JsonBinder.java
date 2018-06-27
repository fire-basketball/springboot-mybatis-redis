package com.wqs.root.util;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.annotate.JsonSerialize;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JsonBinder {
    private static Logger logger = LoggerFactory.getLogger(JsonBinder.class);
    private ObjectMapper mapper;
    @SuppressWarnings("deprecation")
    public JsonBinder(JsonSerialize.Inclusion inclusion){
        mapper=new ObjectMapper();
        //设置输出包含的属性
       mapper.getSerializationConfig().setSerializationInclusion(inclusion);
    }
    public static JsonBinder buildNormalBinder(){
        return null;
    }
}
