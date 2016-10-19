<?php
define("DB_HOST", "localhost");
define("DB_USER", "joshadmin1996");
define("DB_PASSWORD", "password1");
define("DB_DATABASE", "DB_NoticeboardApp");

$con = mysqli_connect(DB_HOST, DB_USER, DB_PASSWORD, DB_DATABASE);

$data = json_decode(file_get_contents('php://input'));
$posttitle = $data->{"postTitle"};
$postdesc = $data->{"postDesc"};
$postuser = $data->{"postUser"};

$date = new DateTime("now", new DateTimeZone('Europe/London') );

if(!$con){
    $response["message"] = "conErr";
}
else{
	$insertQuery = mysqli_query($con,"INSERT INTO tblPosts(post_title, post_desc, post_user, post_date) VALUES ('".$posttitle."','".$postdesc."','".$postuser."','".$date->format('Y-m-d H:i:s')."')");
	if ($insertQuery)
	{
		$response["message"] = "success";
	}
	else
	{
		$response["message"] = "failure";
	}
}
  echo json_encode($response);	
  mysqli_close($con);
?> 
   