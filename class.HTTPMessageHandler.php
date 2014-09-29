<?php

date_default_timezone_set('PRC');

require_once 'class.HTTPMessage.php';
require_once 'class.DBUtils.php';
require_once 'class.Timetools.php';


class HTTPMessageHandler
{

    /**
     * @param string $sourceIP 源IP
     * @param string $destinationIP 目的IP
     * @param string $host host
     * @param string $requestUrl 请求URL
     * @param integer $time 查询开始时间
     * @param integer $result_limit 结果数量限制，默认为10
     * @param integer $type 查询类型，默认为2(既有请求，又有响应)
     * @param integer $status_code 响应码，默认为200
     * @param string $content_type 响应的内容类型
     *
     * @return string 查询结果(已经进行json格式化了)
     * * */
    public function getInfos($sourceIP, $destinationIP, $host, $requestUrl, $time, $result_limit = 10, $type = 2, $status_code = 200, $content_type = 'all')
    {

        $sql_part = array();
        // 其他的响应代码
        if ($type == -1) {
            $sql_part[] = "SELECT * FROM HTTP_MESSAGE WHERE `Type` NOT IN (200, 304, 404, 503) AND `StatusCode` = " . $status_code;

        } else {
            // 这里就是响应码为200，304，404， 503的情形
            $sql_part[] = "SELECT * FROM HTTP_MESSAGE WHERE `Type` = " . $type . " AND `StatusCode` = " . $status_code;
        }

        if (!empty($sourceIP)) {
            $sql_part[] = " AND `SourceIP` = '" . $sourceIP . "'";
        }

        if (!empty($destinationIP)) {
            $sql_part[] = " AND `DestinationIP` ='" . $destinationIP . "'";
        }

        if (!empty($host)) {
            $sql_part[] = " AND `Host` = '" . $host . "'";
        }

        if (!empty($requestUrl)) {
            $sql_part[] = " AND `RequestUrl` = '" . $requestUrl . "'";
        }

        if (!empty($time)) {
            // 注意一分钟
            $sql_part[] = " AND `BeginTime` BETWEEN " . Timetools::secondsFrom1970($time) . " AND " . (Timetools::secondsFrom1970($time) + 60);
        }

        if ($content_type !== 'all') {
            $sql_part[] = " AND `ContentType` = ' " . $content_type . "'";
        }

        // 只有当限制的结果数不为0的时候才加上LIMIT子句
        if ($result_limit != 0) {
            $sql_part[] = " LIMIT " . $result_limit;
        } else {
            $sql_part[] = " LIMIT 50";
        }

        // 将各段使用空格拼接起来
        $sql = implode("  ", $sql_part);

        // echo $sql;
        // 创建一个数据库工具对象
        $dbutils = new DBUtils;

        // 获得数据库连接
        $connection = $dbutils->getConnection();

        if ($connection) {

            $result_array = array('name' => $destinationIP, 'parent' => null, 'children' => array());

            mysqli_set_charset($connection, 'utf8');
            $result = mysqli_query($connection, $sql, MYSQLI_USE_RESULT);

            $children_array = array();

            while ($row = mysqli_fetch_array($result)) {

                // 数组内容进行清空
                unset($children_array);

                $children_array['parent'] = $destinationIP;
                $children_array['name'] = $row['SourceIP'];

                $children_array['ID'] = $row['ID'];
                $children_array['Type'] = $row['Type'];
                $children_array['BeginTime'] = date('Y-n-j H:i:s', $row['BeginTime']);
                $children_array['BeginMicrosecond'] = $row['BeginMicrosecond'];
                $children_array['EndTime'] = date('Y-n-j H:i:s', $row['EndTime']);
                $children_array['EndMicrosecond'] = $row['EndMicrosecond'];
                $children_array['SourceIP'] = $row['SourceIP'];
                $children_array['SourcePort'] = $row['SourcePort'];
                $children_array['DestinationIP'] = $row['DestinationIP'];
                $children_array['DestinationPort'] = $row['DestinationPort'];
                $children_array['Method'] = $row['Method'];
                $children_array['RequestUrl'] = $row['RequestUrl'];
                $children_array['Version'] = $row['Version'];
                $children_array['ReasonPhrase'] = $row['ReasonPhrase'];
                $children_array['Host'] = $row['Host'];
                $children_array['UserAgent'] = $row['UserAgent'];
                $children_array['Server'] = $row['Server'];
                $children_array['ContentType'] = empty($row['ContentType']) ? "Unkown" : $row['ContentType'];

                array_push($result_array['children'], $children_array);
            }

            return json_encode($result_array);
        }
    }

}
