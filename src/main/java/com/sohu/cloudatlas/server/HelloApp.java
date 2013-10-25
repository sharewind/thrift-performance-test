package com.sohu.cloudatlas.server;

import java.util.concurrent.atomic.AtomicLong;

import org.apache.thrift.TException;
import org.apache.thrift.TProcessor;
import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.protocol.TBinaryProtocol.Factory;
import org.apache.thrift.server.TThreadedSelectorServer;
import org.apache.thrift.transport.TNonblockingServerSocket;

import com.sohu.cloudatlas.services.UserService;

/**
 * 
 * @author Caijianfeng
 * 
 */
public class HelloApp {

	static AtomicLong transactionCount = new AtomicLong(0);
	static {
		new Thread() {
			long lastTime = System.currentTimeMillis();

			@Override
			public void run() {
				while (true) {
					long currentTime = System.currentTimeMillis();
					double seconds = (currentTime - lastTime) / 1000.0;
					System.out.println("TPS: " + (transactionCount.get() / seconds));
					transactionCount.set(0);
					lastTime = currentTime;
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}

		}.start();
	}

	public static void main(String[] args) throws Exception {
		int port = 12345;

		TNonblockingServerSocket serverTransport = new TNonblockingServerSocket(port);
		Factory portFactory = new TBinaryProtocol.Factory(true, true);
		TProcessor processor = new UserService.Processor<UserService.Iface>(new UserService.Iface() {

			@Override
			public String sayHi(String nick) throws TException {
				//try{
				//	Thread.sleep(100);
				//}catch(Exception e){}
				transactionCount.incrementAndGet();
				return "hello";
			}

		});


		TThreadedSelectorServer.Args serverArgs = new TThreadedSelectorServer.Args(serverTransport);
		serverArgs.processor(processor);
		serverArgs.protocolFactory(portFactory);
		serverArgs.acceptQueueSizePerThread(100000);
		serverArgs.selectorThreads(10);
		serverArgs.workerThreads(10000);

		TThreadedSelectorServer server = new TThreadedSelectorServer(serverArgs);
		server.serve();
	}
}
