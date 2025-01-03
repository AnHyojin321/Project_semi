package com.kh.member.controller;

import java.io.IOException;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.kh.member.model.service.WishlistService;
import com.kh.member.model.vo.HeartProduct;
// 안효진 부분
/**
 * Servlet implementation class HeartProducController
 */
@WebServlet("/heartProduct.hp")
public class HeartProducController extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public HeartProducController() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		int userNo = Integer.parseInt(request.getParameter("userNo"));
		

		boolean isWishlisted = new WishlistService().isProductWishlisted(userNo); // 찜 여부 확인

		request.setAttribute("isWishlisted", isWishlisted);
		
		// isWishlisted 잘 받아오는지 확인
		System.out.println("여기는 컨트롤러" + isWishlisted);
		
		ArrayList<HeartProduct> list = new WishlistService().selectWishlist(userNo);
		
		request.setAttribute("list", list);
		
		
		request.getRequestDispatcher("/views/member/heartProduct.jsp").forward(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
