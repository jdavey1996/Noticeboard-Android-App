<?php
define("DB_HOST", "localhost");
define("DB_USER", "joshadmin1996");
define("DB_PASSWORD", "password1");
define("DB_DATABASE", "DB_NoticeboardApp");

$con = mysqli_connect(DB_HOST, DB_USER, DB_PASSWORD, DB_DATABASE);

$data = json_decode(file_get_contents('php://input'));
$postnumber = $data->{"postNum"};

if(!$con){
    $response["message"] = "conErr";
}
else{
	$deleteQuery= mysqli_query($con,"DELETE FROM tblPosts WHERE post_num = $postnumber");
	if ($deleteQuery)
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
   