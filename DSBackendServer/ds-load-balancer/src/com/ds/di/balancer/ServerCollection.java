package com.ds.di.balancer;

import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ServerCollection {
	private List<ServerUnit> servers;
	private boolean alreadyReplaced;
	private ServerCollectionUpdateThread updateThread;
	
	public ServerCollection(InetSocketAddress... srvs){
		servers = new ArrayList<ServerUnit>();
		for(InetSocketAddress address : srvs)
			servers.add(new ServerUnit(address));
		updateThread = null;
	}
	
	public ServerUnit getForRequest(){
		ServerUnit result = servers.get(0);
		result.recordRequest();
		if(servers.size()>1 && result.compareTo(servers.get(1))>0){
			if(alreadyReplaced)
				Collections.sort(servers);
			else{
				servers.set(0, servers.set(1, result));
				alreadyReplaced = true;
			}
		}
		System.out.println(result);
		return result;
	}
	
	public void startStatusUpdates(long interval){
		updateThread = new ServerCollectionUpdateThread(this, interval);
		updateThread.start();
	}
	
	public void stopStatusUpdates(long interval){
		if(updateThread!=null){
			updateThread.terminate();
			updateThread = null;
		}
	}
	
	public void updateStatuses(){
		for(ServerUnit unit : servers){
			unit.getStatus();
		}
		Collections.sort(servers);
		alreadyReplaced = false;
		
//		for(ServerUnit unit : servers){
//			//System.out.println(unit);
//		}

	}
	
}
