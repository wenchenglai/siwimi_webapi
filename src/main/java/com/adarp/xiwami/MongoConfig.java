package com.adarp.xiwami;


import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.config.AbstractMongoConfiguration;

import com.mongodb.Mongo;
import com.mongodb.MongoClient;

@Configuration
public class MongoConfig extends AbstractMongoConfiguration {

	@Override
	public String getDatabaseName() {
		return "xiwami";
	}

	@Override
	public Mongo mongo() throws Exception {
		return new MongoClient("127.0.0.1");
	}
	
	// Register :  for Spring to output raw JSON
//    @Override
//    public CustomConversions customConversions() {
//		return new CustomConversions(Arrays.asList(new DBObjectToStringConverter()));
//	}
}
