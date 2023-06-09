package kr.or.ddit.controller.ch12;

public class MybatisController {
	/*
	 * 12장 마이바티스
	 * 
	 * 1. 마이바티스란?
	 * 
	 * 	1) 마이바티스는 자바 퍼시스턴스 프레임워크 중 하나로 XML 서술자나 어노테이션을 사용하여 저장 프로시저나 SQL문으로 객체들을 연결시킨다
	 * 	마이바티스는 Apache 라이선스 2.0으로 배포되는 자유소프트 웨어이다
	 * 	
	 * 	2) 장점
	 * 	- SQL의 체계적인 관리
	 * 	- 자바 객체와 SQL 입출력 값의 투명한 바인딩
	 * 	- 동적 SQL 조합
	 * 
	 * 	3) 마이바티스 설정
	 * 		1) 의존관계 정의
	 * 		- 총 6가지 라이브러리를 등록하여 관계를 정의(DatabaseConnectController 참고)
	 * 
	 * 		2) 스프링과 마이바티스 연결 설정
	 * 		- root-context.xml 설정
	 * 		- 총 3가지를 등록하여 설정(추가로 Mapper를 등록하기 위한 basePackage 정보도 함께 추가한다)
	 * 	
	 * 		3) 마이바티스 설정
	 * 		- WEB-INF/mybatisAlias/mybatisAlias.xml 설정
	 * 		- 마이바티스의 위치 설정은 root-context.xml의 'sqlSessionFactory' 설정 시, property 요소로 적용
	 * 
	 * 		4) 관련 테이블 생성
	 * 		- board 테이블 설정
	 * 		- member 테이블 설정
	 * 		- member_auth 테이블 설정
	 * 
	 * 2. Mapper 인터페이스
	 * - 인터페이스의 구현을 mybatis-spring에서 자동으로 생성할 수 있다
	 * 
	 * 	1) 마이바티스 구현
	 * 		1) Mapper 인터페이스
	 * 		- BoardMapper.java(interface)
	 * 
	 * 		2) Mapper 인터페이스와 매핑할 Mapper
	 * 		- sqlmap/boardMapper_SQL.xml 생성
	 * 
	 * 3. 별칭 적용
	 * - TypeAlias로 매핑 파일에서 반복적으로 사용될 패키지의 이름을 정의한다
	 * 	
	 * 	1) 마이바티스 설정
	 * 		1) mybatisAlias.xml 설정
	 * 		- typeAlias 설정을 한다
	 * 		2) boardMapper_SQL.xml의 type 설정을 별칭으로 설정
	 * 		- mybatisAlias가 설정되어 있지 않는 경우에는 타입으로 설정하고자 하는 타입 형태를 패키지명이 포함되어 있는 구조로 설정
	 * 		- 쿼리 태그에 각각 세팅한 패키지명 대신 alias로 설정할 별칭으로 대체
	 * 
	 * 4. '_'로 구분된 컬럼명 자동 매핑
	 * - 마이바티스 설정의 mapUnderscoreToCamelCase 프로퍼티 값을 true로 지정하면 '_'로 구분된 컬럼명을 소문자 낙타표기법의 프로퍼티명으로 자동 매핑할 수 있다
	 * ex) bo_no가 boNo가 된다
	 * 
	 * 	1) 마이바티스 설정
	 * 		1) mybatisAlias.xml 설정
	 * 		<settings>
	 * 			<setting name="mapUnderscoreToCamelCase" value="true" />
	 * 		</settings>
	 * 
	 * 5. 기본키 취득
	 * - 마이바티스는 useGeneratedKeys 속성을 이용하여 insert 할 때 데이터 베이스 측에서 채번된 기본키를 취득할 수 있다
	 * 
	 * 	1) 마이바티스 설정
	 * 		1) 매핑 파일 수정(boardMapper_SQL.xml)
	 * 		- create 부분 속성 추가
	 * 			> useGeneratedKeys = "true"  keyProperty = boardNo
	 * 			<selectKey order="BEFORE" resultType="int" keyProperty="boardNo">
	 * 				select seq_board.nextval from dual
	 * 			<selectKey>
	 * 		
	 * 		insert 쿼리가 실행되기 전 selectKey 태그 내에 있는 쿼리가 먼저 실행되어 최신의 boardNo를 생성하고
	 * 		생성된 boardNo를 sql 쿼리가 담겨있는 insert 태그까지 넘어올때 넘겨주고 있는 파라미터(board)의 property인 boardNo에 세팅된다
	 * 		세팅된 boardNo를 아래 insert 시 등록 값으로 사용하고 board라는 자바빈즈 객체에 담겨 최종 컨트롤러 까지 넘오간다
	 * 
	 * 		** seq_board.nextval 대신 현재 번호를 가져오기 위해 currval을 사용하는 경우가 있는데
	 * 		이때 주의 사항이 있다
	 * 
	 * 		- select seq_board.currval from dual
	 * 		> 위 쿼리를 사용시 currval를 사용하는데 있어서 사용 불가능할 때가 있는데 
	 * 		currval은 select seq_board.nextval from dual 이 먼저 실행된 후 사용해야 한다.
	 * 		> 같은 세션 내에서의 실행이 이뤄지지 않기 때문에 currval로 데이터를 가져오는데 에러가 발생한다
	 * 
	 * 		그럼에도 가져오고 싶다면
	 * 		> select last_number from user_sequences where sequence_name = '시퀀스 명';
	 * 		으로 가능하지만 권장 하지는 않는다
	 * 
	 * 6. 동적 SQL
	 * - 마이바티스는 동적 SQL을 조립하는 구조를 지원하고 있으며, SQL 조립 규칙을 매핑 파일에 정의 할 수 있다
	 * 
	 * 	1) 동적으로 SQL을 조립하기 위한 SQL 요소
	 * 	- <where>
	 * 	> where 절 앞 뒤에 내용을 더 추가하거나 삭제할 때 사용하는 요소
	 * 
	 * 	- <if>
	 * 	> 조건을 만족할때만 SQL을 조립할 수 있게 만드는 요소
	 * 
	 * 	- <choose>
	 * 	> 여러 선택 항목에서 조건에 만족할 때만 SQL을 조립할 수 있게 만드는 요소
	 * 
	 * 	- <foreach>
	 * 	> 컬렉션이나 배열에 대해 반복처리를 하기 위한 요소
	 * 
	 * 	- <set>
	 * 	> set 절 앞 뒤에 내용을 더 추가하거나 삭제할 때 사용하는 요소
	 * 
	 * 7. 일대다 관계 테이블 매핑
	 * - 마이바티스 기능을 활용하여 매핑 파일을 적절하게 정의하면 일대다 관계 테이블 매핑을 쉽게 처리할 수 있다
	 */
	
	
	/*
	 * ============부트스트랩을 이용한 CRUD================
	 * 
	 * 페이지 모듈화를 위한 Tiles를 사용
	 * 
	 * 1. Tiles 란?
	 * - 어떤 jsp를 템플릿으로 사용하고 템플릿의 각 영역을 어떤 내용으로 채울지에 대한 정보를 설정
	 * - 하나의 화면들을 만들다보면 공통으로 반복적으로 생성해야 하는 header, footer와 같은 영역이 존재
	 * 우리는 그러한 공통 부분들을 분리하여 반복적으로 컴포넌트들을 사용하는게 아니라 공통적인 부분은 한번만 가져다 쓰고
	 * 변화하는 부분에 대해서만 동적으로 변환해 페이지를 관리할 수 있게된다
	 * 이렇게 header/footer/menu 등 공통적인 소스를 분리하여 한 화면에서 동적으로 레이아웃을 한 곳에 배치하여 설정하고 관리할 수 있도록 도와주는 페이지 모듈화를 돕는 프레임워크가 Tiles이다
	 * 
	 * ** 모듈화를 진행할 페이지
	 * 	- template.jsp  (메인으로 사용할 템플릿)
	 * 	- header.jsp	(header영역에서 사용할 페이지)
	 * 	- footer.jsp	(footer영역에서 사용할 페이지)
	 * 	- content source (body 영역에서 사용할 동적으로 변화할 페이지)
	 * 
	 * 	그 외에 다양한 영역의 페이지는 구현하고자 하는 시나리오를 바탕으로 페이지가 구성될 때 추가적으로 레이아웃 영역을 분리하여 작성하면 된다
	 * 
	 * 2. Tiles 설정
	 * 	1) Tiles를 사용하기 위한 의존 관계 등록
	 * 	- tiles-core
	 * 	- tiles-api
	 * 	- tiles-servlet
	 * 	- tiles-jsp
	 * 
	 * 	** 의존관계를 등록 후 꼭 Project Update를 진행해서 라이브러리 등록을 진행
	 * 
	 * 	2) servlet-context.xml 수정
	 * 		- viewResolver order 순서 변경
	 * 		- tilesViewResolver Bean 등록
	 * 
	 * 	3) tiles 설정을 위한 xml 생성
	 * 		- WEB-INF/spring/tiles-config.xml
	 * 
	 * 	4) tiles xml에 설정한 layout 설정대로 페이지를 생성(.jsp)
	 */
}
