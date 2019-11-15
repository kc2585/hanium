<?php
$conn = mysqli_connect(
  'localhost',
  'root',
  '1234',
  'admin');
$sql = "SELECT * FROM user";
$result = mysqli_query($conn, $sql);
while($row = mysqli_fetch_array($result)){
echo '<h1>'.$row['user_name'].'</h1>';
echo $row['user_pw'];
}
?>
