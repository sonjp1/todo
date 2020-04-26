# Todo Application

### - 실행 방법
- 빌드 완료 후 내장톰캣으로 실행하여 아래 URL로 접속하여 테스트
- To-do
  - http://localhost:8080/todo
- H2 console
  - URL : http://localhost:8080/h2-console
  - JDBC : jdbc:h2:mem:testdb
  - User Name : sa

### - 사용 방법
- 검색 :카테고리 선택 → 검색창 입력 → `Search Todo`
- 등록 : 내용, 참조 No 입력 → `Add Todo`
- 수정 : 수정 아이콘을 클릭 → `text필드`가 `input필드`로 변경 → 변경할 내용, 참조 No 입력 → 수정 아이콘을 클릭하여 마무리
- 삭제 : 삭제 아이콘을 클릭
