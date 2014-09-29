<?php

require_once 'class.HTTPMessageHandler.php';

$type = htmlspecialchars($_POST['type']);
$statusCode = htmlspecialchars($_POST['statusCode']);
$sourceIP = htmlspecialchars($_POST['sourceIP']);
$destinationIP = htmlspecialchars($_POST['destinationIP']);
$host = htmlspecialchars($_POST['host']);
$requestUrl = htmlspecialchars($_POST['requestUrl']);
$time = htmlspecialchars($_POST['time']);
// 注意select返回的是一个数组
$contentType = htmlspecialchars($_POST['contentType'][0]);
$resultLimit = htmlspecialchars($_POST['resultLimit'][0]);

// 从type(格式:0 只有请求)字符串的0号位置取出类型值
$type_int = intval(substr($type, 0, 1));
// 注意”其他“的状态类型
if ($statusCode == '其他') {
    $statusCode_int = -1;
} else {
    $statusCode_int = intval($statusCode);
}

$ct = substr($contentType, 0, 2);
if ($ct == 'al') {
    $contentTypeVal = 'all';
} else if ($ct == 'Im') {
    // Image类型
    $contentTypeVal = substr($contentType, 0, 5);
} else if ($ct == 'Te') {
    // Text类型
    $contentTypeVal = substr($contentType, 0, 4);
} else {
    // Application
    $contentTypeVal = substr($contentType, 0, 11);
}

if($resultLimit=='无限制'){
    $resultLimit_int = 0;
}else{
    $resultLimit_int = intval($resultLimit);
}

header('Content-Type:application/json;charset=utf-8');

$hmh = new HTTPMessageHandler();
// 这里返回的是json数据
//$results = $hmh->getInfos('66.222.224.202', '210.28.129.4', 'bbs.nju.edu.cn', '/download/styles/style0.css', 0, 10, 2, 304);
$results = $hmh->getInfos($sourceIP, $destinationIP, $host, $requestUrl, $time, $resultLimit, $type_int, $statusCode_int);
echo $results;