package com.example.test;

import org.dbunit.PropertiesBasedJdbcDatabaseTester;
import org.dbunit.database.DatabaseConfig;
import org.dbunit.database.IDatabaseConnection;
import org.dbunit.database.QueryDataSet;
import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.ITable;
import org.dbunit.dataset.xml.FlatXmlDataSetBuilder;
import org.dbunit.ext.hsqldb.HsqldbDataTypeFactory;
import org.dbunit.operation.DatabaseOperation;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.example.DummyProcessor;
import com.start.DbUnitExampleApplication;

import lombok.extern.log4j.Log4j2;

/**
 * @author HP PC
 * 
 *         http://www.rndblog.com/adding-dbunit-to-your-project/
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { DbUnitExampleApplication.class })
@SpringBootTest
@Log4j2
public class DbUnitExampleApplicationTest extends PrepareConfigForDbUnit {

	@Value("${spring.dataSource.url}")
	private String url;
	
	@Value("${spring.dataSource.driver.class}")
	private String driver;

	IDatabaseConnection connection;

	@Autowired
	DummyProcessor processor;

	@Before
	public void setUp() throws Exception {
		System.setProperty(PropertiesBasedJdbcDatabaseTester.DBUNIT_DRIVER_CLASS,driver);
		System.setProperty(PropertiesBasedJdbcDatabaseTester.DBUNIT_CONNECTION_URL,url);
		connection = getConnection();
		DatabaseConfig dbConfig = connection.getConfig();
		dbConfig.setProperty(DatabaseConfig.PROPERTY_DATATYPE_FACTORY, new HsqldbDataTypeFactory());
	}

	@Test
	public void test1() throws Exception {
		IDataSet dataSet = new FlatXmlDataSetBuilder()
				.build(DbUnitExampleApplicationTest.class.getResourceAsStream("/test/test1.xml"));
		DatabaseOperation.REFRESH.execute(connection, dataSet);
		String profile = null;
		try {
			processor.printResult();
			int count = connection.getRowCount("profile_details");
			Assert.assertTrue("count should be 1", count == 1);
			ITable table = connection.createQueryTable("user_profile",
					"select * from user_profile, profile_details where user_profile.profile=profile_details.profile");
			String userId = (String) table.getValue(0, "userid");
			Assert.assertTrue("user id should be test1@gmail.com", userId.equals("test1@gmail.com"));
			profile = (String) table.getValue(0, "profile");
		} catch (Exception e) {
			log.error("Error {}", e);
			throw e;
		} finally {
			cleanTableData("'" + profile + "'");
		}
	}

	@Test
	public void test2() throws Exception {
		IDataSet dataSet = new FlatXmlDataSetBuilder()
				.build(DbUnitExampleApplicationTest.class.getResourceAsStream("/test/test2.xml"));
		DatabaseOperation.REFRESH.execute(connection, dataSet);
		String profile = null;
		try {
			processor.printResult();
			int count = connection.getRowCount("profile_details", "where role_id=2");
			Assert.assertTrue("count should be 1", count == 1);
			ITable table = connection.createQueryTable("user_profile",
					"select * from user_profile, profile_details where user_profile.profile=profile_details.profile");
			String userId = (String) table.getValue(0, "userid");
			Assert.assertTrue("user id should be test2@gmail.com", userId.equals("test2@gmail.com"));
			profile = (String) table.getValue(0, "profile");
		} catch (Exception e) {
			log.error("Error {}", e);
			throw e;
		} finally {
			cleanTableData("'" + profile + "'");
		}
	}

	private void cleanTableData(String profile) throws Exception {
		QueryDataSet ds = new QueryDataSet(connection);
		try {
			ds.addTable("user_profile", null);
			DatabaseOperation.DELETE.execute(connection, ds);
			ds.addTable("profile_details", "select * from profile_details where profile=" + profile);
			DatabaseOperation.DELETE.execute(connection, ds);
		} catch (Exception e) {
			log.error("Error {}", e);
			throw e;
		}
	}
}
