<?php
error_reporting(E_ALL);
ini_set('display_errors',1);

include('dbcon_search.php');



//POST 값을 읽어온다.
$id=isset($_POST['id']) ? $_POST['id'] : '';
$android = strpos($_SERVER['HTTP_USER_AGENT'], "Android");


if ($id != "" ){

    $sql="select * from log_db where id='$id'";
    $stmt = $con->prepare($sql);
    $stmt->execute();

    if ($stmt->rowCount() == 0){

        echo "'";
        echo $id;
        echo "'은 찾을 수 없습니다.";
    }
        else{

                $data = array();

        while($row=$stmt->fetch(PDO::FETCH_ASSOC)){

                extract($row);

            array_push($data,
                array('id'=>$row["id"],
                'Start'=>$row["Start"],
                'Goal'=>$row["Goal"]
            ));
        }


        if (!$android) {
            echo "<pre>";
            print_r($data);
            echo '</pre>';
        }else
        {
            header('Content-Type: application/json; charset=utf8');
            $json = json_encode(array("log_db"=>$data), JSON_PRETTY_PRINT+JSON_UNESCAPED_UNICODE);
            echo $json;
        }
    }
}
else {
    echo "id 를 입력하세요 ";
}

?>



<?php

$android = strpos($_SERVER['HTTP_USER_AGENT'], "Android");

if (!$android){
?>

<html>
   <body>

      <form action="<?php $_PHP_SELF ?>" method="POST">
         id : <input type = "text" name = "id" />
         <input type = "submit" />
      </form>

   </body>
</html>
<?php
}


?>

