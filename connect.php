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
