<?php

require_once 'class.Timetools.php';

/**
 * Created by PhpStorm.
 * User: yzh
 * Date: 14-9-25
 * Time: 16:23
 */

$tt = new Timetools();
echo $tt->secondsFrom1970("201408011200");
echo $tt->humanReadableTime(1405265168);
