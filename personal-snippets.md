# personal code snippts about IT/software

=====

## 1. Vertx event bus, message consumer消费组

Vertx event bus message consumer没有消费组的概念。一个集群多个节点或多个vertical instance共同监听同一个address的话，publish消息会同时收到；如果只想其中一个收到消息，需要使用send。面对复杂的场景，有的多个都收到，有的只有一个收到，则不能满足，需自己实现。

## 2.  java.net.URL#openStream阻塞问题

摄像头联动截图卡住的问题（vertx.executeBlocking)，原因是调用了URL#openStream，而该接口默认没有超时时间（timeout），
故服务器出故障的话，会一直阻塞下去。

## 3. Yaml, ansible playbook

## 4.vi可以直接编辑jar文件

## 5. Vertx event bus bridge (sockJS based)原理？为啥地址使用的是http
