package com.ytsquare.push2me.server;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.UUID;

import javax.imageio.ImageIO;

import com.ytsquare.push2me.entity.Message;
import com.ytsquare.push2me.entity.User;
import com.ytsquare.push2me.message.ErrorMessage;
import com.ytsquare.push2me.message.ResponseMessage;

public class test {
	private static void testRegisterUser() {
		String driver = "com.mysql.jdbc.Driver";
		String url = "jdbc:mysql://localhost:3306/ytsquare?autoReconnect=true";

		Connection conn = null;
		PreparedStatement stmt = null;

		try {
			Class.forName(driver);
			conn = DriverManager.getConnection(url, "ytsquare", "ytsquare");
			
			stmt = conn.prepareStatement(DBUtils.REGISTER_USER);
			// set userid(email)
			stmt.setString(1, "6");
			// set password
			stmt.setString(2, "6");
			stmt.setString(3, "6");
			stmt.setString(4, "6");
			stmt.setString(5, "6");
			InputStream image = null;
			   try{
				   
				   BufferedImage originalImage = ImageIO.read(new File("/home/n200/push2me.png"));
				 
					ByteArrayOutputStream baos = new ByteArrayOutputStream();
					ImageIO.write(originalImage, "png", baos);
					baos.flush();
					byte[] imageInByte = baos.toByteArray();
					baos.close();
					image = new ByteArrayInputStream(imageInByte);
					}catch(Exception e){
						System.out.println(e.getMessage());
					}	
			   
			stmt.setBinaryStream(6, image);
			stmt.setString(7, "6");
			stmt.setInt(8, 6);
			// set birthday, format yyyy-mm-dd
//			java.util.Date date1 = new SimpleDateFormat("yyyy-MM-dd").parse("2012-04-25");
//			stmt.setDate(9, new java.sql.Date(date1.getTime()));
			stmt.setString(9, "2012-04-25");
			stmt.setInt(10, 6);
			
			int i = stmt.executeUpdate();					
			System.out.println("4");
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (stmt != null) {
				try {
					stmt.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			if (conn != null) {
				try {
					conn.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}		
	}

	private static void testAddFriend() {
		String driver = "com.mysql.jdbc.Driver";
		String url = "jdbc:mysql://localhost:3306/ytsquare?autoReconnect=true";

		Connection conn = null;
		PreparedStatement stmt = null;

		try {
			Class.forName(driver);
			conn = DriverManager.getConnection(url, "ytsquare", "ytsquare");
			
			stmt = conn.prepareStatement(DBUtils.ADD_FRIEND);
			stmt.setString(1, "1");
			stmt.setString(2, "4");
			stmt.setString(3, "0");	
			if (stmt.executeUpdate() > 0) {
				System.out.println("succeed");
			}
			else {
				System.out.println("failed");
			}
			System.out.println("4");
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (stmt != null) {
				try {
					stmt.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			if (conn != null) {
				try {
					conn.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}		
	}
	
	private static void testGetAllFriends() {
		String driver = "com.mysql.jdbc.Driver";
		String url = "jdbc:mysql://localhost:3306/ytsquare?autoReconnect=true";

		Connection conn = null;
		PreparedStatement stmt = null;

		try {
			Class.forName(driver);
			conn = DriverManager.getConnection(url, "ytsquare", "ytsquare");
			
			stmt = conn.prepareStatement(DBUtils.GET_ALL_FRIENDS);
			stmt.setString(1, "1");
			ResultSet rs = stmt.executeQuery();
			while (rs.next()) {
				User user = new User();
				user.setUserId(rs.getString(1));
				user.setFirstname(rs.getString(3));
				user.setLastname(rs.getString(4));
				user.setNickname(rs.getString(5));
				user.setImage(rs.getBytes(6));
				user.setCountry(rs.getString(7));
				user.setSex(rs.getInt(8));
				user.setBirthday(rs.getString(9));
				user.setStatusId(rs.getInt(10));
				System.out.println(user.getBirthday());
			}
			rs.close();
			System.out.println("4");
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (stmt != null) {
				try {
					stmt.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			if (conn != null) {
				try {
					conn.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}		
	}

	private static void testSentMessage() {
		String driver = "com.mysql.jdbc.Driver";
		String url = "jdbc:mysql://localhost:3306/ytsquare?autoReconnect=true";

		Connection conn = null;
		PreparedStatement stmt1 = null;
		PreparedStatement stmt2 = null;

		try {
			Class.forName(driver);
			conn = DriverManager.getConnection(url, "ytsquare", "ytsquare");
			conn.setAutoCommit(false);
			// use batch statement
			stmt1 = conn.prepareStatement(DBUtils.ADD_MESSAGE);
			// message Id
			String messageId = UUID.randomUUID().toString();
			stmt1.setString(1, messageId);
			stmt1.setString(2, "test");
			stmt1.setString(3, "test");
			stmt1.setString(4, new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
			stmt1.setString(5, "1"); // from
			stmt1.setInt(6, 5);
			if (stmt1.executeUpdate() > 0) {
				System.out.println("succeed");
			}
			else {
				System.out.println("failed");
			}
			
			stmt2 = conn.prepareStatement(DBUtils.SENT_MESSAGE_TO);
			for (int i = 2; i<5; i++) {
				stmt2.setString(1, Integer.toString(i));
				stmt2.setString(2, messageId);
				stmt2.setInt(3, Message.STATUS_UNHANDLE);
				stmt2.addBatch("ggg");
		    }
		    stmt2.executeBatch();
		    conn.commit();		    
		} catch (Exception e) {
			if (conn != null) {
                try {
                	conn.rollback();
                } catch (SQLException ex1) {
                }
            }
			e.printStackTrace();
		} finally {
			if (stmt1 != null) {
				try {
					stmt1.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			if (stmt2 != null) {
				try {
					stmt2.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			if (conn != null) {
				try {
					conn.setAutoCommit(true);
					conn.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}		
	}
	
	public static void main(String[] args) {
		testSentMessage();
	}
}
