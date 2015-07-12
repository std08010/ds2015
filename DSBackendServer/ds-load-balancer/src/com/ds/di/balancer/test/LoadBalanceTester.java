package com.ds.di.balancer.test;




class LoadBalanceTester {
	
	private static final String 	HTTP_PREFIX		= "http://";
	private static final String 	DOMAIN 			= "10.0.0.2:80/";
	private static final String 	HOME_URL 		= "ds-web";
	//private static final String 	REST_TEST_URL 	= "ds-web/rest/general/test/calculate";
	private static final String 	REST_TEST_URL 	= "ds-web/index/show-users.htm";
	
	
	private static final Integer	NO_OF_REQUESTS_1K	= 1000;
	private static final Integer	NO_OF_REQUESTS_10K	= 10000;
	private static final Integer	NO_OF_REQUESTS_100K	= 100000;
	private static final Integer	NO_OF_REQUESTS_1M	= 1000000;
	private static final Integer	NO_OF_REQUESTS_10M	= 10000000;
	private static final Integer	NO_OF_REQUESTS_100M	= 100000000;
	
	private static final Integer	NO_OF_THREADS	= 100;
	

	public static void main(String[] args) throws Exception {
		
		sendMassiveParallelRequests(NO_OF_THREADS, NO_OF_REQUESTS_10K, DOMAIN, REST_TEST_URL);
	}
	
	
	private static void sendMassiveParallelRequests(Integer threadsNo, Integer totalRequests, String domain, String requestURL){
		
		for(int i=0; i<threadsNo; i++) {
			LoadBalanceTesterThread thread = new LoadBalanceTesterThread(i, (totalRequests/threadsNo), domain, requestURL);
			thread.start();
		}
	}
	
	
}