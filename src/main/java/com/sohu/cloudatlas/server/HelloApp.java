package com.sohu.cloudatlas.server;

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

//	private static final Logger log = LoggerFactory.getLogger(HelloApp.class);

    public static void main(String[] args)throws Exception {
		int port = 12345;

		TNonblockingServerSocket serverTransport = new TNonblockingServerSocket(port);
		Factory portFactory = new TBinaryProtocol.Factory(true, true);
		TProcessor processor = new UserService.Processor<UserService.Iface>(new UserService.Iface() {
			@Override
			public String sayHi(String nick) throws TException {
				return "hello";
			}
		});

		TThreadedSelectorServer.Args serverArgs = new TThreadedSelectorServer.Args(serverTransport);
		serverArgs.processor(processor);
		serverArgs.protocolFactory(portFactory);

		TThreadedSelectorServer server = new TThreadedSelectorServer(serverArgs);
		server.serve();
    }
}
