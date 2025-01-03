<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="com.kh.product.model.vo.*, 
				  java.util.ArrayList" %>
<%
	ProductInfo p = (ProductInfo)request.getAttribute("p");

	ArrayList<ProductImage> pImg = (ArrayList<ProductImage>)request.getAttribute("pImg");
%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
<link href="https://cdn.jsdelivr.net/npm/bootstrap@5.2.3/dist/css/bootstrap.min.css" rel="stylesheet">

<style>
    body {
    background-color: #f8f9fa; /* 연한 회색 배경 */
}

h1 {
    color: #343a40; /* 어두운 색상 */
}

.form-control,
.form-select {
    border-radius: 0.5rem; /* 테두리 둥글게 */
}

.btn-primary {
    background-color: #007bff; /* 기본 버튼 색상 */
    border-color: #007bff; /* 버튼 테두리 색상 */
}

.btn-primary:hover {
    background-color: #0056b3; /* 버튼 호버 시 색상 */
    border-color: #0056b3; /* 버튼 호버 시 테두리 색상 */
}

.shadow {
    box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1); /* 그림자 효과 */
}

</style>
</head>
<%@ include file="../common/nav.jsp" %>
	
	<div id="layoutSidenav_content">
       <main>
	
		    <div class="container mt-5">
		        <h1 class="text-center mb-4">상품 등록</h1>
				<form id="update-form" action="<%= contextPath %>/updateProduct.pr" method="post" enctype="multipart/form-data" class="shadow p-4 rounded bg-light">
					<input type="hidden" name="pno" value="<%= p.getProductNo() %>">
					<div class="mb-3">
						<label for="productName" class="form-label">상품 이름:</label>
						<input type="text" 
								value="<%= p.getProductName()  %>"
								id="productName" 
								name="productName" 
								class="form-control" 
								required>
					</div>
					<div class="mb-3">
						<label for="productDescription" class="form-label">상품 설명:</label>
						<textarea id="productDescription" name="productDescription" class="form-control">
								<%= p.getProductDescription() %></textarea>
					</div>
					<div class="mb-3">
						<label for="categoryNo" class="form-label">카테고리:</label>
						<select id="categoryNo" name="categoryNo" class="form-select" required> <!-- 수정된 부분 -->
							<option value="" disabled selected>카테고리 선택</option>
							<option value="1">침대</option>
							<option value="2">테이블·식탁·책상</option>
							<option value="3">서랍·수납장</option>
							<option value="4">진열장·책장</option>
							<option value="5">의자</option>
							<option value="6">행거·옷장</option>
						</select>
					</div>
					
		            <div class="mb-3">
		                <label for="price" class="form-label">가격:</label>
		                <input type="number" id="price" name="price" 
		                	   value="<%= p.getPrice() %>" class="form-control" required min="0">
		                <input type="range" id="priceSlider" min="0" max="5000000" step="100" value="0" oninput="updatePrice(this)" class="form-range mt-2">
		                <small class="form-text text-muted">슬라이더로 가격을 조절하거나 직접 입력하세요.</small>
		            </div>
		            <div class="mb-3">
		                <label for="quantity" class="form-label">재고:</label>
		                <input type="number" id="quantity" name="productQuantity" 
		                	   value="<%= p.getProductQuantity() %>" class="form-control" required min="0" step="1">     
		            </div>
		            <div class="mb-3">
		                <label for="productSize" class="form-label">상품 사이즈  (가로 x 세로 x 높이):</label>
		                <input type="text" id="productSize" name="productSize" 
		                	   value="<%= p.getProductSize() %>"	class="form-control">
		            </div>
		            <div class="mb-3">
		                <label for="material" class="form-label">재질:</label>
		                <input type="text" id="material" name="material" 
		                	   value="<%= p.getMaterial() %>" class="form-control">
		            </div>
		            <div class="mb-3">
		                <label for="color" class="form-label">색상:</label>
		                <input type="text" id="color" name="color" 
		               		   value="<%= p.getColor() %>" class="form-control">
		            </div>
		            <div class="mb-3">
		                <label for="assemblyYN" class="form-label">조립 여부:</label>
		                <select id="assemblyYN" name="assemblyYN" 
		                		value="<%= p.getAssemblyYN() %>"class="form-select" required>
		                    <option value="Y">예</option>
		                    <option value="N">아니오</option>
		                </select>
		            </div>
		            <div class="mb-3">
		                <label for="discount" class="form-label">할인율 (%):</label>
		                <input type="number" id="discount" name="discount" 
		               		   value="<%= p.getDiscount() %>"class="form-control" 
		                       min="0" max="100" step="1">
		            </div>
					<div class="mb-3">
						<label for="country" class="form-label">제조국 : </label>
						<select id="countrySelect" name="country" class="form-select" onchange="toggleCountryInput()" required>
							<option value="" disabled selected>국가 선택</option>
							<option value="USA">미국</option>
							<option value="KOR">대한민국</option>
							<option value="CHN">중국</option>
							<option value="JP">일본</option>
							<option value="other">기타</option>
						</select>
						<input type="text" id="countryInput" name="countryInput" class="form-control mt-2" placeholder="국가를 직접 입력" style="display: none;">

					</div>
		            
		      
		            
		          <!-- 썸네일 이미지 -->
					<div class="mb-3">
					    <label for="thumbnailImage" class="form-label">썸네일 이미지:</label>
					    
					    <% if (pImg != null && !pImg.isEmpty()) { %>
					        <!-- 기존 썸네일 이미지 파일명 표시 -->
					        <span><%= pImg.get(0).getImgOriginalFile() %></span>
					        
					        <!-- 기존 파일 정보 hidden으로 저장 -->
					        <input type="hidden" name="originFileNo" value="<%= pImg.get(0).getImageNo() %>">
					        <input type="hidden" name="originChangeName" value="<%= pImg.get(0).getImgSaveFile() %>">
					    <% } %>
					    
					    <!-- 새로운 썸네일 이미지 업로드 -->
					    <input type="file" id="thumbnailImage" name="thumbnailImage" class="form-control">
					</div>
					
					<!-- 상세 이미지 -->
					<div class="mb-3">
					    <label for="detailImages" class="form-label">상품 상세 이미지 (최대 5개):</label>
					    <ul>
					        <% 
					        // 기존 이미지가 있는 경우
					        if (pImg != null && pImg.size() > 1) { 
					            for (int i = 1; i < pImg.size(); i++) { 
					        %>
					            <li>
					                <!-- 기존 상세 이미지 파일명 표시 -->
					                <span><%= pImg.get(i).getImgOriginalFile() %></span>
					                
					                <!-- 기존 파일 정보 hidden으로 저장 -->
					                <input type="hidden" name="detailOriginFileNo<%= i %>" value="<%= pImg.get(i).getImageNo() %>">
					                <input type="hidden" name="detailOriginChangeName<%= i %>" value="<%= pImg.get(i).getImgSaveFile() %>">
					                
					                <!-- 기존 이미지 표시 -->
					                <img src="<%= contextPath %>/<%= pImg.get(i).getImageUrl() + pImg.get(i).getImgSaveFile() %>" alt="Detail Image" style="width: 100px; height: auto;">
					                
					                <!-- 새 이미지 업로드 입력란 -->
					                <input type="file" id="detailImage<%= i %>" name="detailImage<%= i %>" class="form-control mt-2">
					            </li>
					        <% 
					            } 
					        } 
					        
					        // 새 이미지 업로드 입력란 추가 (총 5개 입력란)
					        int totalImages = (pImg != null) ? pImg.size() - 1 : 0; // 기존 이미지 개수
					        for (int j = totalImages + 1; j <= 5; j++) { 
					        %>
					            <li>
					                <input type="file" id="detailImage<%= j %>" name="detailImage<%= j %>" class="form-control mt-2">
					            </li>
					        <% 
					        } 
					        %>
					    </ul>
					    <small class="form-text text-muted">최대 5개의 이미지를 업로드할 수 있습니다.</small>
					</div>

		            
		            <button type="submit" class="btn btn-primary">수정하기</button>
		        </form>
		    </div>
		    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.2.3/dist/js/bootstrap.bundle.min.js"></script>
		
		    <script>
		        function toggleCountryInput() {
		            var select = document.getElementById("countrySelect");
		            var input = document.getElementById("countryInput");
		            if (select.value === "other") {
		                input.style.display = "block"; // '기타' 선택 시 입력 필드 보이기
		                input.required = true; // 필수 입력으로 설정
		            } else {
		                input.style.display = "none"; // '기타' 아닐 경우 숨기기
		                input.required = false; // 필수 입력 해제
		            }
		        }
		
		        function updatePrice(slider) {
		            var priceInput = document.getElementById("price");
		            priceInput.value = slider.value; // 슬라이더 값이 가격 입력 필드에 반영
		        }
		        
		        // 기존에 선택한 카테고리가 보여지게끔
		        $("#categoryNo option").each(function() {
				    if ($(this).text() === "<%= p.getCategoryName() %>") {
				        $(this).prop("selected", true);
				    }
				});
		        
		        $("#assemblyYN option").each(function() {
				    if ($(this).text() === "<%= p.getAssemblyYN() %>") {
				        $(this).prop("selected", true);
				    }
				});
		        
		        $(document).ready(function() {
		            var selectedCountry = "<%= p.getCountry() %>"; // 기존에 저장된 국가 값을 가져옴

		            // 옵션을 비교하여 일치하는 경우 선택 상태로 설정
		            $("#countrySelect option").each(function() {
		                if ($(this).val() === selectedCountry) {
		                    $(this).prop("selected", true);
		                }
		            });

		            // "기타"를 선택한 경우 입력 필드 보여주기
		            if (selectedCountry === "other") {
		                $("#countryInput").val("<%= p.getCountry() %>"); // 기존에 저장된 국가 값을 입력 필드에 설정
		                $("#countryInput").show();
		            } else {
		                $("#countryInput").hide();
		            }
		        });

		        
		
		        
		        
		        
		    </script>
		  </main>
		</div>
</body>
</html>