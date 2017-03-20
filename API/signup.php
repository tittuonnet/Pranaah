<?php
/**
 * Sign Up using POST request.
 * Input Values Name Username (email) Password and Action
 * Action ==> signup
 */
require_once('class/class.config.php');
require_once('functions.php');
require_once('lib/Hashids/Hashids.php');

$DBConfig =  DatabaseConnection::getInstance();
$DBConnect = $DBConfig->getConnection ();
$UserCollection = $DBConfig->getCollection("UserData");

if(isset($_POST['action']) && $_POST['action'] == 'signup') {
    $Bhamashah_ID = $_POST['Bhamashah_ID'];
    $UserCheck = array("login.Bhamashah_ID" => $Bhamashah_ID);
    $UserCheckSearch = $UserCollection->count($UserCheck);
    if($UserCheckSearch <= 0) {
        $DestURL = "https://apitest.sewadwaar.rajasthan.gov.in/app/live/Service/bahmashah/hofAndMembers/".$Bhamashah_ID."?client_id=".$Client_ID;
        $APIResult = API_Call($DestURL,"GET");
        //1428-WKMY-25373?client_id=ad7288a4-7764-436d-a727-783a977f1fe1
        if ($APIResult['err']) {
            $response['status'] = false;
            $response['message'] = "The system encountered some issues";
        } else {
            // API Connected
            $result = json_decode($APIResult['response'],true);
            if(isset($result['result']) && $result['result'] == "No data found") {
                $response['status'] = false;
                $response['message'] = "Invalid  Bhamashah ID";
            } else {
                $UserData = array();
                $UserData['login']['Bhamashah_ID'] = $Bhamashah_ID;
                $UserData['login']['Password']=null;
                $UserData['hof_Details'] = $result['hof_Details'];
                $HashCode = new Hashids\Hashids($result['hof_Details']['NAME_ENG'], 6);
                $UserData['hof_Details']['uid'] = $HashCode->encode(rand(1254,92367), rand(1,9));

                foreach ($result['family_Details'] as $SingleData) {
                    $HashCode = new Hashids\Hashids($SingleData['NAME_ENG'], 8);
                    $SingleData['uid'] = $HashCode->encode(rand(1254,92367), rand(1,9));
                    $UserData['family_Details'][] = $SingleData;
                }


                //Inserting Data
                $CreateUser = $UserCollection->insert($UserData);
                if(isset($CreateUser)) {
                    $response['status'] = true;
                    $response['message'] = "Successfully signed-up";
                    $response['data']['name'] = $UserData['hof_Details']['NAME_ENG'];
                    $response['data']['Bhamashah_ID'] = $UserData['hof_Details']['BHAMASHAH_ID'];
                } else
                {
                    $response['status'] = false;
                    $response['message'] = "Something went wrong";
                }
            }
        }

    } else {
        $response['status'] = false;
        $response['message'] = "Account already registered";
    }
    header('Content-Type: application/json');
    echo json_encode($response);
}
?>