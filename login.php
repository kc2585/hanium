<!DOCTYPE html>
<?php session_start(); ?>
<html>
    <head>
        <meta charset="utf-8" />
        <title>PHP Session Login Test</title>
    </head>
    <body>
        <h1>Hello, world!</h1>
        <hr />
        <h2>로그인</h2>
        <?php if(!isset($_SESSION['user_id']) || !isset($_SESSION['user_name'])) { ?>
        <form method="post" action="login_ok.php">
            <p>아이디: <input type="text" name="user_id" /></p>
            <p>비밀번호: <input type="password" name="user_pw" /></p>
            <p><input type="submit" value="로그인" /></p>
        </form>
        <?php } else {
            $user_id = $_SESSION['user_id'];
            $user_name = $_SESSION['user_name'];
            echo "<p><strong>$user_name</strong>($user_id)님은 이미 로그인하고 있습니다. ";
            echo "<a href=\"index.php\">[돌아가기]</a> ";
            echo "<a href=\"logout.php\">[로그아웃]</a></p>";
        } ?>
    </body>
</html>
