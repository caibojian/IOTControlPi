package com.caibojian;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;

import com.caibojian.app.constants.Constants;
import com.caibojian.iotservice.AWSIoTConsumer;
import com.caibojian.iotservice.AWSIoTFactory;

public class AutoStartServer extends HttpServlet {
	@Override
	public void init() throws ServletException {
		Thread thread = new Thread(new Runnable() {
			@Override
			public void run() {
				// Sync.run();
				AWSIoTFactory iotFactory = new AWSIoTFactory();
				try {
					String topic = Constants.IOT_TOPOIC_LOGIN;
					String topic1 = Constants.IOT_TOPOIC_GETFRIENDS;
					String topic2 = Constants.IOT_TOPOIC_GETFRIENDSGROUP;
					String topic3 = Constants.IOT_TOPOIC_LEDCONTROL;
					AWSIoTConsumer cmd = iotFactory.create();
					System.out.println(cmd.isConnected() );
					cmd.subscribe(topic);
					cmd.subscribe(topic1);
					cmd.subscribe(topic2);
					cmd.subscribe(topic3);
				} catch (Exception e) {
					e.printStackTrace();
				}

			}
		});
		thread.start();
	}

}