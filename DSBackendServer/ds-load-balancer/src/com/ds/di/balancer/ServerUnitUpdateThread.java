package com.ds.di.balancer;

public class ServerUnitUpdateThread extends Thread {
    private ServerUnit server;
    private long interval;
    private boolean toStop;
	public ServerUnitUpdateThread(ServerUnit server, long interval)
    {
    	this.server = server;
    	this.interval = interval;
    }
	
	@Override
	public void run(){
    	toStop = false;
    	synchronized(this){
    		while(!toStop){
    			server.getStatus();
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
}
