<?php
error_reporting(E_ALL);
ini_set('display_errors',1);

include('dbcon_search.php');



//POST 값을 읽어온다.
$Start=isset($_POST['Start']) ? $_POST['Start'] : '';
$Goal=isset($_POST['Goal']) ? $_POST['Goal'] : '';
$android = strpos($_SERVER['HTTP_USER_AGENT'], "Android");


if ($Goal != "" && $Start != ""){

    $sql="select * from search_db where Goal='$Goal' AND Start='$Start'";
    $stmt = $con->prepare($sql);
    $stmt->execute();

    if ($stmt->rowCount() == 0){

        echo "'";
        echo $Start;
        echo ", ";
        echo $Goal;
        echo "'은 찾을 수 없습니다.";
    }
        else{

                $data = array();

        while($row=$stmt->fetch(PDO::FETCH_ASSOC)){

                extract($row);

            array_push($data,
                array('idx'=>$row["idx"],
	    'Start'=>$row["Start"],
                'Goal'=>$row["Goal"],
                'b_pos'=>$row["b_pos"],
                'n_pos'=>$row["n_pos"],
                'Guide'=>$row["Guide"]
            ));
        }


        if (!$android) {
            echo "<pre>";
            print_r($data);
            echo '</pre>';
        }else
        {
            header('Content-Type: application/json; charset=utf8');
            $json = json_encode(array("search_db"=>$data), JSON_PRETTY_PRINT+JSON_UNESCAPED_UNICODE);
            echo $json;
        }
    }
}
else {
    echo "출발지와 목적지를 입력하세요 ";
}

?>



<?php

$android = strpos($_SERVER['HTTP_USER_AGENT'], "Android");

if (!$android){
?>

<html>
   <body>

      <form action="<?php $_PHP_SELF ?>" method="POST">
         출발지 : <input type = "text" name = "Start" />
         목적지 : <input type = "text" name = "Goal" />
         <input type = "submit" />
      </form>

   </body>
</html>
<?php
}


?>

