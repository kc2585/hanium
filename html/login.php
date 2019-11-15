
<?php
session_start(); // 세션
if(@$_SESSION['user_name']==null) { // 로그인 하지 않았다면

?>


<html>
<head>
        <link rel="stylesheet" type="text/css" href="css/login.css?after">
        <link href="https://fonts.googleapis.com/css?family=Indie+Flower&display=swap" rel="stylesheet">
<style type="text/css">
    * { margin:0; padding: 0; }
    </style></head>
<body>
<div id="logo">
            STL
        </div>
<div id="sub">
        Admin
        </div>
<div id="main">
             <form id="login-form" method="post" action="./login_check.php">
                    <div>
                        <label for="id">ID</label>
                        <input type="text" name="id">
                        <label for="pw">PW</label>
                        <input type="password" name="pw">
                        <input type="submit" name="login" value="Login">
                </div>
             </form>
        </div>
</body>
</html>

<?php

}
else{ // 로그인 했다면

    $conn = mysqli_connect('localhost', 'root', '1234', 'admin');
    if ( !$conn ) die('DB Error');

    /* Set to UTF-8 Encoding */
    mysqli_query($conn, 'set session character_set_connection=utf8;');
    mysqli_query($conn, 'set session character_set_results=utf8;');
    mysqli_query($conn, 'set session character_set_client=utf8;');

    /* Load data */
    $query = 'SELECT * FROM light_db';
    $result = mysqli_query($conn, $query);
    echo 'hihi <br>';
    echo '<table class="text-center"><tr>' .
        '<th>idx</th><th>id</th><th>CDS</th><th>pos</th>' .
        '</tr>';
    while( $row = mysqli_fetch_array($result) ) {
        echo '<tr><td>' . $row['idx'] . '</td>' .
            '<td>' . $row['id'] . '</td>' .
            '<td>' . $row['CDS'] . '</td>' .
            '<td>' . $row['pos'] . '</td></tr>';
    }

    echo '</table>';
    mysqli_close($conn);

    $conn = mysqli_connect('localhost', 'root', '1234', 'search');
    if ( !$conn ) die('DB Error');

    /* Set to UTF-8 Encoding */
    mysqli_query($conn, 'set session character_set_connection=utf8;');
    mysqli_query($conn, 'set session character_set_results=utf8;');
    mysqli_query($conn, 'set session character_set_client=utf8;');

    /* Load data */
    $query = 'SELECT * FROM search_db';
    $result = mysqli_query($conn, $query);

    echo '<table class="text-center"><tr>' .
        '<th>idx</th><th>Start</th><th>Goal</th><th>b_pos</th><th>n_pos</th><th>Guide</th>' .
        '</tr>';
    while( $row = mysqli_fetch_array($result) ) {
        echo '<tr><td>' . $row['idx'] . '</td>' .
            '<td>' . $row['Start'] . '</td>' .
            '<td>' . $row['Goal'] . '</td>' .
            '<td>' . $row['b_pos'] . '</td>' .
            '<td>' . $row['n_pos'] . '</td>' .
            '<td>' . $row['Guide'] . '</td></tr>';
    }

    echo '</table>';
    mysqli_close($conn);
}

?>

