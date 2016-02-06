package co.elastic.jmeter.elasticsearch;

import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.Set;

import org.apache.jmeter.threads.JMeterContext;
import org.apache.jmeter.threads.JMeterContextService;
import org.apache.jmeter.threads.JMeterVariables;
import org.apache.jmeter.util.JMeterUtils;
import org.apache.jmeter.assertions.AssertionResult;
import org.apache.jmeter.config.Arguments;
import org.apache.jmeter.samplers.SampleResult;
import org.apache.jmeter.visualizers.backend.AbstractBackendListenerClient;
import org.apache.jmeter.visualizers.backend.BackendListenerContext;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.client.Client;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.shield.ShieldPlugin;


public class ElasticSearchBackendListenerClient extends AbstractBackendListenerClient {
	
	private Client client; 
	private String indexName;
	private String dateTimeAppendFormat;
	private String indexType;
	private String testRunId;
	private JMeterVariables vars;
	
	private static final int DEFAULT_ELASTICSEARCH_PORT = 9300;
	private static final String VAR_DELIMITER = "~";
	
	
	@Override
	public void handleSampleResults(List<SampleResult> results,
			BackendListenerContext context) {
		
		String indexNameToUse = indexName;
		
		for(SampleResult result : results) {
			Map<String,Object> jsonObject = getMap(result);
			if(dateTimeAppendFormat != null) {
				SimpleDateFormat sdf = new SimpleDateFormat(dateTimeAppendFormat);
				indexNameToUse = indexName + sdf.format(jsonObject.get("timestamp"));
			}
			//System.out.println("RUN ID" + testRunId + " "+ indexNameToUse);
			client.prepareIndex(indexNameToUse, indexType).setSource(jsonObject).execute().actionGet();
			//System.out.println("Indexed ");
			
		}
		
		
		
	}
	

	
	/**
	 * @param result
	 * @return
	 */
	private Map<String, Object> getMap(SampleResult result) {
		Map<String, Object> map = new HashMap<String, Object>();
		
		String[] sampleLabels = result.getSampleLabel().split(VAR_DELIMITER);
		
		map.put("bodySize", result.getBodySize());
		map.put("bytes", result.getBytes());
		map.put("contentType", result.getContentType());
		map.put("dataType", result.getDataType());
		map.put("sampleTime", result.getTime());
		map.put("responseCode", result.getResponseCode());
		map.put("latency", result.getLatency());
		map.put("responseMessage", result.getResponseMessage());
	
		map.put("sampleCount", result.getSampleCount());
		map.put("sampleLabel", sampleLabels[0]);
		map.put("threadName", result.getThreadName());
		map.put("groupThreads", result.getGroupThreads());
		map.put("success", String.valueOf(result.isSuccessful()));
		map.put("url", result.getUrlAsString());
		map.put("errorCount", result.getErrorCount());
		map.put("idleTime", result.getIdleTime());
		
		map.put("testRunId", testRunId);
		map.put("timestamp", new Date(result.getTimeStamp()));
		
		
		AssertionResult[] assertions = result.getAssertionResults();
		int count=0;
		if(assertions != null) {
			Map<String,Object> [] assertionArray = new HashMap[assertions.length];
			for(AssertionResult assertionResult : assertions) {
				Map<String,Object> assertionMap = new HashMap<String,Object>();
				assertionMap.put("failure", assertionResult.isError() || assertionResult.isFailure());
				assertionMap.put("failureMessage", assertionResult.getFailureMessage());
				assertionMap.put("name", assertionResult.getName());
				assertionArray[count++] = assertionMap;
			}
			map.put("assertions", assertionArray);
		}
		
		
		// Additional metrics may be passed using bsh samplers
		Properties props = JMeterUtils.getJMeterProperties();
		Set propKeys =props.keySet();
		
		Iterator i = propKeys.iterator();
		while (i.hasNext()) {
			String anEntry = (String)i.next();
			//System.out.println(" prop  "+anEntry);
			if (anEntry.startsWith("es_")) {
				
				
				 String metric = JMeterUtils.getPropDefault(anEntry, null);
				 if (metric != null) {
					 //System.out.println(anEntry + " "+metric);
					 if (anEntry.indexOf("_F") > 0) {
						 Float m = Float.valueOf(metric);
						 map.put(anEntry, m);
					 }
					 else
					 if (anEntry.indexOf("_S") > 0) {
						 
						 map.put(anEntry, metric);
					 }
					 else
					 if (anEntry.indexOf("_I") > 0) {
						 Integer m = Integer.valueOf(metric);
						 map.put(anEntry, m);
					 }
					 else
					 if (anEntry.indexOf("_L") > 0) {
						 Long m = Long.valueOf(metric);
						 map.put(anEntry, m);
					 }
					 
					
				 }
			}
			
		}
		
		
		
			
		return map;
	}

	@Override
	public void setupTest(BackendListenerContext context) throws Exception {
		
		
		
		
		/* comma separated list of hosts */
        String host = context.getParameter("host");
        String userPass = context.getParameter("userPass");
        String[] servers = host.split(",");
        
        indexName = context.getParameter("indexName");
        dateTimeAppendFormat=context.getParameter("dateTimeAppendFormat");
        if(dateTimeAppendFormat!=null && dateTimeAppendFormat.trim().equals("")) {
        	dateTimeAppendFormat = null;
        }
        
        
        indexType = context.getParameter("indexType");
        
        testRunId = JMeterUtils.getPropDefault("testRunId", context.getParameter( "testRunId" ) );
        
  
        String clusterName = context.getParameter("clusterName");
        
        boolean cloudCluster = Boolean.valueOf(context.getParameter("cloudCluster"));
        
        Settings settings = null;
        
        if (userPass != null) {
         
        	if ( !cloudCluster) {
        	settings = Settings.settingsBuilder()
                .put("cluster.name", clusterName)
                .put("client.transport.sniff", Boolean.valueOf(context.getParameter("sniff")))
                .put("shield.user", userPass)
                .build();
        	}
        	else {
        		settings = Settings.settingsBuilder()
                .put("cluster.name", clusterName)
                .put("client.transport.sniff", Boolean.valueOf(context.getParameter("sniff")))
                .put("shield.user", userPass)
                .put("transport.ping_schedule", "5s")
                .put("action.bulk.compress", false)
                .put("shield.transport.ssl", true)
                .put("request.headers.X-Found-Cluster", clusterName)
                .build();
        	}
        		
        }
        else {
        	settings = Settings.settingsBuilder()
                    .put("cluster.name", clusterName)
                    .put("client.transport.sniff", true)
                    .build();
        	}
  
         client = TransportClient.builder()
  			  .addPlugin(ShieldPlugin.class)
  			  .settings(settings).build();
         
  			
		//client = TransportClient.builder().settings(settings).build();
       System.out.println("Connecting ... ");
		for(String serverPort: servers) {
			String[] serverAndPort = serverPort.split(":");
			int port = DEFAULT_ELASTICSEARCH_PORT;
			if(serverAndPort.length == 2) {
				port = Integer.parseInt(serverAndPort[1]);
			}
			
			((TransportClient)client).addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName(serverAndPort[0]), port));
		}
		
		
		// TODO Get extra parameters 
		// Iterator<String> iter = context.getParameterNamesIterator();
		
		
		
		
		
		super.setupTest(context);
	}

	@Override
	public Arguments getDefaultParameters() {
        Arguments arguments = new Arguments();
        arguments.addArgument("clusterName", "elasticsearch");
        arguments.addArgument("userPass", "");
        arguments.addArgument("host", "localhost:9300");
        arguments.addArgument("indexName", "jmeter-sampler");
        arguments.addArgument("indexType", "samplerResults");
        arguments.addArgument("dateTimeAppendFormat", "-yyyy-MM-dd");
        arguments.addArgument("testRunId", "test_0");
        arguments.addArgument("sniff", "true");
        arguments.addArgument("cloudCluster", "false");
        return arguments;
	}

	@Override
	public void teardownTest(BackendListenerContext context) throws Exception {
		client.close();	
		super.teardownTest(context);
	}

}