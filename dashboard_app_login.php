<?php
define("DB_HOST", "localhost");
define("DB_USER", "joshadmin1996");
define("DB_PASSWORD", "password1");
define("DB_DATABASE", "DB_DashboardApp");

$con = mysqli_connect(DB_HOST, DB_USER, DB_PASSWORD, DB_DATABASE);

$data = json_decode(file_get_contents('php://input'));
$username = $data->{"username"};
$password = $data->{"password"};

if(!$con){
    $response["message"] = "conErr";
}
else{
	$checkUser = mysqli_query($con,"SELECT * FROM tblUsers WHERE BINARY user_username = '".$username."'");
	if (mysqli_num_rows($checkUser) > 0) {
		$login = mysqli_query($con,"SELECT * FROM tblUsers WHERE BINARY user_username = '".$username."' AND BINARY user_password = '".$password."'");
		if (mysqli_num_rows($login) > 0) {
		
			$response["message"] = "authenticated";

		}
		else
		{
			$response["message"] = "wrongpass";

		}	
	}	
	else{
		$response["message"] = "notexists";
	}

}
  echo json_encode($response);	
  mysqli_close($con);
?> 
   