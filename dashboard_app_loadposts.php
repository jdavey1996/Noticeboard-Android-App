<?php
define("DB_HOST", "localhost");
define("DB_USER", "joshadmin1996");
define("DB_PASSWORD", "password1");
define("DB_DATABASE", "DB_DashboardApp");

$con = mysqli_connect(DB_HOST, DB_USER, DB_PASSWORD, DB_DATABASE);

	$sql = mysqli_query($con,"SELECT * FROM tblPosts");

	$data = array();
	while($row = $sql->fetch_assoc()){
    	$data[] = $row;
	}
echo json_encode($data);


mysqli_close($con);
?> 
   