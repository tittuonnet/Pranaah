<?php
/**
 * MongoDB configuration file.
 * $DBConnect = new MongoClient("mongodb://parkitAPIuser:szKwSASqon9hETwlCUywntwiwodyvIy1Q/7wszGJ8In49Zoc9sphKq83RpNz72Le@localhost:27017/ParkIt");

 */
error_reporting(E_ALL);
ini_set('display_errors', 'On');

date_default_timezone_set ("Asia/Kolkata");
class DatabaseConnection {
    private $_connection;
    private static $_instance;
    private $_host = "localhost";
    private $_db = "APIV2";
    private $_username = "parkitAPI";
    private $_password = 'MydbqFVJTa2qIshz3wfybLt9NMkfNjsvZbbgjgoF6hc=';
    public static function getInstance() {
        if(!self::$_instance) { // If no instance then make one
            self::$_instance = new self();
        }
        return self::$_instance;
    }
    private function __construct ()
    {
        //mongodb://parkitAPI:MydbqFVJTa2qIshz3wfybLt9NMkfNjsvZbbgjgoF6hc=@parkitapp.cloudapp.net:27017/ParkIt
        //"mongodb://".$this->_username.":".$this->_password."@".$this->_db.":27017/".$this->_db;
        //$DBConfig = new MongoClient("mongodb://parkitAPI:MydbqFVJTa2qIshz3wfybLt9NMkfNjsvZbbgjgoF6hc=@parkitapp.cloudapp.net:27017/ParkIt");
        $DBConfig = new MongoClient("mongodb://$this->_username:$this->_password@$this->_host:27017/$this->_db");
        $this->_connection = $DBConfig->selectDB($this->_db);
    }
    public function getConnection () {
        return $this->_connection;
    }
    public function getCollection ($CollectionName) {
        return $this->_connection->selectCollection($CollectionName);
    }
}


## Common Function
if (!function_exists('getallheaders'))
{
    function getallheaders()
    {
        $headers = '';
        foreach ($_SERVER as $name => $value)
        {
            if (substr($name, 0, 5) == 'HTTP_')
            {
                if(strtolower(substr($name, 5)) == 'access_token')
                    $headers[str_replace(' ', '-', strtolower(substr($name, 5)))] = $value;
                else
                    $headers[str_replace(' ', '-', strtolower(str_replace('_', ' ', substr($name, 5))))] = $value;
            }
        }
        return $headers;
    }
}