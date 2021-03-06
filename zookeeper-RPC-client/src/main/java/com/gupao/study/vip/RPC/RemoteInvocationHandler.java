package com.gupao.study.vip.RPC;


import com.gupao.study.vip.RPC.zk.IServiceDiscovery;
import com.gupao.study.vip.RPC.zk.ServiceDiscoveryImpl;

import javax.xml.ws.soap.Addressing;
import java.io.Serializable;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

/**
 * <br>类说明 :
 * <br>属性说明：
 * <br>作者：Darian
 **/
public class RemoteInvocationHandler implements InvocationHandler {
    private IServiceDiscovery iServiceDiscovery;
    private String version;

    public RemoteInvocationHandler(IServiceDiscovery iServiceDiscovery,String version) {
        this.iServiceDiscovery = iServiceDiscovery;
        this.version = version;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        // 传输一些东西，让服务端知道我在做什么
        // 就必须传一些东西给服务端。
        // 建立一个传输
        RpcRequest request = new RpcRequest();

        System.out.println("调用的方法的名字：" + method);

        request.setClassName(method.getDeclaringClass().getName());
        request.setMethodName(method.getName());
        request.setParameters(args);
        request.setVersion(version);

        System.out.println("要调用的接口的名字：" + request.getClassName());
        // 根据接口名称得到对应的服务地址
        String serviceAddress = iServiceDiscovery.discover(request.getClassName());

        //   System.out.println("serviceAddress:" + serviceAddress);

        TCPTransport tcpTransport = new TCPTransport(serviceAddress);

        return tcpTransport.send(request);
    }
}
