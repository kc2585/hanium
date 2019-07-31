# hanium
*/
1. ubuntu 루트 계정 전환

$ su 
password : 1234

2. ip4 주소 : http://13.125.253.240 -> http://13.125.253.240/index.html  같은 주소

3. 웹페이지 작업하는 법

루트 계정으로 전환 -> cd /var/www/html  경로로 들어가 vi 작업. 

/var/www/html 경로에 css, html ,php 작업

4. phpmyadmin 접속 방법

주소 : http://13.125.253.240/dbmyadmin
id : root 
password : 1234

#현재 db 서버 오류 발생
*/
hanium project

1. connect DB with WEB server
<?php
$conn_DB = mysql_connext( 'mysql', 'root', 1234 );

if ( !$conn_DB) {
        die( "MySQL connect eror : ".mysql_error() );
}
?>

<!DOCTYPE HTML>
<html>

<head>

</head>

<body>
        <form method=POST action=connect.php>
         id: <input type=text name=name /></br>
         pw: <input type=text name=age /></br>
         <input type=submit> </br>

        <?php
          echo "id : $_POST[name] </br>";
          echo "pw : $_POST[age] </br>";
        ?>

        </form>
</body>

</html>
