package com.ds.di.balancer;

public class ServerCollectionUpdateThread extends Thread {
    private ServerCollection servers;
    private long interval;
    private boolean toStop;
	public ServerCollectionUpdateThread(ServerCollection servers, long interval)
    {
    	this.servers = servers;
    	this.interval = interval;
    }
	
	@Override
	public void run(){
    	toStop = false;
    	synchronized(this){
    		while(!toStop){
    			servers.updateStatuses();
    			try {
    				this.wait(interval);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
    		}
    	}
    }
	

	public void terminate(){
		toStop = true;
		synchronized(this){
			this.notify();
		}
		try {
			join();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
    
    public void updateLoop(){
    	
    	servers.updateStatuses();
    }
                                     
}
