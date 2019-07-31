<?php
    $host = '172.31.40.66';
    $user = 'root';
    $pw = '1234';
    $dbName = 'hanium';
    $conn = mysql_connect($host,$user,$pw) or die("연결 실패);
    mysql_select_db($dbName,$conn);
?>
