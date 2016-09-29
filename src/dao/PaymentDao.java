package dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import controller.Controllers;
import domain.Cart;
import domain.NonUserPayment;
import domain.UserPayment;
import repository.CartRepository;
import repository.LoginRepository;

public class PaymentDao {

	public PaymentDao() {

	}

	public boolean userRegister() { //회원 결제 요청 처리

		boolean success = false;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		PreparedStatement pstmt1 = null;
		String sql = null;
		int orderAmountSum = 0;
		int userClass = 0;
		int coupon = 0;

		try {		

			Controllers.getProgramController().getConnection().setAutoCommit(false);

			sql = " select userOrderNumber from UserOrder where userId = ? and paymentflag = 'n'";
			pstmt = Controllers.getProgramController().getConnection().prepareStatement(sql);
			pstmt.setString(1, LoginRepository.getLogin().getLoginId());
			rs = pstmt.executeQuery();

			//처음에만 뷰값증가시키기
			if(rs.next()) {

				sql = "insert into UserPayment(userPaymentNumber, userOrderNumber) values(User1_paymentNumber_seq.nextval, ?)";
				pstmt1 = Controllers.getProgramController().getConnection().prepareStatement(sql);
				pstmt1.setInt(1, rs.getInt(1));
				pstmt1.executeUpdate();

			}
			//두번째부터 뷰값 현재값으로
			while(rs.next()) {

				sql = "insert into UserPayment(userPaymentNumber, userOrderNumber) values(User1_paymentNumber_seq.currval, ?)";
				pstmt1 = Controllers.getProgramController().getConnection().prepareStatement(sql);
				pstmt1.setInt(1, rs.getInt(1));
				pstmt1.executeUpdate();

			}

			pstmt.close();

			sql = "update UserOrder set paymentflag = 'y' where paymentflag = 'n' and userId = ?";
			pstmt = Controllers.getProgramController().getConnection().prepareStatement(sql);
			pstmt.setString(1, LoginRepository.getLogin().getLoginId());
			pstmt.executeUpdate();

			pstmt.close();
			rs.close();

			//상품 수량 뽑아오기
			sql = "select orderAmount from UserOrder where paymentflag = 'y' and userId = ?";
			pstmt = Controllers.getProgramController().getConnection().prepareStatement(sql);
			pstmt.setString(1, LoginRepository.getLogin().getLoginId());
			rs = pstmt.executeQuery(sql);

			while(rs.next()){ //수량 누적증가시키기
				orderAmountSum = orderAmountSum + rs.getInt(1);
			}

			if(orderAmountSum < 10){	//수량으로 등급확인시키기
				userClass = 1;
			}else if(orderAmountSum < 20) {
				userClass = 2;
			}else{
				userClass = 3;
			}

			pstmt.close();
			rs.close();

			//쿠폰 개수 증가
			sql = "select coupon from User1 where userId = ?";
			pstmt = Controllers.getProgramController().getConnection().prepareStatement(sql);
			pstmt.setString(1, LoginRepository.getLogin().getLoginId());
			rs = pstmt.executeQuery(sql);
			if(rs.next()) {
				coupon = rs.getInt(1);
			}

			pstmt.close();

			//총상품수량 증가, 쿠폰개수 증가, 등급결정 
			sql = "update User1 set purchaseQuantity = ?, userClass = ?, coupon = ? where userId = ?";
			pstmt = Controllers.getProgramController().getConnection().prepareStatement(sql);
			pstmt.setInt(1, orderAmountSum);
			pstmt.setInt(2, userClass);
			pstmt.setInt(3, coupon);
			pstmt.setString(4, LoginRepository.getLogin().getLoginId());
			pstmt.executeUpdate();

			success = true;
			Controllers.getProgramController().getConnection().commit();

		} catch (SQLException e) {
			System.out.println("결제 시 예외가 발생했습니다.");
			e.printStackTrace();

			try {Controllers.getProgramController().getConnection().rollback();} catch (SQLException e1) {e1.printStackTrace();	}

			return false;
		}finally {
			if(pstmt != null){
				try {pstmt.close();	} catch (SQLException e) {e.printStackTrace();}
			}
			if(rs != null){
				try {rs.close();	} catch (SQLException e) {e.printStackTrace();}
			}
			if(pstmt1 != null){
				try {pstmt1.close();	} catch (SQLException e) {e.printStackTrace();}
			}
			try {
				Controllers.getProgramController().getConnection().setAutoCommit(true);
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

		return success;
	}

	public boolean nonUserRegister() { //비회원 결제 요청 처리

		boolean success = false;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		PreparedStatement pstmt1 = null;
		String sql = null;

		try {		

			Controllers.getProgramController().getConnection().setAutoCommit(false);

			sql = " select nonUserOrderNumber from NonUserOrder where nonUserTel = ? and paymentflag = 'n'";
			pstmt = Controllers.getProgramController().getConnection().prepareStatement(sql);
			pstmt.setString(1, "qwer522"); //로그인 레파지토이 아이디로 호출할것 qwer522는 대체로넣어놓은것
			rs = pstmt.executeQuery();

			//처음에만 뷰값증가시키기
			if(rs.next()) {

				sql = "insert into NonUserPayment(nonUserPaymentNumer, nonUserOrderNumber) values(NonUser_paymentNumber_seq.nextval, ?)";
				pstmt1 = Controllers.getProgramController().getConnection().prepareStatement(sql);
				pstmt1.setInt(1, rs.getInt(1));
				pstmt1.executeUpdate();

			}
			//두번째부터 뷰값 현재값으로
			while(rs.next()) {

				sql = "insert into NonUserPayment(nonUserPaymentNumer, nonUserOrderNumber) values(NonUser_paymentNumber_seq.currval, ?)";
				pstmt1 = Controllers.getProgramController().getConnection().prepareStatement(sql);
				pstmt1.setInt(1, rs.getInt(1));
				pstmt1.executeUpdate();

			}

			pstmt.close();

			sql = "update NonUserOrder set paymentflag = 'y' where paymentflag = 'n' and nonUserTel = ?";
			pstmt = Controllers.getProgramController().getConnection().prepareStatement(sql);
			pstmt.setString(1, "qwer522");
			pstmt.executeUpdate();

			success = true;
			Controllers.getProgramController().getConnection().commit();

		} catch (SQLException e) {
			System.out.println("결제 시 예외가 발생했습니다.");
			e.printStackTrace();

			try {Controllers.getProgramController().getConnection().rollback();} catch (SQLException e1) {e1.printStackTrace();	}

			return false;
		}finally {
			if(pstmt != null){
				try {pstmt.close();	} catch (SQLException e) {e.printStackTrace();}
			}
			if(rs != null){
				try {rs.close();	} catch (SQLException e) {e.printStackTrace();}
			}
			if(pstmt1 != null){
				try {pstmt1.close();	} catch (SQLException e) {e.printStackTrace();}
			}
			try {
				Controllers.getProgramController().getConnection().setAutoCommit(true);
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

		return success;
	}

	public ArrayList<UserPayment> userPayment() { // 회원 결제내역 보여주기

		UserPayment userPayment = null ;
		ArrayList<UserPayment> userPayments = new ArrayList<UserPayment>();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql = null;

		try {

			sql = "select * from payment4_view_paymentInfor where userId = ?";
			pstmt = Controllers.getProgramController().getConnection().prepareStatement(sql);
			pstmt.setString(1, "qwer522");
			rs = pstmt.executeQuery();

			while(rs.next()){

				userPayment = new UserPayment();
				userPayment.setPaymentNumber(rs.getInt(1));
				userPayment.setUserId(rs.getString(2));
				userPayment.setOrderNumber(rs.getInt(3));
				userPayment.setProductName(rs.getString(4));
				userPayment.setOrderCount(rs.getInt(5));
				userPayment.setOrderSum(rs.getInt(5) * rs.getInt(6));
				userPayment.setPaymentDate(rs.getString(7));
				userPayments.add(userPayment);

			}

		} catch (SQLException e) {
			e.printStackTrace();
		}finally {
			if(rs != null) {
				try {rs.close();} catch (SQLException e) {e.printStackTrace();}
			}
			if(pstmt != null) {
				try {pstmt.close();} catch (SQLException e) {e.printStackTrace();}
			}
		}

		return userPayments;
	}

	public ArrayList<NonUserPayment> nonUserPayment() { // 비회원 결제내역 보여주기

		NonUserPayment nonUserPayment = null ;
		ArrayList<NonUserPayment> nonUserPayments = new ArrayList<NonUserPayment>();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql = null;

		try {

			sql = "select * from Nonuserpay_view_paymentInfor where nonUserTel = ?";
			pstmt = Controllers.getProgramController().getConnection().prepareStatement(sql);
			pstmt.setString(1, "qwer522");
			rs = pstmt.executeQuery();

			while(rs.next()){

				nonUserPayment = new NonUserPayment();
				nonUserPayment.setPaymentNumber(rs.getInt(1));
				nonUserPayment.setUserId(rs.getString(2));
				nonUserPayment.setOrderNumber(rs.getInt(3));
				nonUserPayment.setProductName(rs.getString(4));
				nonUserPayment.setOrderCount(rs.getInt(5));
				nonUserPayment.setOrderSum(rs.getInt(5) * rs.getInt(6));
				nonUserPayment.setPaymentDate(rs.getString(7));
				nonUserPayments.add(nonUserPayment);

			}

		} catch (SQLException e) {
			e.printStackTrace();
		}finally {
			if(rs != null) {
				try {rs.close();} catch (SQLException e) {e.printStackTrace();}
			}
			if(pstmt != null) {
				try {pstmt.close();} catch (SQLException e) {e.printStackTrace();}
			}
		}

		return nonUserPayments;
	}
	
	public int couponChecking() { //쿠폰개수 확인
		int userCoupon = 0;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		try {

			String sql = "select coupon from User1 where userId = ?";
			pstmt = Controllers.getProgramController().getConnection().prepareStatement(sql);
			pstmt.setString(1, LoginRepository.getLogin().getLoginId());
			rs = pstmt.executeQuery();
			
			if(rs.next()) {
				if( rs.getInt(1) >= 10) {
					userCoupon = 1;
				}
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}finally {
			if(rs != null) {
				try {rs.close();} catch (SQLException e) {e.printStackTrace();}
			}
			if(pstmt != null) {
				try {pstmt.close();} catch (SQLException e) {e.printStackTrace();}
			}
		}
		
		return userCoupon;
	}
	
	public ArrayList<Cart> selectCartList() { //장바구니 레파지토리 호출

		return CartRepository.getCart();

	}
	
	public boolean UserCouponUseNumber(int number) { //번호있는지 확인
		
		boolean success =false;
		for(int i = 0 ; i < CartRepository.getCart().size() ; i ++) {
			if(CartRepository.getCart().get(i).getCartNumber() == number) {
				success = true;
			}
			
		}
		return success;
		
	}

}