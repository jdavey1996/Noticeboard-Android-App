<?php
define("DB_HOST", "localhost");
define("DB_USER", "joshadmin1996");
define("DB_PASSWORD", "password1");
define("DB_DATABASE", "DB_DashboardApp");

$con = mysqli_connect(DB_HOST, DB_USER, DB_PASSWORD, DB_DATABASE);

$username  = urldecode($_POST['username']);
$password   = urldecode($_POST['password']);

if(!$con){
    $response["message"] = "conErr";
}
else{
	$checkExists = mysqli_query($con,"SELECT * FROM tblUsers WHERE user_username = '".$username."'");
  	if (mysqli_num_rows($checkExists) > 0) {
		$response["message"] = "exists";
	}
	else
	{
		$insertQuery = mysqli_query($con,"INSERT INTO tblUsers(user_username, user_password) VALUES ('".$username."','".$password."')");
		if ($insertQuery)
		{
		    $response["message"] = "success";
		}
		else
		{
		    $response["message"] = "failure";
		}
	}
}
  echo json_encode($response);	
  mysqli_close($con);
?> 
   