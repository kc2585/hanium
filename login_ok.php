<?php
    if ( !isset($_POST['user_id']) || !isset($_POST['user_pw']) ) {
        header("Content-Type: text/html; charset=UTF-8");
        echo "<script>alert('아이디 또는 비밀번호가 빠졌거나 잘못된 접근입니다.');";
        echo "window.location.replace('login.php');</script>";
        exit;
    }
    $user_id = $_POST['user_id'];
    $user_pw = $_POST['user_pw'];
    $members = array(
        'sowon' => array('password' => '951207', 'name' => '소원'),
        'yerin' => array('password' => '960819', 'name' => '예린'),
        'eunha' => array('password' => '970530', 'name' => '은하'),
        'yuju'  => array('password' => '971014', 'name' => '유주'),
        'sinb'  => array('password' => '980603', 'name' => '신비'),
        'umji'  => array('password' => '980819', 'name' => '엄지'),
    );

    if( !isset($members[$user_id]) || $members[$user_id]['password'] != $user_pw ) {
        header("Content-Type: text/html; charset=UTF-8");
        echo "<script>alert('아이디 또는 비밀번호가 잘못되었습니다.');";
        echo "window.location.replace('login.php');</script>";
        exit;
    }
    /* If success */
    session_start();
    $_SESSION['user_id'] = $user_id;
    $_SESSION['user_name'] = $members[$user_id]['name'];
?>
<meta http-equiv="refresh" content="0;url=index.php" />
