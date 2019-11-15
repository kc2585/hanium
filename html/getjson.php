<?php

    error_reporting(E_ALL);
    ini_set('display_errors',1);

    include('dbcon_search.php');


    $stmt = $con->prepare('select * from log_db');
    $stmt->execute();

    if ($stmt->rowCount() > 0)
    {
        $data = array();

        while($row=$stmt->fetch(PDO::FETCH_ASSOC))
        {
            extract($row);

            array_push($data,
                array('id'=>$id,
                'Start'=>$Start,
                'Goal'=>$Goal
            ));
        }

        header('Content-Type: application/json; charset=utf8');
        $json = json_encode(array("log_db"=>$data), JSON_PRETTY_PRINT+JSON_UNESCAPED_UNICODE);
        echo $json;
    }

?>

