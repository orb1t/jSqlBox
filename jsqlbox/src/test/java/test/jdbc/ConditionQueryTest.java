package test.jdbc;

import static com.github.drinkjava2.jsqlbox.SqlHelper.q;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.github.drinkjava2.jsqlbox.Dao;
import com.github.drinkjava2.jsqlbox.SqlBox;

import test.config.InitializeDatabase;
import test.config.po.User;

public class ConditionQueryTest {
	@Before
	public void setup() {
		InitializeDatabase.recreateTables();
		User u = SqlBox.createBean(User.class);
		u.setUserName("User1");
		u.setAddress("Address1");
		u.setAge(10);
		u.dao().save();
	}

	public int conditionQuery(int condition, Object parameter) {
		User u = SqlBox.createBean(User.class);
		String sql = "Select count(*) from " + u.Table() + " where ";
		if (condition == 1 || condition == 3)
			sql = sql + u.UserName() + "=" + q(parameter) + " and " + u.Address() + "=" + q("Address1");

		if (condition == 2)
			sql = sql + u.UserName() + "=" + q(parameter);

		if (condition == 3)
			sql = sql + " or " + u.Age() + "=" + q(parameter);

		return Dao.dao().queryForInteger(sql);
	}

	@Test
	public void doJdbcConditionQuery() {
		Assert.assertEquals(1, conditionQuery(1, "User1"));
		Assert.assertEquals(0, conditionQuery(2, "User does not exist"));
		Assert.assertEquals(1, conditionQuery(3, 10));
		Assert.assertEquals(0, conditionQuery(3, 20));
	}

	@After
	public void cleanup() {

	}

}