<?php

    error_reporting(E_ALL);
    ini_set('display_errors',1);

    include('dbcon_search.php');


    $android = strpos($_SERVER['HTTP_USER_AGENT'], "Android");


    if( (($_SERVER['REQUEST_METHOD'] == 'POST') && isset($_POST['submit'])) || $android )
    {

        // 안드로이드 코드의 postParameters 변수에 적어준 이름을 가지고 값을 전달 받습니다.
        $id=$_POST['id'];
        $Start=$_POST['Start'];
        $Goal=$_POST['Goal'];

        if(empty($id)){
            $errMSG = "id를 입력하세요.";
        }    
        else if(empty($Start)){
            $errMSG = "출발지를 입력하세요.";
        }
        else if(empty($Goal)){
            $errMSG = "목적지를 입력하세요.";
        }

        if(!isset($errMSG)) // 출발지와 목적지가 모두입력되었으면 실행
        {
            try{
                // SQL문을 실행하여 데이터를 MySQL 서버의 log 테이블에 저장합니다.
                $stmt = $con->prepare('INSERT INTO log_db(id, Start, Goal) VALUES(:id, :Start, :Goal)');
                $stmt->bindParam(':id', $id);
                $stmt->bindParam(':Start', $Start);
                $stmt->bindParam(':Goal', $Goal);
                    
                if($stmt->execute())
                {
                    $successMSG = "검색기록을 추가했습니다.";
                }
                else
                {
                    $errMSG = "검색기록 추가 에러";
                }

            } catch(PDOException $e) {
                die("Database error: " . $e->getMessage());
            }
        }

    }

?>


<?php
    if (isset($errMSG)) echo $errMSG;
    if (isset($successMSG)) echo $successMSG;

        $android = strpos($_SERVER['HTTP_USER_AGENT'], "Android");

    if( !$android )
    {
?>
    <html>
       <body>

            <form action="<?php $_PHP_SELF ?>" method="POST">
                ID : <input type = "text" name = "id" />
                출발지: <input type = "text" name = "Start" />
                목적지: <input type = "text" name = "Goal" />
                <input type = "submit" name = "submit" />
            </form>

       </body>
    </html>

<?php
    }
?>

