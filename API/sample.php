<?php
/**
 * Sign Up using POST request.
 * Input Values Name Username (email) Password and Action
 * Action ==> signup
 */
require_once('class/class.config.php');
require_once('functions.php');
require 'vendor/autoload.php';
require_once('api_auth.php');
require_once('lib/Hashids/Hashids.php');

## E-Mail Instance
$mail = new PHPMailerOAuth;

$DBConfig =  DatabaseConnection::getInstance();
$DBConnect = $DBConfig->getConnection ();
$UserCollection = $DBConfig->getCollection("UserData");
//Get Header
$RequestHeader = getallheaders ();
if(isset($RequestHeader))
    $source = $RequestHeader['source'];
else
    $source = null;

if(isset($_POST['action'])) {
    $action = $_POST['action'];
    //echo $action;
    if ($action == 'signup') {
        $name = $_POST['name'];
        $email = $_POST['username'];
        $password = $_POST['password'];
        $password = md5($password);

        if (!filter_var($email, FILTER_VALIDATE_EMAIL) === false) {
            /* Checking User E-Mail Existence */
            $UserCheck = array("login.email"=> strtolower($email) );
            $UserCheckSearch = $UserCollection->count($UserCheck);
            if($UserCheckSearch <= 0) {
                /* User Array Details */
                $UserArray = array ();
                ## Login Credentials
                $AuthKey = generateAuthToken ($email, $source);
                $loginCred['email'] = $email;
                $loginCred['password'] = $password;
                $UserArray['login'] = $loginCred;
                ## User Profile Details
                $profileData['name'] = $name;
                $UserArray['profile'] = $profileData;
                ## Verification
                $VerificationKeyEmail = getRandomStringNumbers (5);
                $userVerification['key_e'] = $VerificationKeyEmail;
                $userVerification['status'] = "email";
                $userVerification['mobile'] = false;
                $userVerification['email'] = false;
                $UserArray['verification'] = $userVerification;
                ## Address
                $UserArray['address'] = '';
                ## Social
                $UserArray['social'] = '';
                ## Timestamp
                $UserArray['timestamp'] = date('Y-m-d H:i:s');
                ## Status
                $UserArray['account_status'] = "active"; //Blocked Deleted Active
                $UserArray['access_role'] = "user";
                ## Unique Referral
                $HashCode = new Hashids\Hashids($email, 6);
                $UserArray['referral']['code'] = $HashCode->encode(rand(1254,92367), rand(1,9));
                $UserArray['referral']['count'] = 0;
                $UserArray['referral']['log'] = array ();
                /* Payment Data */
                $HashCode = new Hashids\Hashids($email, 16);
                $UserArray['payment_data']['customer_id'] = $HashCode->encode(rand(1254,92367), rand(1,9));
                /* User Array Details */
                $CreateUser = $UserCollection->insert($UserArray);
                if(isset($CreateUser))
                {
                    $response['status'] = true;
                    $response['name'] = $name;
                    $response['email'] = $email;
                    $response['message'] = 'Successfully Signed-up';
                    $response['access_token'] = $AuthKey;
                    include_once('email/signup.php');
                }
                else
                {
                    $response['status'] = false;
                    $response['message'] = 'Unknown Error';
                }
            }
            else {
                $response['status'] = false;
                $response['message'] = 'Email Address already exists. Try login instead of register.';
            }

        }
        else {
            $response['status'] = false;
            $response['message'] = 'Please Enter a valid E-Mail address.';
        }
        header('Content-Type: application/json');
        echo json_encode($response);
    }
}
else {
    $response['status'] = false;
    $response['message'] = 'POST IS NOT ACCEPTING';
    $response['data'] = json_decode(file_get_contents('php://input'));
    header('Content-Type: application/json');
    echo json_encode($response);
}
?>