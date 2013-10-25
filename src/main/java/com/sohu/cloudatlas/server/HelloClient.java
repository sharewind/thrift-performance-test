package com.sohu.cloudatlas.server;

import org.apache.thrift.TException;
import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.transport.TFramedTransport;
import org.apache.thrift.transport.TSocket;
import org.apache.thrift.transport.TTransport;
import org.apache.thrift.transport.TTransportException;

import com.sohu.cloudatlas.services.UserService;

public class HelloClient {

	public static void main(String[] args) throws Exception {
		String host = null;
		int port = 0, concurrency = 0;
		try {
			host = args[0];
			port = Integer.parseInt(args[1]);
			concurrency = Integer.parseInt(args[2]);
		} catch (Exception e) {
			printUsage();
			System.exit(1);
		}

		final String HOST = host;
		final int PORT = port;
		for (int i = 0; i < concurrency; ++i) {
			new Thread() {
				@Override
				public void run() {
					TTransport transport = new TFramedTransport(new TSocket(HOST, PORT));
					TBinaryProtocol binaryProtocol = new TBinaryProtocol(transport);
					UserService.Client client = new UserService.Client.Factory().getClient(binaryProtocol);

					try {
						transport.open();
					} catch (TTransportException e) {
						e.printStackTrace();
					}

					while (true) {
						try {
							client.sayHi("world");
						} catch (TException e) {
							e.printStackTrace();
						}
					}
				}
			}.start();
		}
	}

	private static void printUsage() {
		System.out.println("Usage: java com.sohu.cloudatlas.server.HelloClient" + " <host> <port> <concurrency>");
	}

}
