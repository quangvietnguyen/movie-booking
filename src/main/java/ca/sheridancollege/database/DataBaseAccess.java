package ca.sheridancollege.database;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Repository;

import ca.sheridancollege.beans.Order;
import ca.sheridancollege.beans.Price;
import ca.sheridancollege.beans.User;

@Repository
public class DataBaseAccess {
	
	@Autowired
	protected NamedParameterJdbcTemplate jdbc;
	
	
	public User findUserAccount(String userName) {
		MapSqlParameterSource parameters = new MapSqlParameterSource();
		String query = "SELECT * FROM sec_user where userName=:userName";
		parameters.addValue("userName", userName);
		ArrayList<User> users = (ArrayList<User>)jdbc.query(query, parameters,
				new BeanPropertyRowMapper<User>(User.class));
		if (users.size()>0)
			return users.get(0);
		else
			return null;
	}
	
	
	public List<String> getRolesById(long userId) {
		ArrayList<String> roles = new ArrayList<String>();
		MapSqlParameterSource parameters = new MapSqlParameterSource();
		String query = "select user_role.userId, sec_role.roleName "
				+ "FROM user_role, sec_role "
				+ "WHERE user_role.roleId=sec_role.roleId "
				+ "and userId=:userId";
		parameters.addValue("userId", userId);
		List<Map<String, Object>> rows = jdbc.queryForList(query, parameters);
		for (Map<String, Object> row : rows) {
			roles.add((String)row.get("roleName"));
		}
		return roles;
	}
	
	@Autowired
	private BCryptPasswordEncoder passwordEncoder;

	public void addUser(String userName, String password) {
		MapSqlParameterSource parameters = new MapSqlParameterSource();
		String query = "insert into SEC_User " 
				+ "(userName, encryptedPassword, ENABLED)" 
				+ " values (:userName, :encryptedPassword, 1)";
		parameters.addValue("userName", userName);	
		parameters.addValue("encryptedPassword",
				passwordEncoder.encode(password));
		jdbc.update(query, parameters);
	
	}
	
	public void addRole(long userId, long roleId) {
		MapSqlParameterSource parameters = new MapSqlParameterSource();
		String query = "insert into user_role (userId, roleId)" 
				+ "values (:userId, :roleId);";
		parameters.addValue("userId", userId);
		parameters.addValue("roleId", roleId);
		jdbc.update(query, parameters);
	}
	
	public boolean findMovieOrdered(String movieName, String movieDate, String movieTime, int movieSeat) {
		MapSqlParameterSource parameters = new MapSqlParameterSource();
		String query = "SELECT * FROM movie where movieName=:movieName and movieDate=:movieDate and movieTime=:movieTime and movieSeat=:movieSeat;";
		parameters.addValue("movieName", movieName);
		parameters.addValue("movieDate", movieDate);
		parameters.addValue("movieTime", movieTime);
		parameters.addValue("movieSeat", movieSeat);
		ArrayList<Order> orders = (ArrayList<Order>)jdbc.query(query, parameters,
				new BeanPropertyRowMapper<Order>(Order.class));
		if (orders.size() == 0)
			return true;
		else
			return false;
	}
	
	public void addOrder(String movieName, String movieDate, String movieTime, int movieSeat) {
		MapSqlParameterSource parameters = new MapSqlParameterSource();
		String query = "insert into movie (movieName, movieDate, movieTime, movieSeat)" 
				+ "values (:movieName, :movieDate, :movieTime, :movieSeat);";
		parameters.addValue("movieName", movieName);
		parameters.addValue("movieDate", movieDate);
		parameters.addValue("movieTime", movieTime);
		parameters.addValue("movieSeat", movieSeat);
		jdbc.update(query, parameters);
	}	
	/**---------------CURD------------------------------------------------*/
	
	public void deleteOrder(long orderId) {
		MapSqlParameterSource parameters = new MapSqlParameterSource();
		String query = "delete from movie where orderId=:orderId";
		parameters.addValue("orderId", orderId);
		jdbc.update(query, parameters);
	}
	
	public List<Map<String, Object>> viewTable(long userId) {
		//ArrayList<String> roles = new ArrayList<String>();
		MapSqlParameterSource parameters = new MapSqlParameterSource();
		String query = "select * from movie";
		parameters.addValue("userId", userId);
		List<Map<String, Object>> rows = jdbc.queryForList(query, parameters);
		return rows;
	}
}
