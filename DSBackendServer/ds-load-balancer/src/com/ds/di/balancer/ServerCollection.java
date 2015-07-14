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
		int i=0;
		for(InetSocketAddress address : srvs){
			ServerUnit s = new ServerUnit(address);
			s.setIndex(i);
			servers.add(s);
			i++;
		}
		updateThread = null;
	}
	
	public ServerUnit getForRequest(){
		ServerUnit result = servers.get(0);
		result.recordRequest();
		synchronized (servers) {
			Collections.sort(servers);
		}
//		if(servers.size()>1 && result.compareTo(servers.get(1))>0){
//			if(alreadyReplaced){
//				Collections.sort(servers);
//				alreadyReplaced = false;
//			}
//			else{
//				servers.set(0, servers.set(1, result));
//				alreadyReplaced = true;
//			}
//		}
		return result;
	}
	
	public void registerServerFailure(ServerUnit unit){
		unit.rank += 10000;
		Collections.sort(servers);
		alreadyReplaced = false;
	}
	
	public void startStatusUpdates(long interval){
		updateThread = new ServerCollectionUpdateThread(this, interval);
		updateThread.start();
	}
	
	public void stopStatusUpdates(){
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
		
		for(ServerUnit unit : servers){
			System.out.println(unit);
		}

	}
	
}
