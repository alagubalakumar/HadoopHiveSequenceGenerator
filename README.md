# HadoopHiveSequenceGenerator
a unique sequence no generator for hive hadoop

This project to generate unique big int sequence id for hive .This will act as server in master node by starting this service
by 
build the project it will generate start.jar and stop.jar
This will generate a sequence of range of number on demand , that will used in the hive udf to get and generate the sequence in between once exhausted ,it can be called further to get unused range.

start the service :  java -jar start.jar  -sp {port of the server} -mp {monitoring port - used to stop} -r {range to generate}

stop the service : java -jar stop.jar  -mp {monitoring port - used to stop}


Implement the UDF Function as follows


public class ServiceIDGenericUDF extends GenericUDF {

	private String seqServiceIP = null;
	private LongWritable result = new LongWritable();
	private JSONObject responseMap;
	private long counter = 0;
	private long refreshID = 0;
	private int sum = 0;


	public ServiceIDGenericUDF() {
		result.set(0);
	}

	@Override
	public void configure(MapredContext context) {
		getRange(seqServiceIP);
	}

	public  LongWritable getID() {	
		if(counter > Long.valueOf(responseMap.get("end")+"")){
			getRange(seqServiceIP);
			return getID();
		}else{	
			result.set(counter);
			counter++;
		}
		return  result;
	}

	@Override
	public LongWritable evaluate(DeferredObject[] arg0) throws HiveException {
		if((arg0[0].get()!=null) && (!StringUtils.isEmpty(arg0[0].get().toString()))){
			refreshID = Long.parseLong(arg0[0].get().toString());
		}
		return new LongWritable(Long.valueOf(refreshID+""+getID()));
	}

	@Override
	public String getDisplayString(String[] arg0) {
		return "unique id generic udf generator";
	}

	public int getNumberSum(int number){

		if(number == 0){
			return sum;
		} else {
			sum += (number%10);
			getNumberSum(number/10);
		}
		return sum;
	}

	@Override
	public ObjectInspector initialize(ObjectInspector[] arg0)
			throws UDFArgumentException {
		try{
			if(SessionState.get() != null){	
				seqServiceIP = SessionState.get().getConf().get("seqgenservice");	
			}
			getRange(seqServiceIP);
		}catch(Exception e){
			System.out.println(e.getMessage());
		}

		return PrimitiveObjectInspectorFactory.writableLongObjectInspector;
	}

	public  JSONObject getRange(String ip) {
		try{
			Client client = Client.create();
			WebResource webResource = client.resource("http://"+ip+"/range");
			MultivaluedMap<String, String> queryParam = new MultivaluedMapImpl();
			ClientResponse clientResponse = webResource.queryParams(queryParam).accept(MediaType.APPLICATION_JSON).get(ClientResponse.class);
			responseMap = (JSONObject) JSONSerializer.toJSON(clientResponse.getEntity(String.class));
			counter = Long.valueOf(responseMap.get("start")+"");
		}catch(Exception e){
			System.out.println(e.getMessage());
		}
		return responseMap;
	}	
}


Develop the Jar of the uDF

Step 1: ADD JAR <jar file>;
Step 2: CREATE TEMPORARY FUNCTION seqIDUDF AS '<class name in the udf>';
Step 3: set seqgenservice={MIP}:{port};
Step 4: set hive.execution.engine=mr; (due to some dependency issue udf is not working with tez)
Step 5: select seqIDUDF({some external integer which is unique which is appended in front of the sequnce});
