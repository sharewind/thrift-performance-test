package com.sohu.cloudatlas.server;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

import org.apache.thrift.TException;
import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.transport.TFramedTransport;
import org.apache.thrift.transport.TSocket;
import org.apache.thrift.transport.TTransport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sohu.cloudatlas.services.UserService;

public class HelloClient {

	private static final Logger logger = LoggerFactory.getLogger(HelloClient.class);

	public static void main(String[] args) throws Exception {

		long start = System.currentTimeMillis();
		int count = 0;
		int taskCount = 10000;

//		AtomicInteger finishedCount = new AtomicInteger(0);

		ExecutorService executors = Executors.newFixedThreadPool(10000);
		final CountDownLatch countDownLatch = new CountDownLatch(taskCount);

		while(++count < taskCount){
			executors.submit(new Runnable() {

				@Override
				public void run() {
					try{
						doRequest();
					}catch (Exception e) {
						logger.error(e.getMessage(),e);
					}finally{
						countDownLatch.countDown();
					}
				}
			});
		}
		countDownLatch.await();
		long costTime = System.currentTimeMillis() - start;
		double tps = taskCount/(costTime/1000.0);
		logger.info(String.format("%s ",tps));
	}

	private static void doRequest() throws Exception {
		TTransport transport = new TFramedTransport(new TSocket("localhost", 12345));
		TBinaryProtocol binaryProtocol = new TBinaryProtocol(transport);
		UserService.Client client = new UserService.Client.Factory().getClient(binaryProtocol);

		transport.open();
		String response = client.sayHi("world");
		System.out.println(response);
		transport.close();
	}
}
