<?php
require_once('class/class.config.php');
require_once('functions.php');

/* Doctors Data
$DBConfig =  DatabaseConnection::getInstance();
$DBConnect = $DBConfig->getConnection ();
$UserCollection = $DBConfig->getCollection("DoctorsData");
$i = 3;
    $Data["login"]["email"] = "doctor_" . $i . "_email@gmail.com";
    $Data["login"]["password"] = md5("hack@123");

    $Data["profile"]["name"] = "Doctor_Name_" . $i;
    $Data["profile"]["address"] = "Doctor_" . $i . ", Address_Line_1, Address_Line_2, City, State, Zip_Code";
    $Data["profile"]["phone"] = "Doctor_Phone_" . $i;

    $Data["education"]["degree"] = "MBBS";
    $Data["education"]["specialization"] = "Specialization";

    $j = rand(1, 10);
    $Data["experience"]["years"] = $j;
    for ($k = 0; $k < $j / 2; $k++)
        $Data["experience"]["hospitals"] = "Hospital_Name_" . $k;

    $Data["Hospital"]["name"] = "Hospital_Name_" . $k;
    $Data["Hospital"]["id"] = "Hospital_ID_" . $k;

    $CreateUser = $UserCollection->insert($Data);
    if (isset($CreateUser))
        echo "Success " . $i . "\n";

*/
/* Hospital Data */
$DBConfig =  DatabaseConnection::getInstance();
$DBConnect = $DBConfig->getConnection ();
$UserCollection = $DBConfig->getCollection("HospitalData");

for($i=1; $i<11;$i++) {
    $Data = array();
    $Data['name'] = "Hospital_Name_" . $i;
    $Data['id'] = "Hospital_ID_" . $i;
    $Data["address"] = "Hospital_Name_" . $i . ", Address_Line_1, Address_Line_2, City, State, Zip_Code";
    $Data["phone"] = "Doctor_Phone_" . $i;
    $Data["specializations"] = "Specialization_1, Specialization_2, Specialization_3, Specialization_4";

    $CreateUser = $UserCollection->insert($Data);
    if (isset($CreateUser))
        echo "Success " . $i . "\n";
}

?>