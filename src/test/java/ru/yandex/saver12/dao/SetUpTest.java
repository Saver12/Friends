package ru.yandex.saver12.dao;

import org.dbunit.IDatabaseTester;
import org.dbunit.JdbcDatabaseTester;
import org.dbunit.dataset.IDataSet;
import org.dbunit.operation.DatabaseOperation;
import org.junit.After;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import ru.yandex.saver12.util.DBConnectionManager;

import java.sql.DriverManager;

import static org.powermock.api.mockito.PowerMockito.mockStatic;
import static org.powermock.api.mockito.PowerMockito.when;

@RunWith(PowerMockRunner.class)
@PrepareForTest(DBConnectionManager.class)
public abstract class SetUpTest {

    public static final String JDBC_DRIVER = com.mysql.jdbc.Driver.class.getName();
    public static final String JDBC_URL = "jdbc:mysql://localhost:3306/testdb";
    public static final String USER = "root";
    public static final String PASSWORD = "root";

    private IDatabaseTester databaseTester;

    @Before
    public void setUp() throws Exception {
        IDataSet dataSet = readDataSet();
        cleanlyInsert(dataSet);
        changeConnection();
    }

    @After
    public void tearDown() throws Exception {
        databaseTester.setTearDownOperation(DatabaseOperation.DELETE_ALL);
        databaseTester.onTearDown();
    }

    abstract IDataSet readDataSet() throws Exception;

    private void cleanlyInsert(IDataSet dataSet) throws Exception {
        databaseTester = new JdbcDatabaseTester(JDBC_DRIVER, JDBC_URL, USER, PASSWORD);
        databaseTester.setSetUpOperation(DatabaseOperation.CLEAN_INSERT);
        databaseTester.setDataSet(dataSet);
        databaseTester.onSetup();
    }

    public static void changeConnection() throws Exception {
        mockStatic(DBConnectionManager.class);
        when(DBConnectionManager.getConnection()).thenReturn(DriverManager.getConnection(JDBC_URL, USER, PASSWORD));
    }
}