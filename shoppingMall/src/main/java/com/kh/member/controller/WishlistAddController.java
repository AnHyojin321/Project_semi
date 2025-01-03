package com.kh.member.controller;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.kh.member.model.service.WishlistService;
import com.kh.member.model.vo.HeartProduct;

/**
 * Servlet implementation class WishlistAddController
 */
@WebServlet("/wishlist-add.ad")
public class WishlistAddController extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public WishlistAddController() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		request.setCharacterEncoding("UTF-8");
		
		System.out.println("잘호출됨");
		
		
		
		int productNo = Integer.parseInt(request.getParameter("productNo"));
		
		int userNo = Integer.parseInt(request.getParameter("userNo"));
		
		// 회원번호랑 상품번호 잘 가져오는지 확인
		System.out.println(productNo);
		System.out.println(userNo);
		
		HeartProduct wishlist = new HeartProduct(userNo, productNo);
		
		
		int result = new WishlistService().insertWishlist(wishlist);
		
		response.setContentType("application/json; charset=UTF-8");
	    PrintWriter out = response.getWriter();
		
		if(result > 0) {
			System.out.println("찜상품 추가 성공");
			out.write("{\"status\":\"success\"}");  // 성공 시 JSON 응답
		}else {
			System.out.println("찜상품 추가 실패");
		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
