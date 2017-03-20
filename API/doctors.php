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
$UserCollection = $DBConfig->getCollection("DoctorsData");

if(isset($_POST['action']) && $_POST['action'] == 'doctors') {
    $Doctor_ID = $_POST['Doctor_ID'];
    if(isset($Doctor_ID)) {
        $UserCheck = array("_id" => new MongoId($Doctor_ID));
        //$UserCheck['$and'][] = array("login.Password"=> md5($Password));
        $UserCheckSearch = $UserCollection->count($UserCheck);
        if ($UserCheckSearch <= 0) {
            $response['status'] = false;
            $response['message'] = "Invalid Doctor ID";
        } else {
            $UserDataArray = $UserCollection->find($UserCheck)->limit(1);
            foreach ($UserDataArray as $SingleUsers) {
                $response['status'] = true;
                $response['message'] = "Successfully returned the data";
                $response['data'] = $SingleUsers;
                $SingleUsers['Doctors_ID'] = $SingleUsers['_id']->{'$id'};
                unset($SingleUsers['data']['_id']);
                unset($SingleUsers['data']['login']['password']);
                $response['data']['Doctor_ID'] = $SingleUsers['_id']->{'$id'};
            }
        }
    } else {
        $response['status'] = true;
        $response['message'] = "Successfully returned the data";
        $UserDataArray = $UserCollection->find();
        foreach ($UserDataArray as $SingleUsers) {
            $response['data'] = $SingleUsers;
            $SingleUsers['Doctor_ID'] = $SingleUsers['_id']->{'$id'};
            unset($SingleUsers['_id']);
            unset($SingleUsers['login']['password']);
            $response['data'][] = $SingleUsers;
        }
    }
    header('Content-Type: application/json');
    echo json_encode($response);
}
?>