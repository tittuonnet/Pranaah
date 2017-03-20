<?php
/**
 * Created by PhpStorm.
 * User: tittuvarghese
 * Date: 20/03/17
 * Time: 3:25 PM
 */
require_once('class/class.config.php');
require_once('functions.php');
$DBConfig =  DatabaseConnection::getInstance();
$DBConnect = $DBConfig->getConnection ();
$UserCollection = $DBConfig->getCollection("UserData");

if(isset($_POST['action']) && $_POST['action'] == 'login') {
    $Bhamashah_ID = $_POST['Bhamashah_ID'];
    $Password = $_POST['Password'];

    $UserCheck['$and'][] = array("login.Bhamashah_ID"=> $Bhamashah_ID);
    $UserCheck['$and'][] = array("login.Password"=> md5($Password));
    $UserCheckSearch = $UserCollection->count($UserCheck);
    if($UserCheckSearch <= 0) {
        $response['status'] = false;
        $response['message'] = "Invalid Username or Password";
    } else {
        $UserDataArray = $UserCollection->find($UserCheck)->limit(1);
        foreach ($UserDataArray as $SingleUsers) {
            $response['status'] = true;
            $response['message'] = "Successfully logged in";
            $response['data']['NAME_ENG'] = $SingleUsers['hof_Details']['NAME_ENG'];
            $response['data']['NAME_HND'] = $SingleUsers['hof_Details']['NAME_HND'];
        }
    }
    header('Content-Type: application/json');
    echo json_encode($response);
}