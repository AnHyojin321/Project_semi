package com.kh.order.controller;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.kh.order.model.service.OrderService;

/**
 * Servlet implementation class UpdateOrderStatus
 */
@WebServlet("/updateOrderStatus.me")
public class updateOrderStatus extends HttpServlet {
    private static final long serialVersionUID = 1L;

    /**
     * @see HttpServlet#HttpServlet()
     */
    public updateOrderStatus() {
        super();
    }

    /**
     * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.sendError(HttpServletResponse.SC_METHOD_NOT_ALLOWED, "GET 메소드는 지원하지 않습니다.");
    }

    /**
     * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
     */
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // 요청 인코딩 설정
        request.setCharacterEncoding("UTF-8");

        // 파라미터 수신
        String orderIdParam = request.getParameter("orderId");
        String statusParam = request.getParameter("status");

        // 파라미터 유효성 검사
        if (orderIdParam == null || statusParam == null) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "필수 파라미터가 누락되었습니다.");
            return;
        }

        int orderId;
        int status;
        try {
            orderId = Integer.parseInt(orderIdParam);
            status = Integer.parseInt(statusParam);
        } catch (NumberFormatException e) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "잘못된 파라미터 형식입니다.");
            return;
        }

        // 서비스 객체 생성
        OrderService orderService = new OrderService();

        // 배송 상태 업데이트 시도
        boolean isUpdated = orderService.updateOrderStatus(orderId, status);

        // 응답 출력
        response.setContentType("text/plain; charset=UTF-8");
        PrintWriter out = response.getWriter();
        out.print(isUpdated ? "성공" : "실패");
        out.close();
    }
}
