<?php

$conn = mysqli_connect(
  'localhost',
  'root',
  '1234',
  'admin'); // 데이터베이스 접속

if (!$conn) { //오류가 있으면 오류 메세지 출력
	echo "error";
}
?>
