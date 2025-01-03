package com.kh.member.model.dao;

import static com.kh.common.JDBCTemplate.close;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Properties;

import com.kh.member.model.vo.HeartProduct;

public class WishlistDao {
	
	 private static Properties prop = new Properties();

	    public WishlistDao() {
	        // member-mapper.xml 파일로부터 query문들을 key + value 세트로 읽어서 담아둘 것 
	        String path = "/sql/member/wishlist-mapper.xml";
	        String fileName = MemberDao.class.getResource(path).getPath();
	        try {
	            prop.loadFromXML(new FileInputStream(fileName));
	        } catch (IOException e) {
	            e.printStackTrace();
	        }
	    }
	

	/**
	 * 찜상품 추가 쿼리문 실행 메소드
	 * @param conn
	 * @param wishlist
	 * @return
	 */
	public int insertWishlist(Connection conn, HeartProduct wishlist) {
		
		int result = 0;
		PreparedStatement pstmt = null;
		
		String sql = prop.getProperty("insertWishlist");
		
		try {
			pstmt = conn.prepareStatement(sql);
			
			pstmt.setInt(1, wishlist.getUserNo());
			pstmt.setInt(2, wishlist.getProductNo());
			
			result = pstmt.executeUpdate();
			
			
			
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			close(pstmt);
		}
		
		return result;
		
	}  // insertWishlist 메소드 영역 끝
	
	


	/**
	 * 찜 상품 해제 쿼리문 실행 메소드
	 * @param conn
	 * @param wishlist
	 * @return
	 */
	public int removeWishlist(Connection conn, HeartProduct wishlist) {
		
		int result = 0;
		PreparedStatement pstmt = null;
		
		String sql = prop.getProperty("removeWishlist");
		
		try {
			pstmt = conn.prepareStatement(sql);
			
			pstmt.setInt(1, wishlist.getUserNo());
			pstmt.setInt(2, wishlist.getProductNo());
			
			result = pstmt.executeUpdate();
			
			
			 System.out.println("삭제 쿼리 실행됨, result: " + result);
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			close(pstmt);
		}
		
		return result;
		
	}  // removeWishlist 메소드 영역 끝


	/**
	 * 찜상품 목록 조회용 쿼리문 실행 메소드
	 * @param conn
	 * @return
	 */
	public ArrayList<HeartProduct> selectWishlist(Connection conn, int userNo) {
		
		ArrayList<HeartProduct> list = new ArrayList<>();
		PreparedStatement pstmt = null;
		ResultSet rset = null;
		
		String sql = prop.getProperty("selectWishlist");
		
		try {
			pstmt = conn.prepareStatement(sql);
			
			pstmt.setInt(1, userNo);
			
			rset = pstmt.executeQuery();
			
			while(rset.next()) {
				
				HeartProduct h = new HeartProduct();
				h.setProductNo(rset.getInt("PRODUCT_NO"));
				h.setProductName(rset.getString("PRODUCT_NAME"));
				h.setPrice(rset.getInt("PRICE"));
				h.setTitleImg(rset.getString("TITLEIMG"));
				
				list.add(h);
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		}finally {
			close(rset);
			close(pstmt);
		}
		return list;
		
				
		
	}


	/**
	 * 찜 상품 확인 여부 조회용 쿼리문 실행 메소드
	 * @param userNo
	 * @param productNo
	 * @return
	 */
	public static boolean isProductWishlisted(Connection conn, int userNo) {
		
		boolean isWishlisted = false;
		PreparedStatement pstmt = null;
		ResultSet rset = null;
		
		String sql = prop.getProperty("isProductWishlisted");
		
		try {
			pstmt = conn.prepareStatement(sql);
			
			HeartProduct h = new HeartProduct();
			
			pstmt.setInt(1, userNo);
			pstmt.setInt(2, h.getProductNo());
			
			rset = pstmt.executeQuery();
			
			if(rset.next()) {
				 isWishlisted = rset.getInt(1) > 0;
			}
			
			System.out.println("여기는 dao"+isWishlisted);
		} catch (SQLException e) {

			e.printStackTrace();
		}finally {
			close(rset);
			close(pstmt);
		}
		return isWishlisted;
	}

	
	
	
	
	
	
	
	
	
	
}
