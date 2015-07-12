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
        while(!toStop)
        	updateStep();
    }
	
	public void terminate(){
		toStop = true;
		notify();
	}
    
    public synchronized void updateStep(){
    	
    	try {
    		servers.updateStatuses();
			wait(interval);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
    }
                                     
}
