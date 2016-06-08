package com.jms.spring;

import java.io.File;
import java.util.Arrays;

import javax.jms.ConnectionFactory;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.ObjectMessage;
import javax.jms.Session;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.jms.annotation.EnableJms;
import org.springframework.jms.config.JmsListenerContainerFactory;
import org.springframework.jms.config.SimpleJmsListenerContainerFactory;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.util.FileSystemUtils;

@SpringBootApplication
@ComponentScan
@EnableJms
public class JMSApp {


	// vm://embedded?broker.persistent=false,useShutdownHook=false
	@Bean
	public JmsListenerContainerFactory connectionFactory() {
		SimpleJmsListenerContainerFactory sjms = new SimpleJmsListenerContainerFactory();
		ActiveMQConnectionFactory aqf = new ActiveMQConnectionFactory();
		aqf.setTrustedPackages(Arrays.asList("com.jms.spring"));
		sjms.setConnectionFactory(aqf);
		return sjms;
	}

	// @Bean // Strictly speaking this bean is not necessary as boot creates a
	// default
	// public ActiveMQConnectionFactory activeMQConnectionFactory() {
	// ActiveMQConnectionFactory factory = new
	// ActiveMQConnectionFactory("tcp://localhost:61616");
	// factory.setTrustedPackages(Arrays.asList("com.jms.spring"));
	// return factory;
	// }

	public static void main(String[] args) {
		// Clean out any ActiveMQ data from a previous run
		FileSystemUtils.deleteRecursively(new File("activemq-data"));

		// Launch the application
		ConfigurableApplicationContext context = SpringApplication.run(JMSApp.class, args);

		// Send a message
		MessageCreator messageCreator = new MessageCreator() {
			@Override
			public Message createMessage(Session session) throws JMSException {
				ObjectMessage objMessage = session.createObjectMessage();
				objMessage.setObject(new MessageImpl("This is a test message", 1));
				return objMessage;
			}
		};
		JmsTemplate jmsTemplate = context.getBean(JmsTemplate.class);
		System.out.println("Sending a new message.");
		try {
			jmsTemplate.send("mailbox-destination", messageCreator);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

}