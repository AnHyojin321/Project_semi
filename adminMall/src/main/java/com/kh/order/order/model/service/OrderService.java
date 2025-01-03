package com.kh.order.model.service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

import com.kh.order.model.vo.Order;
import com.kh.common.JDBCTemplate; // JDBC 템플릿 예시

public class OrderService {
    public ArrayList<Order> getAllOrders() {
        ArrayList<Order> orderList = new ArrayList<>();
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        // orders 테이블과 product_info 테이블을 조인하는 쿼리
        String query = "SELECT o.ORDER_NO, o.TOTAL_PRICE, o.RECIPIENT, o.ADDRESS, o.PHONE, o.STATUS, " +
                       "o.RES_REQUIREMENT, o.PAY_CODE, o.ORDER_ENROLL, o.REFUND_DATE, o.USER_ID, " + // 주문 날짜 추가
                       "p.PRODUCT_NAME " + // 상품 이름 추가
                       "FROM ORDERS o " +
                       "JOIN PRODUCT_INFO p ON o.PRODUCT_NO = p.PRODUCT_NO"; // 상품 번호 기준 조인

        try {
            conn = JDBCTemplate.getConnection();
            pstmt = conn.prepareStatement(query);
            rs = pstmt.executeQuery();

            while (rs.next()) {
                Order order = new Order();
                order.setOrderNo(rs.getInt("ORDER_NO"));
                order.setTotalPrice(rs.getInt("TOTAL_PRICE"));
                order.setRecipient(rs.getString("RECIPIENT"));
                order.setAddress(rs.getString("ADDRESS"));
                order.setPhone(rs.getString("PHONE"));
                order.setStatus(rs.getInt("STATUS"));
                order.setResRequirement(rs.getString("RES_REQUIREMENT"));
                order.setPayCode(rs.getString("PAY_CODE"));
                order.setOrderEnroll(rs.getDate("ORDER_ENROLL")); // 주문 날짜
                order.setRefundDate(rs.getDate("REFUND_DATE")); // 환불 날짜
                order.setUserId(rs.getString("USER_ID"));
                order.setProductName(rs.getString("PRODUCT_NAME")); // 상품 이름

                orderList.add(order);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // 리소스를 안전하게 닫기
            JDBCTemplate.close(rs, pstmt, conn);
        }
        return orderList;
    }

    // 배송 상태를 업데이트하는 메서드 추가
    public boolean updateOrderStatus(int orderId, int status) {
        Connection conn = null;
        PreparedStatement pstmt = null;
        boolean isUpdated = false;

        String query = "UPDATE ORDERS SET STATUS = ? WHERE ORDER_NO = ?";

        try {
            conn = JDBCTemplate.getConnection();
            pstmt = conn.prepareStatement(query);
            pstmt.setInt(1, status);
            pstmt.setInt(2, orderId);
            int result = pstmt.executeUpdate();

            if (result > 0) {
                isUpdated = true;
                JDBCTemplate.commit(conn); // 성공적으로 업데이트하면 커밋
            } else {
                JDBCTemplate.rollback(conn); // 실패 시 롤백
            }
        } catch (Exception e) {
            e.printStackTrace();
            JDBCTemplate.rollback(conn); // 예외 발생 시 롤백
        } finally {
            JDBCTemplate.close(pstmt);
            JDBCTemplate.close(conn);
        }
        return isUpdated;
    }
}
