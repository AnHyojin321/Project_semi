package com.kh.cart.model.dao;

import static com.kh.common.JDBCTemplate.*;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Properties;

import com.kh.cart.model.vo.Cart;

public class CartDao {
	
	private Properties prop = new Properties();
	
	// 공통코드 - 쿼리문들을 키 + 밸류 세트로 불러오기
	public CartDao() {
	
		String path = "/sql/cart/cart-mapper.xml";
		
		String fileName = CartDao.class.getResource(path).getPath();
		
		try {
			prop.loadFromXML(new FileInputStream(fileName));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 2024.10.28
	 * 장바구니 상품 갯수를 구하는 쿼리문 실행용 메소드
	 * @param conn
	 * @return
	 */
	public int selectListCount(Connection conn) {
		
		int listCount = 0;
		PreparedStatement pstmt = null;
		ResultSet rset = null;
		
		String sql = prop.getProperty("selectListCount");
		
		try {
			
			pstmt = conn.prepareStatement(sql);
			
			rset = pstmt.executeQuery();
			
			if(rset.next()) {
				
				listCount = rset.getInt("COUNT");
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			
			close(rset);
			close(pstmt);
		}
		
		return listCount;
	}

	/**
	 * 2024.10.28
	 * 사용자별 장바구니 조회용 쿼리문 실행 메소드
	 * @param conn
	 * @param c
	 * @return
	 */
	public ArrayList<Cart> getCartList(Connection conn, int userNo){
		
		ArrayList<Cart> list = new ArrayList<>();
		PreparedStatement pstmt = null;
		ResultSet rset = null;
		
		String sql = prop.getProperty("getCartList");
		
		try {
			
			pstmt = conn.prepareStatement(sql);
			
			pstmt.setInt(1, userNo);
			
			rset = pstmt.executeQuery();
			
			while(rset.next()) {
				
				list.add(new Cart(rset.getInt("CART_NO"),
								  rset.getInt("PRODUCT_QUANTITY"),
								  rset.getInt("TOTAL_PRICE"),
								  rset.getInt("USER_NO"),
								  rset.getInt("PRODUCT_NO"),
								  rset.getString("PRODUCT_NAME"),
				                  rset.getInt("PRICE"),
				                  rset.getInt("DISCOUNT"),
				                  rset.getInt("DISCOUNT_PRICE"),
				                  rset.getInt("TOTAL_DISCOUNT_PRICE"),
				                  rset.getString("TITLEIMG")));
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			
			close(rset);
			close(pstmt);
		}
		
		return list;
	}

	/**
	 * 2024.11.01
	 * 장바구니 삭품 삭제용 쿼리문 실행 메소드
	 * @param conn
	 * @param cartNo
	 * @return
	 */
	public int deleteCartItem (Connection conn, int cartNo) {
		
		int result = 0;
		PreparedStatement pstmt = null;
		
		String sql = prop.getProperty("deleteCartItem");
		
		try {
			
			pstmt = conn.prepareStatement(sql);
			
			pstmt.setInt(1, cartNo);
			
			result = pstmt.executeUpdate();
			
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			
			close(pstmt);
		}
		
		return result;
	}
	
	
	public int addToCart(Connection conn, int userNo, int productNo, int quantity) {
	    int result = 0;
	    PreparedStatement pstmt = null;

	    String sql = prop.getProperty("addToCart"); // 쿼리문 확인

	    try {
	        pstmt = conn.prepareStatement(sql);
	        
	        int totalPrice = getPriceFromDatabase(productNo) * quantity; // 총 가격 계산
	        
	        pstmt.setInt(1, userNo);            // 사용자 번호
	        pstmt.setInt(2, productNo);         // 상품 번호
	        pstmt.setInt(3, quantity);          // 상품 수량
	        pstmt.setInt(4, totalPrice);        // 총 가격 설정
	        
	        result = pstmt.executeUpdate();

	    } catch (SQLException e) {
	        e.printStackTrace();
	    } finally {
	        close(pstmt);
	    }
	    
	    return result;
	}

	 
	
	private int getPriceFromDatabase(int productNo) {
	    int price = 0;
	    Connection conn = null;
	    PreparedStatement pstmt = null;
	    ResultSet rset = null;

	    String sql = "SELECT PRICE FROM PRODUCT_INFO WHERE PRODUCT_NO = ?"; // 제품 가격을 조회하는 SQL

	    try {
	        conn = getConnection();
	        pstmt = conn.prepareStatement(sql);
	        pstmt.setInt(1, productNo);
	        
	        rset = pstmt.executeQuery();
	        if (rset.next()) {
	            price = rset.getInt("PRICE");
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	    } finally {
	        close(rset);
	        close(pstmt);
	        close(conn);
	    }
	    
	    return price;
	}

	
}
