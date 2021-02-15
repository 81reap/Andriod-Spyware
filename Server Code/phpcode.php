<?php
$servername = "localhost";
$username = "id16106575_spy";
$password = "sW#?1#[OK?uu1>7)";
$dbname = "id16106575_data";

// Create connection
$conn = new mysqli($servername, $username, $password, $dbname);
// Check connection
if ($conn->connect_error) {
    die("Connection failed: " . $conn->connect_error);
}



$dateTime=$_POST['DateTime'];
$action = $_POST['Action'];
$data = $_POST['Data'];


$sql = "INSERT INTO demotable (DateTime,Action,Data)
VALUES ('{$dateTime}','{$action}','{$data}')";

if ($conn->query($sql) === TRUE) {
    echo "New record created successfully";
} else {
    echo "Error: " . $sql . "<br>" . $conn->error;
}

$conn->close();
?>
