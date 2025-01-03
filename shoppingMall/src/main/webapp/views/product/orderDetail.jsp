<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.kh.product.model.vo.*, com.kh.product.model.service.ProductInfoService" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="com.kh.member.model.vo.Member" %>

<%
    // 세션에서 로그인한 사용자 정보 가져오기
    Member loginUser = (Member) session.getAttribute("loginUser");

    // 사용자 정보가 없으면 로그인 페이지로 리다이렉트
    if (loginUser == null) {
        out.println("<script>alert('로그인이 필요합니다.'); history.go(-1);</script>");
        return;
    }
    
    // ProductNo를 URL 파라미터로 받아옴
    String productNoStr = request.getParameter("productNo");
    int productNo = Integer.parseInt(productNoStr);

    ProductInfoService productService = new ProductInfoService();

    // 상품 정보와 이미지 목록 조회
    ProductInfo product = productService.selectProduct(productNo);
    ArrayList<ProductImage> images = productService.selectProductImg(productNo);

    // 상품 정보가 없을 경우 처리
    if (product == null) {
        out.println("<script>alert('상품 정보가 없습니다.'); history.back();</script>");
        return;
    }
%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>배송 및 결제</title>
   <style>
        body {
            font-family: 'Arial', sans-serif;
            margin: 0;
            padding: 0;
            background-color: #f8f9fa;
        }
        .order-container {
            max-width: 900px;
            margin: 40px auto;
            padding: 30px;
            background: #fff;
            border-radius: 8px;
            box-shadow: 0 2px 10px rgba(0,0,0,0.1);
        }
        h1 {
            font-size: 24px;
            font-weight: bold;
            color: #333;
        }
        .section-title {
            font-size: 18px;
            font-weight: bold;
            border-bottom: 1px solid #ddd;
            padding-bottom: 8px;
            margin-bottom: 20px;
        }
        .address-section, .order-summary, .payment-section {
            margin-bottom: 20px;
        }
        .product-item {
            display: flex;
            align-items: center;
            margin-bottom: 15px;
        }
        .product-image {
            width: 80px;
            height: 80px;
            border-radius: 4px;
            margin-right: 15px;
            border: 1px solid #ddd;
        }
        .product-details {
            flex: 1;
        }
        .product-quantity {
            display: flex;
            align-items: center;
        }
        .product-quantity input {
            width: 50px;
            text-align: center;
            margin: 0 5px;
        }
        .total-summary {
            border-top: 1px solid #ddd;
            padding-top: 10px;
        }
        .total-summary p {
            font-weight: bold;
            font-size: 18px;
            margin: 10px 0;
        }
        .button-container {
            display: flex;
            justify-content: center;
            margin-top: 20px;
        }
        .pay-button {
            padding: 15px 30px;
            font-size: 16px;
            background-color: #000;
            color: #fff;
            border: none;
            border-radius: 4px;
            cursor: pointer;
        }
        .pay-button:hover {
            background-color: #333;
        }
    </style>
</head>
<body>

<div class="order-container">
    <h1>배송 및 결제</h1>
    
    <div class="address-section">
        <div class="section-title">배송 주소</div>
        <p>받는 분: <%= loginUser.getUserName() %></p>
        <p>연락처: <%= loginUser.getPhone() %></p>
        <p>주소: <%= loginUser.getAddress() %></p>
    </div>

    <div class="order-summary">
        <div class="section-title">주문 상품</div>
        
        <div class="product-item">
            <img src="/admin/<%= images.get(0).getImageUrl() + images.get(0).getImgSaveFile() %>" class="product-image" alt="상품 이미지">
            <div class="product-details">
                <p><strong><%= product.getProductName() %></strong></p>
                <p>가격: <%= product.getPrice() %>원</p>
                <p>할인율: <%= product.getDiscount() %>%</p>
                <div class="product-quantity">
                    <button>-</button>
                    <input type="text" value="1">
                    <button>+</button>
                </div>
            </div>
            <div class="product-price">
                <p>배송비: ￦80,000</p>
                <p>총 금액: <%= product.getDiscountPrice() %>원</p>
            </div>
        </div>
    </div>

    <form action="<%= request.getContextPath() %>/kakaoPay.do" method="post" accept-charset="UTF-8">
    <div class="payment-section">
        <div class="section-title">요청 사항</div>
           <textarea name="deliveryRequest"></textarea> <!-- 배송 요청 사항 입력 -->

        <input type="hidden" name="productNo" value="<%= product.getProductNo() %>">
        <input type="hidden" name="price" value="<%= product.getDiscountPrice() %>">
    </div>     
    <div class="button-container">
        <button type="submit" class="pay-button">
            <%= product.getDiscountPrice() %>원 결제하기
        </button>
    </div>
</form>

</div>
</body>
</html>
