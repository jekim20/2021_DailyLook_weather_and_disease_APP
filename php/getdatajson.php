<?php  

$con = mysqli_connect("localhost", "DB_USER", "DB_PASSWORD", "DB_NAME");

if (!$con) {  
    echo "MySQL 접속 에러 : ";
    echo mysqli_connect_error();
    exit();  
}  

mysqli_set_charset($con, "utf8"); 

$sql = "select * from weather_project";

$result = mysqli_query($con, $sql);
$data = array();

if ($result) {      
    while ($row = mysqli_fetch_array($result)) {
        array_push($data, 
            array(
                'option1' => $row[0],
                'option2' => $row[1],
                'option3' => $row[2],
                'option4' => $row[3],
                'result1' => $row[4],
                'result2' => $row[5],
                'result3' => $row[6],
                'result4' => $row[7],
                'result5' => $row[8],
                'result6' => $row[9],
                'result7' => $row[10],
                'result8' => $row[11],
                'result9' => $row[12],
            )
        );
    }

    header('Content-Type: application/json; charset=utf8');
    $json = json_encode(array("webnautes" => $data), JSON_PRETTY_PRINT + JSON_UNESCAPED_UNICODE);
    echo $json;
} else {
    echo "SQL문 처리중 에러 발생 : "; 
    echo mysqli_error($con);
} 

mysqli_close($con);  

?>