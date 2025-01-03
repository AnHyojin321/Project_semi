package com.kh.cart.model.service;

import static com.kh.common.JDBCTemplate.close;
import static com.kh.common.JDBCTemplate.commit;
import static com.kh.common.JDBCTemplate.getConnection;
import static com.kh.common.JDBCTemplate.rollback;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import com.kh.cart.model.dao.CartDao;
import com.kh.cart.model.vo.Cart;

public class CartService {
	
	/**
	 * 2024.10.28
	 * 장바구니 상품 갯수 조회용 서비스 메소드
	 * @return => 장바구니 내 상품 수
	 */
	public int selectListCount() {
		
		Connection conn = getConnection();
		
		int listCount = new CartDao().selectListCount(conn);
		
		close(conn);
		
		return listCount;
	}

	public ArrayList<Cart> getCartList(int userNo) {
	    Connection conn = getConnection(); // 여기에서 연결을 생성
	    ArrayList<Cart> list = new ArrayList<>();
	    
	    String query = "SELECT c.CART_NO, c.PRODUCT_QUANTITY, c.TOTAL_PRICE, c.USER_NO, c.PRODUCT_NO, "
	                 + "p.PRODUCT_NAME, p.PRICE, p.DISCOUNT, "
	                 + "p.PRICE * (1 - p.DISCOUNT / 100) AS DISCOUNT_PRICE, "
	                 + "c.PRODUCT_QUANTITY * (p.PRICE * (1 - p.DISCOUNT / 100)) AS TOTAL_DISCOUNT_PRICE, "
	                 + "i.IMAGE_URL || i.IMG_SAVE_FILE AS TITLEIMG "
	                 + "FROM CART c "
	                 + "JOIN PRODUCT_INFO p ON (c.PRODUCT_NO = p.PRODUCT_NO) "
	                 + "LEFT JOIN PRODUCT_IMAGE i ON p.PRODUCT_NO = i.REF_PNO "
	                 + "WHERE c.USER_NO = ? AND i.THUMBNAIL = 'Y' AND i.DELETE_YN = 'N'";

	    try (PreparedStatement pstmt = conn.prepareStatement(query)) {
	        pstmt.setInt(1, userNo);
	        ResultSet rs = pstmt.executeQuery();

	        while (rs.next()) {
	            Cart cart = new Cart();
	            cart.setCartNo(rs.getInt("CART_NO"));
	            cart.setProductQuantity(rs.getInt("PRODUCT_QUANTITY"));
	            cart.setTotalPrice(rs.getInt("TOTAL_PRICE"));
	            cart.setUserNo(rs.getInt("USER_NO"));
	            cart.setProductNo(rs.getInt("PRODUCT_NO"));
	            cart.setProductName(rs.getString("PRODUCT_NAME"));
	            cart.setPrice(rs.getInt("PRICE"));
	            cart.setDiscount(rs.getInt("DISCOUNT"));
	            cart.setDiscountPrice(rs.getInt("DISCOUNT_PRICE"));
	            cart.setTotalDiscountPrice(rs.getInt("TOTAL_DISCOUNT_PRICE"));
	            cart.setTitleImg(rs.getString("TITLEIMG"));

	            list.add(cart);
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	    } finally {
	        close(conn); // 연결 해제
	    }

	    return list;
	}

	/**
	 * 2024.11.01
	 * 장바구니 삭품 삭제용 서비스 메소드
	 * @param cartNo
	 * @return
	 */
	public int deleteCartItem(int cartNo) {
		
	    Connection conn = getConnection();
	    
	    int result = new CartDao().deleteCartItem(conn, cartNo);
	    
	    if(result > 0) {
	    	
	    	commit(conn);
	    	
	    } else {
	    	
	    	rollback(conn);
	    }
	    
	    close(conn);
	    
	    return result;
	}
	
	/**
	 * 장바구니에 상품 추가용 서비스 메소드
	 * @param userNo 사용자 번호
	 * @param productNo 상품 번호
	 * @param quantity 상품 수량
	 * @return
	 */
	public int addToCart(int userNo, int productNo, int quantity) {
	    Connection conn = getConnection();
	    
	    int result = new CartDao().addToCart(conn, userNo, productNo, quantity);
	    
	    if(result > 0) {
	        commit(conn);
	    } else {
	        rollback(conn);
	    }
	    
	    close(conn);
	    
	    return result;
	}
    
}
