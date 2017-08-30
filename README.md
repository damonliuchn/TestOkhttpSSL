# TestOkhttpSSL

> 本文介绍使用okhttp时配置https证书的用法，关于证书的原理和SSL协议本文不做介绍，需要的同学自行查阅。https证书常见的错误用法是信任所有证书，https证书在移动应用中常见的问题是证书过期但客户端无法及时更新的问题。本文列举了几种配置方法，并做简单总结：

## 1、验证系统中信任的根证书（默认）
- 不适合自颁发的证书（12306.cn）
- 也会存在中间人劫持问题，只要有从信任机构申请的证书就可以作为中间人劫持其他人。
## 2、验证本地证书（certificate pinning），cer 和 pem 格式都可以
- 本地证书打包在App内，无法及时更新，无法应对证书过期或吊销问题。
- 如果是自颁发的证书，期限尽可能设置长点，如果是信任机构颁发的证书，则需要在过期前提前放置新证书，实现平滑过渡。
```java
client = OkHttpClientUtil.getSSLClient(client,this,"12306.cer");
```
## 3、不验证任何证书
- 存在中间人劫持问题
```java
client = OkHttpClientUtil.getTrustAllSSLClient(client);
```
## 4、验证系统中信任的根证书 和 证书域名
- 不适合自颁发的证书（12306.cn）
- 也会存在中间人劫持问题，需要随便找一家信任机构申请相同域名证书，才能劫持，这个几率不大。
```java
        OkHttpClient.Builder builder = client.newBuilder();
        builder.hostnameVerifier(new HostnameVerifier() {
            @Override
            public boolean verify(String hostname, SSLSession session) {
                //verify your hostname
                return true;
            }
        });
```

## 5、验证系统中信任的根证书 和 本地证书但忽略过期时间
- 不适合自颁发的证书（12306.cn）
- 方法是验证系统中信任的根证书、本地证书和服务器证书的发布方是否一致、本地证书和服务器证书的主体是否一致
```java
client = OkHttpClientUtil.getSSLClientIgnoreExpire(client, this, "toutiao.pem");
```


#总结
- 1、使用自颁发的长期限的本地证书（方法2）最安全省事。
- 2、如果证书是信任机构颁发的，又不想处理证书过期问题，可以使用方法5