package com.kh.order.controller;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import com.kh.member.model.vo.Member;

/**
 * Servlet implementation class KakaoPayController
 */
@WebServlet("/kakaoPay.do")
public class KakaoPayController extends HttpServlet {
    private static final long serialVersionUID = 1L;

    public KakaoPayController() {
        super();
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        request.setCharacterEncoding("UTF-8");

        // 가격 확인
        String priceStr = request.getParameter("price");
        int price = 0;

        if (priceStr != null) {
            try {
                price = Integer.parseInt(priceStr);
                System.out.println("전송된 가격: " + price);
            } catch (NumberFormatException e) {
                response.getWriter().println("가격을 올바르게 입력해주세요.");
                return;
            }
        } else {
            response.getWriter().println("가격이 전달되지 않았습니다.");
            return;
        }

        // 배송 요청 사항 받기
        String deliveryRequest = request.getParameter("deliveryRequest");
        System.out.println("전송된 배송 요청 사항: " + deliveryRequest);
        
        if (deliveryRequest == null || deliveryRequest.isEmpty()) {
            deliveryRequest = "선택하지 않음"; // 기본값 설정 (선택 안 했을 경우)
        }
        session.setAttribute("deliveryRequest", deliveryRequest);
        System.out.println("배송 요청 사항: " + deliveryRequest);
        
        // 상품 번호를 요청 파라미터에서 가져옴
        String productNo = request.getParameter("productNo");
        
        // 상품 번호가 null이 아니면 세션에 저장
        if (productNo != null) {
            session.setAttribute("productNo", productNo);
            System.out.println("상품 번호: " + productNo); // 로그로 확인
        } else {
            response.getWriter().println("상품 번호가 전달되지 않았습니다.");
            return;
        }

        // 로그인한 사용자 정보를 세션에서 가져오기
        Member loginUser = (Member) session.getAttribute("loginUser");
        String partnerUserId;

        if (loginUser != null) {
            // 로그인된 사용자 ID 가져오기
            partnerUserId = loginUser.getUserId();
        } else {
            // 로그인하지 않은 경우 기본 사용자 ID 설정
            partnerUserId = "default_user_id";
        }

        // partner_user_id 세션에 저장
        session.setAttribute("partner_user_id", partnerUserId);
        System.out.println("사용자 ID: " + partnerUserId); // 디버깅을 위한 로그

        String apiUrl = "https://kapi.kakao.com/v1/payment/ready";
        String adminKey = "3cf90ec67e8e09bae787a5b2e11a21f0"; // 카카오 관리자 키를 입력하세요

        try {
            URL url = new URL(apiUrl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Authorization", "KakaoAK " + adminKey);
            conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded;charset=UTF-8");

            // 요청 파라미터 설정
            String params = "cid=TC0ONETIME" +
                    "&partner_order_id=order_id_1234" +
                    "&partner_user_id=" + URLEncoder.encode(partnerUserId, "UTF-8") +
                    "&item_name=Sample Item" +
                    "&quantity=1" +
                    "&total_amount=" + price +
                    "&vat_amount=0" +
                    "&tax_free_amount=0" +
                    "&approval_url=http://localhost:8090/mall/views/api/kakaoPaySuccess.jsp?totalPrice=" + price +
                    "&fail_url=http://localhost:8090/mall/views/api/kakaoPaySuccess.jsp" +
                    "&cancel_url=http://localhost:8090/mall/views/api/kakaoPaySuccess.jsp" +
                    "&delivery_request=" + URLEncoder.encode(deliveryRequest, "UTF-8"); // 인코딩 추가

            conn.setDoOutput(true);
            try (DataOutputStream dos = new DataOutputStream(conn.getOutputStream())) {
                dos.writeBytes(params);
                dos.flush();
            }

            // 응답 코드 확인
            int responseCode = conn.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                InputStream is = conn.getInputStream();
                BufferedReader br = new BufferedReader(new InputStreamReader(is, "UTF-8"));
                StringBuilder sb = new StringBuilder();
                String line;
                while ((line = br.readLine()) != null) {
                    sb.append(line);
                }
                br.close();

                JSONParser parser = new JSONParser();
                JSONObject responseJson = (JSONObject) parser.parse(sb.toString());
                String tid = (String) responseJson.get("tid");
                String nextRedirectUrl = (String) responseJson.get("next_redirect_pc_url");

                System.out.println("-------");
                System.out.println("nextRedirectUrl : " + nextRedirectUrl);
                System.out.println("-------");

                if (tid != null && nextRedirectUrl != null) {
                    session.setAttribute("tid", tid); // 세션에 TID 저장
                    response.sendRedirect(nextRedirectUrl);
                } else {
                    response.getWriter().println("결제 준비에 실패했습니다. TID 또는 리다이렉트 URL이 null입니다.");
                }
            } else {
                // 오류 처리
                InputStream errorStream = conn.getErrorStream();
                BufferedReader errorReader = new BufferedReader(new InputStreamReader(errorStream, "UTF-8"));
                StringBuilder errorResponse = new StringBuilder();
                String errorLine;
                while ((errorLine = errorReader.readLine()) != null) {
                    errorResponse.append(errorLine);
                }
                errorReader.close();

                response.getWriter().println("결제 준비 중 오류가 발생했습니다. 응답 코드: " + responseCode);
                response.getWriter().println("응답 내용: " + errorResponse.toString());
            }
        } catch (Exception e) {
            response.getWriter().println("오류 발생: " + e.getMessage());
            e.printStackTrace(response.getWriter());
        }
    }
    
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }
}
