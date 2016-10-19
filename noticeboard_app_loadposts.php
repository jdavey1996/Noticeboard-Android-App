<?php
define("DB_HOST", "localhost");
define("DB_USER", "joshadmin1996");
define("DB_PASSWORD", "password1");
define("DB_DATABASE", "DB_NoticeboardApp");

$con = mysqli_connect(DB_HOST, DB_USER, DB_PASSWORD, DB_DATABASE);

if(!$con){
    $response["message"] = "conErr";
}
else{
	$sql = mysqli_query($con,"SELECT * FROM tblPosts ORDER BY  `post_num` DESC ");
	$data = array();

	while($row = $sql->fetch_assoc()){
		$data[] = $row;
	}
	$response["message"] = "success";
	$response["data"] = $data;	
}

echo json_encode($response);
mysqli_close($con);
?> 
   