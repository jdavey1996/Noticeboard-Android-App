<?php
define("DB_HOST", "localhost");
define("DB_USER", "joshadmin1996");
define("DB_PASSWORD", "password1");
define("DB_DATABASE", "androidDbtest");

$con = mysqli_connect(DB_HOST, DB_USER, DB_PASSWORD, DB_DATABASE);
$value1  = urldecode($_POST['field1']);
$value2   = urldecode($_POST['field2']);

if(!$con){
    $response["message"] = "conErr";
}
else{
	$checkExists = mysqli_query($con,"SELECT * FROM tblData WHERE datainput1 = '".$value1."'");
  	if (mysqli_num_rows($checkExists) > 0) {
		$response["message"] = "exists";
	}
	else
	{
		$insertQuery = mysqli_query($con,"INSERT INTO tblData(datainput1, datainput2) VALUES ('".$value1."','".$value2."')");
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
   