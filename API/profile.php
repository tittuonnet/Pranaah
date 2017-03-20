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

if(isset($_POST['action']) && $_POST['action'] == 'profile') {
    $Bhamashah_ID = $_POST['Bhamashah_ID'];
    //$Password = $_POST['Password'];

    $UserCheck = array("login.Bhamashah_ID"=> $Bhamashah_ID);
    //$UserCheck['$and'][] = array("login.Password"=> md5($Password));
    $UserCheckSearch = $UserCollection->count($UserCheck);
    if($UserCheckSearch <= 0) {
        $response['status'] = false;
        $response['message'] = "Invalid Bhamashah ID";
    } else {
        $UserDataArray = $UserCollection->find($UserCheck)->limit(1);
        foreach ($UserDataArray as $SingleUsers) {
            $response['status'] = true;
            $response['message'] = "Successfully returned the data";
            $response['family_count'] = count($SingleUsers['family_Details']);

            $DestURL = "https://apitest.sewadwaar.rajasthan.gov.in/app/live/Service/hofMembphoto/9999-ZEH1-00068/0?client_id=ad7288a4-7764-436d-a727-783a977f1fe1";
            $APIResult = API_Call($DestURL,"GET");
            $ProfileIMG = json_decode($APIResult['response'],true);

            $response['profile_photo'] = $ProfileIMG['hof_Photo']['PHOTO'];
            $response['data']['HOF']['AADHAR_ID'] = $SingleUsers['hof_Details']['AADHAR_ID'];
            $response['data']['HOF']['DOB'] = $SingleUsers['hof_Details']['DOB'];
            $response['data']['HOF']['GENDER'] = $SingleUsers['hof_Details']['GENDER'];
            $response['data']['HOF']['uid'] = $SingleUsers['hof_Details']['uid'];

            foreach ($SingleUsers['family_Details'] as $SingleFamily) {
                $FamilyMembers = array();
                $FamilyMembers['NAME_ENG'] = $SingleFamily['NAME_ENG'];
                $FamilyMembers['GENDER'] = $SingleFamily['GENDER'];
                $FamilyMembers['DOB'] = $SingleFamily['DOB'];
                $FamilyMembers['uid'] = $SingleFamily['uid'];
                $response['data']['Family_Members'][] = $FamilyMembers;
            }
        }
    }
    header('Content-Type: application/json');
    echo json_encode($response);
}