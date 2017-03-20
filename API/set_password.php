<?php
/**
 * Created by PhpStorm.
 * User: tittuvarghese
 * Date: 20/03/17
 * Time: 1:25 PM
 */
require_once('class/class.config.php');
require_once('functions.php');
$DBConfig =  DatabaseConnection::getInstance();
$DBConnect = $DBConfig->getConnection ();
$UserCollection = $DBConfig->getCollection("UserData");

if(isset($_POST['action']) && $_POST['action'] == 'set_password') {
    $Bhamashah_ID = $_POST['Bhamashah_ID'];
    $Password = $_POST['Password'];
    $UserCheck = array("login.Bhamashah_ID"=> $Bhamashah_ID);
    $UserCheckSearch = $UserCollection->count($UserCheck);
    if($UserCheckSearch <= 0) {
        $response['status'] = false;
        $response['message'] = "Account is not exist in our system";
    } else {
        $UpdateQuery['login.Password'] = md5($Password);
        $ResetUpdate['$set'] = $UpdateQuery;
        $UpdateUserCollection = $UserCollection->update($UserCheck, $ResetUpdate);
        if((isset($UpdateUserCollection) && $UpdateUserCollection['ok'] == 1)) {
            $response['status'] = true;
            $response['message'] = "Password is successfully updated.";
        } else {
            $response['status'] = false;
            $response["message"] = "Something went wrong.";
        }
    }
    header('Content-Type: application/json');
    echo json_encode($response);
}
?>