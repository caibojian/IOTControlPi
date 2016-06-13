package com.caibojian.app.utils;
  
import java.lang.reflect.Type;

import com.google.gson.Gson;  
/** 
 * Java对象和JSON字符串相互转化工具类 
 * @author penghuaiyi 
 * @date 2013-08-10 
 */  
public final class JsonUtil {  
      
    private JsonUtil(){}  
      
    /** 
     * 对象转换成json字符串 
     * @param obj  
     * @return  
     */  
    public static String toJson(Object obj) {  
        Gson gson = new Gson();  
        return gson.toJson(obj);  
    }  
  
    /** 
     * json字符串转成对象 
     * @param str   
     * @param type 
     * @return  
     */  
    public static <T> T fromJson(String str, Type type) {  
        Gson gson = new Gson();  
        return gson.fromJson(str, type);  
    }  
  
    /** 
     * json字符串转成对象 
     * @param str   
     * @param type  
     * @return  
     */  
    public static <T> T fromJson(String str, Class<T> type) {  
        Gson gson = new Gson();  
        return gson.fromJson(str, type);  
    }  
    
    public  static void main(String[] args) throws Exception{
//    	MyMessage msg1  = fromJson("{\"uuid\":\"2J1Cm6kQxBvvoYuXHRs\",\"fromId\":\"1\",\"toId\":\"0\",\"content\":\"{\"attachmentId\":0,\"chatGroupId\":0,\"chatMessageId\":0,\"checked\":false,\"content\":\"hello word\",\"contentType\":0,\"date\":\"May 22, 2016 3:53:21 PM\",\"discussionGroupId\":0,\"fromId\":1,\"id\":1,\"msgType\":0,\"status\":0,\"toId\":0,\"transfer\":false,\"type\":0,\"unCheckedCount\":0,\"uuid\":\"2J1Cm6kQxBvvoYuXHRs\",\"whoId\":0}\",\"date\":\"May 23, 2016 12:25:24 AM\",\"msgType\":6}", MyMessage.class);
//    	MyMessage msg1  = fromJson("{\"content\":\"{\\\"uuid\\\":\\\"cOlPavlGMw68xcrRvRP\\\",\\\"id\\\":12,\\\"checked\\\":true,\\\"content\\\":\\\"少年宫\\\",\\\"date\\\":\\\"May 24, 2016 7:38:22 PM\\\",\\\"discussionGroupId\\\":0,\\\"attachmentId\\\":0,\\\"chatMessageId\\\":0,\\\"fromId\\\":0,\\\"chatGroupId\\\":0,\\\"msgType\\\":0,\\\"status\\\":0,\\\"toId\\\":1,\\\"transfer\\\":false,\\\"type\\\":0,\\\"unCheckedCount\\\":0,\\\"contentType\\\":0,\\\"whoId\\\":0}\",\"date\":\"May 24, 2016 7:38:22 PM\",\"fromId\":\"0\",\"uuid\":\"cOlPavlGMw68xcrRvRP\",\"toId\":\"1\",\"msgType\":6}", MyMessage.class);
//    	System.out.println(msg1.getContent());
//    	ChatMessage msg  = fromJson(msg1.getContent(), ChatMessage.class);
//    	System.out.println(msg.getContent());
//    	ChatMessage msg  = fromJson("{\"attachmentId\":0,\"chatGroupId\":0,\"chatMessageId\":0,\"checked\":false,\"content\":\"hello word\",\"contentType\":0,\"date\":\"May 22, 2016 3:53:21 PM\",\"discussionGroupId\":0,\"fromId\":1,\"id\":1,\"msgType\":0,\"status\":0,\"toId\":0,\"transfer\":false,\"type\":0,\"unCheckedCount\":0,\"uuid\":\"2J1Cm6kQxBvvoYuXHRs\",\"whoId\":0}", ChatMessage.class);
//    	ChatMessage msg  = fromJson(msg1.getContent(), ChatMessage.class);
//    	System.out.println(msg.getContent());
//    	MyMessage mymsg = new MyMessage();
//    	mymsg.setContent("ss");
//    	mymsg.setDate(new Date());
//    	mymsg.setFromId("mymsg");
//    	mymsg.setToId("mymsg");
//    	mymsg.setUuid("mymsg");
//    	System.out.println(toJson(mymsg));
//    	
//    	AWSIoTParams params = new AWSIoTParams();
//		AWSIoTFactory iotFactory = new AWSIoTFactory();
//		
//		String sendMsgs = "{\"content\":\"{\\\"attachmentId\\\":0,\\\"chatGroupId\\\":0,\\\"chatMessageId\\\":2,\\\"checked\\\":false,\\\"content\\\":\\\"helloword1--ok4?\\\",\\\"contentType\\\":0,\\\"date\\\":\\\"May 22, 2016 4:35:39 PM\\\",\\\"discussionGroupId\\\":0,\\\"fromId\\\":1,\\\"msgType\\\":0,\\\"status\\\":0,\\\"toId\\\":0,\\\"transfer\\\":false,\\\"type\\\":0,\\\"unCheckedCount\\\":0,\\\"uuid\\\":\\\"T99sT8ri9CPnoJTKYLd\\\",\\\"whoId\\\":1}\",\"date\":\"May 22, 2016 4:35:39 PM\",\"fromId\":\"0\",\"msgType\":6,\"toId\":\"0\",\"uuid\":\"T57sT7ri8CPnoJTKYLd\"}";
//		String toTopic = null;
//		AWSIoTConfig awsConfig = ConfigLoader.loadConfig();
//		params.setQos(1);
//		params.setAwsConfig(awsConfig);
//		AWSIoTCommand cmd  = iotFactory.create(AWSIOTClientType.PUBLISHER);		
//		toTopic = "0";
//		params.setTopic(toTopic);
//		params.setMessage(sendMsgs);
//		cmd.execute(params);
    	String s1 = "{\"uuid\":\"H2XyDZtQTfoxykjqVPx\",\"id\":26,\"checked\":true,\"content\":\"归根结底发\",\"date\":\"May 24, 2016 8:36:13 PM\",\"discussionGroupId\":0,\"attachmentId\":0,\"chatMessageId\":0,\"fromId\":0,\"chatGroupId\":0,\"msgType\":0,\"status\":0,\"toId\":1,\"transfer\":false,\"type\":0,\"unCheckedCount\":0,\"contentType\":0,\"whoId\":0}";
    	String s = "{\"content\":\"{\\\"uuid\\\":\\\"1Dx9MdqXUyytq2Tra4q\\\",\\\"id\\\":13,\\\"checked\\\":true,\\\"content\\\":\\\"谷歌\\\",\\\"date\\\":\\\"May 24, 2016 7:49:18 PM\\\",\\\"discussionGroupId\\\":0,\\\"attachmentId\\\":0,\\\"chatMessageId\\\":0,\\\"fromId\\\":0,\\\"chatGroupId\\\":0,\\\"msgType\\\":0,\\\"status\\\":0,\\\"toId\\\":1,\\\"transfer\\\":false,\\\"type\\\":0,\\\"unCheckedCount\\\":0,\\\"contentType\\\":0,\\\"whoId\\\":0}\",\"date\":\"May 24, 2016 7:49:18 PM\",\"fromId\":\"0\",\"uuid\":\"1Dx9MdqXUyytq2Tra4q\",\"toId\":\"1\",\"msgType\":6}";
    	String part1 = s.substring(0, s.lastIndexOf("{"));
    	part1 = part1.replaceAll("\\\\","");
    	System.out.println(part1);
    	String part2 = s.substring(s.lastIndexOf("{"), s.indexOf("}"));
    	part2 = part2.replaceAll("\\\\\\\\","");
    	System.out.println(part2);
    	String part3 = s.substring(s.indexOf("}"), s.length());
    	part3 = part3.replaceAll("\\\\","");
    	System.out.println(part3);
    	String ss = part1+part2+part3;
    	System.out.println(ss);
    }
  
} 