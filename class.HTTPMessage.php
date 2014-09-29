<?php

/**
 * Description of newPHPClass
 *
 * @author yzh
 */
class HTTPMessage
{

    private $id;
    private $type;
    private $beginTime;// 这个秒数有没有加 8*3600(东八区)
    private $beginMicrosecond;
    private $endTime;
    private $endMicrosecond;
    private $sourceIP;
    private $sourcePort;
    private $destinationIP;
    private $destinationPort;
    private $method;
    private $requestUrl;
    private $version;
    private $statusCode;
    private $reasonPhrase;
    private $host;
    private $userAgent;
    private $server;
    private $contentType;
    private $children;
    private $childrenNumber;

    function __construct()
    {
        $this->children = array();
        $this->childrenNumber = 0;
    }

    public function getId()
    {
        return $this->id;
    }

    public function setId($id)
    {
        $this->id = $id;
    }

    public function getType()
    {
        return $this->type;
    }

    public function setType($type)
    {
        $this->type = $type;
    }

    public function getBeginTime()
    {
        return $this->beginTime;
    }

    public function setBeginTime($beginTime)
    {
        $this->beginTime = $beginTime;
    }

    public function getBeginMicrosecond()
    {
        return $this->beginMicrosecond;
    }

    public function setBeginMicrosecond($beginMicrosecond)
    {
        $this->beginMicrosecond = $beginMicrosecond;
    }

    public function getEndTime()
    {
        return $this->endTime;
    }

    public function setEndTime($endTime)
    {
        $this->endTime = $endTime;
    }

    public function getDEndMicrosecond()
    {
        return $this->endMicrosecondnd;
    }

    public function getSourceIP()
    {
        return $this->sourceIP;
    }

    public function setSourceIP($sourceIP)
    {
        $this->sourceIP = $sourceIP;
    }

    public function getDestinationIP()
    {
        return $this->destinationIP;
    }

    public function setDestinationIP($destinationIP)
    {
        $this->destinationIP = $destinationIP;
    }

    public function getSourcePort()
    {
        return $this->sourcePort;
    }

    public function setSourcePort($sourcePort)
    {
        $this->sourcePort = $sourcePort;
    }

    public function getDestinationPort()
    {
        return $this->destinationPort;
    }

    public function setDestinationPort($destinationPort)
    {
        $this->destinationPort = $destinationPort;
    }

    public function getMethod()
    {
        return $this->method;
    }

    public function setMethod($method)
    {
        $this->method = $method;
    }

    // 孩子数量就是数组的长度

    public function getRequestUrl()
    {
        return $this->requestUrl;
    }

    public function setRequestUrl($requestUrl)
    {
        $this->requestUrl = $requestUrl;
    }

    public function getVersion()
    {
        return $this->version;
    }

    public function setVersion($version)
    {
        $this->version = $version;
    }

    public function getStatusCode()
    {
        return $this->statusCode;
    }

    public function setStatusCode($statusCode)
    {
        $this->statusCode = $statusCode;
    }

    public function getReasonPhrase()
    {
        return $this->reasonPhrase;
    }

    public function setReasonPhrase($reasonPhrase)
    {
        $this->reasonPhrase = $reasonPhrase;
    }

    public function getHost()
    {
        return $this->host;
    }

    public function setHost($host)
    {
        $this->host = $host;
    }

    public function getUserAgent()
    {
        return $this->userAgent;
    }

    public function setUserAgent($userAgent)
    {
        $this->userAgent = $userAgent;
    }

    public function getServer()
    {
        return $this->server;
    }

    public function setServer($server)
    {
        $this->server = $server;
    }

    public function getContentType()
    {
        return $this->contentType;
    }

    public function setContentType($contentType)
    {
        $this->contentType = $contentType;
    }

    public function getChildren()
    {
        return $this->children;
    }

    public function setChildren($children)
    {
        $this->children[] = $children;
        $this->setChildrenNumber(count($children));
    }

    public function getChildrenNumber()
    {
        return count($this->children);
    }

    public function setChildrenNumber($childrenNumber)
    {
        $this->childrenNumber = $childrenNumber;
    }

    public function setEndMicrosecond($endMicrosecond)
    {
        $this->endMicrosecond = $endMicrosecond;
    }

}
