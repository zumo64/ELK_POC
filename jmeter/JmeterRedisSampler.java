package co.elastic.util;

import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.JSONSerializer;

import org.apache.jmeter.config.Arguments;
import org.apache.jmeter.protocol.java.sampler.AbstractJavaSamplerClient;
import org.apache.jmeter.protocol.java.sampler.JavaSamplerContext;
import org.apache.jmeter.samplers.SampleResult;
import org.apache.commons.logging.*;

import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisCluster;

 
 
 
public class JmeterRedisSampler extends AbstractJavaSamplerClient{
 
  



 
 private SampleResult results;
 
 /**
 
  * Jmeter Sampler
 
  */
 
 private String redisServerList;
 private String method;
 private String key;
 private String keyField;
 private String value;
 private boolean checkparameter;
 private Set<HostAndPort> HostandPort ;
 private String response;
 private boolean isCluster;
 private boolean isPersist;
 private long startime;
 private long endtime;
 private StringBuilder ret;
 private Object redisclient ;
 
 
  public void setupTest(JavaSamplerContext arg0) {
 
  //log.info("execute setupTest...");
 
	  checkparameter = true;
	  HostandPort = new HashSet<HostAndPort>();
	  redisServerList = arg0.getParameter("redisServerList", "");
	  keyField = arg0.getParameter("keyField", "");
	  method = arg0.getParameter("method", "");
	  
	  response = "not found the key";
	  //key = arg0.getParameter("key", "");
	 
	  //value = arg0.getParameter("value", "");
	 
	  isPersist = arg0.getParameter("isPersist","").equals("true")?true:false;
	  isCluster = arg0.getParameter("isCluster", "").equals("true")?true:false;
	  ret = new StringBuilder();
	  Iterator<String> itr = arg0.getParameterNamesIterator();
	
	  while(itr.hasNext()) {
		  String element = (String) itr.next();
		  if(arg0.getParameter(element,"").equals("")) {
	 	//log.info(element+" ");
	 		  checkparameter=false;
		  }
	 
		  String tempString = element + ":" + arg0.getParameter(element,"")+"\n";
	
		  //log.info(tempString);        
		  ret.append(tempString);
	  }
	 
	  String[] servers = redisServerList.split(",", -1);
	 
	  for(int i = 0 ; i < servers.length ; i++) {
		  String[] hostandport = servers[i].split(":",-1);
		  if(!hostandport[0].equals("") && !hostandport[1].equals(""))
			 HostandPort.add(new HostAndPort(hostandport[0],Integer.valueOf( hostandport[1])));
		  else {
	 
			  // log.info(" host or port "+hostandport[0]+hostandport[1]);
	 
			  checkparameter= false;
		  }
	  }
	 
	 
	  if(servers.length > 1 && !isCluster) {
	   //log.info("redis server");
		  checkparameter=false;
	  }
	 
	  if(isCluster) {
	    redisclient  = new JedisCluster(HostandPort);
	  }
	  else {
	    Iterator<HostAndPort> iter = HostandPort.iterator();
	    HostAndPort first = (HostAndPort) iter.next();
	    redisclient = new Jedis(first.getHost(),first.getPort());
	  }
}
 
 
 
 
  public Arguments getDefaultParameters() {
 
  //log.info("execute getDefaultParameters...");
 
  Arguments params = new Arguments();
  params.addArgument("redisServerList", "127.0.0.1:6379");
  params.addArgument("isCluster", "false");
  params.addArgument("method", "bulk|setex|lpush|get|ttl...");
  // when using bulk the keyfield value will be the redis key
  params.addArgument("keyField", "type");
  params.addArgument("key", "key");
  params.addArgument("value", "value");
  params.addArgument("isPersist", "false");
  return params;
 
  }
 
 
  @Override
  public SampleResult runTest(JavaSamplerContext arg0) {
 
	  //log.info("execute runTest...");
	  //key = arg0.getParameter("key", "")+ new BigInteger(130, new SecureRandom()).toString(32);
 
	  key = arg0.getParameter("key", "");
	  keyField =  arg0.getParameter("keyField", null);
	  value = arg0.getParameter("value", "");
	  startime =  new Date().getTime();
	  results = new SampleResult();
	  //log.info("init..." +  results.getTime() + " "+ results.getStartTime()+ " "+results.getEndTime());
	  results.sampleStart();
	  //log.info("after start..." +  results.getTime() + " "+ results.getStartTime()+ " "+results.getEndTime());
	  results.setSamplerData(ret.toString()+" \n actualkey: "+key );

	  if(!checkparameter) {
		  //log.info("fail...");
		  results.setSuccessful(false);
		  return results;
	  }
 
 
	if(isCluster) {
		
		 if(method.equals("lpush")) {
			    response = ((JedisCluster) redisclient).lpush(key, value).toString();
			    if(isPersist)
			    	((JedisCluster) redisclient).persist(key);
			 
		  }
		 else if(method.equals("setex")) {
		    response = ((JedisCluster) redisclient).setex(key, 5000,  value);
		    if(isPersist)
		    	((JedisCluster) redisclient).persist(key);
		 }
		 
		 else if(method.equals("get"))
		    response = ((JedisCluster) redisclient).get(key);
		 
		 else if(method.equals("ttl"))
			 response = String.valueOf(((JedisCluster) redisclient).pttl(key));
		 
		//  else log.info("Invalid method");
	 
	}
	 
	else {
		
		// JSON array of events
		if(method.equals("bulk") && keyField != null) {
			
			JSONArray jsonArray = (JSONArray) JSONSerializer.toJSON( value );  
			
			for (int i = 0; i < jsonArray.size(); i++) {
				
				JSONObject anEvent = jsonArray.getJSONObject(i);
				// use the type as the key
				String keyValue = anEvent.getString(keyField);
				
				// send 
				Long returned = ((Jedis) redisclient).lpush(keyValue, anEvent.toString());
				response =  returned.toString();
			    if(isPersist)
			    	((Jedis) redisclient).persist(key);
				
			}
			   
			
			
			
		 
		}
		else
		if(method.equals("lpush")) {
			Long returned = ((Jedis) redisclient).lpush(key, value);
			response =  returned.toString();
		    if(isPersist)
		    	((Jedis) redisclient).persist(key);
		 
		}
		else if(method.equals("setex")) {
	 
			response = ((Jedis) redisclient).setex(key, 5000, value);       
		  
			if(isPersist)
				((Jedis) redisclient).persist(key);
	 
		}
	 
		else if(method.equals("get"))
			response = ((Jedis) redisclient).get(key);
	 
		else if(method.equals("ttl"))
			response = String.valueOf(((Jedis) redisclient).pttl(key));
	 
	 // else
	 //   log.info("Invalid method .");
	 
	}
 
 
 
  if(response == null) response = "key not found";
  results.setResponseData(response, "utf-8");
  results.setSuccessful(true);
  results.setResponseCodeOK();
  results.setEndTime(System.currentTimeMillis());
 
  return results;
 
  }
 
 
 
  
  public void teardownTest(JavaSamplerContext arg0) {
	  results.cleanAfterSample();
	  //results.setStartNextThreadLoop(true);
  }
 
}