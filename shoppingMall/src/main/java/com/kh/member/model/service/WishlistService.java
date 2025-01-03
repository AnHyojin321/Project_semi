package com.kh.member.model.service;

import static com.kh.common.JDBCTemplate.close;
import static com.kh.common.JDBCTemplate.commit;
import static com.kh.common.JDBCTemplate.getConnection;
import static com.kh.common.JDBCTemplate.rollback;

import java.sql.Connection;
import java.util.ArrayList;

import com.kh.member.model.dao.WishlistDao;
import com.kh.member.model.vo.HeartProduct;

public class WishlistService {

	/**
	 * 찜 상품 추가 서비스 메소드
	 * @param wishlist
	 * @return
	 */
	public int insertWishlist(HeartProduct wishlist) {
		
		Connection conn = getConnection();
		
		int result = new WishlistDao().insertWishlist(conn, wishlist);
		
		if(result > 0) {
			commit(conn);
		}else {
			rollback(conn);
		}
		return result;
		
	}  // insertWishlist 메소드 영역 끝
	
	

	/**
	 * 찜 상품 해제 서비스 메소드
	 * @param wishlist
	 * @return
	 */
	public int removeWishlist(HeartProduct wishlist) {
		Connection conn = getConnection();
		
		int result = new WishlistDao().removeWishlist(conn, wishlist);
		System.out.println("removeWishlist 메서드 호출됨, result: " + result);
		
		if(result > 0) {
			commit(conn);
		}else {
			rollback(conn);
		}
		return result;
	} // removeWishlist 메소드 영역 끝



	/**
	 * 찜 상품 목록 조회 서비스 메소드
	 * @return
	 */
	public ArrayList<HeartProduct> selectWishlist(int userNo) {
		
		Connection conn = getConnection();
		
		ArrayList<HeartProduct> list = new WishlistDao().selectWishlist(conn, userNo);
		
		close(conn);
		
		return list;
		
		
	}



	/**
	 * 찜 상품 여부 체크 서비스 메소드
	 * @param userNo
	 * @param productNo
	 * @return
	 */
	public boolean isProductWishlisted(int userNo) {
		
		Connection conn = getConnection();
		
		boolean isWishlist = WishlistDao.isProductWishlisted(conn, userNo);
		
		close(conn);
		
		return isWishlist;
		
	}

}
