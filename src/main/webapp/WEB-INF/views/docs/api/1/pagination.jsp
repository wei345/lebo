<%@ page contentType="text/html;charset=UTF-8" %>

<h2>概述</h2>

<ul>
    <li>分页查询涉及3个参数：count、maxId、sinceId。</li>
    <li>服务器的查询逻辑很简单，按ID由大到小排序，取小于maxId且大于sinceId的前count条数据，maxId默认值为理论上最大值，sinceId默认值为理论上最小值。</li>
    <li>count参数是可选的，服务器设有默认值、最小值、最大值。</li>
    <li>如果服务器返回的数据不到count条，说明没有更多数据了。</li>
</ul>

<h2>分页故事</h2>
<ol>
    <li>客户端第1次获取数据，向服务器发送count</li>
    <li>服务器返回最新的count条数据</li>
    <li>客户端读取数据，记录最大ID(记为sinceId)和最小ID(记为maxId)</li>
    <li>客户端向服务器发送maxId和count获取下一页数据</li>
    <li>服务器返回小于maxId的count条数据</li>
    <li>客户端读取数据，将数据追加到列表末尾，并记录本次获得的数据中最小ID(记为maxId)</li>
    <li>客户端重复上面3步，获取更多数据</li>
    <li>过了一段时间，客户端想获取最新的数据，但又不想收到重复数据，向服务器发送sinceId、count</li>
    <li>服务器返回最新并且大于sinceId的count条数据</li>
    <li>客户端读取数据，将数据插入到列表开头，并记录本次获得的数据中最小ID(记为maxId2)</li>
    <li>如果服务器返回了count条数据，说明可能还有更多最新数据，客户端向服务器发送maxId2、sinceId、count获取下一页数据</li>
    <li>服务器返回小于maxId2并且大于sinceId的最多count条数据</li>
    <li>客户端重复上面3步，获取更多数据</li>
    <li>如果服务器返回数据小于count条，说明没有更多最新数据了，客户端读取列表开头第一条数据ID记为sinceId</li>
    <li>客户端可随时向服务器发送maxId和count获取列表尾部下一页数据</li>
</ol>
