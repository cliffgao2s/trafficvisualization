<?php
/**
 * Created by PhpStorm.
 * User: yzh
 * Date: 14-9-25
 * Time: 16:06
 */

class Timetools {


    /**
     * @param $strtime
     * @return int
     */
    public static function  secondsFrom1970($strtime){

        $year = substr($strtime, 0, 4);
        $month = substr($strtime, 4, 2);
        $day = substr($strtime, 6, 2);
        $hour = substr($strtime, 8, 2);
        $minute = substr($strtime, 10, 2);
        $second = substr($strtime,12,2);

        return mktime($hour, $minute, $second, $month, $day, $year);

    }

    /**
     * @param $timeSecondsFrom1970
     * @return string
     */
    public function humanReadableTime($timeSecondsFrom1970){
        if(is_numeric($timeSecondsFrom1970)){
            return date('Y-n-j H:i:s', $timeSecondsFrom1970);
        }else{
            return date('Y-n-j H:i:s');
        }

    }

} 