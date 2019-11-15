<?php
$conn = mysqli_connect('localhost', 'root', '1234', 'admin');
    if ( !$conn ) die('DB Error');

    /* Set to UTF-8 Encoding */
    mysqli_query($conn, 'set session character_set_connection=utf8;');
    mysqli_query($conn, 'set session character_set_results=utf8;');
    mysqli_query($conn, 'set session character_set_client=utf8;');

    /* Load data */
    $query = 'SELECT * FROM search_db';
    $result = mysqli_query($conn, $query);

    echo '<table class="text-center"><tr>' .
        '<th>No</th><th>Goal</th><th>b_pos</th><th>n_pos</th><th>Guide</th>' .
        '</tr>';
    while( $row = mysqli_fetch_array($result) ) {
        echo '<tr><td>' . $row['No'] . '</td>' .
            '<td>' . $row['Goal'] . '</td>' .
            '<td>' . $row['b_pos'] . '</td>' .
            '<td>' . $row['n_pos'] . '</td>' .
            '<td class="text-right">' . $row['Guide'] . '</td></tr>';
    }

    echo '</table>';
    mysqli_close($conn);
?>
