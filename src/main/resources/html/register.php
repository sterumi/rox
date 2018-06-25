<?php
include 'UUID.php';

    $mysqli = new mysqli('localhost', 'root', '', 'rox');
    if($mysqli->connect_error) die("Connection failed to database.");
    $uuid = UUID::v4();
    $sql = "INSERT INTO gameserver(uuid, name, password, gametype) VALUES ('".$uuid."','".$_POST['name']."','".$_POST['password']."','".$_POST['serverType']."')";
    if($mysqli->query($sql) === true){
        echo "Registered server.";
    }else{
        echo "Could not register server. Cause: ".$mysqli->error;
    }
    $mysqli->close();
