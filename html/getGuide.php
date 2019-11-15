<?php

    error_reporting(E_ALL);
    ini_set('display_errors',1);

    include('dbcon_search.php');


    $stmt = $con->prepare('select * from search_db');
    $stmt->execute();

    if ($stmt->rowCount() > 0)
    {
        $data = array();

        while($row=$stmt->fetch(PDO::FETCH_ASSOC))
        {
            extract($row);

            array_push($data,
                array('idx'=>$idx,
	    'Start'=>$Start,
                'Goal'=>$Goal,
                'b_pos'=>$b_pos,
                'n_pos'=>$n_pos,
                'Guide'=>$Guide
            ));
        }

        header('Content-Type: application/json; charset=utf8');
        $json = json_encode(array("search_db"=>$data), JSON_PRETTY_PRINT+JSON_UNESCAPED_UNICODE);
        echo $json;
    }

?>

