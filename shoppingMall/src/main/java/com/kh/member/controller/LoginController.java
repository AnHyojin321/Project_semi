package com.kh.member.controller;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.kh.member.model.service.MemberService;
import com.kh.member.model.vo.Member;

/**
 * Servlet implementation class LoginController
 */
@WebServlet("/login.me")
public class LoginController extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public LoginController() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

	/*
	 * HttpServletRequest객체와 HttpServletResponse객체
	 * -request : 서버로 요청할 떄의 전반적인 정보들이 담긴다 
	 *				(요청 시전달값, 요청 전송 방식, 요청한 ip 주소등) 
	 *response : 응답 할 때 필요한 객체
	 *				(응답 시 필요한 각종 메소드들)
	 *get방식과 post방식
	 *get : 사용자가 입력한 값들이 url 노출
	 *		데이터 길이제한 o 
	 *		대신 즐겨찾기 편리하다
	 *
	 *	(url의 Header 영역에 데이터를 담아서 전달하기 때문)
	 *
	 *post : 사용자가 입력한 값들이 url 노출 x 
	 *		 데이터 길이제한 x 
	 *		 즐겨찾기 불가
	 *		 Timeout 존재 
	 *	(url에 Body 영역에 데이터를 담아서 전달하기 때문)
	 */
		
// 		1) post방식이기 때문에 인코딩먼저 설저앟기
		request.setCharacterEncoding("UTF-8");
		
//		2) 요청 시 전달값들을 뽑아서 변수 및 객체에 담기
//		request 객체의 parameter 영역에 
//		key+value 세트로 담겨있다 
//		request.getParameter("키값") : String
//		request.getParameterValues("키값): String[] 
//		아이디 : userId
		String userId = request.getParameter("userId");
//		비번 : userPwd
		String userPwd = request.getParameter("userPwd");
		
//		System.out.println(userId);
//		System.out.println(userPwd);
		
//		쿠키를 활용하여 아이디 저장 기능 추가
//		2.요청 시 전달값으로 넘겼었던 아이디 저장 여부 뽑기
//		saveId
		String saveId = request.getParameter("saveId");
		
//		아이디를 저장하겠다 (체크 시) saveId 가 "y"
//		아이디를 저장하지 않겠다 ( 체크 x 시 ) saveId 가 null
		
//		3.아이디 저장 여부에 따른 로직 보완
		if(saveId != null && saveId.equals("y")) {
			//아이디를 저장하겠다는 뜻 
			//아이디값을 "쿠키"로 저장
			
//			쿠키를 만드는 주체 : 서버
//			쿠키를 보관하는 주체 : 브라우저
			
//			개발자 도구 탭에서 조회,추,수정,삭제가 언제든지 가능하기 때문에
//			주로 보완과 관련 없는 기능을 구현할 때 쓰는 것이 좋다
//			쿠키 사용법 
//			1.쿠키 생성( 서버 단에서 코드로 작성)
//			Cookie cookie = new Cookie("키값","벨류값");
//			쿠키 생성 시 키,벨류는 필수로 넘겨야한다
//			또한 키, 벨류는 모두 문자열타입이여야함 
//			키 값은 중복이 안된다.
//			(키값 중복 시 벨류값은 덮어씌워진다.)
			
//			2.쿠키 생성 시 만료시간(expires) 또한 지정 가능
//			cookie.setMaxAge(조단위로 만료시간 지정);
			
			Cookie cookie = new Cookie("saveId",userId);
			cookie.setMaxAge(1 * 24 * 60 * 60);
			
			/*
			 * 3. 쿠키를 생성했다면 저장은 브라우저에 해야한다
			 * 생성된 쿠키를 브라ㅣ우저가 받아볼 수 있도록 응답 정보에 첨부하기 
			 * 
			 */
			
			response.addCookie(cookie);
		}else {
			//아이디 저장 x 
			
//			쿠키 삭제
//			쿠키를 삭제하는 구문은 따로 정의되지 않는다.
//			쿠키를 똑같은 키값으로 하나 더 만들고 만료시간을 0초로 
			Cookie cookie = new Cookie("saveId", userId);
			cookie.setMaxAge(0);
			response.addCookie(cookie);
		}
		
		Member m = new Member();
		
		m.setUserId(userId);
		m.setUserPwd(userPwd);
		
//		3)Service 로 전달값을 넘기면서 요청 후 결과 받기
		Member loginUser = new MemberService().loginMember(m);
				
//		4) 처리된 결과를 가지고 사용자가 보게 될 응답화면 지정 
		/*
		 * 응답페이지에 전달할 값이 있다면 값을 어딘가에 담아야 함!! 
		 * request의 attribute영역에 그동안 담아왔다 
		 * 담아줄 수 있는 Servlet Scope 내장객체 4종류  
		 * 
		 * 
		 * 1. application
		 * 	: application 에 담은 데이터는 이 웹 어플리케이션 ( 이 웹 프로젝트) 전역에서 다 꺼내 쓸 수 있다.
		 * 2. session
		 *  : session 에 담은 데이터는 이 웹 애플리케이션 ( 이 웹 프로젝트) 전역에서 다 꺼낼 수 있다.
		 *  단, session 에 한번 담은 데이터는 내가 직접 지우기 전까지 서버가 멈추기 전까지 
		 *  브라우저가 종료되기 전까지만 접근해서 꺼내 쓸 수 있다 .
		 *  
		 * 3. request
		 *	: request에 담은 데이터는 해당 request에 대한 Servlet 및 응답페이지에서만 꺼내 쓸 수 있다.  
		 * 4. page
		 * : page에 담은 데이터는 해당 jsp 페이지 내에서만 쓸 수 있다 
		 * 
		 * =>주로 request를 제일 많이 쓰긴 한다
		 * 굳이 응답데이터가 응답 페이지 외의 다른 페이지에서 보여지게 되면 필요 없는 정보가 남발
		 * 상황에 따라서는 session 도 사용한다 
		 * 주로 로그인한 회원의 정보는 게시글 작성, 댓글 작성시,
		 * 바로 작성자의 정보로써 바로 꺼내 쓰기 위해 전역으로 둔다.
		 * 
		 * [사용법]
		 * 공통적으로 데이터를 담고자 한다면
		 * session.setAttribute("키", 벨류);
		 * 공통적으로 데이터를 뽑고자 한다면 
		 * session.getAttribute("키") : object벨류값
		 * 공통적으로 데이터를 삭제하고자 한다면
		 * requeset.removeAttribute("키");
		 * 
		 */
		
		
		if(loginUser == null) { //로그인 실패처리
//			에러문구를 담아서 에러페이지가 보여지게끔 
//			1.에러페이지를 나타내는 jsp파일 먼저 생성
//			views 폴더의 common 폴더 내의 errorPage.jsp파일로
			request.setAttribute("errorMsg", "로그인에 실패");
			
			RequestDispatcher view= request.getRequestDispatcher("views/common/errorPage.jsp");
			
			view.forward(request, response);
		}else { //로그인성공처리
			
//			응답데이터가 로그인할 회원의 정보
//			>로그인한 회원의 정보를 로그아웃 전까지 다 꺼내다 쓸 수 있게끔 session에 담을 것! 
//			Servlet에서 내장객체인 session에 접근하고자 한다면 
//			우선 session 객체를 얻어와야 한다.
//			[표현법]
//			request.getSession() : HttpSession 객체 
			HttpSession session = request.getSession();
			session.setAttribute("loginUser", loginUser);
			
			//이제 menubar.jsp에 1회성 alert기능을 
//			이미 구현했기 때문에 기능 재활용이 가능하다
			session.setAttribute("alertMsg", "로그인 굿");
			
			
			/*
			 * 응답페이지를 보여주는 방법은 두가지 방법이 있다. 
			 * 1.포워딩 방식
			 *  응답페이지용 jsp파일을 새로이 하나 만들어서 
			 *  RequestDispatcher객체를 통해 위임하는 방식 
			 * 	특징 : url 주소는 그대로고 화면만 전환된다. 
			 * 
			 * 2. url 재요청 방식
			 * url 주소를 제시하여 이미 존재하는 서블릿의 재요청을 보내 화면을 띄어주는 방식 
			 * 특징 : 자바스크립트의 location.href = "~~"와 같은 원리  
			 * 		servlet으로 요청을 보내서 응답페이지를 받는 방식이기 대문에
			 * 		request 객체에 응답데이터를 담아둘 경우 
			 * 
			 * 3/
			 */
//			메인페이지로 응답화면이 보여지게끔 
			
//			1.포워딩 방식으로 
//			RequestDispatcher view = request.getRequestDispatcher("index.jsp");
//			
//			view.forward(request, response);
//			분명히 눈에는 메인페이지가 보이나, 
//			주소창에는 http://localhost:8888/jsp/login.me로 바뀌어있음
			
//			2.url 재요청 방식으로 응답뷰를 띄어줄 경우
//			http://localhost:8888/jsp로 요청을 보내면 
//			알아서 내 눈 앞에 메인페이지가 보인다
			
//			response.sendRedirect("url경로");
//			response.sendRedirect("/jsp");
			response.sendRedirect(request.getContextPath());
//			request.getContextPath()로 
//		 	현재 이 웹 애플리케이션의 context root값을 얻을 수 있다. 
//			각 서비스 마다 사용되는 방식이 다르다
//			로그인 시에는 url 재요청 방식이 쓰인다.
			
		}
	}
	
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
