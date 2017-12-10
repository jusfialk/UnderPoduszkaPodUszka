package rest;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SQLLite{

	
	public SQLLite(){};
	
	public Connection connect(){
		Connection connection = null;
		try
		{
			connection = DriverManager.getConnection("jdbc:sqlite:C:/Program Files/Apache Software Foundation/Tomcat 9.0/webapps/ROOT/sleepunderpillow.db");
		}
		catch(SQLException e)
		{
			System.out.println(e.getMessage());
		}
		return connection;	
		}

		public void insert(String gyro, String accel, String time, String userName, String city)
		{
			String sql = "INSERT INTO Measure (Gyroscope,Accelerometer,Time,UserName,City) VALUES (?,?,?,?,?)";		
		 try (Connection conn = this.connect()){
             PreparedStatement pstmt = conn.prepareStatement(sql) ;
            pstmt.setString(1, gyro);
            pstmt.setString(2, accel);
            pstmt.setString(3, time);
            pstmt.setString(4, userName);
            pstmt.setString(5, city);
            pstmt.executeUpdate();
        } 
		catch (SQLException e) 
		{
            System.out.println(e.getMessage());
        }
		
		}
		
		public List<String> getCityList (){
			String sql = "select DISTINCT City from Measure;";
			List<String> list = new ArrayList();
			try (Connection conn = this.connect()){
            Statement stmt  = conn.createStatement();
            ResultSet rs    = stmt.executeQuery(sql);
            
            // loop through the result set
            while (rs.next()) {
				list.add(rs.getString("City"));
            }
			}
			catch (SQLException e) 
			{
				System.out.println(e.getMessage());
			}
			return list;		
			
		}
			
		
		
		public List<String> getUserListOnCity (String city){
			String sql = "select DISTINCT UserName from Measure where City == '" + city + "'";
			List<String> list = new ArrayList();
			try (Connection conn = this.connect()){
            Statement stmt  = conn.createStatement();
            ResultSet rs    = stmt.executeQuery(sql);
            
            // loop through the result set
            while (rs.next()) {
				list.add(rs.getString("UserName"));
            }
			}
			catch (SQLException e) 
			{
				System.out.println(e.getMessage());
			}
			
			return list;
		}
		
		public List<String> getDates(String city, String userName){
			String sql = "select Time from Measure where City == '" + city + "' AND UserName == '" + userName + "'";
			List<String> list = new ArrayList();
			try (Connection conn = this.connect()){
            Statement stmt  = conn.createStatement();
            ResultSet rs    = stmt.executeQuery(sql);
            
            // loop through the result set
            while (rs.next()) {
				String dateTime = rs.getString("Time");
				String[] time = dateTime.split("\\s+");
				if(!list.contains(time[0]))
				{
				list.add(time[0]);
				}
            }
			}
			catch (SQLException e) 
			{
				System.out.println(e.getMessage());
			}
			return list;
		}
		
		public List<DataModel> getDataSets(String city, String userName, String time){
			String sql = "select* from Measure where City == '" + city + "' AND UserName == '" + userName + "' AND Time like '%" + time + "%'";		
			List<DataModel> list = new ArrayList();
			try (Connection conn = this.connect()){
            Statement stmt  = conn.createStatement();
            ResultSet rs    = stmt.executeQuery(sql);
            
            // loop through the result set
            while (rs.next()) {				
				String dateTime = rs.getString("Time");
				String[] timeHourMin = dateTime.split("\\s+");				
				list.add(new DataModel(rs.getString("Gyroscope"), rs.getString("Accelerometer"), timeHourMin[1], rs.getString("UserName"), rs.getString("City")));	
            }
			}
			catch (SQLException e) 
			{
				System.out.println(e.getMessage());
			}
			return list;			
		}
		
			
}