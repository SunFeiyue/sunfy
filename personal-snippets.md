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

## keepAlived+HaProxy避免单点故障

## SSL证书

证书格式：pfx crt cert csr pem  
证书类别：根密钥 根证书 csr（申请）

- pfx：配置证书只需要一个单独的pfx文件。
- keystore：如同pfx文件，配置证书只需要一个keystore文件。适用于java应用。
- cer：分为两个文件。一个证书文件，一个私钥。如Nginx：

```conf
ssl_certificate /path/to/fullchain.cer;
ssl_certificate_key /path/to/xxxx.key
```

### pfx文件转成pem文件

提取证书（公钥）

`openssl pkcs12 -in test.pfx -clcerts -nokeys -out cert.pem`

提取key（私钥）。 不需密码保护的话指定“-nodes”选项

`openssl pkcs12 -in test.pfx -nocerts -out key.pem`

`openssl pkcs12 -in test.pfx -nocerts -out key.pem -nodes`

### Java SSL调试 打开SSL debug日志

设置VM参数：  
-Djavax.net.debug=ssl  
-Djavax.net.debug=all

## 设备ID、SN

android ID： 64bit
wlv6: gw:48bit sub-dev-id:64bit
starNet: XX + 200 + mac(48bit) = 17*4 = 68bit
kk: f42ef703-58b1-4f43-827c-230cd2d48ee8 = 32*4=128
yk: 12*4=48
yy:9位数字

## 后台依赖注入 注解 Anotation

guice, spring DI
