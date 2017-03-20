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
$UserCollection = $DBConfig->getCollection("HospitalData");

if(isset($_POST['action']) && $_POST['action'] == 'hospital') {
    $Hospital_ID = $_POST['Hospital_ID'];
    if(isset($Hospital_ID)) {
        $UserCheck = array("id" => $Hospital_ID);
        //$UserCheck['$and'][] = array("login.Password"=> md5($Password));
        $UserCheckSearch = $UserCollection->count($UserCheck);
        if ($UserCheckSearch <= 0) {
            $response['status'] = false;
            $response['message'] = "Invalid Hospital ID";
        } else {
            $UserDataArray = $UserCollection->find($UserCheck)->limit(1);
            foreach ($UserDataArray as $SingleUsers) {
                $response['status'] = true;
                $response['message'] = "Successfully returned the data";
                unset($SingleUsers['_id']);
                $response['data']=$SingleUsers;
            }
        }
    } else {
        $response['status'] = true;
        $response['message'] = "Successfully returned the data";
        $UserDataArray = $UserCollection->find();
        foreach ($UserDataArray as $SingleUsers) {
            unset($SingleUsers['_id']);
            $response['data'][]=$SingleUsers;
        }
    }
    header('Content-Type: application/json');
    echo json_encode($response);
}
?>