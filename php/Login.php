<?php
$con = mysqli_connect("localhost", "DB_USER", "DB_PASSWORD", "DB_NAME");
mysqli_query($con, 'SET NAMES utf8');

$userID = $_POST["userID"];
$userPassword = $_POST["userPassword"];

$statement = mysqli_prepare($con, "SELECT * FROM USER WHERE userID = ? AND userPassword = ?");
mysqli_stmt_bind_param($statement, "ss", $userID, $userPassword);
mysqli_stmt_execute($statement);

mysqli_stmt_store_result($statement);
mysqli_stmt_bind_result(
    $statement,
    $userID,
    $userPassword,
    $userDisease_1st,
    $userDisease_2nd,
    $userDisease_3rd,
    $userDisease_4th,
    $userDisease_5th,
    $userDisease_6th,
    $userDisease_7th,
    $userDisease_8th,
    $userSex,
    $userAge
);

$response = array();
$response["success"] = false;

if (mysqli_stmt_fetch($statement)) {
    $response["success"] = true;
    $response["userID"] = $userID;
    $response["userSex"] = $userSex;
    $response["userAge"] = $userAge;
}

echo json_encode($response);
?>