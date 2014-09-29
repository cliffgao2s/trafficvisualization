<?php

/**
 * Description of DBUtils
 *
 * @author yzh
 */
class DBUtils
{

    /**
     * 获取数据库的连接
     * * */
    public function getConnection()
    {

        $host = 'localhost';
        $user = '****';
        $password = '*****';
        $database = 'traffic_visualization';
        $port = '3306';

        $connection = mysqli_connect($host, $user, $password, $database, $port) or die("连接失败...");

        return $connection;
    }

    /**
     * 关闭数据库的连接
     * * */
    public function closeConnection($connection)
    {
        if (!$connection) {
            mysqli_close($connection);
        }
    }

}
