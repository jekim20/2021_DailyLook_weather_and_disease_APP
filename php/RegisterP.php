<?php 
    $con = mysqli_connect("localhost", "YOUR_DB_USER", "YOUR_DB_PASSWORD", "YOUR_DB_NAME");
    mysqli_query($con,'SET NAMES utf8');

    $userID = $_POST["userID"];
    $userPassword = $_POST["userPassword"];
    $userDisease_1st = $_POST["userDisease_1st"];
    $userDisease_2nd = $_POST["userDisease_2nd"];
    $userDisease_3rd = $_POST["userDisease_3rd"];
    $userDisease_4th = $_POST["userDisease_4th"];
    $userDisease_5th = $_POST["userDisease_5th"];
    $userDisease_6th = $_POST["userDisease_6th"];
    $userDisease_7th = $_POST["userDisease_7th"];
    $userDisease_8th = $_POST["userDisease_8th"];
    $userSex = $_POST["userSex"];
    $userAge = $_POST["userAge"];

    $statement = mysqli_prepare($con, "INSERT INTO USER VALUES (?,?,?,?,?,?,?,?,?,?,?,?)");
    mysqli_stmt_bind_param($statement, "sssssssssssi", $userID, $userPassword, $userDisease_1st, $userDisease_2nd, $userDisease_3rd, $userDisease_4th, $userDisease_5th, $userDisease_6th, $userDisease_7th, $userDisease_8th, $userSex, $userAge);
    mysqli_stmt_execute($statement);


    $response = array();
    if ($userID==""||$userPassword==""||$userAge=="") {	
        echo("    ");
    }
    else {
	    $response["success"] = true;
    }   

    echo json_encode($response);
?>